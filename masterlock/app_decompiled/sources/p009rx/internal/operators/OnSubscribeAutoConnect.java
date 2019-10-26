package p009rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.observables.ConnectableObservable;
import p009rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeAutoConnect */
public final class OnSubscribeAutoConnect<T> extends AtomicInteger implements OnSubscribe<T> {
    final Action1<? super Subscription> connection;
    final int numberOfSubscribers;
    final ConnectableObservable<? extends T> source;

    public OnSubscribeAutoConnect(ConnectableObservable<? extends T> connectableObservable, int i, Action1<? super Subscription> action1) {
        if (i > 0) {
            this.source = connectableObservable;
            this.numberOfSubscribers = i;
            this.connection = action1;
            return;
        }
        throw new IllegalArgumentException("numberOfSubscribers > 0 required");
    }

    public void call(Subscriber<? super T> subscriber) {
        this.source.unsafeSubscribe(Subscribers.wrap(subscriber));
        if (incrementAndGet() == this.numberOfSubscribers) {
            this.source.connect(this.connection);
        }
    }
}
