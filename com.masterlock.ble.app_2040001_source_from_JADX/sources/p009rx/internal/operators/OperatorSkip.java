package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorSkip */
public final class OperatorSkip<T> implements Operator<T, T> {
    final int toSkip;

    public OperatorSkip(int i) {
        if (i >= 0) {
            this.toSkip = i;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("n >= 0 required but it was ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            int skipped;

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T t) {
                if (this.skipped >= OperatorSkip.this.toSkip) {
                    subscriber.onNext(t);
                } else {
                    this.skipped++;
                }
            }

            public void setProducer(Producer producer) {
                subscriber.setProducer(producer);
                producer.request((long) OperatorSkip.this.toSkip);
            }
        };
    }
}
