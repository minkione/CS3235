package p009rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.BackpressureOverflow;
import p009rx.BackpressureOverflow.Strategy;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.functions.Action0;
import p009rx.internal.util.BackpressureDrainManager;
import p009rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback;

/* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer */
public class OperatorOnBackpressureBuffer<T> implements Operator<T, T> {
    private final Long capacity;
    private final Action0 onOverflow;
    private final Strategy overflowStrategy;

    /* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer$BufferSubscriber */
    static final class BufferSubscriber<T> extends Subscriber<T> implements BackpressureQueueCallback {
        private final AtomicLong capacity;
        private final Subscriber<? super T> child;
        private final BackpressureDrainManager manager;
        private final Action0 onOverflow;
        private final Strategy overflowStrategy;
        private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        private final AtomicBoolean saturated = new AtomicBoolean(false);

        public BufferSubscriber(Subscriber<? super T> subscriber, Long l, Action0 action0, Strategy strategy) {
            this.child = subscriber;
            this.capacity = l != null ? new AtomicLong(l.longValue()) : null;
            this.onOverflow = action0;
            this.manager = new BackpressureDrainManager(this);
            this.overflowStrategy = strategy;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onCompleted() {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain();
            }
        }

        public void onError(Throwable th) {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain(th);
            }
        }

        public void onNext(T t) {
            if (assertCapacity()) {
                this.queue.offer(NotificationLite.next(t));
                this.manager.drain();
            }
        }

        public boolean accept(Object obj) {
            return NotificationLite.accept(this.child, obj);
        }

        public void complete(Throwable th) {
            if (th != null) {
                this.child.onError(th);
            } else {
                this.child.onCompleted();
            }
        }

        public Object peek() {
            return this.queue.peek();
        }

        public Object poll() {
            Object poll = this.queue.poll();
            AtomicLong atomicLong = this.capacity;
            if (!(atomicLong == null || poll == null)) {
                atomicLong.incrementAndGet();
            }
            return poll;
        }

        private boolean assertCapacity() {
            long j;
            boolean z;
            if (this.capacity == null) {
                return true;
            }
            do {
                j = this.capacity.get();
                if (j <= 0) {
                    try {
                        z = this.overflowStrategy.mayAttemptDrop() && poll() != null;
                    } catch (MissingBackpressureException e) {
                        if (this.saturated.compareAndSet(false, true)) {
                            unsubscribe();
                            this.child.onError(e);
                        }
                        z = false;
                    }
                    Action0 action0 = this.onOverflow;
                    if (action0 != null) {
                        try {
                            action0.call();
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.manager.terminateAndDrain(th);
                            return false;
                        }
                    }
                    if (!z) {
                        return false;
                    }
                }
            } while (!this.capacity.compareAndSet(j, j - 1));
            return true;
        }

        /* access modifiers changed from: protected */
        public Producer manager() {
            return this.manager;
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer$Holder */
    static final class Holder {
        static final OperatorOnBackpressureBuffer<?> INSTANCE = new OperatorOnBackpressureBuffer<>();

        Holder() {
        }
    }

    public static <T> OperatorOnBackpressureBuffer<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorOnBackpressureBuffer() {
        this.capacity = null;
        this.onOverflow = null;
        this.overflowStrategy = BackpressureOverflow.ON_OVERFLOW_DEFAULT;
    }

    public OperatorOnBackpressureBuffer(long j) {
        this(j, null, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long j, Action0 action0) {
        this(j, action0, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long j, Action0 action0, Strategy strategy) {
        if (j <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be > 0");
        } else if (strategy != null) {
            this.capacity = Long.valueOf(j);
            this.onOverflow = action0;
            this.overflowStrategy = strategy;
        } else {
            throw new NullPointerException("The BackpressureOverflow strategy must not be null");
        }
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        BufferSubscriber bufferSubscriber = new BufferSubscriber(subscriber, this.capacity, this.onOverflow, this.overflowStrategy);
        subscriber.add(bufferSubscriber);
        subscriber.setProducer(bufferSubscriber.manager());
        return bufferSubscriber;
    }
}
