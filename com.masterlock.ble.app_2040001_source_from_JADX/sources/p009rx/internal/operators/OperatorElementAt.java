package p009rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorElementAt */
public final class OperatorElementAt<T> implements Operator<T, T> {
    final T defaultValue;
    final boolean hasDefault;
    final int index;

    /* renamed from: rx.internal.operators.OperatorElementAt$InnerProducer */
    static class InnerProducer extends AtomicBoolean implements Producer {
        private static final long serialVersionUID = 1;
        final Producer actual;

        public InnerProducer(Producer producer) {
            this.actual = producer;
        }

        public void request(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (j > 0 && compareAndSet(false, true)) {
                this.actual.request(Long.MAX_VALUE);
            }
        }
    }

    public OperatorElementAt(int i) {
        this(i, null, false);
    }

    public OperatorElementAt(int i, T t) {
        this(i, t, true);
    }

    private OperatorElementAt(int i, T t, boolean z) {
        if (i >= 0) {
            this.index = i;
            this.defaultValue = t;
            this.hasDefault = z;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" is out of bounds");
        throw new IndexOutOfBoundsException(sb.toString());
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        C20801 r0 = new Subscriber<T>() {
            private int currentIndex;

            public void onNext(T t) {
                int i = this.currentIndex;
                this.currentIndex = i + 1;
                if (i == OperatorElementAt.this.index) {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                    unsubscribe();
                }
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onCompleted() {
                if (this.currentIndex > OperatorElementAt.this.index) {
                    return;
                }
                if (OperatorElementAt.this.hasDefault) {
                    subscriber.onNext(OperatorElementAt.this.defaultValue);
                    subscriber.onCompleted();
                    return;
                }
                Subscriber subscriber = subscriber;
                StringBuilder sb = new StringBuilder();
                sb.append(OperatorElementAt.this.index);
                sb.append(" is out of bounds");
                subscriber.onError(new IndexOutOfBoundsException(sb.toString()));
            }

            public void setProducer(Producer producer) {
                subscriber.setProducer(new InnerProducer(producer));
            }
        };
        subscriber.add(r0);
        return r0;
    }
}
