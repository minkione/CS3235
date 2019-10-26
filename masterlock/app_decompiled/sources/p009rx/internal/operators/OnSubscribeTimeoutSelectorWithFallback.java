package p009rx.internal.operators;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;
import p009rx.internal.producers.ProducerArbiter;
import p009rx.internal.subscriptions.SequentialSubscription;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeTimeoutSelectorWithFallback */
public final class OnSubscribeTimeoutSelectorWithFallback<T, U, V> implements OnSubscribe<T> {
    final Observable<? extends T> fallback;
    final Observable<U> firstTimeoutIndicator;
    final Func1<? super T, ? extends Observable<V>> itemTimeoutIndicator;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeTimeoutSelectorWithFallback$TimeoutMainSubscriber */
    static final class TimeoutMainSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super T> actual;
        final ProducerArbiter arbiter = new ProducerArbiter();
        long consumed;
        final Observable<? extends T> fallback;
        final AtomicLong index = new AtomicLong();
        final Func1<? super T, ? extends Observable<?>> itemTimeoutIndicator;
        final SequentialSubscription task = new SequentialSubscription();
        final SequentialSubscription upstream = new SequentialSubscription(this);

        /* renamed from: rx.internal.operators.OnSubscribeTimeoutSelectorWithFallback$TimeoutMainSubscriber$TimeoutConsumer */
        final class TimeoutConsumer extends Subscriber<Object> {
            boolean done;
            final long idx;

            TimeoutConsumer(long j) {
                this.idx = j;
            }

            public void onNext(Object obj) {
                if (!this.done) {
                    this.done = true;
                    unsubscribe();
                    TimeoutMainSubscriber.this.onTimeout(this.idx);
                }
            }

            public void onError(Throwable th) {
                if (!this.done) {
                    this.done = true;
                    TimeoutMainSubscriber.this.onTimeoutError(this.idx, th);
                    return;
                }
                RxJavaHooks.onError(th);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    TimeoutMainSubscriber.this.onTimeout(this.idx);
                }
            }
        }

        TimeoutMainSubscriber(Subscriber<? super T> subscriber, Func1<? super T, ? extends Observable<?>> func1, Observable<? extends T> observable) {
            this.actual = subscriber;
            this.itemTimeoutIndicator = func1;
            this.fallback = observable;
            add(this.task);
        }

        public void onNext(T t) {
            long j = this.index.get();
            if (j != Long.MAX_VALUE) {
                long j2 = j + 1;
                if (this.index.compareAndSet(j, j2)) {
                    Subscription subscription = (Subscription) this.task.get();
                    if (subscription != null) {
                        subscription.unsubscribe();
                    }
                    this.actual.onNext(t);
                    this.consumed++;
                    try {
                        Observable observable = (Observable) this.itemTimeoutIndicator.call(t);
                        if (observable != null) {
                            TimeoutConsumer timeoutConsumer = new TimeoutConsumer(j2);
                            if (this.task.replace(timeoutConsumer)) {
                                observable.subscribe((Subscriber<? super T>) timeoutConsumer);
                            }
                            return;
                        }
                        throw new NullPointerException("The itemTimeoutIndicator returned a null Observable");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        unsubscribe();
                        this.index.getAndSet(Long.MAX_VALUE);
                        this.actual.onError(th);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startFirst(Observable<?> observable) {
            if (observable != null) {
                TimeoutConsumer timeoutConsumer = new TimeoutConsumer(0);
                if (this.task.replace(timeoutConsumer)) {
                    observable.subscribe((Subscriber<? super T>) timeoutConsumer);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.unsubscribe();
                this.actual.onError(th);
                return;
            }
            RxJavaHooks.onError(th);
        }

        public void onCompleted() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.unsubscribe();
                this.actual.onCompleted();
            }
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        /* access modifiers changed from: 0000 */
        public void onTimeout(long j) {
            if (this.index.compareAndSet(j, Long.MAX_VALUE)) {
                unsubscribe();
                if (this.fallback == null) {
                    this.actual.onError(new TimeoutException());
                } else {
                    long j2 = this.consumed;
                    if (j2 != 0) {
                        this.arbiter.produced(j2);
                    }
                    FallbackSubscriber fallbackSubscriber = new FallbackSubscriber(this.actual, this.arbiter);
                    if (this.upstream.replace(fallbackSubscriber)) {
                        this.fallback.subscribe((Subscriber<? super T>) fallbackSubscriber);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void onTimeoutError(long j, Throwable th) {
            if (this.index.compareAndSet(j, Long.MAX_VALUE)) {
                unsubscribe();
                this.actual.onError(th);
                return;
            }
            RxJavaHooks.onError(th);
        }
    }

    public OnSubscribeTimeoutSelectorWithFallback(Observable<T> observable, Observable<U> observable2, Func1<? super T, ? extends Observable<V>> func1, Observable<? extends T> observable3) {
        this.source = observable;
        this.firstTimeoutIndicator = observable2;
        this.itemTimeoutIndicator = func1;
        this.fallback = observable3;
    }

    public void call(Subscriber<? super T> subscriber) {
        TimeoutMainSubscriber timeoutMainSubscriber = new TimeoutMainSubscriber(subscriber, this.itemTimeoutIndicator, this.fallback);
        subscriber.add(timeoutMainSubscriber.upstream);
        subscriber.setProducer(timeoutMainSubscriber.arbiter);
        timeoutMainSubscriber.startFirst(this.firstTimeoutIndicator);
        this.source.subscribe((Subscriber<? super T>) timeoutMainSubscriber);
    }
}
