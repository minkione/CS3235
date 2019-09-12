package p009rx.internal.operators;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.OperatorSubscribeOn */
public final class OperatorSubscribeOn<T> implements OnSubscribe<T> {
    final boolean requestOn;
    final Scheduler scheduler;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OperatorSubscribeOn$SubscribeOnSubscriber */
    static final class SubscribeOnSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> actual;
        final boolean requestOn;
        Observable<T> source;

        /* renamed from: t */
        Thread f252t;
        final Worker worker;

        SubscribeOnSubscriber(Subscriber<? super T> subscriber, boolean z, Worker worker2, Observable<T> observable) {
            this.actual = subscriber;
            this.requestOn = z;
            this.worker = worker2;
            this.source = observable;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            try {
                this.actual.onError(th);
            } finally {
                this.worker.unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.actual.onCompleted();
            } finally {
                this.worker.unsubscribe();
            }
        }

        public void call() {
            Observable<T> observable = this.source;
            this.source = null;
            this.f252t = Thread.currentThread();
            observable.unsafeSubscribe(this);
        }

        public void setProducer(final Producer producer) {
            this.actual.setProducer(new Producer() {
                public void request(final long j) {
                    if (SubscribeOnSubscriber.this.f252t == Thread.currentThread() || !SubscribeOnSubscriber.this.requestOn) {
                        producer.request(j);
                    } else {
                        SubscribeOnSubscriber.this.worker.schedule(new Action0() {
                            public void call() {
                                producer.request(j);
                            }
                        });
                    }
                }
            });
        }
    }

    public OperatorSubscribeOn(Observable<T> observable, Scheduler scheduler2, boolean z) {
        this.scheduler = scheduler2;
        this.source = observable;
        this.requestOn = z;
    }

    public void call(Subscriber<? super T> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        SubscribeOnSubscriber subscribeOnSubscriber = new SubscribeOnSubscriber(subscriber, this.requestOn, createWorker, this.source);
        subscriber.add(subscribeOnSubscriber);
        subscriber.add(createWorker);
        createWorker.schedule(subscribeOnSubscriber);
    }
}
