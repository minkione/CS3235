package p009rx.internal.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.CompositeException;
import p009rx.functions.FuncN;
import p009rx.internal.util.RxRingBuffer;
import p009rx.internal.util.atomic.SpscLinkedArrayQueue;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeCombineLatest */
public final class OnSubscribeCombineLatest<T, R> implements OnSubscribe<R> {
    final int bufferSize;
    final FuncN<? extends R> combiner;
    final boolean delayError;
    final Observable<? extends T>[] sources;
    final Iterable<? extends Observable<? extends T>> sourcesIterable;

    /* renamed from: rx.internal.operators.OnSubscribeCombineLatest$CombinerSubscriber */
    static final class CombinerSubscriber<T, R> extends Subscriber<T> {
        boolean done;
        final int index;
        final LatestCoordinator<T, R> parent;

        public CombinerSubscriber(LatestCoordinator<T, R> latestCoordinator, int i) {
            this.parent = latestCoordinator;
            this.index = i;
            request((long) latestCoordinator.bufferSize);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.parent.combine(NotificationLite.next(t), this.index);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            this.parent.onError(th);
            this.done = true;
            this.parent.combine(null, this.index);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.parent.combine(null, this.index);
            }
        }

        public void requestMore(long j) {
            request(j);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCombineLatest$LatestCoordinator */
    static final class LatestCoordinator<T, R> extends AtomicInteger implements Producer, Subscription {
        static final Object MISSING = new Object();
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final FuncN<? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Object[] latest;
        final SpscLinkedArrayQueue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final CombinerSubscriber<T, R>[] subscribers;

        public LatestCoordinator(Subscriber<? super R> subscriber, FuncN<? extends R> funcN, int i, int i2, boolean z) {
            this.actual = subscriber;
            this.combiner = funcN;
            this.bufferSize = i2;
            this.delayError = z;
            this.latest = new Object[i];
            Arrays.fill(this.latest, MISSING);
            this.subscribers = new CombinerSubscriber[i];
            this.queue = new SpscLinkedArrayQueue<>(i2);
        }

        public void subscribe(Observable<? extends T>[] observableArr) {
            CombinerSubscriber<T, R>[] combinerSubscriberArr = this.subscribers;
            int length = combinerSubscriberArr.length;
            for (int i = 0; i < length; i++) {
                combinerSubscriberArr[i] = new CombinerSubscriber<>(this, i);
            }
            lazySet(0);
            this.actual.add(this);
            this.actual.setProducer(this);
            for (int i2 = 0; i2 < length && !this.cancelled; i2++) {
                observableArr[i2].subscribe((Subscriber<? super T>) combinerSubscriberArr[i2]);
            }
        }

        public void request(long j) {
            if (j < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= required but it was ");
                sb.append(j);
                throw new IllegalArgumentException(sb.toString());
            } else if (j != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, j);
                drain();
            }
        }

        public void unsubscribe() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    cancel(this.queue);
                }
            }
        }

        public boolean isUnsubscribed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancel(Queue<?> queue2) {
            queue2.clear();
            for (CombinerSubscriber<T, R> unsubscribe : this.subscribers) {
                unsubscribe.unsubscribe();
            }
        }

        /* access modifiers changed from: 0000 */
        public void combine(Object obj, int i) {
            boolean z;
            CombinerSubscriber<T, R> combinerSubscriber = this.subscribers[i];
            synchronized (this) {
                int length = this.latest.length;
                Object obj2 = this.latest[i];
                int i2 = this.active;
                if (obj2 == MISSING) {
                    i2++;
                    this.active = i2;
                }
                int i3 = this.complete;
                if (obj == null) {
                    i3++;
                    this.complete = i3;
                } else {
                    this.latest[i] = NotificationLite.getValue(obj);
                }
                boolean z2 = false;
                z = i2 == length;
                if (i3 == length || (obj == null && obj2 == MISSING)) {
                    z2 = true;
                }
                if (z2) {
                    this.done = true;
                } else if (obj != null && z) {
                    this.queue.offer(combinerSubscriber, this.latest.clone());
                } else if (obj == null && this.error.get() != null && (obj2 == MISSING || !this.delayError)) {
                    this.done = true;
                }
            }
            if (z || obj == null) {
                drain();
            } else {
                combinerSubscriber.requestMore(1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            long j;
            long j2;
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
                Subscriber<? super R> subscriber = this.actual;
                boolean z = this.delayError;
                AtomicLong atomicLong = this.requested;
                int i = 1;
                do {
                    if (!checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), subscriber, spscLinkedArrayQueue, z)) {
                        long j3 = atomicLong.get();
                        long j4 = 0;
                        while (true) {
                            if (j4 == j3) {
                                j = j4;
                                j2 = 0;
                                break;
                            }
                            boolean z2 = this.done;
                            CombinerSubscriber combinerSubscriber = (CombinerSubscriber) spscLinkedArrayQueue.peek();
                            boolean z3 = combinerSubscriber == null;
                            CombinerSubscriber combinerSubscriber2 = combinerSubscriber;
                            long j5 = j4;
                            if (!checkTerminated(z2, z3, subscriber, spscLinkedArrayQueue, z)) {
                                if (z3) {
                                    j = j5;
                                    j2 = 0;
                                    break;
                                }
                                spscLinkedArrayQueue.poll();
                                Object[] objArr = (Object[]) spscLinkedArrayQueue.poll();
                                if (objArr == null) {
                                    this.cancelled = true;
                                    cancel(spscLinkedArrayQueue);
                                    subscriber.onError(new IllegalStateException("Broken queue?! Sender received but not the array."));
                                    return;
                                }
                                try {
                                    subscriber.onNext(this.combiner.call(objArr));
                                    combinerSubscriber2.requestMore(1);
                                    j4 = j5 + 1;
                                } catch (Throwable th) {
                                    this.cancelled = true;
                                    cancel(spscLinkedArrayQueue);
                                    subscriber.onError(th);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(j == j2 || j3 == Long.MAX_VALUE)) {
                            BackpressureUtils.produced(atomicLong, j);
                        }
                        i = addAndGet(-i);
                    } else {
                        return;
                    }
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, Queue<?> queue2, boolean z3) {
            if (this.cancelled) {
                cancel(queue2);
                return true;
            }
            if (z) {
                if (!z3) {
                    Throwable th = (Throwable) this.error.get();
                    if (th != null) {
                        cancel(queue2);
                        subscriber.onError(th);
                        return true;
                    } else if (z2) {
                        subscriber.onCompleted();
                        return true;
                    }
                } else if (z2) {
                    Throwable th2 = (Throwable) this.error.get();
                    if (th2 != null) {
                        subscriber.onError(th2);
                    } else {
                        subscriber.onCompleted();
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable th) {
            Throwable th2;
            Throwable th3;
            AtomicReference<Throwable> atomicReference = this.error;
            do {
                th2 = (Throwable) atomicReference.get();
                if (th2 == null) {
                    th3 = th;
                } else if (th2 instanceof CompositeException) {
                    ArrayList arrayList = new ArrayList(((CompositeException) th2).getExceptions());
                    arrayList.add(th);
                    th3 = new CompositeException((Collection<? extends Throwable>) arrayList);
                } else {
                    th3 = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{th2, th}));
                }
            } while (!atomicReference.compareAndSet(th2, th3));
        }
    }

    public OnSubscribeCombineLatest(Iterable<? extends Observable<? extends T>> iterable, FuncN<? extends R> funcN) {
        this(null, iterable, funcN, RxRingBuffer.SIZE, false);
    }

    public OnSubscribeCombineLatest(Observable<? extends T>[] observableArr, Iterable<? extends Observable<? extends T>> iterable, FuncN<? extends R> funcN, int i, boolean z) {
        this.sources = observableArr;
        this.sourcesIterable = iterable;
        this.combiner = funcN;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void call(Subscriber<? super R> subscriber) {
        int i;
        Observable<? extends T>[] observableArr = this.sources;
        if (observableArr == null) {
            Iterable<? extends Observable<? extends T>> iterable = this.sourcesIterable;
            if (iterable instanceof List) {
                List list = (List) iterable;
                observableArr = (Observable[]) list.toArray(new Observable[list.size()]);
                i = observableArr.length;
            } else {
                Observable<? extends T>[] observableArr2 = new Observable[8];
                int i2 = 0;
                for (Observable<? extends T> observable : iterable) {
                    if (i2 == observableArr2.length) {
                        Observable<? extends T>[] observableArr3 = new Observable[((i2 >> 2) + i2)];
                        System.arraycopy(observableArr2, 0, observableArr3, 0, i2);
                        observableArr2 = observableArr3;
                    }
                    int i3 = i2 + 1;
                    observableArr2[i2] = observable;
                    i2 = i3;
                }
                observableArr = observableArr2;
                i = i2;
            }
        } else {
            i = observableArr.length;
        }
        if (i == 0) {
            subscriber.onCompleted();
            return;
        }
        LatestCoordinator latestCoordinator = new LatestCoordinator(subscriber, this.combiner, i, this.bufferSize, this.delayError);
        latestCoordinator.subscribe(observableArr);
    }
}
