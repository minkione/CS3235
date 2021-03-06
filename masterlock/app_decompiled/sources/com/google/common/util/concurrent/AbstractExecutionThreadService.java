package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    private final Service delegate = new AbstractService() {
        /* access modifiers changed from: protected */
        public final void doStart() {
            MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), (Supplier<String>) new Supplier<String>() {
                public String get() {
                    return AbstractExecutionThreadService.this.serviceName();
                }
            }).execute(new Runnable() {
                public void run() {
                    try {
                        AbstractExecutionThreadService.this.startUp();
                        C08991.this.notifyStarted();
                        if (C08991.this.isRunning()) {
                            AbstractExecutionThreadService.this.run();
                        }
                        AbstractExecutionThreadService.this.shutDown();
                        C08991.this.notifyStopped();
                    } catch (Throwable th) {
                        C08991.this.notifyFailed(th);
                        throw Throwables.propagate(th);
                    }
                }
            });
        }

        /* access modifiers changed from: protected */
        public void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    };

    /* access modifiers changed from: protected */
    public abstract void run() throws Exception;

    /* access modifiers changed from: protected */
    public void shutDown() throws Exception {
    }

    /* access modifiers changed from: protected */
    public void startUp() throws Exception {
    }

    /* access modifiers changed from: protected */
    public void triggerShutdown() {
    }

    protected AbstractExecutionThreadService() {
    }

    /* access modifiers changed from: protected */
    public Executor executor() {
        return new Executor() {
            public void execute(Runnable runnable) {
                MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), runnable).start();
            }
        };
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceName());
        sb.append(" [");
        sb.append(state());
        sb.append("]");
        return sb.toString();
    }

    @Deprecated
    public final ListenableFuture<State> start() {
        return this.delegate.start();
    }

    @Deprecated
    public final State startAndWait() {
        return this.delegate.startAndWait();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    public final State state() {
        return this.delegate.state();
    }

    @Deprecated
    public final ListenableFuture<State> stop() {
        return this.delegate.stop();
    }

    @Deprecated
    public final State stopAndWait() {
        return this.delegate.stopAndWait();
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        this.delegate.awaitRunning(j, timeUnit);
    }

    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        this.delegate.awaitTerminated(j, timeUnit);
    }

    /* access modifiers changed from: protected */
    public String serviceName() {
        return getClass().getSimpleName();
    }
}
