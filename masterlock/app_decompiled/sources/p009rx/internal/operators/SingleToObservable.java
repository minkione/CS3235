package p009rx.internal.operators;

import p009rx.Observable.OnSubscribe;
import p009rx.Single;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.SingleToObservable */
public final class SingleToObservable<T> implements OnSubscribe<T> {
    final Single.OnSubscribe<T> source;

    public SingleToObservable(Single.OnSubscribe<T> onSubscribe) {
        this.source = onSubscribe;
    }

    public void call(Subscriber<? super T> subscriber) {
        WrapSubscriberIntoSingle wrapSubscriberIntoSingle = new WrapSubscriberIntoSingle(subscriber);
        subscriber.add(wrapSubscriberIntoSingle);
        this.source.call(wrapSubscriberIntoSingle);
    }
}
