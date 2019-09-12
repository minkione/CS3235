package p009rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.OnSubscribeTimerOnce */
public final class OnSubscribeTimerOnce implements OnSubscribe<Long> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OnSubscribeTimerOnce(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super Long> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedule(new Action0() {
            public void call() {
                try {
                    subscriber.onNext(Long.valueOf(0));
                    subscriber.onCompleted();
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer<?>) subscriber);
                }
            }
        }, this.time, this.unit);
    }
}
