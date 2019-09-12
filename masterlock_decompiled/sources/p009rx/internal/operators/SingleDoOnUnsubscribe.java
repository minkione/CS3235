package p009rx.internal.operators;

import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.functions.Action0;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.SingleDoOnUnsubscribe */
public final class SingleDoOnUnsubscribe<T> implements OnSubscribe<T> {
    final Action0 onUnsubscribe;
    final OnSubscribe<T> source;

    public SingleDoOnUnsubscribe(OnSubscribe<T> onSubscribe, Action0 action0) {
        this.source = onSubscribe;
        this.onUnsubscribe = action0;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        singleSubscriber.add(Subscriptions.create(this.onUnsubscribe));
        this.source.call(singleSubscriber);
    }
}
