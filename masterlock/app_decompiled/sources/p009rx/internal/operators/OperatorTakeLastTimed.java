package p009rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Scheduler;
import p009rx.Subscriber;
import p009rx.functions.Func1;

/* renamed from: rx.internal.operators.OperatorTakeLastTimed */
public final class OperatorTakeLastTimed<T> implements Operator<T, T> {
    final long ageMillis;
    final int count;
    final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorTakeLastTimed$TakeLastTimedSubscriber */
    static final class TakeLastTimedSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final long ageMillis;
        final int count;
        final ArrayDeque<Object> queue = new ArrayDeque<>();
        final ArrayDeque<Long> queueTimes = new ArrayDeque<>();
        final AtomicLong requested = new AtomicLong();
        final Scheduler scheduler;

        public TakeLastTimedSubscriber(Subscriber<? super T> subscriber, int i, long j, Scheduler scheduler2) {
            this.actual = subscriber;
            this.count = i;
            this.ageMillis = j;
            this.scheduler = scheduler2;
        }

        public void onNext(T t) {
            if (this.count != 0) {
                long now = this.scheduler.now();
                if (this.queue.size() == this.count) {
                    this.queue.poll();
                    this.queueTimes.poll();
                }
                evictOld(now);
                this.queue.offer(NotificationLite.next(t));
                this.queueTimes.offer(Long.valueOf(now));
            }
        }

        /* access modifiers changed from: protected */
        public void evictOld(long j) {
            long j2 = j - this.ageMillis;
            while (true) {
                Long l = (Long) this.queueTimes.peek();
                if (l != null && l.longValue() < j2) {
                    this.queue.poll();
                    this.queueTimes.poll();
                } else {
                    return;
                }
            }
        }

        public void onError(Throwable th) {
            this.queue.clear();
            this.queueTimes.clear();
            this.actual.onError(th);
        }

        public void onCompleted() {
            evictOld(this.scheduler.now());
            this.queueTimes.clear();
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public T call(Object obj) {
            return NotificationLite.getValue(obj);
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long j) {
            BackpressureUtils.postCompleteRequest(this.requested, j, this.queue, this.actual, this);
        }
    }

    public OperatorTakeLastTimed(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.ageMillis = timeUnit.toMillis(j);
        this.scheduler = scheduler2;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        if (i >= 0) {
            this.ageMillis = timeUnit.toMillis(j);
            this.scheduler = scheduler2;
            this.count = i;
            return;
        }
        throw new IndexOutOfBoundsException("count could not be negative");
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final TakeLastTimedSubscriber takeLastTimedSubscriber = new TakeLastTimedSubscriber(subscriber, this.count, this.ageMillis, this.scheduler);
        subscriber.add(takeLastTimedSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                takeLastTimedSubscriber.requestMore(j);
            }
        });
        return takeLastTimedSubscriber;
    }
}
