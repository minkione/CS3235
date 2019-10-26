package p009rx;

import p009rx.functions.Cancellable;

/* renamed from: rx.CompletableEmitter */
public interface CompletableEmitter {
    void onCompleted();

    void onError(Throwable th);

    void setCancellation(Cancellable cancellable);

    void setSubscription(Subscription subscription);
}
