package p009rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;

/* renamed from: rx.internal.operators.OnSubscribeFromIterable */
public final class OnSubscribeFromIterable<T> implements OnSubscribe<T> {

    /* renamed from: is */
    final Iterable<? extends T> f241is;

    /* renamed from: rx.internal.operators.OnSubscribeFromIterable$IterableProducer */
    static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;

        /* renamed from: it */
        private final Iterator<? extends T> f242it;

        /* renamed from: o */
        private final Subscriber<? super T> f243o;

        IterableProducer(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
            this.f243o = subscriber;
            this.f242it = it;
        }

        public void request(long j) {
            if (get() != Long.MAX_VALUE) {
                if (j == Long.MAX_VALUE && compareAndSet(0, Long.MAX_VALUE)) {
                    fastPath();
                } else if (j > 0 && BackpressureUtils.getAndAddRequest(this, j) == 0) {
                    slowPath(j);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Subscriber<? super T> subscriber = this.f243o;
            Iterator<? extends T> it = this.f242it;
            long j2 = j;
            long j3 = 0;
            while (true) {
                if (j3 == j2) {
                    j2 = get();
                    if (j3 == j2) {
                        j2 = BackpressureUtils.produced(this, j3);
                        if (j2 != 0) {
                            j3 = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!subscriber.isUnsubscribed()) {
                    try {
                        subscriber.onNext(it.next());
                        if (!subscriber.isUnsubscribed()) {
                            try {
                                if (!it.hasNext()) {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onCompleted();
                                    }
                                    return;
                                }
                                j3++;
                            } catch (Throwable th) {
                                Exceptions.throwOrReport(th, (Observer<?>) subscriber);
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th2) {
                        Exceptions.throwOrReport(th2, (Observer<?>) subscriber);
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Subscriber<? super T> subscriber = this.f243o;
            Iterator<? extends T> it = this.f242it;
            while (!subscriber.isUnsubscribed()) {
                try {
                    subscriber.onNext(it.next());
                    if (!subscriber.isUnsubscribed()) {
                        try {
                            if (!it.hasNext()) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onCompleted();
                                }
                                return;
                            }
                        } catch (Throwable th) {
                            Exceptions.throwOrReport(th, (Observer<?>) subscriber);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    Exceptions.throwOrReport(th2, (Observer<?>) subscriber);
                    return;
                }
            }
        }
    }

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable != null) {
            this.f241is = iterable;
            return;
        }
        throw new NullPointerException("iterable must not be null");
    }

    public void call(Subscriber<? super T> subscriber) {
        try {
            Iterator it = this.f241is.iterator();
            boolean hasNext = it.hasNext();
            if (!subscriber.isUnsubscribed()) {
                if (!hasNext) {
                    subscriber.onCompleted();
                } else {
                    subscriber.setProducer(new IterableProducer(subscriber, it));
                }
            }
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer<?>) subscriber);
        }
    }
}
