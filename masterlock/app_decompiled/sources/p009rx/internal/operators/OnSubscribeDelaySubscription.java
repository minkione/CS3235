package p009rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.functions.Action0;
import p009rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeDelaySubscription */
public final class OnSubscribeDelaySubscription<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<? extends T> source;
    final long time;
    final TimeUnit unit;

    public OnSubscribeDelaySubscription(Observable<? extends T> observable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.source = observable;
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super T> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedule(new Action0() {
            public void call() {
                if (!subscriber.isUnsubscribed()) {
                    OnSubscribeDelaySubscription.this.source.unsafeSubscribe(Subscribers.wrap(subscriber));
                }
            }
        }, this.time, this.unit);
    }
}
