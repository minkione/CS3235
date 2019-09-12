package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter {
    double maxPermits;
    private final Object mutex;
    private long nextFreeTicketMicros;
    private final long offsetNanos;
    volatile double stableIntervalMicros;
    double storedPermits;
    private final SleepingTicker ticker;

    private static class Bursty extends RateLimiter {
        final double maxBurstSeconds;

        /* access modifiers changed from: 0000 */
        public long storedPermitsToWaitTime(double d, double d2) {
            return 0;
        }

        Bursty(SleepingTicker sleepingTicker, double d) {
            super(sleepingTicker);
            this.maxBurstSeconds = d;
        }

        /* access modifiers changed from: 0000 */
        public void doSetRate(double d, double d2) {
            double d3 = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * d;
            double d4 = 0.0d;
            if (d3 != 0.0d) {
                d4 = (this.storedPermits * this.maxPermits) / d3;
            }
            this.storedPermits = d4;
        }
    }

    @VisibleForTesting
    static abstract class SleepingTicker extends Ticker {
        static final SleepingTicker SYSTEM_TICKER = new SleepingTicker() {
            public long read() {
                return systemTicker().read();
            }

            public void sleepMicrosUninterruptibly(long j) {
                if (j > 0) {
                    Uninterruptibles.sleepUninterruptibly(j, TimeUnit.MICROSECONDS);
                }
            }
        };

        /* access modifiers changed from: 0000 */
        public abstract void sleepMicrosUninterruptibly(long j);

        SleepingTicker() {
        }
    }

    private static class WarmingUp extends RateLimiter {
        private double halfPermits;
        private double slope;
        final long warmupPeriodMicros;

        WarmingUp(SleepingTicker sleepingTicker, long j, TimeUnit timeUnit) {
            super(sleepingTicker);
            this.warmupPeriodMicros = timeUnit.toMicros(j);
        }

        /* access modifiers changed from: 0000 */
        public void doSetRate(double d, double d2) {
            double d3 = this.maxPermits;
            double d4 = (double) this.warmupPeriodMicros;
            Double.isNaN(d4);
            this.maxPermits = d4 / d2;
            this.halfPermits = this.maxPermits / 2.0d;
            this.slope = ((3.0d * d2) - d2) / this.halfPermits;
            if (d3 == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = d3 == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / d3;
            }
        }

        /* access modifiers changed from: 0000 */
        public long storedPermitsToWaitTime(double d, double d2) {
            long j;
            double d3 = d - this.halfPermits;
            if (d3 > 0.0d) {
                double min = Math.min(d3, d2);
                j = (long) (((permitsToTime(d3) + permitsToTime(d3 - min)) * min) / 2.0d);
                d2 -= min;
            } else {
                j = 0;
            }
            double d4 = (double) j;
            double d5 = this.stableIntervalMicros * d2;
            Double.isNaN(d4);
            return (long) (d4 + d5);
        }

        private double permitsToTime(double d) {
            return this.stableIntervalMicros + (d * this.slope);
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract void doSetRate(double d, double d2);

    /* access modifiers changed from: 0000 */
    public abstract long storedPermitsToWaitTime(double d, double d2);

    public static RateLimiter create(double d) {
        return create(SleepingTicker.SYSTEM_TICKER, d);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker sleepingTicker, double d) {
        Bursty bursty = new Bursty(sleepingTicker, 1.0d);
        bursty.setRate(d);
        return bursty;
    }

    public static RateLimiter create(double d, long j, TimeUnit timeUnit) {
        return create(SleepingTicker.SYSTEM_TICKER, d, j, timeUnit);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker sleepingTicker, double d, long j, TimeUnit timeUnit) {
        WarmingUp warmingUp = new WarmingUp(sleepingTicker, j, timeUnit);
        warmingUp.setRate(d);
        return warmingUp;
    }

    @VisibleForTesting
    static RateLimiter createWithCapacity(SleepingTicker sleepingTicker, double d, long j, TimeUnit timeUnit) {
        double nanos = (double) timeUnit.toNanos(j);
        Double.isNaN(nanos);
        Bursty bursty = new Bursty(sleepingTicker, nanos / 1.0E9d);
        bursty.setRate(d);
        return bursty;
    }

    private RateLimiter(SleepingTicker sleepingTicker) {
        this.mutex = new Object();
        this.nextFreeTicketMicros = 0;
        this.ticker = sleepingTicker;
        this.offsetNanos = sleepingTicker.read();
    }

    public final void setRate(double d) {
        Preconditions.checkArgument(d > 0.0d && !Double.isNaN(d), "rate must be positive");
        synchronized (this.mutex) {
            resync(readSafeMicros());
            double micros = (double) TimeUnit.SECONDS.toMicros(1);
            Double.isNaN(micros);
            double d2 = micros / d;
            this.stableIntervalMicros = d2;
            doSetRate(d, d2);
        }
    }

    public final double getRate() {
        double micros = (double) TimeUnit.SECONDS.toMicros(1);
        double d = this.stableIntervalMicros;
        Double.isNaN(micros);
        return micros / d;
    }

    public void acquire() {
        acquire(1);
    }

    public void acquire(int i) {
        long reserveNextTicket;
        checkPermits(i);
        synchronized (this.mutex) {
            reserveNextTicket = reserveNextTicket((double) i, readSafeMicros());
        }
        this.ticker.sleepMicrosUninterruptibly(reserveNextTicket);
    }

    public boolean tryAcquire(long j, TimeUnit timeUnit) {
        return tryAcquire(1, j, timeUnit);
    }

    public boolean tryAcquire(int i) {
        return tryAcquire(i, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int i, long j, TimeUnit timeUnit) {
        long micros = timeUnit.toMicros(j);
        checkPermits(i);
        synchronized (this.mutex) {
            long readSafeMicros = readSafeMicros();
            if (this.nextFreeTicketMicros > micros + readSafeMicros) {
                return false;
            }
            long reserveNextTicket = reserveNextTicket((double) i, readSafeMicros);
            this.ticker.sleepMicrosUninterruptibly(reserveNextTicket);
            return true;
        }
    }

    private static void checkPermits(int i) {
        Preconditions.checkArgument(i > 0, "Requested permits must be positive");
    }

    private long reserveNextTicket(double d, long j) {
        resync(j);
        long j2 = this.nextFreeTicketMicros - j;
        double min = Math.min(d, this.storedPermits);
        this.nextFreeTicketMicros += storedPermitsToWaitTime(this.storedPermits, min) + ((long) ((d - min) * this.stableIntervalMicros));
        this.storedPermits -= min;
        return j2;
    }

    private void resync(long j) {
        long j2 = this.nextFreeTicketMicros;
        if (j > j2) {
            double d = this.maxPermits;
            double d2 = this.storedPermits;
            double d3 = (double) (j - j2);
            double d4 = this.stableIntervalMicros;
            Double.isNaN(d3);
            this.storedPermits = Math.min(d, d2 + (d3 / d4));
            this.nextFreeTicketMicros = j;
        }
    }

    private long readSafeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
    }

    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(1000000.0d / this.stableIntervalMicros)});
    }
}
