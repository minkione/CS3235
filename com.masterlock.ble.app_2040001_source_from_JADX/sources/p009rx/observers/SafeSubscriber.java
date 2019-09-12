package p009rx.observers;

import java.util.Arrays;
import java.util.Collection;
import p009rx.Observer;
import p009rx.Subscriber;
import p009rx.exceptions.CompositeException;
import p009rx.exceptions.Exceptions;
import p009rx.exceptions.OnErrorFailedException;
import p009rx.exceptions.OnErrorNotImplementedException;
import p009rx.exceptions.UnsubscribeFailedException;
import p009rx.plugins.RxJavaHooks;
import p009rx.plugins.RxJavaPlugins;

/* renamed from: rx.observers.SafeSubscriber */
public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done;

    public SafeSubscriber(Subscriber<? super T> subscriber) {
        super(subscriber);
        this.actual = subscriber;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
                try {
                    unsubscribe();
                } catch (Throwable th) {
                    RxJavaHooks.onError(th);
                    throw new UnsubscribeFailedException(th.getMessage(), th);
                }
            } catch (Throwable th2) {
                RxJavaHooks.onError(th2);
                throw new UnsubscribeFailedException(th2.getMessage(), th2);
            }
        }
    }

    public void onError(Throwable th) {
        Exceptions.throwIfFatal(th);
        if (!this.done) {
            this.done = true;
            _onError(th);
        }
    }

    public void onNext(T t) {
        try {
            if (!this.done) {
                this.actual.onNext(t);
            }
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer<?>) this);
        }
    }

    /* access modifiers changed from: protected */
    public void _onError(Throwable th) {
        RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
        try {
            this.actual.onError(th);
            try {
                unsubscribe();
            } catch (Throwable th2) {
                RxJavaHooks.onError(th2);
                throw new OnErrorFailedException(th2);
            }
        } catch (OnErrorNotImplementedException e) {
            unsubscribe();
            throw e;
        } catch (Throwable th3) {
            RxJavaHooks.onError(th3);
            throw new OnErrorNotImplementedException("Observer.onError not implemented and error while unsubscribing.", new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{th, th3})));
        }
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
