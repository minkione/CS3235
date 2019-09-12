package com.google.common.util.concurrent;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
    private final Guard hasReachedRunning = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) >= 0;
        }
    };
    private final Guard isStartable = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state() == State.NEW;
        }
    };
    private final Guard isStoppable = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) <= 0;
        }
    };
    private final Guard isStopped = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    };
    @GuardedBy("monitor")
    private final List<ListenerExecutorPair> listeners = Lists.newArrayList();
    private final Monitor monitor = new Monitor();
    private final ExecutionQueue queuedListeners = new ExecutionQueue();
    /* access modifiers changed from: private */
    public final Transition shutdown = new Transition();
    @GuardedBy("monitor")
    private volatile StateSnapshot snapshot = new StateSnapshot(State.NEW);
    /* access modifiers changed from: private */
    public final Transition startup = new Transition();

    private static class ListenerExecutorPair {
        final Executor executor;
        final Listener listener;

        ListenerExecutorPair(Listener listener2, Executor executor2) {
            this.listener = listener2;
            this.executor = executor2;
        }
    }

    @Immutable
    private static final class StateSnapshot {
        @Nullable
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final State state;

        StateSnapshot(State state2) {
            this(state2, false, null);
        }

        StateSnapshot(State state2, boolean z, @Nullable Throwable th) {
            Preconditions.checkArgument(!z || state2 == State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", state2);
            Preconditions.checkArgument(!((th != null) ^ (state2 == State.FAILED)), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", state2, th);
            this.state = state2;
            this.shutdownWhenStartupFinishes = z;
            this.failure = th;
        }

        /* access modifiers changed from: 0000 */
        public State externalState() {
            if (!this.shutdownWhenStartupFinishes || this.state != State.STARTING) {
                return this.state;
            }
            return State.STOPPING;
        }

        /* access modifiers changed from: 0000 */
        public Throwable failureCause() {
            Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }

    private class Transition extends AbstractFuture<State> {
        private Transition() {
        }

        public State get(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
            try {
                return (State) super.get(j, timeUnit);
            } catch (TimeoutException unused) {
                throw new TimeoutException(AbstractService.this.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract void doStart();

    /* access modifiers changed from: protected */
    public abstract void doStop();

    protected AbstractService() {
        addListener(new Listener() {
            public void running() {
                AbstractService.this.startup.set(State.RUNNING);
            }

            public void stopping(State state) {
                if (state == State.STARTING) {
                    AbstractService.this.startup.set(State.STOPPING);
                }
            }

            public void terminated(State state) {
                if (state == State.NEW) {
                    AbstractService.this.startup.set(State.TERMINATED);
                }
                AbstractService.this.shutdown.set(State.TERMINATED);
            }

            public void failed(State state, Throwable th) {
                switch (state) {
                    case STARTING:
                        AbstractService.this.startup.setException(th);
                        AbstractService.this.shutdown.setException(new Exception("Service failed to start.", th));
                        return;
                    case RUNNING:
                        AbstractService.this.shutdown.setException(new Exception("Service failed while running", th));
                        return;
                    case STOPPING:
                        AbstractService.this.shutdown.setException(th);
                        return;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected from state: ");
                        sb.append(state);
                        throw new AssertionError(sb.toString());
                }
            }
        }, MoreExecutors.sameThreadExecutor());
    }

    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                starting();
                doStart();
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
                throw th;
            }
            this.monitor.leave();
            executeListeners();
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Service ");
        sb.append(this);
        sb.append(" has already been started");
        throw new IllegalStateException(sb.toString());
    }

    @Deprecated
    public final ListenableFuture<State> start() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                starting();
                doStart();
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
                throw th;
            }
            this.monitor.leave();
            executeListeners();
        }
        return this.startup;
    }

    public final Service stopAsync() {
        stop();
        return this;
    }

    @Deprecated
    public final ListenableFuture<State> stop() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                State state = state();
                switch (state) {
                    case STARTING:
                        this.snapshot = new StateSnapshot(State.STARTING, true, null);
                        stopping(State.STARTING);
                        break;
                    case RUNNING:
                        this.snapshot = new StateSnapshot(State.STOPPING);
                        stopping(State.RUNNING);
                        doStop();
                        break;
                    case STOPPING:
                    case TERMINATED:
                    case FAILED:
                        StringBuilder sb = new StringBuilder();
                        sb.append("isStoppable is incorrectly implemented, saw: ");
                        sb.append(state);
                        throw new AssertionError(sb.toString());
                    case NEW:
                        this.snapshot = new StateSnapshot(State.TERMINATED);
                        terminated(State.NEW);
                        break;
                    default:
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unexpected state: ");
                        sb2.append(state);
                        throw new AssertionError(sb2.toString());
                }
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
                throw th;
            }
            this.monitor.leave();
            executeListeners();
        }
        return this.shutdown;
    }

    @Deprecated
    public State startAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    @Deprecated
    public State stopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }

    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, j, timeUnit)) {
            try {
                checkCurrentState(State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Timed out waiting for ");
            sb.append(this);
            sb.append(" to reach the RUNNING state. ");
            sb.append("Current state: ");
            sb.append(state());
            throw new TimeoutException(sb.toString());
        }
    }

    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, j, timeUnit)) {
            try {
                state();
                checkCurrentState(State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Timed out waiting for ");
            sb.append(this);
            sb.append(" to reach a terminal state. ");
            sb.append("Current state: ");
            sb.append(state());
            throw new TimeoutException(sb.toString());
        }
    }

    @GuardedBy("monitor")
    private void checkCurrentState(State state) {
        State state2 = state();
        if (state2 == state) {
            return;
        }
        if (state2 == State.FAILED) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected the service to be ");
            sb.append(state);
            sb.append(", but the service has FAILED");
            throw new IllegalStateException(sb.toString(), failureCause());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected the service to be ");
        sb2.append(state);
        sb2.append(", but was ");
        sb2.append(state2);
        throw new IllegalStateException(sb2.toString());
    }

    /* access modifiers changed from: protected */
    public final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state == State.STARTING) {
                if (this.snapshot.shutdownWhenStartupFinishes) {
                    this.snapshot = new StateSnapshot(State.STOPPING);
                    doStop();
                } else {
                    this.snapshot = new StateSnapshot(State.RUNNING);
                    running();
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot notifyStarted() when the service is ");
            sb.append(this.snapshot.state);
            IllegalStateException illegalStateException = new IllegalStateException(sb.toString());
            notifyFailed(illegalStateException);
            throw illegalStateException;
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyStopped() {
        this.monitor.enter();
        try {
            State state = this.snapshot.state;
            if (state != State.STOPPING) {
                if (state != State.RUNNING) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot notifyStopped() when the service is ");
                    sb.append(state);
                    IllegalStateException illegalStateException = new IllegalStateException(sb.toString());
                    notifyFailed(illegalStateException);
                    throw illegalStateException;
                }
            }
            this.snapshot = new StateSnapshot(State.TERMINATED);
            terminated(state);
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.monitor.enter();
        try {
            State state = state();
            switch (state) {
                case STARTING:
                case RUNNING:
                case STOPPING:
                    this.snapshot = new StateSnapshot(State.FAILED, false, th);
                    failed(state, th);
                    break;
                case TERMINATED:
                case NEW:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed while in state:");
                    sb.append(state);
                    throw new IllegalStateException(sb.toString(), th);
                case FAILED:
                    break;
                default:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected state: ");
                    sb2.append(state);
                    throw new AssertionError(sb2.toString());
            }
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, CastExtraArgs.LISTENER);
        Preconditions.checkNotNull(executor, "executor");
        this.monitor.enter();
        try {
            State state = state();
            if (!(state == State.TERMINATED || state == State.FAILED)) {
                this.listeners.add(new ListenerExecutorPair(listener, executor));
            }
        } finally {
            this.monitor.leave();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(state());
        sb.append("]");
        return sb.toString();
    }

    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            this.queuedListeners.execute();
        }
    }

    @GuardedBy("monitor")
    private void starting() {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {
                public void run() {
                    listenerExecutorPair.listener.starting();
                }
            }, listenerExecutorPair.executor);
        }
    }

    @GuardedBy("monitor")
    private void running() {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {
                public void run() {
                    listenerExecutorPair.listener.running();
                }
            }, listenerExecutorPair.executor);
        }
    }

    @GuardedBy("monitor")
    private void stopping(final State state) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {
                public void run() {
                    listenerExecutorPair.listener.stopping(state);
                }
            }, listenerExecutorPair.executor);
        }
    }

    @GuardedBy("monitor")
    private void terminated(final State state) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {
                public void run() {
                    listenerExecutorPair.listener.terminated(state);
                }
            }, listenerExecutorPair.executor);
        }
        this.listeners.clear();
    }

    @GuardedBy("monitor")
    private void failed(final State state, final Throwable th) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {
                public void run() {
                    listenerExecutorPair.listener.failed(state, th);
                }
            }, listenerExecutorPair.executor);
        }
        this.listeners.clear();
    }
}
