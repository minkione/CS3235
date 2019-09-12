package com.google.common.util.concurrent;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Beta
public final class ServiceManager {
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
    private final ImmutableMap<Service, ServiceListener> services;
    private final ServiceManagerState state;

    private static final class EmptyServiceManagerWarning extends Throwable {
        private EmptyServiceManagerWarning() {
        }
    }

    @Beta
    public static abstract class Listener {
        public void failure(Service service) {
        }

        public void healthy() {
        }

        public void stopped() {
        }
    }

    @Immutable
    private static final class ListenerExecutorPair {
        final Executor executor;
        final Listener listener;

        ListenerExecutorPair(Listener listener2, Executor executor2) {
            this.listener = listener2;
            this.executor = executor2;
        }
    }

    private static final class NoOpService extends AbstractService {
        private NoOpService() {
        }

        /* access modifiers changed from: protected */
        public void doStart() {
            notifyStarted();
        }

        /* access modifiers changed from: protected */
        public void doStop() {
            notifyStopped();
        }
    }

    private static final class ServiceListener extends com.google.common.util.concurrent.Service.Listener {
        final Service service;
        final ServiceManagerState state;
        @GuardedBy("watch")
        final Stopwatch watch = Stopwatch.createUnstarted();

        ServiceListener(Service service2, ServiceManagerState serviceManagerState) {
            this.service = service2;
            this.state = serviceManagerState;
        }

        public void starting() {
            startTimer();
        }

        public void running() {
            this.state.monitor.enter();
            try {
                finishedStarting(true);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        public void stopping(State state2) {
            if (state2 == State.STARTING) {
                this.state.monitor.enter();
                try {
                    finishedStarting(false);
                } finally {
                    this.state.monitor.leave();
                    this.state.executeListeners();
                }
            }
        }

        public void terminated(State state2) {
            if (!(this.service instanceof NoOpService)) {
                ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[]{this.service, state2});
            }
            this.state.monitor.enter();
            try {
                if (state2 == State.NEW) {
                    startTimer();
                    finishedStarting(false);
                }
                this.state.serviceTerminated(this.service);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        public void failed(State state2, Throwable th) {
            Logger access$300 = ServiceManager.logger;
            Level level = Level.SEVERE;
            StringBuilder sb = new StringBuilder();
            sb.append("Service ");
            sb.append(this.service);
            sb.append(" has failed in the ");
            sb.append(state2);
            sb.append(" state.");
            access$300.log(level, sb.toString(), th);
            this.state.monitor.enter();
            try {
                if (state2 == State.STARTING) {
                    finishedStarting(false);
                }
                this.state.serviceFailed(this.service);
            } finally {
                this.state.monitor.leave();
                this.state.executeListeners();
            }
        }

        /* access modifiers changed from: 0000 */
        @GuardedBy("monitor")
        public void finishedStarting(boolean z) {
            synchronized (this.watch) {
                this.watch.stop();
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Started {0} in {1} ms.", new Object[]{this.service, Long.valueOf(startupTimeMillis())});
                }
            }
            this.state.serviceFinishedStarting(this.service, z);
        }

        /* access modifiers changed from: 0000 */
        public void start() {
            startTimer();
            this.service.startAsync();
        }

        /* access modifiers changed from: 0000 */
        public void startTimer() {
            synchronized (this.watch) {
                if (!this.watch.isRunning()) {
                    this.watch.start();
                    if (!(this.service instanceof NoOpService)) {
                        ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public long startupTimeMillis() {
            long elapsed;
            synchronized (this.watch) {
                elapsed = this.watch.elapsed(TimeUnit.MILLISECONDS);
            }
            return elapsed;
        }
    }

    private static final class ServiceManagerState {
        final Guard awaitHealthGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                boolean z = true;
                boolean z2 = ServiceManagerState.this.unstartedServices == 0;
                if (ServiceManagerState.this.unstoppedServices == ServiceManagerState.this.numberOfServices) {
                    z = false;
                }
                return z2 | z;
            }
        };
        @GuardedBy("monitor")
        final List<ListenerExecutorPair> listeners = Lists.newArrayList();
        final Monitor monitor = new Monitor();
        final int numberOfServices;
        @GuardedBy("monitor")
        final ExecutionQueue queuedListeners = new ExecutionQueue();
        final Guard stoppedGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                return ServiceManagerState.this.unstoppedServices == 0;
            }
        };
        @GuardedBy("monitor")
        int unstartedServices;
        @GuardedBy("monitor")
        int unstoppedServices;

        ServiceManagerState(int i) {
            this.numberOfServices = i;
            this.unstoppedServices = i;
            this.unstartedServices = i;
        }

        /* access modifiers changed from: 0000 */
        public void addListener(Listener listener, Executor executor) {
            Preconditions.checkNotNull(listener, CastExtraArgs.LISTENER);
            Preconditions.checkNotNull(executor, "executor");
            this.monitor.enter();
            try {
                if (this.unstartedServices > 0 || this.unstoppedServices > 0) {
                    this.listeners.add(new ListenerExecutorPair(listener, executor));
                }
            } finally {
                this.monitor.leave();
            }
        }

        /* access modifiers changed from: 0000 */
        public void awaitHealthy() {
            this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
            this.monitor.leave();
        }

        /* access modifiers changed from: 0000 */
        public boolean awaitHealthy(long j, TimeUnit timeUnit) {
            if (!this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard, j, timeUnit)) {
                return false;
            }
            this.monitor.leave();
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void awaitStopped() {
            this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
            this.monitor.leave();
        }

        /* access modifiers changed from: 0000 */
        public boolean awaitStopped(long j, TimeUnit timeUnit) {
            if (!this.monitor.enterWhenUninterruptibly(this.stoppedGuard, j, timeUnit)) {
                return false;
            }
            this.monitor.leave();
            return true;
        }

        /* access modifiers changed from: private */
        @GuardedBy("monitor")
        public void serviceFinishedStarting(Service service, boolean z) {
            Preconditions.checkState(this.unstartedServices > 0, "All services should have already finished starting but %s just finished.", service);
            this.unstartedServices--;
            if (z && this.unstartedServices == 0 && this.unstoppedServices == this.numberOfServices) {
                for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
                    this.queuedListeners.add(new Runnable() {
                        public void run() {
                            listenerExecutorPair.listener.healthy();
                        }
                    }, listenerExecutorPair.executor);
                }
            }
        }

        /* access modifiers changed from: private */
        @GuardedBy("monitor")
        public void serviceTerminated(Service service) {
            serviceStopped(service);
        }

        /* access modifiers changed from: private */
        @GuardedBy("monitor")
        public void serviceFailed(final Service service) {
            for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
                this.queuedListeners.add(new Runnable() {
                    public void run() {
                        listenerExecutorPair.listener.failure(service);
                    }
                }, listenerExecutorPair.executor);
            }
            serviceStopped(service);
        }

        @GuardedBy("monitor")
        private void serviceStopped(Service service) {
            Preconditions.checkState(this.unstoppedServices > 0, "All services should have already stopped but %s just stopped.", service);
            this.unstoppedServices--;
            if (this.unstoppedServices == 0) {
                Preconditions.checkState(this.unstartedServices == 0, "All services are stopped but %d services haven't finished starting", Integer.valueOf(this.unstartedServices));
                for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
                    this.queuedListeners.add(new Runnable() {
                        public void run() {
                            listenerExecutorPair.listener.stopped();
                        }
                    }, listenerExecutorPair.executor);
                }
                this.listeners.clear();
            }
        }

        /* access modifiers changed from: private */
        public void executeListeners() {
            Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
            this.queuedListeners.execute();
        }
    }

    public ServiceManager(Iterable<? extends Service> iterable) {
        ImmutableList copyOf = ImmutableList.copyOf(iterable);
        if (copyOf.isEmpty()) {
            logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
            copyOf = ImmutableList.m66of(new NoOpService());
        }
        this.state = new ServiceManagerState(copyOf.size());
        Builder builder = ImmutableMap.builder();
        ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();
        Iterator it = copyOf.iterator();
        while (it.hasNext()) {
            Service service = (Service) it.next();
            ServiceListener serviceListener = new ServiceListener(service, this.state);
            service.addListener(serviceListener, sameThreadExecutor);
            Preconditions.checkArgument(service.state() == State.NEW, "Can only manage NEW services, %s", service);
            builder.put(service, serviceListener);
        }
        this.services = builder.build();
    }

    @Inject
    ServiceManager(Set<Service> set) {
        this((Iterable<? extends Service>) set);
    }

    public void addListener(Listener listener, Executor executor) {
        this.state.addListener(listener, executor);
    }

    public void addListener(Listener listener) {
        this.state.addListener(listener, MoreExecutors.sameThreadExecutor());
    }

    public ServiceManager startAsync() {
        Iterator it = this.services.entrySet().iterator();
        while (it.hasNext()) {
            Service service = (Service) ((Entry) it.next()).getKey();
            State state2 = service.state();
            Preconditions.checkState(state2 == State.NEW, "Service %s is %s, cannot start it.", service, state2);
        }
        Iterator it2 = this.services.values().iterator();
        while (it2.hasNext()) {
            ServiceListener serviceListener = (ServiceListener) it2.next();
            try {
                serviceListener.start();
            } catch (IllegalStateException e) {
                Logger logger2 = logger;
                Level level = Level.WARNING;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to start Service ");
                sb.append(serviceListener.service);
                logger2.log(level, sb.toString(), e);
            }
        }
        return this;
    }

    public void awaitHealthy() {
        this.state.awaitHealthy();
        Preconditions.checkState(isHealthy(), "Expected to be healthy after starting");
    }

    public void awaitHealthy(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.state.awaitHealthy(j, timeUnit)) {
            Preconditions.checkState(isHealthy(), "Expected to be healthy after starting");
            return;
        }
        throw new TimeoutException("Timeout waiting for the services to become healthy.");
    }

    public ServiceManager stopAsync() {
        Iterator it = this.services.keySet().iterator();
        while (it.hasNext()) {
            ((Service) it.next()).stop();
        }
        return this;
    }

    public void awaitStopped() {
        this.state.awaitStopped();
    }

    public void awaitStopped(long j, TimeUnit timeUnit) throws TimeoutException {
        if (!this.state.awaitStopped(j, timeUnit)) {
            throw new TimeoutException("Timeout waiting for the services to stop.");
        }
    }

    public boolean isHealthy() {
        Iterator it = this.services.keySet().iterator();
        while (it.hasNext()) {
            if (!((Service) it.next()).isRunning()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableMultimap<State, Service> servicesByState() {
        ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
        Iterator it = this.services.keySet().iterator();
        while (it.hasNext()) {
            Service service = (Service) it.next();
            if (!(service instanceof NoOpService)) {
                builder.put(service.state(), service);
            }
        }
        return builder.build();
    }

    public ImmutableMap<Service, Long> startupTimes() {
        ArrayList<Entry> newArrayListWithCapacity = Lists.newArrayListWithCapacity(this.services.size());
        Iterator it = this.services.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Service service = (Service) entry.getKey();
            State state2 = service.state();
            boolean z = true;
            boolean z2 = (state2 != State.STARTING) & (state2 != State.NEW);
            if (service instanceof NoOpService) {
                z = false;
            }
            if (z2 && z) {
                newArrayListWithCapacity.add(Maps.immutableEntry(service, Long.valueOf(((ServiceListener) entry.getValue()).startupTimeMillis())));
            }
        }
        Collections.sort(newArrayListWithCapacity, Ordering.natural().onResultOf(new Function<Entry<Service, Long>, Long>() {
            public Long apply(Entry<Service, Long> entry) {
                return (Long) entry.getValue();
            }
        }));
        Builder builder = ImmutableMap.builder();
        for (Entry put : newArrayListWithCapacity) {
            builder.put(put);
        }
        return builder.build();
    }

    public String toString() {
        return Objects.toStringHelper(ServiceManager.class).add("services", (Object) Collections2.filter(this.services.keySet(), Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
    }
}
