package p009rx.internal.operators;

import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.SingleObserveOn */
public final class SingleObserveOn<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleObserveOn$ObserveOnSingleSubscriber */
    static final class ObserveOnSingleSubscriber<T> extends SingleSubscriber<T> implements Action0 {
        final SingleSubscriber<? super T> actual;
        Throwable error;
        T value;

        /* renamed from: w */
        final Worker f255w;

        public ObserveOnSingleSubscriber(SingleSubscriber<? super T> singleSubscriber, Worker worker) {
            this.actual = singleSubscriber;
            this.f255w = worker;
        }

        public void onSuccess(T t) {
            this.value = t;
            this.f255w.schedule(this);
        }

        public void onError(Throwable th) {
            this.error = th;
            this.f255w.schedule(this);
        }

        public void call() {
            try {
                Throwable th = this.error;
                if (th != null) {
                    this.error = null;
                    this.actual.onError(th);
                } else {
                    T t = this.value;
                    this.value = null;
                    this.actual.onSuccess(t);
                }
            } finally {
                this.f255w.unsubscribe();
            }
        }
    }

    public SingleObserveOn(OnSubscribe<T> onSubscribe, Scheduler scheduler2) {
        this.source = onSubscribe;
        this.scheduler = scheduler2;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        Worker createWorker = this.scheduler.createWorker();
        ObserveOnSingleSubscriber observeOnSingleSubscriber = new ObserveOnSingleSubscriber(singleSubscriber, createWorker);
        singleSubscriber.add(createWorker);
        singleSubscriber.add(observeOnSingleSubscriber);
        this.source.call(observeOnSingleSubscriber);
    }
}
