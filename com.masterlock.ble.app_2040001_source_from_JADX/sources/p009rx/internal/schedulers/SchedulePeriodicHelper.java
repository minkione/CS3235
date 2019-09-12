package p009rx.internal.schedulers;

import java.util.concurrent.TimeUnit;
import p009rx.Scheduler.Worker;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.internal.subscriptions.SequentialSubscription;

/* renamed from: rx.internal.schedulers.SchedulePeriodicHelper */
public final class SchedulePeriodicHelper {
    public static final long CLOCK_DRIFT_TOLERANCE_NANOS = TimeUnit.MINUTES.toNanos(Long.getLong("rx.scheduler.drift-tolerance", 15).longValue());

    /* renamed from: rx.internal.schedulers.SchedulePeriodicHelper$NowNanoSupplier */
    public interface NowNanoSupplier {
        long nowNanos();
    }

    private SchedulePeriodicHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static Subscription schedulePeriodically(Worker worker, Action0 action0, long j, long j2, TimeUnit timeUnit, NowNanoSupplier nowNanoSupplier) {
        long j3 = j;
        TimeUnit timeUnit2 = timeUnit;
        final long nanos = timeUnit2.toNanos(j2);
        final long nowNanos = nowNanoSupplier != null ? nowNanoSupplier.nowNanos() : TimeUnit.MILLISECONDS.toNanos(worker.now());
        final long nanos2 = timeUnit2.toNanos(j3) + nowNanos;
        SequentialSubscription sequentialSubscription = new SequentialSubscription();
        SequentialSubscription sequentialSubscription2 = new SequentialSubscription(sequentialSubscription);
        final Action0 action02 = action0;
        final SequentialSubscription sequentialSubscription3 = sequentialSubscription2;
        final NowNanoSupplier nowNanoSupplier2 = nowNanoSupplier;
        SequentialSubscription sequentialSubscription4 = sequentialSubscription2;
        C21781 r15 = r3;
        final Worker worker2 = worker;
        C21781 r3 = new Action0() {
            long count;
            long lastNowNanos = nowNanos;
            long startInNanos = nanos2;

            public void call() {
                long j;
                action02.call();
                if (!sequentialSubscription3.isUnsubscribed()) {
                    NowNanoSupplier nowNanoSupplier = nowNanoSupplier2;
                    long nowNanos = nowNanoSupplier != null ? nowNanoSupplier.nowNanos() : TimeUnit.MILLISECONDS.toNanos(worker2.now());
                    long j2 = SchedulePeriodicHelper.CLOCK_DRIFT_TOLERANCE_NANOS + nowNanos;
                    long j3 = this.lastNowNanos;
                    if (j2 < j3 || nowNanos >= j3 + nanos + SchedulePeriodicHelper.CLOCK_DRIFT_TOLERANCE_NANOS) {
                        long j4 = nanos;
                        long j5 = nowNanos + j4;
                        long j6 = this.count + 1;
                        this.count = j6;
                        this.startInNanos = j5 - (j4 * j6);
                        j = j5;
                    } else {
                        long j7 = this.startInNanos;
                        long j8 = this.count + 1;
                        this.count = j8;
                        j = j7 + (j8 * nanos);
                    }
                    this.lastNowNanos = nowNanos;
                    sequentialSubscription3.replace(worker2.schedule(this, j - nowNanos, TimeUnit.NANOSECONDS));
                }
            }
        };
        sequentialSubscription.replace(worker.schedule(r15, j3, timeUnit2));
        return sequentialSubscription4;
    }
}
