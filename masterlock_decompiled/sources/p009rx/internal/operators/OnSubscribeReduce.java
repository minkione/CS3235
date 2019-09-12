package p009rx.internal.operators;

import java.util.NoSuchElementException;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func2;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeReduce */
public final class OnSubscribeReduce<T> implements OnSubscribe<T> {
    final Func2<T, T, T> reducer;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeReduce$ReduceSubscriber */
    static final class ReduceSubscriber<T> extends Subscriber<T> {
        static final Object EMPTY = new Object();
        final Subscriber<? super T> actual;
        boolean done;
        final Func2<T, T, T> reducer;
        T value = EMPTY;

        public ReduceSubscriber(Subscriber<? super T> subscriber, Func2<T, T, T> func2) {
            this.actual = subscriber;
            this.reducer = func2;
            request(0);
        }

        public void onNext(T t) {
            if (!this.done) {
                T t2 = this.value;
                if (t2 == EMPTY) {
                    this.value = t;
                } else {
                    try {
                        this.value = this.reducer.call(t2, t);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        unsubscribe();
                        onError(th);
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                this.actual.onError(th);
                return;
            }
            RxJavaHooks.onError(th);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                T t = this.value;
                if (t != EMPTY) {
                    this.actual.onNext(t);
                    this.actual.onCompleted();
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void downstreamRequest(long j) {
            if (j < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(j);
                throw new IllegalArgumentException(sb.toString());
            } else if (j != 0) {
                request(Long.MAX_VALUE);
            }
        }
    }

    public OnSubscribeReduce(Observable<T> observable, Func2<T, T, T> func2) {
        this.source = observable;
        this.reducer = func2;
    }

    public void call(Subscriber<? super T> subscriber) {
        final ReduceSubscriber reduceSubscriber = new ReduceSubscriber(subscriber, this.reducer);
        subscriber.add(reduceSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                reduceSubscriber.downstreamRequest(j);
            }
        });
        this.source.unsafeSubscribe(reduceSubscriber);
    }
}
