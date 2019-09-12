package p009rx.plugins;

import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.Completable.Operator;

/* renamed from: rx.plugins.RxJavaCompletableExecutionHook */
public abstract class RxJavaCompletableExecutionHook {
    @Deprecated
    public OnSubscribe onCreate(OnSubscribe onSubscribe) {
        return onSubscribe;
    }

    @Deprecated
    public Operator onLift(Operator operator) {
        return operator;
    }

    @Deprecated
    public Throwable onSubscribeError(Throwable th) {
        return th;
    }

    @Deprecated
    public OnSubscribe onSubscribeStart(Completable completable, OnSubscribe onSubscribe) {
        return onSubscribe;
    }
}
