package p009rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.OnSubscribeSkipTimed */
public final class OnSubscribeSkipTimed<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<T> source;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeSkipTimed$SkipTimedSubscriber */
    static final class SkipTimedSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        volatile boolean gate;

        SkipTimedSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        public void call() {
            this.gate = true;
        }

        public void onNext(T t) {
            if (this.gate) {
                this.child.onNext(t);
            }
        }

        public void onError(Throwable th) {
            try {
                this.child.onError(th);
            } finally {
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.child.onCompleted();
            } finally {
                unsubscribe();
            }
        }
    }

    public OnSubscribeSkipTimed(Observable<T> observable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.source = observable;
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void call(Subscriber<? super T> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        SkipTimedSubscriber skipTimedSubscriber = new SkipTimedSubscriber(subscriber);
        skipTimedSubscriber.add(createWorker);
        subscriber.add(skipTimedSubscriber);
        createWorker.schedule(skipTimedSubscriber, this.time, this.unit);
        this.source.unsafeSubscribe(skipTimedSubscriber);
    }
}
