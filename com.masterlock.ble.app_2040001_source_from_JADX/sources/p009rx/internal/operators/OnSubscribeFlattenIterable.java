package p009rx.internal.operators;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.functions.Func1;
import p009rx.internal.util.ExceptionsUtils;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.ScalarSynchronousObservable;
import p009rx.internal.util.atomic.SpscAtomicArrayQueue;
import p009rx.internal.util.atomic.SpscLinkedArrayQueue;
import p009rx.internal.util.unsafe.SpscArrayQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeFlattenIterable */
public final class OnSubscribeFlattenIterable<T, R> implements OnSubscribe<R> {
    final Func1<? super T, ? extends Iterable<? extends R>> mapper;
    final int prefetch;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OnSubscribeFlattenIterable$FlattenIterableSubscriber */
    static final class FlattenIterableSubscriber<T, R> extends Subscriber<T> {
        Iterator<? extends R> active;
        final Subscriber<? super R> actual;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final long limit;
        final Func1<? super T, ? extends Iterable<? extends R>> mapper;
        long produced;
        final Queue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final AtomicInteger wip = new AtomicInteger();

        public FlattenIterableSubscriber(Subscriber<? super R> subscriber, Func1<? super T, ? extends Iterable<? extends R>> func1, int i) {
            this.actual = subscriber;
            this.mapper = func1;
            if (i == Integer.MAX_VALUE) {
                this.limit = Long.MAX_VALUE;
                this.queue = new SpscLinkedArrayQueue(RxRingBuffer.SIZE);
            } else {
                this.limit = (long) (i - (i >> 2));
                if (UnsafeAccess.isUnsafeAvailable()) {
                    this.queue = new SpscArrayQueue(i);
                } else {
                    this.queue = new SpscAtomicArrayQueue(i);
                }
            }
            request((long) i);
        }

        public void onNext(T t) {
            if (!this.queue.offer(NotificationLite.next(t))) {
                unsubscribe();
                onError(new MissingBackpressureException());
                return;
            }
            drain();
        }

        public void onError(Throwable th) {
            if (ExceptionsUtils.addThrowable(this.error, th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaHooks.onError(th);
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long j) {
            if (j > 0) {
                BackpressureUtils.getAndAddRequest(this.requested, j);
                drain();
            } else if (j < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(j);
                throw new IllegalStateException(sb.toString());
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00cd, code lost:
            if (r14 == null) goto L_0x00cf;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r17 = this;
                r1 = r17
                java.util.concurrent.atomic.AtomicInteger r0 = r1.wip
                int r0 = r0.getAndIncrement()
                if (r0 == 0) goto L_0x000b
                return
            L_0x000b:
                rx.Subscriber<? super R> r2 = r1.actual
                java.util.Queue<java.lang.Object> r3 = r1.queue
                r5 = 1
            L_0x0010:
                java.util.Iterator<? extends R> r0 = r1.active
                r6 = 1
                r8 = 0
                r10 = 0
                if (r0 != 0) goto L_0x0065
                boolean r11 = r1.done
                java.lang.Object r12 = r3.poll()
                if (r12 != 0) goto L_0x0023
                r13 = 1
                goto L_0x0024
            L_0x0023:
                r13 = 0
            L_0x0024:
                boolean r11 = r1.checkTerminated(r11, r13, r2, r3)
                if (r11 == 0) goto L_0x002b
                return
            L_0x002b:
                if (r13 != 0) goto L_0x0063
                long r13 = r1.produced
                long r13 = r13 + r6
                r15 = r5
                long r4 = r1.limit
                int r0 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1))
                if (r0 != 0) goto L_0x003d
                r1.produced = r8
                r1.request(r13)
                goto L_0x003f
            L_0x003d:
                r1.produced = r13
            L_0x003f:
                rx.functions.Func1<? super T, ? extends java.lang.Iterable<? extends R>> r0 = r1.mapper     // Catch:{ Throwable -> 0x005a }
                java.lang.Object r4 = p009rx.internal.operators.NotificationLite.getValue(r12)     // Catch:{ Throwable -> 0x005a }
                java.lang.Object r0 = r0.call(r4)     // Catch:{ Throwable -> 0x005a }
                java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ Throwable -> 0x005a }
                java.util.Iterator r0 = r0.iterator()     // Catch:{ Throwable -> 0x005a }
                boolean r4 = r0.hasNext()     // Catch:{ Throwable -> 0x005a }
                if (r4 != 0) goto L_0x0057
                goto L_0x00cf
            L_0x0057:
                r1.active = r0
                goto L_0x0066
            L_0x005a:
                r0 = move-exception
                p009rx.exceptions.Exceptions.throwIfFatal(r0)
                r1.onError(r0)
                goto L_0x00cf
            L_0x0063:
                r15 = r5
                goto L_0x0066
            L_0x0065:
                r15 = r5
            L_0x0066:
                if (r0 == 0) goto L_0x00d2
                java.util.concurrent.atomic.AtomicLong r4 = r1.requested
                long r4 = r4.get()
                r12 = r8
            L_0x006f:
                r14 = 0
                int r16 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
                if (r16 == 0) goto L_0x00ad
                boolean r11 = r1.done
                boolean r11 = r1.checkTerminated(r11, r10, r2, r3)
                if (r11 == 0) goto L_0x007d
                return
            L_0x007d:
                java.lang.Object r11 = r0.next()     // Catch:{ Throwable -> 0x00a2 }
                r2.onNext(r11)
                boolean r11 = r1.done
                boolean r11 = r1.checkTerminated(r11, r10, r2, r3)
                if (r11 == 0) goto L_0x008d
                return
            L_0x008d:
                long r12 = r12 + r6
                boolean r11 = r0.hasNext()     // Catch:{ Throwable -> 0x0097 }
                if (r11 != 0) goto L_0x006f
                r1.active = r14
                goto L_0x00ae
            L_0x0097:
                r0 = move-exception
                r6 = r0
                p009rx.exceptions.Exceptions.throwIfFatal(r6)
                r1.active = r14
                r1.onError(r6)
                goto L_0x00ae
            L_0x00a2:
                r0 = move-exception
                r6 = r0
                p009rx.exceptions.Exceptions.throwIfFatal(r6)
                r1.active = r14
                r1.onError(r6)
                goto L_0x00ae
            L_0x00ad:
                r14 = r0
            L_0x00ae:
                int r0 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
                if (r0 != 0) goto L_0x00c4
                boolean r0 = r1.done
                boolean r4 = r3.isEmpty()
                if (r4 == 0) goto L_0x00bd
                if (r14 != 0) goto L_0x00bd
                r10 = 1
            L_0x00bd:
                boolean r0 = r1.checkTerminated(r0, r10, r2, r3)
                if (r0 == 0) goto L_0x00c4
                return
            L_0x00c4:
                int r0 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
                if (r0 == 0) goto L_0x00cd
                java.util.concurrent.atomic.AtomicLong r0 = r1.requested
                p009rx.internal.operators.BackpressureUtils.produced(r0, r12)
            L_0x00cd:
                if (r14 != 0) goto L_0x00d2
            L_0x00cf:
                r5 = r15
                goto L_0x0010
            L_0x00d2:
                java.util.concurrent.atomic.AtomicInteger r0 = r1.wip
                int r4 = -r15
                int r5 = r0.addAndGet(r4)
                if (r5 != 0) goto L_0x0010
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.operators.OnSubscribeFlattenIterable.FlattenIterableSubscriber.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, Queue<?> queue2) {
            if (subscriber.isUnsubscribed()) {
                queue2.clear();
                this.active = null;
                return true;
            }
            if (z) {
                if (((Throwable) this.error.get()) != null) {
                    Throwable terminate = ExceptionsUtils.terminate(this.error);
                    unsubscribe();
                    queue2.clear();
                    this.active = null;
                    subscriber.onError(terminate);
                    return true;
                } else if (z2) {
                    subscriber.onCompleted();
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFlattenIterable$OnSubscribeScalarFlattenIterable */
    static final class OnSubscribeScalarFlattenIterable<T, R> implements OnSubscribe<R> {
        final Func1<? super T, ? extends Iterable<? extends R>> mapper;
        final T value;

        public OnSubscribeScalarFlattenIterable(T t, Func1<? super T, ? extends Iterable<? extends R>> func1) {
            this.value = t;
            this.mapper = func1;
        }

        public void call(Subscriber<? super R> subscriber) {
            try {
                Iterator it = ((Iterable) this.mapper.call(this.value)).iterator();
                if (!it.hasNext()) {
                    subscriber.onCompleted();
                } else {
                    subscriber.setProducer(new IterableProducer(subscriber, it));
                }
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer<?>) subscriber, (Object) this.value);
            }
        }
    }

    protected OnSubscribeFlattenIterable(Observable<? extends T> observable, Func1<? super T, ? extends Iterable<? extends R>> func1, int i) {
        this.source = observable;
        this.mapper = func1;
        this.prefetch = i;
    }

    public void call(Subscriber<? super R> subscriber) {
        final FlattenIterableSubscriber flattenIterableSubscriber = new FlattenIterableSubscriber(subscriber, this.mapper, this.prefetch);
        subscriber.add(flattenIterableSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                flattenIterableSubscriber.requestMore(j);
            }
        });
        this.source.unsafeSubscribe(flattenIterableSubscriber);
    }

    public static <T, R> Observable<R> createFrom(Observable<? extends T> observable, Func1<? super T, ? extends Iterable<? extends R>> func1, int i) {
        if (observable instanceof ScalarSynchronousObservable) {
            return Observable.unsafeCreate(new OnSubscribeScalarFlattenIterable(((ScalarSynchronousObservable) observable).get(), func1));
        }
        return Observable.unsafeCreate(new OnSubscribeFlattenIterable(observable, func1, i));
    }
}
