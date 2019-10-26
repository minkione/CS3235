package p009rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Completable;
import p009rx.CompletableSubscriber;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.Subscription;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTakeUntilCompletable */
public final class SingleTakeUntilCompletable<T> implements OnSubscribe<T> {
    final Completable other;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleTakeUntilCompletable$TakeUntilSourceSubscriber */
    static final class TakeUntilSourceSubscriber<T> extends SingleSubscriber<T> implements CompletableSubscriber {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();

        TakeUntilSourceSubscriber(SingleSubscriber<? super T> singleSubscriber) {
            this.actual = singleSubscriber;
        }

        public void onSubscribe(Subscription subscription) {
            add(subscription);
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

        public void onCompleted() {
            onError(new CancellationException("Single::takeUntil(Completable) - Stream was canceled before emitting a terminal event."));
        }
    }

    public SingleTakeUntilCompletable(OnSubscribe<T> onSubscribe, Completable completable) {
        this.source = onSubscribe;
        this.other = completable;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        TakeUntilSourceSubscriber takeUntilSourceSubscriber = new TakeUntilSourceSubscriber(singleSubscriber);
        singleSubscriber.add(takeUntilSourceSubscriber);
        this.other.subscribe((CompletableSubscriber) takeUntilSourceSubscriber);
        this.source.call(takeUntilSourceSubscriber);
    }
}
