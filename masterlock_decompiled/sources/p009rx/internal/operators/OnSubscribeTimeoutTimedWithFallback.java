package p009rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.internal.producers.ProducerArbiter;
import p009rx.internal.subscriptions.SequentialSubscription;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeTimeoutTimedWithFallback */
public final class OnSubscribeTimeoutTimedWithFallback<T> implements OnSubscribe<T> {
    final Observable<? extends T> fallback;
    final Scheduler scheduler;
    final Observable<T> source;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeTimeoutTimedWithFallback$FallbackSubscriber */
    static final class FallbackSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super T> actual;
        final ProducerArbiter arbiter;

        FallbackSubscriber(Subscriber<? super T> subscriber, ProducerArbiter producerArbiter) {
            this.actual = subscriber;
            this.arbiter = producerArbiter;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeTimeoutTimedWithFallback$TimeoutMainSubscriber */
    static final class TimeoutMainSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super T> actual;
        final ProducerArbiter arbiter = new ProducerArbiter();
        long consumed;
        final Observable<? extends T> fallback;
        final AtomicLong index = new AtomicLong();
        final SequentialSubscription task = new SequentialSubscription();
        final long timeout;
        final TimeUnit unit;
        final SequentialSubscription upstream = new SequentialSubscription(this);
        final Worker worker;

        /* renamed from: rx.internal.operators.OnSubscribeTimeoutTimedWithFallback$TimeoutMainSubscriber$TimeoutTask */
        final class TimeoutTask implements Action0 {
            final long idx;

            TimeoutTask(long j) {
                this.idx = j;
            }

            public void call() {
                TimeoutMainSubscriber.this.onTimeout(this.idx);
            }
        }

        TimeoutMainSubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker2, Observable<? extends T> observable) {
            this.actual = subscriber;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
            this.fallback = observable;
            add(worker2);
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
                    this.consumed++;
                    this.actual.onNext(t);
                    startTimeout(j2);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long j) {
            this.task.replace(this.worker.schedule(new TimeoutTask(j), this.timeout, this.unit));
        }

        public void onError(Throwable th) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.unsubscribe();
                this.actual.onError(th);
                this.worker.unsubscribe();
                return;
            }
            RxJavaHooks.onError(th);
        }

        public void onCompleted() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.unsubscribe();
                this.actual.onCompleted();
                this.worker.unsubscribe();
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
    }

    public OnSubscribeTimeoutTimedWithFallback(Observable<T> observable, long j, TimeUnit timeUnit, Scheduler scheduler2, Observable<? extends T> observable2) {
        this.source = observable;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.fallback = observable2;
    }

    public void call(Subscriber<? super T> subscriber) {
        TimeoutMainSubscriber timeoutMainSubscriber = new TimeoutMainSubscriber(subscriber, this.timeout, this.unit, this.scheduler.createWorker(), this.fallback);
        subscriber.add(timeoutMainSubscriber.upstream);
        subscriber.setProducer(timeoutMainSubscriber.arbiter);
        timeoutMainSubscriber.startTimeout(0);
        this.source.subscribe((Subscriber<? super T>) timeoutMainSubscriber);
    }
}
