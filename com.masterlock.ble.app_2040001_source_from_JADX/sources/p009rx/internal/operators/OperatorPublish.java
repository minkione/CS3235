package p009rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.functions.Action0;
import p009rx.functions.Func1;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.atomic.SpscAtomicArrayQueue;
import p009rx.internal.util.unsafe.SpscArrayQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.observables.ConnectableObservable;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorPublish */
public final class OperatorPublish<T> extends ConnectableObservable<T> {
    final AtomicReference<PublishSubscriber<T>> current;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorPublish$InnerProducer */
    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long NOT_REQUESTED = -4611686018427387904L;
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        final PublishSubscriber<T> parent;

        public InnerProducer(PublishSubscriber<T> publishSubscriber, Subscriber<? super T> subscriber) {
            this.parent = publishSubscriber;
            this.child = subscriber;
            lazySet(NOT_REQUESTED);
        }

        public void request(long j) {
            long j2;
            long j3;
            if (j >= 0) {
                do {
                    j2 = get();
                    if (j2 != UNSUBSCRIBED) {
                        if (j2 >= 0 && j == 0) {
                            return;
                        }
                        if (j2 == NOT_REQUESTED) {
                            j3 = j;
                        } else {
                            j3 = j2 + j;
                            if (j3 < 0) {
                                j3 = Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(j2, j3));
                this.parent.dispatch();
            }
        }

        public long produced(long j) {
            long j2;
            long j3;
            if (j > 0) {
                do {
                    j2 = get();
                    if (j2 == NOT_REQUESTED) {
                        throw new IllegalStateException("Produced without request");
                    } else if (j2 == UNSUBSCRIBED) {
                        return UNSUBSCRIBED;
                    } else {
                        j3 = j2 - j;
                        if (j3 < 0) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("More produced (");
                            sb.append(j);
                            sb.append(") than requested (");
                            sb.append(j2);
                            sb.append(")");
                            throw new IllegalStateException(sb.toString());
                        }
                    }
                } while (!compareAndSet(j2, j3));
                return j3;
            }
            throw new IllegalArgumentException("Cant produce zero or less");
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.dispatch();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorPublish$PublishSubscriber */
    static final class PublishSubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final AtomicReference<PublishSubscriber<T>> current;
        boolean emitting;
        boolean missed;
        final AtomicReference<InnerProducer[]> producers;
        final Queue<Object> queue;
        final AtomicBoolean shouldConnect;
        volatile Object terminalEvent;

        public PublishSubscriber(AtomicReference<PublishSubscriber<T>> atomicReference) {
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue<>(RxRingBuffer.SIZE) : new SpscAtomicArrayQueue<>(RxRingBuffer.SIZE);
            this.producers = new AtomicReference<>(EMPTY);
            this.current = atomicReference;
            this.shouldConnect = new AtomicBoolean();
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            add(Subscriptions.create(new Action0() {
                public void call() {
                    PublishSubscriber.this.producers.getAndSet(PublishSubscriber.TERMINATED);
                    PublishSubscriber.this.current.compareAndSet(PublishSubscriber.this, null);
                }
            }));
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (!this.queue.offer(NotificationLite.next(t))) {
                onError(new MissingBackpressureException());
            } else {
                dispatch();
            }
        }

        public void onError(Throwable th) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(th);
                dispatch();
            }
        }

        public void onCompleted() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.completed();
                dispatch();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerProducer<T> innerProducer) {
            InnerProducer[] innerProducerArr;
            InnerProducer[] innerProducerArr2;
            if (innerProducer != null) {
                do {
                    innerProducerArr = (InnerProducer[]) this.producers.get();
                    if (innerProducerArr == TERMINATED) {
                        return false;
                    }
                    int length = innerProducerArr.length;
                    innerProducerArr2 = new InnerProducer[(length + 1)];
                    System.arraycopy(innerProducerArr, 0, innerProducerArr2, 0, length);
                    innerProducerArr2[length] = innerProducer;
                } while (!this.producers.compareAndSet(innerProducerArr, innerProducerArr2));
                return true;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerProducer<T> innerProducer) {
            InnerProducer[] innerProducerArr;
            InnerProducer[] innerProducerArr2;
            do {
                innerProducerArr = (InnerProducer[]) this.producers.get();
                if (innerProducerArr != EMPTY && innerProducerArr != TERMINATED) {
                    int i = -1;
                    int length = innerProducerArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerProducerArr[i2].equals(innerProducer)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerProducerArr2 = EMPTY;
                        } else {
                            InnerProducer[] innerProducerArr3 = new InnerProducer[(length - 1)];
                            System.arraycopy(innerProducerArr, 0, innerProducerArr3, 0, i);
                            System.arraycopy(innerProducerArr, i + 1, innerProducerArr3, i, (length - i) - 1);
                            innerProducerArr2 = innerProducerArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(innerProducerArr, innerProducerArr2));
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(Object obj, boolean z) {
            int i = 0;
            if (obj != null) {
                if (!NotificationLite.isCompleted(obj)) {
                    Throwable error = NotificationLite.getError(obj);
                    this.current.compareAndSet(this, null);
                    try {
                        InnerProducer[] innerProducerArr = (InnerProducer[]) this.producers.getAndSet(TERMINATED);
                        int length = innerProducerArr.length;
                        while (i < length) {
                            innerProducerArr[i].child.onError(error);
                            i++;
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                } else if (z) {
                    this.current.compareAndSet(this, null);
                    try {
                        InnerProducer[] innerProducerArr2 = (InnerProducer[]) this.producers.getAndSet(TERMINATED);
                        int length2 = innerProducerArr2.length;
                        while (i < length2) {
                            innerProducerArr2[i].child.onCompleted();
                            i++;
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:100:0x00f0, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
            r0 = r1.terminalEvent;
            r4 = r1.queue.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
            if (checkTerminated(r0, r4) == false) goto L_0x0021;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
            if (r4 != false) goto L_0x00ca;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
            r5 = (p009rx.internal.operators.OperatorPublish.InnerProducer[]) r1.producers.get();
            r0 = r5.length;
            r8 = r5.length;
            r9 = Long.MAX_VALUE;
            r6 = 0;
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0038, code lost:
            if (r6 >= r8) goto L_0x0054;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003a, code lost:
            r13 = r5[r6].get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
            if (r13 < 0) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
            r9 = java.lang.Math.min(r9, r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x004d, code lost:
            if (r13 != Long.MIN_VALUE) goto L_0x0051;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004f, code lost:
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0051, code lost:
            r6 = r6 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0056, code lost:
            if (r0 != r7) goto L_0x0070;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0058, code lost:
            r0 = r1.terminalEvent;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0060, code lost:
            if (r1.queue.poll() != null) goto L_0x0064;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0062, code lost:
            r4 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0064, code lost:
            r4 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0069, code lost:
            if (checkTerminated(r0, r4) == false) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x006b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x006c, code lost:
            request(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0070, code lost:
            r0 = r4;
            r4 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0072, code lost:
            r6 = (long) r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0075, code lost:
            if (r6 >= r9) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0077, code lost:
            r0 = r1.terminalEvent;
            r8 = r1.queue.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007f, code lost:
            if (r8 != null) goto L_0x0083;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0081, code lost:
            r15 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0083, code lost:
            r15 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0088, code lost:
            if (checkTerminated(r0, r15) == false) goto L_0x008b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x008a, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x008b, code lost:
            if (r15 == false) goto L_0x008e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x008e, code lost:
            r6 = p009rx.internal.operators.NotificationLite.getValue(r8);
            r7 = r5.length;
            r8 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0094, code lost:
            if (r8 >= r7) goto L_0x00b6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0096, code lost:
            r2 = r5[r8];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x009e, code lost:
            if (r2.get() <= 0) goto L_0x00b2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            r2.child.onNext(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            r2.produced(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a9, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00aa, code lost:
            r2.unsubscribe();
            p009rx.exceptions.Exceptions.throwOrReport(r0, (p009rx.Observer<?>) r2.child, r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b6, code lost:
            r4 = r4 + 1;
            r0 = r15;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00bb, code lost:
            r15 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00bc, code lost:
            if (r4 <= 0) goto L_0x00c1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x00be, code lost:
            request(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x00c3, code lost:
            if (r9 == 0) goto L_0x00ca;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x00c5, code lost:
            if (r15 != false) goto L_0x00ca;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ca, code lost:
            monitor-enter(r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x00cd, code lost:
            if (r1.missed != false) goto L_0x00d6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00cf, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
            monitor-exit(r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d2, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x00d3, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x00d4, code lost:
            r2 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
            r1.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x00d8, code lost:
            monitor-exit(r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x00dc, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x00dd, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
            monitor-exit(r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x00e0, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x00e2, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x00e4, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x00e5, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x00e6, code lost:
            if (r2 == false) goto L_0x00e8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x00e8, code lost:
            monitor-enter(r18);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:?, code lost:
            r1.emitting = false;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            /*
                r18 = this;
                r1 = r18
                monitor-enter(r18)
                boolean r0 = r1.emitting     // Catch:{ all -> 0x00f1 }
                r2 = 1
                if (r0 == 0) goto L_0x000c
                r1.missed = r2     // Catch:{ all -> 0x00f1 }
                monitor-exit(r18)     // Catch:{ all -> 0x00f1 }
                return
            L_0x000c:
                r1.emitting = r2     // Catch:{ all -> 0x00f1 }
                r3 = 0
                r1.missed = r3     // Catch:{ all -> 0x00f1 }
                monitor-exit(r18)     // Catch:{ all -> 0x00f1 }
            L_0x0012:
                java.lang.Object r0 = r1.terminalEvent     // Catch:{ all -> 0x00e4 }
                java.util.Queue<java.lang.Object> r4 = r1.queue     // Catch:{ all -> 0x00e4 }
                boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x00e4 }
                boolean r0 = r1.checkTerminated(r0, r4)     // Catch:{ all -> 0x00e4 }
                if (r0 == 0) goto L_0x0021
                return
            L_0x0021:
                if (r4 != 0) goto L_0x00ca
                java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$InnerProducer[]> r0 = r1.producers     // Catch:{ all -> 0x00e4 }
                java.lang.Object r0 = r0.get()     // Catch:{ all -> 0x00e4 }
                r5 = r0
                rx.internal.operators.OperatorPublish$InnerProducer[] r5 = (p009rx.internal.operators.OperatorPublish.InnerProducer[]) r5     // Catch:{ all -> 0x00e4 }
                int r0 = r5.length     // Catch:{ all -> 0x00e4 }
                r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r8 = r5.length     // Catch:{ all -> 0x00e4 }
                r9 = r6
                r6 = 0
                r7 = 0
            L_0x0036:
                r11 = 0
                if (r6 >= r8) goto L_0x0054
                r13 = r5[r6]     // Catch:{ all -> 0x00e4 }
                long r13 = r13.get()     // Catch:{ all -> 0x00e4 }
                int r15 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
                if (r15 < 0) goto L_0x0049
                long r9 = java.lang.Math.min(r9, r13)     // Catch:{ all -> 0x00e4 }
                goto L_0x0051
            L_0x0049:
                r11 = -9223372036854775808
                int r15 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
                if (r15 != 0) goto L_0x0051
                int r7 = r7 + 1
            L_0x0051:
                int r6 = r6 + 1
                goto L_0x0036
            L_0x0054:
                r13 = 1
                if (r0 != r7) goto L_0x0070
                java.lang.Object r0 = r1.terminalEvent     // Catch:{ all -> 0x00e4 }
                java.util.Queue<java.lang.Object> r4 = r1.queue     // Catch:{ all -> 0x00e4 }
                java.lang.Object r4 = r4.poll()     // Catch:{ all -> 0x00e4 }
                if (r4 != 0) goto L_0x0064
                r4 = 1
                goto L_0x0065
            L_0x0064:
                r4 = 0
            L_0x0065:
                boolean r0 = r1.checkTerminated(r0, r4)     // Catch:{ all -> 0x00e4 }
                if (r0 == 0) goto L_0x006c
                return
            L_0x006c:
                r1.request(r13)     // Catch:{ all -> 0x00e4 }
                goto L_0x0012
            L_0x0070:
                r0 = r4
                r4 = 0
            L_0x0072:
                long r6 = (long) r4     // Catch:{ all -> 0x00e4 }
                int r8 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                if (r8 >= 0) goto L_0x00bb
                java.lang.Object r0 = r1.terminalEvent     // Catch:{ all -> 0x00e4 }
                java.util.Queue<java.lang.Object> r8 = r1.queue     // Catch:{ all -> 0x00e4 }
                java.lang.Object r8 = r8.poll()     // Catch:{ all -> 0x00e4 }
                if (r8 != 0) goto L_0x0083
                r15 = 1
                goto L_0x0084
            L_0x0083:
                r15 = 0
            L_0x0084:
                boolean r0 = r1.checkTerminated(r0, r15)     // Catch:{ all -> 0x00e4 }
                if (r0 == 0) goto L_0x008b
                return
            L_0x008b:
                if (r15 == 0) goto L_0x008e
                goto L_0x00bc
            L_0x008e:
                java.lang.Object r6 = p009rx.internal.operators.NotificationLite.getValue(r8)     // Catch:{ all -> 0x00e4 }
                int r7 = r5.length     // Catch:{ all -> 0x00e4 }
                r8 = 0
            L_0x0094:
                if (r8 >= r7) goto L_0x00b6
                r2 = r5[r8]     // Catch:{ all -> 0x00e4 }
                long r16 = r2.get()     // Catch:{ all -> 0x00e4 }
                int r0 = (r16 > r11 ? 1 : (r16 == r11 ? 0 : -1))
                if (r0 <= 0) goto L_0x00b2
                rx.Subscriber<? super T> r0 = r2.child     // Catch:{ Throwable -> 0x00a9 }
                r0.onNext(r6)     // Catch:{ Throwable -> 0x00a9 }
                r2.produced(r13)     // Catch:{ all -> 0x00e4 }
                goto L_0x00b2
            L_0x00a9:
                r0 = move-exception
                r2.unsubscribe()     // Catch:{ all -> 0x00e4 }
                rx.Subscriber<? super T> r2 = r2.child     // Catch:{ all -> 0x00e4 }
                p009rx.exceptions.Exceptions.throwOrReport(r0, r2, r6)     // Catch:{ all -> 0x00e4 }
            L_0x00b2:
                int r8 = r8 + 1
                r2 = 1
                goto L_0x0094
            L_0x00b6:
                int r4 = r4 + 1
                r0 = r15
                r2 = 1
                goto L_0x0072
            L_0x00bb:
                r15 = r0
            L_0x00bc:
                if (r4 <= 0) goto L_0x00c1
                r1.request(r6)     // Catch:{ all -> 0x00e4 }
            L_0x00c1:
                int r0 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
                if (r0 == 0) goto L_0x00ca
                if (r15 != 0) goto L_0x00ca
                r2 = 1
                goto L_0x0012
            L_0x00ca:
                monitor-enter(r18)     // Catch:{ all -> 0x00e4 }
                boolean r0 = r1.missed     // Catch:{ all -> 0x00dc }
                if (r0 != 0) goto L_0x00d6
                r1.emitting = r3     // Catch:{ all -> 0x00dc }
                monitor-exit(r18)     // Catch:{ all -> 0x00d3 }
                return
            L_0x00d3:
                r0 = move-exception
                r2 = 1
                goto L_0x00de
            L_0x00d6:
                r1.missed = r3     // Catch:{ all -> 0x00dc }
                monitor-exit(r18)     // Catch:{ all -> 0x00dc }
                r2 = 1
                goto L_0x0012
            L_0x00dc:
                r0 = move-exception
                r2 = 0
            L_0x00de:
                monitor-exit(r18)     // Catch:{ all -> 0x00e2 }
                throw r0     // Catch:{ all -> 0x00e0 }
            L_0x00e0:
                r0 = move-exception
                goto L_0x00e6
            L_0x00e2:
                r0 = move-exception
                goto L_0x00de
            L_0x00e4:
                r0 = move-exception
                r2 = 0
            L_0x00e6:
                if (r2 != 0) goto L_0x00f0
                monitor-enter(r18)
                r1.emitting = r3     // Catch:{ all -> 0x00ed }
                monitor-exit(r18)     // Catch:{ all -> 0x00ed }
                goto L_0x00f0
            L_0x00ed:
                r0 = move-exception
                monitor-exit(r18)     // Catch:{ all -> 0x00ed }
                throw r0
            L_0x00f0:
                throw r0
            L_0x00f1:
                r0 = move-exception
                monitor-exit(r18)     // Catch:{ all -> 0x00f1 }
                throw r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.operators.OperatorPublish.PublishSubscriber.dispatch():void");
        }
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable) {
        final AtomicReference atomicReference = new AtomicReference();
        return new OperatorPublish(new OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
                while (true) {
                    PublishSubscriber publishSubscriber = (PublishSubscriber) atomicReference.get();
                    if (publishSubscriber == null || publishSubscriber.isUnsubscribed()) {
                        PublishSubscriber publishSubscriber2 = new PublishSubscriber(atomicReference);
                        publishSubscriber2.init();
                        if (!atomicReference.compareAndSet(publishSubscriber, publishSubscriber2)) {
                            continue;
                        } else {
                            publishSubscriber = publishSubscriber2;
                        }
                    }
                    InnerProducer innerProducer = new InnerProducer(publishSubscriber, subscriber);
                    if (publishSubscriber.add(innerProducer)) {
                        subscriber.add(innerProducer);
                        subscriber.setProducer(innerProducer);
                        return;
                    }
                }
            }
        }, observable, atomicReference);
    }

    public static <T, R> Observable<R> create(Observable<? extends T> observable, Func1<? super Observable<T>, ? extends Observable<R>> func1) {
        return create(observable, func1, false);
    }

    public static <T, R> Observable<R> create(final Observable<? extends T> observable, final Func1<? super Observable<T>, ? extends Observable<R>> func1, final boolean z) {
        return unsafeCreate(new OnSubscribe<R>() {
            public void call(final Subscriber<? super R> subscriber) {
                final OnSubscribePublishMulticast onSubscribePublishMulticast = new OnSubscribePublishMulticast(RxRingBuffer.SIZE, z);
                C21011 r1 = new Subscriber<R>() {
                    public void onNext(R r) {
                        subscriber.onNext(r);
                    }

                    public void onError(Throwable th) {
                        onSubscribePublishMulticast.unsubscribe();
                        subscriber.onError(th);
                    }

                    public void onCompleted() {
                        onSubscribePublishMulticast.unsubscribe();
                        subscriber.onCompleted();
                    }

                    public void setProducer(Producer producer) {
                        subscriber.setProducer(producer);
                    }
                };
                subscriber.add(onSubscribePublishMulticast);
                subscriber.add(r1);
                ((Observable) func1.call(Observable.unsafeCreate(onSubscribePublishMulticast))).unsafeSubscribe(r1);
                observable.unsafeSubscribe(onSubscribePublishMulticast.subscriber());
            }
        });
    }

    private OperatorPublish(OnSubscribe<T> onSubscribe, Observable<? extends T> observable, AtomicReference<PublishSubscriber<T>> atomicReference) {
        super(onSubscribe);
        this.source = observable;
        this.current = atomicReference;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p009rx.functions.Action1<? super p009rx.Subscription> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            rx.internal.operators.OperatorPublish$PublishSubscriber r0 = (p009rx.internal.operators.OperatorPublish.PublishSubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isUnsubscribed()
            if (r1 == 0) goto L_0x0024
        L_0x0010:
            rx.internal.operators.OperatorPublish$PublishSubscriber r1 = new rx.internal.operators.OperatorPublish$PublishSubscriber
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r2 = r4.current
            r1.<init>(r2)
            r1.init()
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r2 = r4.current
            boolean r0 = r2.compareAndSet(r0, r1)
            if (r0 != 0) goto L_0x0023
            goto L_0x0000
        L_0x0023:
            r0 = r1
        L_0x0024:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0037
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r2 = 0
        L_0x0038:
            r5.call(r0)
            if (r2 == 0) goto L_0x0042
            rx.Observable<? extends T> r5 = r4.source
            r5.unsafeSubscribe(r0)
        L_0x0042:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.operators.OperatorPublish.connect(rx.functions.Action1):void");
    }
}
