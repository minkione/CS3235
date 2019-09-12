package p009rx.internal.subscriptions;

import java.util.concurrent.atomic.AtomicReference;
import p009rx.Subscription;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Cancellable;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.subscriptions.CancellableSubscription */
public final class CancellableSubscription extends AtomicReference<Cancellable> implements Subscription {
    private static final long serialVersionUID = 5718521705281392066L;

    public CancellableSubscription(Cancellable cancellable) {
        super(cancellable);
    }

    public boolean isUnsubscribed() {
        return get() == null;
    }

    public void unsubscribe() {
        if (get() != null) {
            Cancellable cancellable = (Cancellable) getAndSet(null);
            if (cancellable != null) {
                try {
                    cancellable.cancel();
                } catch (Exception e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaHooks.onError(e);
                }
            }
        }
    }
}
