package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Deprecated
@Beta
public abstract class ForwardingService extends ForwardingObject implements Service {
    /* access modifiers changed from: protected */
    public abstract Service delegate();

    protected ForwardingService() {
    }

    @Deprecated
    public ListenableFuture<State> start() {
        return delegate().start();
    }

    public State state() {
        return delegate().state();
    }

    @Deprecated
    public ListenableFuture<State> stop() {
        return delegate().stop();
    }

    @Deprecated
    public State startAndWait() {
        return delegate().startAndWait();
    }

    @Deprecated
    public State stopAndWait() {
        return delegate().stopAndWait();
    }

    public boolean isRunning() {
        return delegate().isRunning();
    }

    public void addListener(Listener listener, Executor executor) {
        delegate().addListener(listener, executor);
    }

    public Throwable failureCause() {
        return delegate().failureCause();
    }

    public Service startAsync() {
        delegate().startAsync();
        return this;
    }

    public Service stopAsync() {
        delegate().stopAsync();
        return this;
    }

    public void awaitRunning() {
        delegate().awaitRunning();
    }

    public void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        delegate().awaitRunning(j, timeUnit);
    }

    public void awaitTerminated() {
        delegate().awaitTerminated();
    }

    public void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        delegate().awaitTerminated(j, timeUnit);
    }

    /* access modifiers changed from: protected */
    public State standardStartAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    /* access modifiers changed from: protected */
    public State standardStopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }
}
