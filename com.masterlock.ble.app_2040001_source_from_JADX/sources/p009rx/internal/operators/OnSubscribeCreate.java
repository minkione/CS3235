package p009rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Emitter;
import p009rx.Emitter.BackpressureMode;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.functions.Action1;
import p009rx.functions.Cancellable;
import p009rx.internal.subscriptions.CancellableSubscription;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import p009rx.internal.util.unsafe.SpscUnboundedArrayQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OnSubscribeCreate */
public final class OnSubscribeCreate<T> implements OnSubscribe<T> {
    final Action1<Emitter<T>> Emitter;
    final BackpressureMode backpressure;

    /* renamed from: rx.internal.operators.OnSubscribeCreate$BaseEmitter */
    static abstract class BaseEmitter<T> extends AtomicLong implements Emitter<T>, Producer, Subscription {
        private static final long serialVersionUID = 7326289992464377023L;
        final Subscriber<? super T> actual;
        final SerialSubscription serial = new SerialSubscription();

        /* access modifiers changed from: 0000 */
        public void onRequested() {
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
        }

        public BaseEmitter(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onCompleted() {
            if (!this.actual.isUnsubscribed()) {
                try {
                    this.actual.onCompleted();
                } finally {
                    this.serial.unsubscribe();
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.actual.isUnsubscribed()) {
                try {
                    this.actual.onError(th);
                } finally {
                    this.serial.unsubscribe();
                }
            }
        }

        public final void unsubscribe() {
            this.serial.unsubscribe();
            onUnsubscribed();
        }

        public final boolean isUnsubscribed() {
            return this.serial.isUnsubscribed();
        }

        public final void request(long j) {
            if (BackpressureUtils.validate(j)) {
                BackpressureUtils.getAndAddRequest(this, j);
                onRequested();
            }
        }

        public final void setSubscription(Subscription subscription) {
            this.serial.set(subscription);
        }

        public final void setCancellation(Cancellable cancellable) {
            setSubscription(new CancellableSubscription(cancellable));
        }

        public final long requested() {
            return get();
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$BufferEmitter */
    static final class BufferEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 2427151001689639875L;
        volatile boolean done;
        Throwable error;
        final Queue<Object> queue;
        final AtomicInteger wip;

        public BufferEmitter(Subscriber<? super T> subscriber, int i) {
            super(subscriber);
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscUnboundedArrayQueue<>(i) : new SpscUnboundedAtomicArrayQueue<>(i);
            this.wip = new AtomicInteger();
        }

        public void onNext(T t) {
            this.queue.offer(NotificationLite.next(t));
            drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onRequested() {
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
            if (this.wip.getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                Queue<Object> queue2 = this.queue;
                int i = 1;
                do {
                    long j = get();
                    long j2 = 0;
                    while (j2 != j) {
                        if (subscriber.isUnsubscribed()) {
                            queue2.clear();
                            return;
                        }
                        boolean z = this.done;
                        Object poll = queue2.poll();
                        boolean z2 = poll == null;
                        if (z && z2) {
                            Throwable th = this.error;
                            if (th != null) {
                                super.onError(th);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        } else if (z2) {
                            break;
                        } else {
                            subscriber.onNext(NotificationLite.getValue(poll));
                            j2++;
                        }
                    }
                    if (j2 == j) {
                        if (subscriber.isUnsubscribed()) {
                            queue2.clear();
                            return;
                        }
                        boolean z3 = this.done;
                        boolean isEmpty = queue2.isEmpty();
                        if (z3 && isEmpty) {
                            Throwable th2 = this.error;
                            if (th2 != null) {
                                super.onError(th2);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        }
                    }
                    if (j2 != 0) {
                        BackpressureUtils.produced(this, j2);
                    }
                    i = this.wip.addAndGet(-i);
                } while (i != 0);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$DropEmitter */
    static final class DropEmitter<T> extends NoOverflowBaseEmitter<T> {
        private static final long serialVersionUID = 8360058422307496563L;

        /* access modifiers changed from: 0000 */
        public void onOverflow() {
        }

        public DropEmitter(Subscriber<? super T> subscriber) {
            super(subscriber);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$ErrorEmitter */
    static final class ErrorEmitter<T> extends NoOverflowBaseEmitter<T> {
        private static final long serialVersionUID = 338953216916120960L;
        private boolean done;

        public ErrorEmitter(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onNext(T t) {
            if (!this.done) {
                super.onNext(t);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                super.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            this.done = true;
            super.onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void onOverflow() {
            onError(new MissingBackpressureException("create: could not emit value due to lack of requests"));
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$LatestEmitter */
    static final class LatestEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 4023437720691792495L;
        volatile boolean done;
        Throwable error;
        final AtomicReference<Object> queue = new AtomicReference<>();
        final AtomicInteger wip = new AtomicInteger();

        public LatestEmitter(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onNext(T t) {
            this.queue.set(NotificationLite.next(t));
            drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onRequested() {
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
            if (this.wip.getAndIncrement() == 0) {
                this.queue.lazySet(null);
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            boolean z;
            if (this.wip.getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                AtomicReference<Object> atomicReference = this.queue;
                int i = 1;
                do {
                    long j = get();
                    long j2 = 0;
                    while (true) {
                        z = false;
                        if (j2 == j) {
                            break;
                        } else if (subscriber.isUnsubscribed()) {
                            atomicReference.lazySet(null);
                            return;
                        } else {
                            boolean z2 = this.done;
                            Object andSet = atomicReference.getAndSet(null);
                            boolean z3 = andSet == null;
                            if (z2 && z3) {
                                Throwable th = this.error;
                                if (th != null) {
                                    super.onError(th);
                                } else {
                                    super.onCompleted();
                                }
                                return;
                            } else if (z3) {
                                break;
                            } else {
                                subscriber.onNext(NotificationLite.getValue(andSet));
                                j2++;
                            }
                        }
                    }
                    if (j2 == j) {
                        if (subscriber.isUnsubscribed()) {
                            atomicReference.lazySet(null);
                            return;
                        }
                        boolean z4 = this.done;
                        if (atomicReference.get() == null) {
                            z = true;
                        }
                        if (z4 && z) {
                            Throwable th2 = this.error;
                            if (th2 != null) {
                                super.onError(th2);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        }
                    }
                    if (j2 != 0) {
                        BackpressureUtils.produced(this, j2);
                    }
                    i = this.wip.addAndGet(-i);
                } while (i != 0);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$NoOverflowBaseEmitter */
    static abstract class NoOverflowBaseEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 4127754106204442833L;

        /* access modifiers changed from: 0000 */
        public abstract void onOverflow();

        public NoOverflowBaseEmitter(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onNext(T t) {
            if (!this.actual.isUnsubscribed()) {
                if (get() != 0) {
                    this.actual.onNext(t);
                    BackpressureUtils.produced(this, 1);
                } else {
                    onOverflow();
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCreate$NoneEmitter */
    static final class NoneEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 3776720187248809713L;

        public NoneEmitter(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onNext(T t) {
            long j;
            if (!this.actual.isUnsubscribed()) {
                this.actual.onNext(t);
                do {
                    j = get();
                    if (j == 0) {
                        break;
                    }
                } while (!compareAndSet(j, j - 1));
            }
        }
    }

    public OnSubscribeCreate(Action1<Emitter<T>> action1, BackpressureMode backpressureMode) {
        this.Emitter = action1;
        this.backpressure = backpressureMode;
    }

    public void call(Subscriber<? super T> subscriber) {
        BaseEmitter baseEmitter;
        switch (this.backpressure) {
            case NONE:
                baseEmitter = new NoneEmitter(subscriber);
                break;
            case ERROR:
                baseEmitter = new ErrorEmitter(subscriber);
                break;
            case DROP:
                baseEmitter = new DropEmitter(subscriber);
                break;
            case LATEST:
                baseEmitter = new LatestEmitter(subscriber);
                break;
            default:
                baseEmitter = new BufferEmitter(subscriber, RxRingBuffer.SIZE);
                break;
        }
        subscriber.add(baseEmitter);
        subscriber.setProducer(baseEmitter);
        this.Emitter.call(baseEmitter);
    }
}
