package p009rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.functions.Action0;
import p009rx.internal.schedulers.ImmediateScheduler;
import p009rx.internal.schedulers.TrampolineScheduler;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.atomic.SpscAtomicArrayQueue;
import p009rx.internal.util.unsafe.SpscArrayQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.plugins.RxJavaHooks;
import p009rx.schedulers.Schedulers;

/* renamed from: rx.internal.operators.OperatorObserveOn */
public final class OperatorObserveOn<T> implements Operator<T, T> {
    private final int bufferSize;
    private final boolean delayError;
    private final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorObserveOn$ObserveOnSubscriber */
    static final class ObserveOnSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        final AtomicLong counter = new AtomicLong();
        final boolean delayError;
        long emitted;
        Throwable error;
        volatile boolean finished;
        final int limit;
        final Queue<Object> queue;
        final Worker recursiveScheduler;
        final AtomicLong requested = new AtomicLong();

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> subscriber, boolean z, int i) {
            this.child = subscriber;
            this.recursiveScheduler = scheduler.createWorker();
            this.delayError = z;
            if (i <= 0) {
                i = RxRingBuffer.SIZE;
            }
            this.limit = i - (i >> 2);
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(i);
            } else {
                this.queue = new SpscAtomicArrayQueue(i);
            }
            request((long) i);
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            Subscriber<? super T> subscriber = this.child;
            subscriber.setProducer(new Producer() {
                public void request(long j) {
                    if (j > 0) {
                        BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, j);
                        ObserveOnSubscriber.this.schedule();
                    }
                }
            });
            subscriber.add(this.recursiveScheduler);
            subscriber.add(this);
        }

        public void onNext(T t) {
            if (!isUnsubscribed() && !this.finished) {
                if (!this.queue.offer(NotificationLite.next(t))) {
                    onError(new MissingBackpressureException());
                } else {
                    schedule();
                }
            }
        }

        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        public void onError(Throwable th) {
            if (isUnsubscribed() || this.finished) {
                RxJavaHooks.onError(th);
                return;
            }
            this.error = th;
            this.finished = true;
            schedule();
        }

        /* access modifiers changed from: protected */
        public void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this);
            }
        }

        public void call() {
            long j = this.emitted;
            Queue<Object> queue2 = this.queue;
            Subscriber<? super T> subscriber = this.child;
            long j2 = j;
            long j3 = 1;
            do {
                long j4 = this.requested.get();
                while (j4 != j2) {
                    boolean z = this.finished;
                    Object poll = queue2.poll();
                    boolean z2 = poll == null;
                    if (!checkTerminated(z, z2, subscriber, queue2)) {
                        if (z2) {
                            break;
                        }
                        subscriber.onNext(NotificationLite.getValue(poll));
                        j2++;
                        if (j2 == ((long) this.limit)) {
                            j4 = BackpressureUtils.produced(this.requested, j2);
                            request(j2);
                            j2 = 0;
                        }
                    } else {
                        return;
                    }
                }
                if (j4 != j2 || !checkTerminated(this.finished, queue2.isEmpty(), subscriber, queue2)) {
                    this.emitted = j2;
                    j3 = this.counter.addAndGet(-j3);
                } else {
                    return;
                }
            } while (j3 != 0);
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<? super T> subscriber, Queue<Object> queue2) {
            if (subscriber.isUnsubscribed()) {
                queue2.clear();
                return true;
            }
            if (z) {
                if (!this.delayError) {
                    Throwable th = this.error;
                    if (th != null) {
                        queue2.clear();
                        try {
                            subscriber.onError(th);
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    } else if (z2) {
                        try {
                            subscriber.onCompleted();
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    }
                } else if (z2) {
                    Throwable th2 = this.error;
                    if (th2 != null) {
                        try {
                            subscriber.onError(th2);
                        } catch (Throwable th3) {
                            this.recursiveScheduler.unsubscribe();
                            throw th3;
                        }
                    } else {
                        subscriber.onCompleted();
                    }
                    this.recursiveScheduler.unsubscribe();
                }
            }
            return false;
        }
    }

    public OperatorObserveOn(Scheduler scheduler2, boolean z) {
        this(scheduler2, z, RxRingBuffer.SIZE);
    }

    public OperatorObserveOn(Scheduler scheduler2, boolean z, int i) {
        this.scheduler = scheduler2;
        this.delayError = z;
        if (i <= 0) {
            i = RxRingBuffer.SIZE;
        }
        this.bufferSize = i;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Scheduler scheduler2 = this.scheduler;
        if ((scheduler2 instanceof ImmediateScheduler) || (scheduler2 instanceof TrampolineScheduler)) {
            return subscriber;
        }
        ObserveOnSubscriber observeOnSubscriber = new ObserveOnSubscriber(scheduler2, subscriber, this.delayError, this.bufferSize);
        observeOnSubscriber.init();
        return observeOnSubscriber;
    }

    public static <T> Operator<T, T> rebatch(final int i) {
        return new Operator<T, T>() {
            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                ObserveOnSubscriber observeOnSubscriber = new ObserveOnSubscriber(Schedulers.immediate(), subscriber, false, i);
                observeOnSubscriber.init();
                return observeOnSubscriber;
            }
        };
    }
}
