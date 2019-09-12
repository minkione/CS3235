package p009rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Observable;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.Subscriber;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTakeUntilObservable */
public final class SingleTakeUntilObservable<T, U> implements OnSubscribe<T> {
    final Observable<? extends U> other;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleTakeUntilObservable$TakeUntilSourceSubscriber */
    static final class TakeUntilSourceSubscriber<T, U> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final Subscriber<U> other = new OtherSubscriber();

        /* renamed from: rx.internal.operators.SingleTakeUntilObservable$TakeUntilSourceSubscriber$OtherSubscriber */
        final class OtherSubscriber extends Subscriber<U> {
            OtherSubscriber() {
            }

            public void onNext(U u) {
                onCompleted();
            }

            public void onError(Throwable th) {
                TakeUntilSourceSubscriber.this.onError(th);
            }

            public void onCompleted() {
                onError(new CancellationException("Single::takeUntil(Observable) - Stream was canceled before emitting a terminal event."));
            }
        }

        TakeUntilSourceSubscriber(SingleSubscriber<? super T> singleSubscriber) {
            this.actual = singleSubscriber;
            add(this.other);
        }

        public void onSuccess(T t) {
            if (this.once.compareAndSet(false, true)) {
                unsubscribe();
                this.actual.onSuccess(t);
            }
        }

        public void onError(Throwable th) {
            if (this.once.compareAndSet(false, true)) {
                unsubscribe();
                this.actual.onError(th);
                return;
            }
            RxJavaHooks.onError(th);
        }
    }

    public SingleTakeUntilObservable(OnSubscribe<T> onSubscribe, Observable<? extends U> observable) {
        this.source = onSubscribe;
        this.other = observable;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        TakeUntilSourceSubscriber takeUntilSourceSubscriber = new TakeUntilSourceSubscriber(singleSubscriber);
        singleSubscriber.add(takeUntilSourceSubscriber);
        this.other.subscribe(takeUntilSourceSubscriber.other);
        this.source.call(takeUntilSourceSubscriber);
    }
}
