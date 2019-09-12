package p009rx;

import p009rx.internal.util.SubscriptionList;

/* renamed from: rx.SingleSubscriber */
public abstract class SingleSubscriber<T> implements Subscription {

    /* renamed from: cs */
    private final SubscriptionList f234cs = new SubscriptionList();

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public final void add(Subscription subscription) {
        this.f234cs.add(subscription);
    }

    public final void unsubscribe() {
        this.f234cs.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.f234cs.isUnsubscribed();
    }
}
