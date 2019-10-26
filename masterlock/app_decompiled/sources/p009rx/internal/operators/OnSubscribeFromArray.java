package p009rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OnSubscribeFromArray */
public final class OnSubscribeFromArray<T> implements OnSubscribe<T> {
    final T[] array;

    /* renamed from: rx.internal.operators.OnSubscribeFromArray$FromArrayProducer */
    static final class FromArrayProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = 3534218984725836979L;
        final T[] array;
        final Subscriber<? super T> child;
        int index;

        public FromArrayProducer(Subscriber<? super T> subscriber, T[] tArr) {
            this.child = subscriber;
            this.array = tArr;
        }

        public void request(long j) {
            if (j < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(j);
                throw new IllegalArgumentException(sb.toString());
            } else if (j == Long.MAX_VALUE) {
                if (BackpressureUtils.getAndAddRequest(this, j) == 0) {
                    fastPath();
                }
            } else if (j != 0 && BackpressureUtils.getAndAddRequest(this, j) == 0) {
                slowPath(j);
            }
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Subscriber<? super T> subscriber = this.child;
            T[] tArr = this.array;
            int length = tArr.length;
            int i = 0;
            while (i < length) {
                T t = tArr[i];
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(t);
                    i++;
                } else {
                    return;
                }
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Subscriber<? super T> subscriber = this.child;
            T[] tArr = this.array;
            int length = tArr.length;
            int i = this.index;
            long j2 = 0;
            while (true) {
                if (j == 0 || i == length) {
                    j = get() + j2;
                    if (j == 0) {
                        this.index = i;
                        j = addAndGet(j2);
                        if (j != 0) {
                            j2 = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(tArr[i]);
                    i++;
                    if (i == length) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                        return;
                    }
                    j--;
                    j2--;
                } else {
                    return;
                }
            }
        }
    }

    public OnSubscribeFromArray(T[] tArr) {
        this.array = tArr;
    }

    public void call(Subscriber<? super T> subscriber) {
        subscriber.setProducer(new FromArrayProducer(subscriber, this.array));
    }
}
