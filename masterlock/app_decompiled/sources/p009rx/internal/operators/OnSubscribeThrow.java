package p009rx.internal.operators;

import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OnSubscribeThrow */
public final class OnSubscribeThrow<T> implements OnSubscribe<T> {
    private final Throwable exception;

    public OnSubscribeThrow(Throwable th) {
        this.exception = th;
    }

    public void call(Subscriber<? super T> subscriber) {
        subscriber.onError(this.exception);
    }
}
