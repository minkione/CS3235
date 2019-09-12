package p009rx.plugins;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observable.Operator;
import p009rx.Subscription;

/* renamed from: rx.plugins.RxJavaObservableExecutionHook */
public abstract class RxJavaObservableExecutionHook {
    @Deprecated
    public <T> OnSubscribe<T> onCreate(OnSubscribe<T> onSubscribe) {
        return onSubscribe;
    }

    @Deprecated
    public <T, R> Operator<? extends R, ? super T> onLift(Operator<? extends R, ? super T> operator) {
        return operator;
    }

    @Deprecated
    public <T> Throwable onSubscribeError(Throwable th) {
        return th;
    }

    @Deprecated
    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        return subscription;
    }

    @Deprecated
    public <T> OnSubscribe<T> onSubscribeStart(Observable<? extends T> observable, OnSubscribe<T> onSubscribe) {
        return onSubscribe;
    }
}
