package p009rx;

import java.util.concurrent.TimeUnit;
import p009rx.functions.Action0;
import p009rx.functions.Func1;
import p009rx.internal.schedulers.SchedulePeriodicHelper;
import p009rx.internal.schedulers.SchedulerWhen;

/* renamed from: rx.Scheduler */
public abstract class Scheduler {

    /* renamed from: rx.Scheduler$Worker */
    public static abstract class Worker implements Subscription {
        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action0, long j, long j2, TimeUnit timeUnit) {
            return SchedulePeriodicHelper.schedulePeriodically(this, action0, j, j2, timeUnit, null);
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public abstract Worker createWorker();

    public long now() {
        return System.currentTimeMillis();
    }

    public <S extends Scheduler & Subscription> S when(Func1<Observable<Observable<Completable>>, Completable> func1) {
        return new SchedulerWhen(func1, this);
    }
}
