package p009rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Observable;
import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.CompositeException;
import p009rx.functions.Action0;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.atomic.SpscLinkedArrayQueue;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.SerialSubscription;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorSwitch */
public final class OperatorSwitch<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayError;

    /* renamed from: rx.internal.operators.OperatorSwitch$Holder */
    static final class Holder {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>(false);

        Holder() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$HolderDelayError */
    static final class HolderDelayError {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>(true);

        HolderDelayError() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$InnerSubscriber */
    static final class InnerSubscriber<T> extends Subscriber<T> {
        /* access modifiers changed from: private */

        /* renamed from: id */
        public final long f253id;
        private final SwitchSubscriber<T> parent;

        InnerSubscriber(long j, SwitchSubscriber<T> switchSubscriber) {
            this.f253id = j;
            this.parent = switchSubscriber;
        }

        public void setProducer(Producer producer) {
            this.parent.innerProducer(producer, this.f253id);
        }

        public void onNext(T t) {
            this.parent.emit(t, this);
        }

        public void onError(Throwable th) {
            this.parent.error(th, this.f253id);
        }

        public void onCompleted() {
            this.parent.complete(this.f253id);
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$SwitchSubscriber */
    static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final Throwable TERMINAL_ERROR = new Throwable("Terminal error");
        final Subscriber<? super T> child;
        final boolean delayError;
        boolean emitting;
        Throwable error;
        final AtomicLong index;
        boolean innerActive;
        volatile boolean mainDone;
        boolean missed;
        Producer producer;
        final SpscLinkedArrayQueue<Object> queue;
        long requested;
        final SerialSubscription serial = new SerialSubscription();

        SwitchSubscriber(Subscriber<? super T> subscriber, boolean z) {
            this.child = subscriber;
            this.delayError = z;
            this.index = new AtomicLong();
            this.queue = new SpscLinkedArrayQueue<>(RxRingBuffer.SIZE);
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            this.child.add(this.serial);
            this.child.add(Subscriptions.create(new Action0() {
                public void call() {
                    SwitchSubscriber.this.clearProducer();
                }
            }));
            this.child.setProducer(new Producer() {
                public void request(long j) {
                    if (j > 0) {
                        SwitchSubscriber.this.childRequested(j);
                    } else if (j < 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("n >= 0 expected but it was ");
                        sb.append(j);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            });
        }

        /* access modifiers changed from: 0000 */
        public void clearProducer() {
            synchronized (this) {
                this.producer = null;
            }
        }

        public void onNext(Observable<? extends T> observable) {
            InnerSubscriber innerSubscriber;
            long incrementAndGet = this.index.incrementAndGet();
            Subscription subscription = this.serial.get();
            if (subscription != null) {
                subscription.unsubscribe();
            }
            synchronized (this) {
                innerSubscriber = new InnerSubscriber(incrementAndGet, this);
                this.innerActive = true;
                this.producer = null;
            }
            this.serial.set(innerSubscriber);
            observable.unsafeSubscribe(innerSubscriber);
        }

        public void onError(Throwable th) {
            boolean updateError;
            synchronized (this) {
                updateError = updateError(th);
            }
            if (updateError) {
                this.mainDone = true;
                drain();
                return;
            }
            pluginError(th);
        }

        /* access modifiers changed from: 0000 */
        public boolean updateError(Throwable th) {
            Throwable th2 = this.error;
            if (th2 == TERMINAL_ERROR) {
                return false;
            }
            if (th2 == null) {
                this.error = th;
            } else if (th2 instanceof CompositeException) {
                ArrayList arrayList = new ArrayList(((CompositeException) th2).getExceptions());
                arrayList.add(th);
                this.error = new CompositeException((Collection<? extends Throwable>) arrayList);
            } else {
                this.error = new CompositeException(th2, th);
            }
            return true;
        }

        public void onCompleted() {
            this.mainDone = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void emit(T t, InnerSubscriber<T> innerSubscriber) {
            synchronized (this) {
                if (this.index.get() == innerSubscriber.f253id) {
                    this.queue.offer(innerSubscriber, NotificationLite.next(t));
                    drain();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th, long j) {
            boolean z;
            synchronized (this) {
                if (this.index.get() == j) {
                    z = updateError(th);
                    this.innerActive = false;
                    this.producer = null;
                } else {
                    z = true;
                }
            }
            if (z) {
                drain();
            } else {
                pluginError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void complete(long j) {
            synchronized (this) {
                if (this.index.get() == j) {
                    this.innerActive = false;
                    this.producer = null;
                    drain();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void pluginError(Throwable th) {
            RxJavaHooks.onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerProducer(Producer producer2, long j) {
            synchronized (this) {
                if (this.index.get() == j) {
                    long j2 = this.requested;
                    this.producer = producer2;
                    producer2.request(j2);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void childRequested(long j) {
            Producer producer2;
            synchronized (this) {
                producer2 = this.producer;
                this.requested = BackpressureUtils.addCap(this.requested, j);
            }
            if (producer2 != null) {
                producer2.request(j);
            }
            drain();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0023, code lost:
            r9 = r8.queue;
            r10 = r8.index;
            r11 = r8.child;
            r12 = r1;
            r14 = r3;
            r15 = r8.mainDone;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
            r16 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
            if (r16 == r12) goto L_0x0076;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
            if (r11.isUnsubscribed() == false) goto L_0x003d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
            r18 = r9.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004e, code lost:
            if (checkTerminated(r15, r0, r14, r9, r11, r18) == false) goto L_0x0051;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0050, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0051, code lost:
            if (r18 == false) goto L_0x0054;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
            r1 = (p009rx.internal.operators.OperatorSwitch.InnerSubscriber) r9.poll();
            r2 = p009rx.internal.operators.NotificationLite.getValue(r9.poll());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x006c, code lost:
            if (r10.get() != p009rx.internal.operators.OperatorSwitch.InnerSubscriber.access$000(r1)) goto L_0x0032;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x006e, code lost:
            r11.onNext(r2);
            r16 = r16 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0078, code lost:
            if (r16 != r12) goto L_0x0094;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x007e, code lost:
            if (r11.isUnsubscribed() == false) goto L_0x0081;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0080, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0091, code lost:
            if (checkTerminated(r8.mainDone, r0, r14, r9, r11, r9.isEmpty()) == false) goto L_0x0094;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0093, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0094, code lost:
            monitor-enter(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            r0 = r8.requested;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x009e, code lost:
            if (r0 == Long.MAX_VALUE) goto L_0x00a6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a0, code lost:
            r0 = r0 - r16;
            r8.requested = r0;
            r12 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a6, code lost:
            r12 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00aa, code lost:
            if (r8.missed != false) goto L_0x00b0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x00ac, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ae, code lost:
            monitor-exit(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x00af, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b0, code lost:
            r8.missed = false;
            r15 = r8.mainDone;
            r0 = r8.innerActive;
            r14 = r8.error;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b8, code lost:
            if (r14 == null) goto L_0x00c6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x00bc, code lost:
            if (r14 == TERMINAL_ERROR) goto L_0x00c6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00c0, code lost:
            if (r8.delayError != false) goto L_0x00c6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c2, code lost:
            r8.error = TERMINAL_ERROR;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00c6, code lost:
            monitor-exit(r19);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r19 = this;
                r8 = r19
                monitor-enter(r19)
                boolean r0 = r8.emitting     // Catch:{ all -> 0x00cc }
                r1 = 1
                if (r0 == 0) goto L_0x000c
                r8.missed = r1     // Catch:{ all -> 0x00cc }
                monitor-exit(r19)     // Catch:{ all -> 0x00cc }
                return
            L_0x000c:
                r8.emitting = r1     // Catch:{ all -> 0x00cc }
                boolean r0 = r8.innerActive     // Catch:{ all -> 0x00cc }
                long r1 = r8.requested     // Catch:{ all -> 0x00cc }
                java.lang.Throwable r3 = r8.error     // Catch:{ all -> 0x00cc }
                if (r3 == 0) goto L_0x0022
                java.lang.Throwable r4 = TERMINAL_ERROR     // Catch:{ all -> 0x00cc }
                if (r3 == r4) goto L_0x0022
                boolean r4 = r8.delayError     // Catch:{ all -> 0x00cc }
                if (r4 != 0) goto L_0x0022
                java.lang.Throwable r4 = TERMINAL_ERROR     // Catch:{ all -> 0x00cc }
                r8.error = r4     // Catch:{ all -> 0x00cc }
            L_0x0022:
                monitor-exit(r19)     // Catch:{ all -> 0x00cc }
                rx.internal.util.atomic.SpscLinkedArrayQueue<java.lang.Object> r9 = r8.queue
                java.util.concurrent.atomic.AtomicLong r10 = r8.index
                rx.Subscriber<? super T> r11 = r8.child
                boolean r4 = r8.mainDone
                r12 = r1
                r14 = r3
                r15 = r4
            L_0x002e:
                r1 = 0
                r16 = r1
            L_0x0032:
                int r1 = (r16 > r12 ? 1 : (r16 == r12 ? 0 : -1))
                if (r1 == 0) goto L_0x0076
                boolean r1 = r11.isUnsubscribed()
                if (r1 == 0) goto L_0x003d
                return
            L_0x003d:
                boolean r18 = r9.isEmpty()
                r1 = r19
                r2 = r15
                r3 = r0
                r4 = r14
                r5 = r9
                r6 = r11
                r7 = r18
                boolean r1 = r1.checkTerminated(r2, r3, r4, r5, r6, r7)
                if (r1 == 0) goto L_0x0051
                return
            L_0x0051:
                if (r18 == 0) goto L_0x0054
                goto L_0x0076
            L_0x0054:
                java.lang.Object r1 = r9.poll()
                rx.internal.operators.OperatorSwitch$InnerSubscriber r1 = (p009rx.internal.operators.OperatorSwitch.InnerSubscriber) r1
                java.lang.Object r2 = r9.poll()
                java.lang.Object r2 = p009rx.internal.operators.NotificationLite.getValue(r2)
                long r3 = r10.get()
                long r5 = r1.f253id
                int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r1 != 0) goto L_0x0032
                r11.onNext(r2)
                r1 = 1
                long r16 = r16 + r1
                goto L_0x0032
            L_0x0076:
                int r1 = (r16 > r12 ? 1 : (r16 == r12 ? 0 : -1))
                if (r1 != 0) goto L_0x0094
                boolean r1 = r11.isUnsubscribed()
                if (r1 == 0) goto L_0x0081
                return
            L_0x0081:
                boolean r2 = r8.mainDone
                boolean r7 = r9.isEmpty()
                r1 = r19
                r3 = r0
                r4 = r14
                r5 = r9
                r6 = r11
                boolean r0 = r1.checkTerminated(r2, r3, r4, r5, r6, r7)
                if (r0 == 0) goto L_0x0094
                return
            L_0x0094:
                monitor-enter(r19)
                long r0 = r8.requested     // Catch:{ all -> 0x00c9 }
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r4 == 0) goto L_0x00a6
                long r0 = r0 - r16
                r8.requested = r0     // Catch:{ all -> 0x00c9 }
                r12 = r0
                goto L_0x00a7
            L_0x00a6:
                r12 = r0
            L_0x00a7:
                boolean r0 = r8.missed     // Catch:{ all -> 0x00c9 }
                r1 = 0
                if (r0 != 0) goto L_0x00b0
                r8.emitting = r1     // Catch:{ all -> 0x00c9 }
                monitor-exit(r19)     // Catch:{ all -> 0x00c9 }
                return
            L_0x00b0:
                r8.missed = r1     // Catch:{ all -> 0x00c9 }
                boolean r15 = r8.mainDone     // Catch:{ all -> 0x00c9 }
                boolean r0 = r8.innerActive     // Catch:{ all -> 0x00c9 }
                java.lang.Throwable r14 = r8.error     // Catch:{ all -> 0x00c9 }
                if (r14 == 0) goto L_0x00c6
                java.lang.Throwable r1 = TERMINAL_ERROR     // Catch:{ all -> 0x00c9 }
                if (r14 == r1) goto L_0x00c6
                boolean r1 = r8.delayError     // Catch:{ all -> 0x00c9 }
                if (r1 != 0) goto L_0x00c6
                java.lang.Throwable r1 = TERMINAL_ERROR     // Catch:{ all -> 0x00c9 }
                r8.error = r1     // Catch:{ all -> 0x00c9 }
            L_0x00c6:
                monitor-exit(r19)     // Catch:{ all -> 0x00c9 }
                goto L_0x002e
            L_0x00c9:
                r0 = move-exception
                monitor-exit(r19)     // Catch:{ all -> 0x00c9 }
                throw r0
            L_0x00cc:
                r0 = move-exception
                monitor-exit(r19)     // Catch:{ all -> 0x00cc }
                throw r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.operators.OperatorSwitch.SwitchSubscriber.drain():void");
        }

        /* access modifiers changed from: protected */
        public boolean checkTerminated(boolean z, boolean z2, Throwable th, SpscLinkedArrayQueue<Object> spscLinkedArrayQueue, Subscriber<? super T> subscriber, boolean z3) {
            if (this.delayError) {
                if (z && !z2 && z3) {
                    if (th != null) {
                        subscriber.onError(th);
                    } else {
                        subscriber.onCompleted();
                    }
                    return true;
                }
            } else if (th != null) {
                spscLinkedArrayQueue.clear();
                subscriber.onError(th);
                return true;
            } else if (z && !z2 && z3) {
                subscriber.onCompleted();
                return true;
            }
            return false;
        }
    }

    public static <T> OperatorSwitch<T> instance(boolean z) {
        if (z) {
            return HolderDelayError.INSTANCE;
        }
        return Holder.INSTANCE;
    }

    OperatorSwitch(boolean z) {
        this.delayError = z;
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> subscriber) {
        SwitchSubscriber switchSubscriber = new SwitchSubscriber(subscriber, this.delayError);
        subscriber.add(switchSubscriber);
        switchSubscriber.init();
        return switchSubscriber;
    }
}
