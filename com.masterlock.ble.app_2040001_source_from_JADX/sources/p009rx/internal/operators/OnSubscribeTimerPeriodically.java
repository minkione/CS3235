package p009rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.OnSubscribeTimerPeriodically */
public final class OnSubscribeTimerPeriodically implements OnSubscribe<Long> {
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    public OnSubscribeTimerPeriodically(long j, long j2, TimeUnit timeUnit, Scheduler scheduler2) {
        this.initialDelay = j;
        this.period = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super Long> subscriber) {
        final Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedulePeriodically(new Action0() {
            long counter;

            public void call() {
                try {
                    Subscriber subscriber = subscriber;
                    long j = this.counter;
                    this.counter = 1 + j;
                    subscriber.onNext(Long.valueOf(j));
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer<?>) subscriber);
                    throw th;
                }
            }
        }, this.initialDelay, this.period, this.unit);
    }
}
