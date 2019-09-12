package p009rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Single;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTakeUntilSingle */
public final class SingleTakeUntilSingle<T, U> implements OnSubscribe<T> {
    final Single<? extends U> other;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleTakeUntilSingle$TakeUntilSourceSubscriber */
    static final class TakeUntilSourceSubscriber<T, U> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final SingleSubscriber<U> other = new OtherSubscriber();

        /* renamed from: rx.internal.operators.SingleTakeUntilSingle$TakeUntilSourceSubscriber$OtherSubscriber */
        final class OtherSubscriber extends SingleSubscriber<U> {
            OtherSubscriber() {
            }

            public void onSuccess(U u) {
                onError(new CancellationException("Single::takeUntil(Single) - Stream was canceled before emitting a terminal event."));
            }

            public void onError(Throwable th) {
                TakeUntilSourceSubscriber.this.onError(th);
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

    public SingleTakeUntilSingle(OnSubscribe<T> onSubscribe, Single<? extends U> single) {
        this.source = onSubscribe;
        this.other = single;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        TakeUntilSourceSubscriber takeUntilSourceSubscriber = new TakeUntilSourceSubscriber(singleSubscriber);
        singleSubscriber.add(takeUntilSourceSubscriber);
        this.other.subscribe(takeUntilSourceSubscriber.other);
        this.source.call(takeUntilSourceSubscriber);
    }
}
