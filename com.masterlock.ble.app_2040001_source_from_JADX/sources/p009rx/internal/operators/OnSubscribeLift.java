package p009rx.internal.operators;

import p009rx.Observable.OnSubscribe;
import p009rx.Observable.Operator;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeLift */
public final class OnSubscribeLift<T, R> implements OnSubscribe<R> {
    final Operator<? extends R, ? super T> operator;
    final OnSubscribe<T> parent;

    public OnSubscribeLift(OnSubscribe<T> onSubscribe, Operator<? extends R, ? super T> operator2) {
        this.parent = onSubscribe;
        this.operator = operator2;
    }

    public void call(Subscriber<? super R> subscriber) {
        Subscriber subscriber2;
        try {
            subscriber2 = (Subscriber) RxJavaHooks.onObservableLift(this.operator).call(subscriber);
            subscriber2.onStart();
            this.parent.call(subscriber2);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            subscriber.onError(th);
        }
    }
}
