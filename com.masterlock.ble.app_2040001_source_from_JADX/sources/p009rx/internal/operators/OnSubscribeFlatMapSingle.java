package p009rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Single;
import p009rx.SingleSubscriber;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;
import p009rx.internal.util.ExceptionsUtils;
import p009rx.internal.util.atomic.MpscLinkedAtomicQueue;
import p009rx.internal.util.unsafe.MpscLinkedQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OnSubscribeFlatMapSingle */
public final class OnSubscribeFlatMapSingle<T, R> implements OnSubscribe<R> {
    final boolean delayErrors;
    final Func1<? super T, ? extends Single<? extends R>> mapper;
    final int maxConcurrency;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeFlatMapSingle$FlatMapSingleSubscriber */
    static final class FlatMapSingleSubscriber<T, R> extends Subscriber<T> {
        final AtomicInteger active = new AtomicInteger();
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicReference<Throwable> errors = new AtomicReference<>();
        final Func1<? super T, ? extends Single<? extends R>> mapper;
        final int maxConcurrency;
        final Queue<Object> queue;
        final Requested requested = new Requested<>();
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger();

        /* renamed from: rx.internal.operators.OnSubscribeFlatMapSingle$FlatMapSingleSubscriber$InnerSubscriber */
        final class InnerSubscriber extends SingleSubscriber<R> {
            InnerSubscriber() {
            }

            public void onSuccess(R r) {
                FlatMapSingleSubscriber.this.innerSuccess(this, r);
            }

            public void onError(Throwable th) {
                FlatMapSingleSubscriber.this.innerError(this, th);
            }
        }

        /* renamed from: rx.internal.operators.OnSubscribeFlatMapSingle$FlatMapSingleSubscriber$Requested */
        final class Requested extends AtomicLong implements Producer, Subscription {
            private static final long serialVersionUID = -887187595446742742L;

            Requested() {
            }

            public void request(long j) {
                if (j > 0) {
                    BackpressureUtils.getAndAddRequest(this, j);
                    FlatMapSingleSubscriber.this.drain();
                }
            }

            /* access modifiers changed from: 0000 */
            public void produced(long j) {
                BackpressureUtils.produced(this, j);
            }

            public void unsubscribe() {
                FlatMapSingleSubscriber flatMapSingleSubscriber = FlatMapSingleSubscriber.this;
                flatMapSingleSubscriber.cancelled = true;
                flatMapSingleSubscriber.unsubscribe();
                if (FlatMapSingleSubscriber.this.wip.getAndIncrement() == 0) {
                    FlatMapSingleSubscriber.this.queue.clear();
                }
            }

            public boolean isUnsubscribed() {
                return FlatMapSingleSubscriber.this.cancelled;
            }
        }

        FlatMapSingleSubscriber(Subscriber<? super R> subscriber, Func1<? super T, ? extends Single<? extends R>> func1, boolean z, int i) {
            this.actual = subscriber;
            this.mapper = func1;
            this.delayErrors = z;
            this.maxConcurrency = i;
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new MpscLinkedQueue();
            } else {
                this.queue = new MpscLinkedAtomicQueue();
            }
            request(i != Integer.MAX_VALUE ? (long) i : Long.MAX_VALUE);
        }

        public void onNext(T t) {
            try {
                Single single = (Single) this.mapper.call(t);
                if (single != null) {
                    InnerSubscriber innerSubscriber = new InnerSubscriber();
                    this.set.add(innerSubscriber);
                    this.active.incrementAndGet();
                    single.subscribe((SingleSubscriber<? super T>) innerSubscriber);
                    return;
                }
                throw new NullPointerException("The mapper returned a null Single");
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                unsubscribe();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            if (this.delayErrors) {
                ExceptionsUtils.addThrowable(this.errors, th);
            } else {
                this.set.unsubscribe();
                if (!this.errors.compareAndSet(null, th)) {
                    RxJavaHooks.onError(th);
                    return;
                }
            }
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerSuccess(InnerSubscriber innerSubscriber, R r) {
            this.queue.offer(NotificationLite.next(r));
            this.set.remove(innerSubscriber);
            this.active.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerSubscriber innerSubscriber, Throwable th) {
            if (this.delayErrors) {
                ExceptionsUtils.addThrowable(this.errors, th);
                this.set.remove(innerSubscriber);
                if (!this.done && this.maxConcurrency != Integer.MAX_VALUE) {
                    request(1);
                }
            } else {
                this.set.unsubscribe();
                unsubscribe();
                if (!this.errors.compareAndSet(null, th)) {
                    RxJavaHooks.onError(th);
                    return;
                }
                this.done = true;
            }
            this.active.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super R> subscriber = this.actual;
                Queue<Object> queue2 = this.queue;
                boolean z = this.delayErrors;
                AtomicInteger atomicInteger = this.active;
                int i = 1;
                do {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (j2 != j) {
                        if (this.cancelled) {
                            queue2.clear();
                            return;
                        }
                        boolean z2 = this.done;
                        if (z || !z2 || ((Throwable) this.errors.get()) == null) {
                            Object poll = queue2.poll();
                            boolean z3 = poll == null;
                            if (z2 && atomicInteger.get() == 0 && z3) {
                                if (((Throwable) this.errors.get()) != null) {
                                    subscriber.onError(ExceptionsUtils.terminate(this.errors));
                                } else {
                                    subscriber.onCompleted();
                                }
                                return;
                            } else if (z3) {
                                break;
                            } else {
                                subscriber.onNext(NotificationLite.getValue(poll));
                                j2++;
                            }
                        } else {
                            queue2.clear();
                            subscriber.onError(ExceptionsUtils.terminate(this.errors));
                            return;
                        }
                    }
                    if (j2 == j) {
                        if (this.cancelled) {
                            queue2.clear();
                            return;
                        } else if (this.done) {
                            if (z) {
                                if (atomicInteger.get() == 0 && queue2.isEmpty()) {
                                    if (((Throwable) this.errors.get()) != null) {
                                        subscriber.onError(ExceptionsUtils.terminate(this.errors));
                                    } else {
                                        subscriber.onCompleted();
                                    }
                                    return;
                                }
                            } else if (((Throwable) this.errors.get()) != null) {
                                queue2.clear();
                                subscriber.onError(ExceptionsUtils.terminate(this.errors));
                                return;
                            } else if (atomicInteger.get() == 0 && queue2.isEmpty()) {
                                subscriber.onCompleted();
                                return;
                            }
                        }
                    }
                    if (j2 != 0) {
                        this.requested.produced(j2);
                        if (!this.done && this.maxConcurrency != Integer.MAX_VALUE) {
                            request(j2);
                        }
                    }
                    i = this.wip.addAndGet(-i);
                } while (i != 0);
            }
        }
    }

    public OnSubscribeFlatMapSingle(Observable<T> observable, Func1<? super T, ? extends Single<? extends R>> func1, boolean z, int i) {
        if (func1 == null) {
            throw new NullPointerException("mapper is null");
        } else if (i > 0) {
            this.source = observable;
            this.mapper = func1;
            this.delayErrors = z;
            this.maxConcurrency = i;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("maxConcurrency > 0 required but it was ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void call(Subscriber<? super R> subscriber) {
        FlatMapSingleSubscriber flatMapSingleSubscriber = new FlatMapSingleSubscriber(subscriber, this.mapper, this.delayErrors, this.maxConcurrency);
        subscriber.add(flatMapSingleSubscriber.set);
        subscriber.add(flatMapSingleSubscriber.requested);
        subscriber.setProducer(flatMapSingleSubscriber.requested);
        this.source.unsafeSubscribe(flatMapSingleSubscriber);
    }
}
