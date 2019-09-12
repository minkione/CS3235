package p009rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.CompositeException;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.exceptions.OnErrorThrowable;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.ScalarSynchronousObservable;
import p009rx.internal.util.atomic.SpscAtomicArrayQueue;
import p009rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import p009rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import p009rx.internal.util.unsafe.Pow2;
import p009rx.internal.util.unsafe.SpscArrayQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OperatorMerge */
public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    /* renamed from: rx.internal.operators.OperatorMerge$HolderDelayErrors */
    static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(true, Integer.MAX_VALUE);

        HolderDelayErrors() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$HolderNoDelay */
    static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(false, Integer.MAX_VALUE);

        HolderNoDelay() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$InnerSubscriber */
    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int LIMIT = (RxRingBuffer.SIZE / 4);
        volatile boolean done;

        /* renamed from: id */
        final long f250id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        public InnerSubscriber(MergeSubscriber<T> mergeSubscriber, long j) {
            this.parent = mergeSubscriber;
            this.f250id = j;
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onError(Throwable th) {
            this.parent.getOrCreateErrorQueue().offer(th);
            this.done = true;
            this.parent.emit();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long j) {
            int i = this.outstanding - ((int) j);
            if (i > LIMIT) {
                this.outstanding = i;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int i2 = RxRingBuffer.SIZE - i;
            if (i2 > 0) {
                request((long) i2);
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$MergeProducer */
    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> mergeSubscriber) {
            this.subscriber = mergeSubscriber;
        }

        public void request(long j) {
            if (j > 0) {
                if (get() != Long.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, j);
                    this.subscriber.emit();
                }
            } else if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }

        public long produced(int i) {
            return addAndGet((long) (-i));
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$MergeSubscriber */
    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY = new InnerSubscriber[0];
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard = new Object();
        volatile InnerSubscriber<?>[] innerSubscribers = EMPTY;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        int scalarEmissionCount;
        final int scalarEmissionLimit;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        public MergeSubscriber(Subscriber<? super T> subscriber, boolean z, int i) {
            this.child = subscriber;
            this.delayErrors = z;
            this.maxConcurrent = i;
            if (i == Integer.MAX_VALUE) {
                this.scalarEmissionLimit = Integer.MAX_VALUE;
                request(Long.MAX_VALUE);
                return;
            }
            this.scalarEmissionLimit = Math.max(1, i >> 1);
            request((long) i);
        }

        /* access modifiers changed from: 0000 */
        public Queue<Throwable> getOrCreateErrorQueue() {
            ConcurrentLinkedQueue<Throwable> concurrentLinkedQueue = this.errors;
            if (concurrentLinkedQueue == null) {
                synchronized (this) {
                    concurrentLinkedQueue = this.errors;
                    if (concurrentLinkedQueue == null) {
                        concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
                        this.errors = concurrentLinkedQueue;
                    }
                }
            }
            return concurrentLinkedQueue;
        }

        /* access modifiers changed from: 0000 */
        public CompositeSubscription getOrCreateComposite() {
            CompositeSubscription compositeSubscription;
            CompositeSubscription compositeSubscription2 = this.subscriptions;
            if (compositeSubscription2 != null) {
                return compositeSubscription2;
            }
            boolean z = false;
            synchronized (this) {
                compositeSubscription = this.subscriptions;
                if (compositeSubscription == null) {
                    CompositeSubscription compositeSubscription3 = new CompositeSubscription();
                    this.subscriptions = compositeSubscription3;
                    compositeSubscription = compositeSubscription3;
                    z = true;
                }
            }
            if (z) {
                add(compositeSubscription);
            }
            return compositeSubscription;
        }

        public void onNext(Observable<? extends T> observable) {
            if (observable != null) {
                if (observable == Observable.empty()) {
                    emitEmpty();
                } else if (observable instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) observable).get());
                } else {
                    long j = this.uniqueId;
                    this.uniqueId = 1 + j;
                    InnerSubscriber innerSubscriber = new InnerSubscriber(this, j);
                    addInner(innerSubscriber);
                    observable.unsafeSubscribe(innerSubscriber);
                    emit();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void emitEmpty() {
            int i = this.scalarEmissionCount + 1;
            if (i == this.scalarEmissionLimit) {
                this.scalarEmissionCount = 0;
                requestMore((long) i);
                return;
            }
            this.scalarEmissionCount = i;
        }

        private void reportError() {
            ArrayList arrayList = new ArrayList(this.errors);
            if (arrayList.size() == 1) {
                this.child.onError((Throwable) arrayList.get(0));
            } else {
                this.child.onError(new CompositeException((Collection<? extends Throwable>) arrayList));
            }
        }

        public void onError(Throwable th) {
            getOrCreateErrorQueue().offer(th);
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* access modifiers changed from: 0000 */
        public void addInner(InnerSubscriber<T> innerSubscriber) {
            getOrCreateComposite().add(innerSubscriber);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] innerSubscriberArr = this.innerSubscribers;
                int length = innerSubscriberArr.length;
                InnerSubscriber<?>[] innerSubscriberArr2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
                this.innerSubscribers = innerSubscriberArr2;
            }
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerSubscriber<T> innerSubscriber) {
            RxRingBuffer rxRingBuffer = innerSubscriber.queue;
            if (rxRingBuffer != null) {
                rxRingBuffer.release();
            }
            this.subscriptions.remove(innerSubscriber);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] innerSubscriberArr = this.innerSubscribers;
                int length = innerSubscriberArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (innerSubscriber.equals(innerSubscriberArr[i2])) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        this.innerSubscribers = EMPTY;
                        return;
                    }
                    InnerSubscriber<?>[] innerSubscriberArr2 = new InnerSubscriber[(length - 1)];
                    System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, i);
                    System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr2, i, (length - i) - 1);
                    this.innerSubscribers = innerSubscriberArr2;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(InnerSubscriber<T> innerSubscriber, T t) {
            long j = this.producer.get();
            boolean z = false;
            if (j != 0) {
                synchronized (this) {
                    j = this.producer.get();
                    if (!this.emitting && j != 0) {
                        this.emitting = true;
                        z = true;
                    }
                }
            }
            if (z) {
                RxRingBuffer rxRingBuffer = innerSubscriber.queue;
                if (rxRingBuffer == null || rxRingBuffer.isEmpty()) {
                    emitScalar(innerSubscriber, t, j);
                    return;
                }
                queueScalar(innerSubscriber, t);
                emitLoop();
                return;
            }
            queueScalar(innerSubscriber, t);
            emit();
        }

        /* access modifiers changed from: protected */
        public void queueScalar(InnerSubscriber<T> innerSubscriber, T t) {
            RxRingBuffer rxRingBuffer = innerSubscriber.queue;
            if (rxRingBuffer == null) {
                rxRingBuffer = RxRingBuffer.getSpscInstance();
                innerSubscriber.add(rxRingBuffer);
                innerSubscriber.queue = rxRingBuffer;
            }
            try {
                rxRingBuffer.onNext(NotificationLite.next(t));
            } catch (MissingBackpressureException e) {
                innerSubscriber.unsubscribe();
                innerSubscriber.onError(e);
            } catch (IllegalStateException e2) {
                if (!innerSubscriber.isUnsubscribed()) {
                    innerSubscriber.unsubscribe();
                    innerSubscriber.onError(e2);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void emitScalar(InnerSubscriber<T> innerSubscriber, T t, long j) {
            boolean z = true;
            try {
                this.child.onNext(t);
            } catch (Throwable th) {
                th = th;
            }
            if (j != Long.MAX_VALUE) {
                this.producer.produced(1);
            }
            innerSubscriber.requestMore(1);
            synchronized (this) {
                if (!this.missed) {
                    this.emitting = false;
                    return;
                }
                this.missed = false;
                emitLoop();
                return;
            }
            if (!z) {
                synchronized (this) {
                    this.emitting = false;
                }
            }
            throw th;
        }

        public void requestMore(long j) {
            request(j);
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(T t) {
            long j = this.producer.get();
            boolean z = false;
            if (j != 0) {
                synchronized (this) {
                    j = this.producer.get();
                    if (!this.emitting && j != 0) {
                        this.emitting = true;
                        z = true;
                    }
                }
            }
            if (z) {
                Queue<Object> queue2 = this.queue;
                if (queue2 == null || queue2.isEmpty()) {
                    emitScalar(t, j);
                    return;
                }
                queueScalar(t);
                emitLoop();
                return;
            }
            queueScalar(t);
            emit();
        }

        /* access modifiers changed from: protected */
        public void queueScalar(T t) {
            Queue<Object> queue2 = this.queue;
            if (queue2 == null) {
                int i = this.maxConcurrent;
                if (i == Integer.MAX_VALUE) {
                    queue2 = new SpscUnboundedAtomicArrayQueue<>(RxRingBuffer.SIZE);
                } else if (!Pow2.isPowerOfTwo(i)) {
                    queue2 = new SpscExactAtomicArrayQueue<>(i);
                } else if (UnsafeAccess.isUnsafeAvailable()) {
                    queue2 = new SpscArrayQueue<>(i);
                } else {
                    queue2 = new SpscAtomicArrayQueue<>(i);
                }
                this.queue = queue2;
            }
            if (!queue2.offer(NotificationLite.next(t))) {
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), t));
            }
        }

        /* access modifiers changed from: protected */
        public void emitScalar(T t, long j) {
            boolean z = true;
            try {
                this.child.onNext(t);
            } catch (Throwable th) {
                th = th;
            }
            if (j != Long.MAX_VALUE) {
                this.producer.produced(1);
            }
            int i = this.scalarEmissionCount + 1;
            if (i == this.scalarEmissionLimit) {
                this.scalarEmissionCount = 0;
                requestMore((long) i);
            } else {
                this.scalarEmissionCount = i;
            }
            synchronized (this) {
                if (!this.missed) {
                    this.emitting = false;
                    return;
                }
                this.missed = false;
                emitLoop();
                return;
            }
            if (!z) {
                synchronized (this) {
                    this.emitting = false;
                }
            }
            throw th;
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:169:0x01cc, code lost:
            r4 = r13;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:176:0x01d3, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:177:0x01d4, code lost:
            r21 = r2;
         */
        /* JADX WARNING: Removed duplicated region for block: B:182:0x01de  */
        /* JADX WARNING: Removed duplicated region for block: B:214:0x019f A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:85:0x00f8 A[Catch:{ Throwable -> 0x0046, all -> 0x01d9, all -> 0x0059 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r22 = this;
                r1 = r22
                rx.Subscriber<? super T> r4 = r1.child     // Catch:{ all -> 0x01d9 }
            L_0x0004:
                boolean r0 = r22.checkTerminate()     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x000b
                return
            L_0x000b:
                java.util.Queue<java.lang.Object> r5 = r1.queue     // Catch:{ all -> 0x01d9 }
                rx.internal.operators.OperatorMerge$MergeProducer<T> r0 = r1.producer     // Catch:{ all -> 0x01d9 }
                long r6 = r0.get()     // Catch:{ all -> 0x01d9 }
                r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r0 != 0) goto L_0x001e
                r10 = 1
                goto L_0x001f
            L_0x001e:
                r10 = 0
            L_0x001f:
                r11 = 1
                r14 = 0
                if (r5 == 0) goto L_0x0093
                r0 = 0
            L_0x0026:
                r16 = r0
                r0 = 0
                r2 = 0
            L_0x002a:
                int r17 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
                if (r17 <= 0) goto L_0x0074
                java.lang.Object r17 = r5.poll()     // Catch:{ all -> 0x01d9 }
                boolean r0 = r22.checkTerminate()     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x0039
                return
            L_0x0039:
                if (r17 != 0) goto L_0x003e
                r0 = r17
                goto L_0x0074
            L_0x003e:
                java.lang.Object r0 = p009rx.internal.operators.NotificationLite.getValue(r17)     // Catch:{ all -> 0x01d9 }
                r4.onNext(r0)     // Catch:{ Throwable -> 0x0046 }
                goto L_0x0067
            L_0x0046:
                r0 = move-exception
                r18 = r0
                boolean r0 = r1.delayErrors     // Catch:{ all -> 0x01d9 }
                if (r0 != 0) goto L_0x005e
                p009rx.exceptions.Exceptions.throwIfFatal(r18)     // Catch:{ all -> 0x01d9 }
                r22.unsubscribe()     // Catch:{ all -> 0x0059 }
                r2 = r18
                r4.onError(r2)     // Catch:{ all -> 0x0059 }
                return
            L_0x0059:
                r0 = move-exception
                r21 = 1
                goto L_0x01dc
            L_0x005e:
                r8 = r18
                java.util.Queue r0 = r22.getOrCreateErrorQueue()     // Catch:{ all -> 0x01d9 }
                r0.offer(r8)     // Catch:{ all -> 0x01d9 }
            L_0x0067:
                int r16 = r16 + 1
                int r2 = r2 + 1
                long r6 = r6 - r11
                r0 = r17
                r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                goto L_0x002a
            L_0x0074:
                if (r2 <= 0) goto L_0x0084
                if (r10 == 0) goto L_0x007e
                r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                goto L_0x0084
            L_0x007e:
                rx.internal.operators.OperatorMerge$MergeProducer<T> r6 = r1.producer     // Catch:{ all -> 0x01d9 }
                long r6 = r6.produced(r2)     // Catch:{ all -> 0x01d9 }
            L_0x0084:
                int r2 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
                if (r2 == 0) goto L_0x0095
                if (r0 != 0) goto L_0x008b
                goto L_0x0095
            L_0x008b:
                r0 = r16
                r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                goto L_0x0026
            L_0x0093:
                r16 = 0
            L_0x0095:
                boolean r0 = r1.done     // Catch:{ all -> 0x01d9 }
                java.util.Queue<java.lang.Object> r2 = r1.queue     // Catch:{ all -> 0x01d9 }
                rx.internal.operators.OperatorMerge$InnerSubscriber<?>[] r5 = r1.innerSubscribers     // Catch:{ all -> 0x01d9 }
                int r8 = r5.length     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x00bb
                if (r2 == 0) goto L_0x00a6
                boolean r0 = r2.isEmpty()     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x00bb
            L_0x00a6:
                if (r8 != 0) goto L_0x00bb
                java.util.concurrent.ConcurrentLinkedQueue<java.lang.Throwable> r0 = r1.errors     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x00b7
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x01d9 }
                if (r0 == 0) goto L_0x00b3
                goto L_0x00b7
            L_0x00b3:
                r22.reportError()     // Catch:{ all -> 0x01d9 }
                goto L_0x00ba
            L_0x00b7:
                r4.onCompleted()     // Catch:{ all -> 0x01d9 }
            L_0x00ba:
                return
            L_0x00bb:
                if (r8 <= 0) goto L_0x01ac
                long r11 = r1.lastId     // Catch:{ all -> 0x01d9 }
                int r0 = r1.lastIndex     // Catch:{ all -> 0x01d9 }
                if (r8 <= r0) goto L_0x00ce
                r2 = r5[r0]     // Catch:{ all -> 0x01d9 }
                r17 = r4
                long r3 = r2.f250id     // Catch:{ all -> 0x01d9 }
                int r2 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
                if (r2 == 0) goto L_0x00f3
                goto L_0x00d0
            L_0x00ce:
                r17 = r4
            L_0x00d0:
                if (r8 > r0) goto L_0x00d3
                r0 = 0
            L_0x00d3:
                r2 = r0
                r0 = 0
            L_0x00d5:
                if (r0 >= r8) goto L_0x00ea
                r3 = r5[r2]     // Catch:{ all -> 0x01d9 }
                long r3 = r3.f250id     // Catch:{ all -> 0x01d9 }
                int r21 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
                if (r21 != 0) goto L_0x00e0
                goto L_0x00ea
            L_0x00e0:
                int r3 = r2 + 1
                if (r3 != r8) goto L_0x00e6
                r2 = 0
                goto L_0x00e7
            L_0x00e6:
                r2 = r3
            L_0x00e7:
                int r0 = r0 + 1
                goto L_0x00d5
            L_0x00ea:
                r1.lastIndex = r2     // Catch:{ all -> 0x01d9 }
                r0 = r5[r2]     // Catch:{ all -> 0x01d9 }
                long r3 = r0.f250id     // Catch:{ all -> 0x01d9 }
                r1.lastId = r3     // Catch:{ all -> 0x01d9 }
                r0 = r2
            L_0x00f3:
                r2 = r0
                r0 = 0
                r3 = 0
            L_0x00f6:
                if (r0 >= r8) goto L_0x019f
                boolean r4 = r22.checkTerminate()     // Catch:{ all -> 0x01d9 }
                if (r4 == 0) goto L_0x00ff
                return
            L_0x00ff:
                r4 = r5[r2]     // Catch:{ all -> 0x01d9 }
                r11 = 0
            L_0x0102:
                r12 = r11
                r11 = 0
            L_0x0104:
                int r21 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
                if (r21 <= 0) goto L_0x0147
                boolean r21 = r22.checkTerminate()     // Catch:{ all -> 0x01d9 }
                if (r21 == 0) goto L_0x010f
                return
            L_0x010f:
                rx.internal.util.RxRingBuffer r9 = r4.queue     // Catch:{ all -> 0x01d9 }
                if (r9 != 0) goto L_0x0118
                r13 = r17
                r19 = 1
                goto L_0x014b
            L_0x0118:
                java.lang.Object r12 = r9.poll()     // Catch:{ all -> 0x01d9 }
                if (r12 != 0) goto L_0x0123
                r13 = r17
                r19 = 1
                goto L_0x014b
            L_0x0123:
                java.lang.Object r9 = p009rx.internal.operators.NotificationLite.getValue(r12)     // Catch:{ all -> 0x01d9 }
                r13 = r17
                r13.onNext(r9)     // Catch:{ Throwable -> 0x0135 }
                r19 = 1
                long r6 = r6 - r19
                int r11 = r11 + 1
                r17 = r13
                goto L_0x0104
            L_0x0135:
                r0 = move-exception
                r2 = r0
                p009rx.exceptions.Exceptions.throwIfFatal(r2)     // Catch:{ all -> 0x0059 }
                r13.onError(r2)     // Catch:{ all -> 0x0141 }
                r22.unsubscribe()     // Catch:{ all -> 0x0059 }
                return
            L_0x0141:
                r0 = move-exception
                r2 = r0
                r22.unsubscribe()     // Catch:{ all -> 0x0059 }
                throw r2     // Catch:{ all -> 0x0059 }
            L_0x0147:
                r13 = r17
                r19 = 1
            L_0x014b:
                if (r11 <= 0) goto L_0x0161
                if (r10 != 0) goto L_0x0156
                rx.internal.operators.OperatorMerge$MergeProducer<T> r6 = r1.producer     // Catch:{ all -> 0x01d9 }
                long r6 = r6.produced(r11)     // Catch:{ all -> 0x01d9 }
                goto L_0x015b
            L_0x0156:
                r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            L_0x015b:
                long r14 = (long) r11     // Catch:{ all -> 0x01d9 }
                r4.requestMore(r14)     // Catch:{ all -> 0x01d9 }
                r14 = 0
            L_0x0161:
                int r9 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
                if (r9 == 0) goto L_0x016e
                if (r12 != 0) goto L_0x0168
                goto L_0x016e
            L_0x0168:
                r11 = r12
                r17 = r13
                r14 = 0
                goto L_0x0102
            L_0x016e:
                boolean r9 = r4.done     // Catch:{ all -> 0x01d9 }
                rx.internal.util.RxRingBuffer r11 = r4.queue     // Catch:{ all -> 0x01d9 }
                if (r9 == 0) goto L_0x018c
                if (r11 == 0) goto L_0x017c
                boolean r9 = r11.isEmpty()     // Catch:{ all -> 0x01d9 }
                if (r9 == 0) goto L_0x018c
            L_0x017c:
                r1.removeInner(r4)     // Catch:{ all -> 0x01d9 }
                boolean r3 = r22.checkTerminate()     // Catch:{ all -> 0x01d9 }
                if (r3 == 0) goto L_0x0186
                return
            L_0x0186:
                int r16 = r16 + 1
                r3 = 1
                r11 = 0
                goto L_0x018e
            L_0x018c:
                r11 = 0
            L_0x018e:
                int r4 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
                if (r4 != 0) goto L_0x0193
                goto L_0x01a1
            L_0x0193:
                int r2 = r2 + 1
                if (r2 != r8) goto L_0x0198
                r2 = 0
            L_0x0198:
                int r0 = r0 + 1
                r14 = r11
                r17 = r13
                goto L_0x00f6
            L_0x019f:
                r13 = r17
            L_0x01a1:
                r1.lastIndex = r2     // Catch:{ all -> 0x01d9 }
                r0 = r5[r2]     // Catch:{ all -> 0x01d9 }
                long r4 = r0.f250id     // Catch:{ all -> 0x01d9 }
                r1.lastId = r4     // Catch:{ all -> 0x01d9 }
                r0 = r16
                goto L_0x01b0
            L_0x01ac:
                r13 = r4
                r0 = r16
                r3 = 0
            L_0x01b0:
                if (r0 <= 0) goto L_0x01b6
                long r4 = (long) r0     // Catch:{ all -> 0x01d9 }
                r1.request(r4)     // Catch:{ all -> 0x01d9 }
            L_0x01b6:
                if (r3 == 0) goto L_0x01bb
                r4 = r13
                goto L_0x0004
            L_0x01bb:
                monitor-enter(r22)     // Catch:{ all -> 0x01d9 }
                boolean r0 = r1.missed     // Catch:{ all -> 0x01cf }
                if (r0 != 0) goto L_0x01c8
                r2 = 0
                r1.emitting = r2     // Catch:{ all -> 0x01c5 }
                monitor-exit(r22)     // Catch:{ all -> 0x01c5 }
                return
            L_0x01c5:
                r0 = move-exception
                r2 = 1
                goto L_0x01d1
            L_0x01c8:
                r2 = 0
                r1.missed = r2     // Catch:{ all -> 0x01cf }
                monitor-exit(r22)     // Catch:{ all -> 0x01cf }
                r4 = r13
                goto L_0x0004
            L_0x01cf:
                r0 = move-exception
                r2 = 0
            L_0x01d1:
                monitor-exit(r22)     // Catch:{ all -> 0x01d7 }
                throw r0     // Catch:{ all -> 0x01d3 }
            L_0x01d3:
                r0 = move-exception
                r21 = r2
                goto L_0x01dc
            L_0x01d7:
                r0 = move-exception
                goto L_0x01d1
            L_0x01d9:
                r0 = move-exception
                r21 = 0
            L_0x01dc:
                if (r21 != 0) goto L_0x01e7
                monitor-enter(r22)
                r2 = 0
                r1.emitting = r2     // Catch:{ all -> 0x01e4 }
                monitor-exit(r22)     // Catch:{ all -> 0x01e4 }
                goto L_0x01e7
            L_0x01e4:
                r0 = move-exception
                monitor-exit(r22)     // Catch:{ all -> 0x01e4 }
                throw r0
            L_0x01e7:
                throw r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            ConcurrentLinkedQueue<Throwable> concurrentLinkedQueue = this.errors;
            if (this.delayErrors || concurrentLinkedQueue == null || concurrentLinkedQueue.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
        }
    }

    public static <T> OperatorMerge<T> instance(boolean z) {
        if (z) {
            return HolderDelayErrors.INSTANCE;
        }
        return HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean z, int i) {
        if (i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("maxConcurrent > 0 required but it was ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == Integer.MAX_VALUE) {
            return instance(z);
        } else {
            return new OperatorMerge<>(z, i);
        }
    }

    OperatorMerge(boolean z, int i) {
        this.delayErrors = z;
        this.maxConcurrent = i;
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> subscriber) {
        MergeSubscriber mergeSubscriber = new MergeSubscriber(subscriber, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> mergeProducer = new MergeProducer<>(mergeSubscriber);
        mergeSubscriber.producer = mergeProducer;
        subscriber.add(mergeSubscriber);
        subscriber.setProducer(mergeProducer);
        return mergeSubscriber;
    }
}
