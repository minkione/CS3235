package p009rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.functions.Func1;

/* renamed from: rx.internal.operators.OperatorTakeLast */
public final class OperatorTakeLast<T> implements Operator<T, T> {
    final int count;

    /* renamed from: rx.internal.operators.OperatorTakeLast$TakeLastSubscriber */
    static final class TakeLastSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final int count;
        final ArrayDeque<Object> queue = new ArrayDeque<>();
        final AtomicLong requested = new AtomicLong();

        public TakeLastSubscriber(Subscriber<? super T> subscriber, int i) {
            this.actual = subscriber;
            this.count = i;
        }

        public void onNext(T t) {
            if (this.queue.size() == this.count) {
                this.queue.poll();
            }
            this.queue.offer(NotificationLite.next(t));
        }

        public void onError(Throwable th) {
            this.queue.clear();
            this.actual.onError(th);
        }

        public void onCompleted() {
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public T call(Object obj) {
            return NotificationLite.getValue(obj);
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long j) {
            if (j > 0) {
                BackpressureUtils.postCompleteRequest(this.requested, j, this.queue, this.actual, this);
            }
        }
    }

    public OperatorTakeLast(int i) {
        if (i >= 0) {
            this.count = i;
            return;
        }
        throw new IndexOutOfBoundsException("count cannot be negative");
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final TakeLastSubscriber takeLastSubscriber = new TakeLastSubscriber(subscriber, this.count);
        subscriber.add(takeLastSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                takeLastSubscriber.requestMore(j);
            }
        });
        return takeLastSubscriber;
    }
}
