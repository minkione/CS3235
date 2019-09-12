package p009rx.observers;

import p009rx.CompletableSubscriber;
import p009rx.Subscription;
import p009rx.exceptions.CompositeException;
import p009rx.exceptions.Exceptions;
import p009rx.exceptions.OnCompletedFailedException;
import p009rx.exceptions.OnErrorFailedException;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.observers.SafeCompletableSubscriber */
public final class SafeCompletableSubscriber implements CompletableSubscriber, Subscription {
    final CompletableSubscriber actual;
    boolean done;

    /* renamed from: s */
    Subscription f273s;

    public SafeCompletableSubscriber(CompletableSubscriber completableSubscriber) {
        this.actual = completableSubscriber;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                throw new OnCompletedFailedException(th);
            }
        }
    }

    public void onError(Throwable th) {
        if (this.done) {
            RxJavaHooks.onError(th);
            return;
        }
        this.done = true;
        try {
            this.actual.onError(th);
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            throw new OnErrorFailedException(new CompositeException(th, th2));
        }
    }

    public void onSubscribe(Subscription subscription) {
        this.f273s = subscription;
        try {
            this.actual.onSubscribe(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            subscription.unsubscribe();
            onError(th);
        }
    }

    public void unsubscribe() {
        this.f273s.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return this.done || this.f273s.isUnsubscribed();
    }
}
