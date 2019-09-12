package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;

/* renamed from: rx.internal.operators.OperatorTakeUntilPredicate */
public final class OperatorTakeUntilPredicate<T> implements Operator<T, T> {
    final Func1<? super T, Boolean> stopPredicate;

    /* renamed from: rx.internal.operators.OperatorTakeUntilPredicate$ParentSubscriber */
    final class ParentSubscriber extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private boolean done;

        ParentSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        public void onNext(T t) {
            this.child.onNext(t);
            try {
                if (((Boolean) OperatorTakeUntilPredicate.this.stopPredicate.call(t)).booleanValue()) {
                    this.done = true;
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable th) {
                this.done = true;
                Exceptions.throwOrReport(th, (Observer<?>) this.child, (Object) t);
                unsubscribe();
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.child.onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void downstreamRequest(long j) {
            request(j);
        }
    }

    public OperatorTakeUntilPredicate(Func1<? super T, Boolean> func1) {
        this.stopPredicate = func1;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final ParentSubscriber parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.add(parentSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                parentSubscriber.downstreamRequest(j);
            }
        });
        return parentSubscriber;
    }
}
