package p009rx.internal.producers;

import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;

/* renamed from: rx.internal.producers.SingleDelayedProducer */
public final class SingleDelayedProducer<T> extends AtomicInteger implements Producer {
    static final int HAS_REQUEST_HAS_VALUE = 3;
    static final int HAS_REQUEST_NO_VALUE = 2;
    static final int NO_REQUEST_HAS_VALUE = 1;
    static final int NO_REQUEST_NO_VALUE = 0;
    private static final long serialVersionUID = -2873467947112093874L;
    final Subscriber<? super T> child;
    T value;

    public SingleDelayedProducer(Subscriber<? super T> subscriber) {
        this.child = subscriber;
    }

    public void request(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (j != 0) {
            while (true) {
                int i = get();
                if (i == 0) {
                    if (compareAndSet(0, 2)) {
                        break;
                    }
                } else if (i == 1 && compareAndSet(1, 3)) {
                    emit(this.child, this.value);
                }
            }
        }
    }

    public void setValue(T t) {
        do {
            int i = get();
            if (i == 0) {
                this.value = t;
            } else if (i == 2 && compareAndSet(2, 3)) {
                emit(this.child, t);
                return;
            } else {
                return;
            }
        } while (!compareAndSet(0, 1));
    }

    private static <T> void emit(Subscriber<? super T> subscriber, T t) {
        if (!subscriber.isUnsubscribed()) {
            try {
                subscriber.onNext(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer<?>) subscriber, (Object) t);
            }
        }
    }
}
