package p009rx.internal.producers;

import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;

/* renamed from: rx.internal.producers.SingleProducer */
public final class SingleProducer<T> extends AtomicBoolean implements Producer {
    private static final long serialVersionUID = -3353584923995471404L;
    final Subscriber<? super T> child;
    final T value;

    public SingleProducer(Subscriber<? super T> subscriber, T t) {
        this.child = subscriber;
        this.value = t;
    }

    public void request(long j) {
        if (j >= 0) {
            if (j != 0 && compareAndSet(false, true)) {
                Subscriber<? super T> subscriber = this.child;
                if (!subscriber.isUnsubscribed()) {
                    T t = this.value;
                    try {
                        subscriber.onNext(t);
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        Exceptions.throwOrReport(th, (Observer<?>) subscriber, (Object) t);
                        return;
                    }
                } else {
                    return;
                }
            }
            return;
        }
        throw new IllegalArgumentException("n >= 0 required");
    }
}
