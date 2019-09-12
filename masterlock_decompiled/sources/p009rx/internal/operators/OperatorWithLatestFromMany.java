package p009rx.internal.operators;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.FuncN;
import p009rx.observers.SerializedSubscriber;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorWithLatestFromMany */
public final class OperatorWithLatestFromMany<T, R> implements OnSubscribe<R> {
    final FuncN<R> combiner;
    final Observable<T> main;
    final Observable<?>[] others;
    final Iterable<Observable<?>> othersIterable;

    /* renamed from: rx.internal.operators.OperatorWithLatestFromMany$WithLatestMainSubscriber */
    static final class WithLatestMainSubscriber<T, R> extends Subscriber<T> {
        static final Object EMPTY = new Object();
        final Subscriber<? super R> actual;
        final FuncN<R> combiner;
        final AtomicReferenceArray<Object> current;
        boolean done;
        final AtomicInteger ready;

        public WithLatestMainSubscriber(Subscriber<? super R> subscriber, FuncN<R> funcN, int i) {
            this.actual = subscriber;
            this.combiner = funcN;
            AtomicReferenceArray<Object> atomicReferenceArray = new AtomicReferenceArray<>(i + 1);
            for (int i2 = 0; i2 <= i; i2++) {
                atomicReferenceArray.lazySet(i2, EMPTY);
            }
            this.current = atomicReferenceArray;
            this.ready = new AtomicInteger(i);
            request(0);
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.ready.get() == 0) {
                    AtomicReferenceArray<Object> atomicReferenceArray = this.current;
                    int length = atomicReferenceArray.length();
                    atomicReferenceArray.lazySet(0, t);
                    Object[] objArr = new Object[atomicReferenceArray.length()];
                    for (int i = 0; i < length; i++) {
                        objArr[i] = atomicReferenceArray.get(i);
                    }
                    try {
                        this.actual.onNext(this.combiner.call(objArr));
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        onError(th);
                    }
                } else {
                    request(1);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            this.done = true;
            unsubscribe();
            this.actual.onError(th);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                unsubscribe();
                this.actual.onCompleted();
            }
        }

        public void setProducer(Producer producer) {
            super.setProducer(producer);
            this.actual.setProducer(producer);
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(int i, Object obj) {
            if (this.current.getAndSet(i, obj) == EMPTY) {
                this.ready.decrementAndGet();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(int i, Throwable th) {
            onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int i) {
            if (this.current.get(i) == EMPTY) {
                onCompleted();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorWithLatestFromMany$WithLatestOtherSubscriber */
    static final class WithLatestOtherSubscriber extends Subscriber<Object> {
        final int index;
        final WithLatestMainSubscriber<?, ?> parent;

        public WithLatestOtherSubscriber(WithLatestMainSubscriber<?, ?> withLatestMainSubscriber, int i) {
            this.parent = withLatestMainSubscriber;
            this.index = i;
        }

        public void onNext(Object obj) {
            this.parent.innerNext(this.index, obj);
        }

        public void onError(Throwable th) {
            this.parent.innerError(this.index, th);
        }

        public void onCompleted() {
            this.parent.innerComplete(this.index);
        }
    }

    public OperatorWithLatestFromMany(Observable<T> observable, Observable<?>[] observableArr, Iterable<Observable<?>> iterable, FuncN<R> funcN) {
        this.main = observable;
        this.others = observableArr;
        this.othersIterable = iterable;
        this.combiner = funcN;
    }

    public void call(Subscriber<? super R> subscriber) {
        Observable<?>[] observableArr;
        int i;
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        Observable<?>[] observableArr2 = this.others;
        int i2 = 0;
        if (observableArr2 != null) {
            observableArr = observableArr2;
            i = observableArr2.length;
        } else {
            Observable<?>[] observableArr3 = new Observable[8];
            observableArr = observableArr3;
            i = 0;
            for (Observable<?> observable : this.othersIterable) {
                if (i == observableArr.length) {
                    observableArr = (Observable[]) Arrays.copyOf(observableArr, (i >> 2) + i);
                }
                int i3 = i + 1;
                observableArr[i] = observable;
                i = i3;
            }
        }
        WithLatestMainSubscriber withLatestMainSubscriber = new WithLatestMainSubscriber(subscriber, this.combiner, i);
        serializedSubscriber.add(withLatestMainSubscriber);
        while (i2 < i) {
            if (!serializedSubscriber.isUnsubscribed()) {
                int i4 = i2 + 1;
                WithLatestOtherSubscriber withLatestOtherSubscriber = new WithLatestOtherSubscriber(withLatestMainSubscriber, i4);
                withLatestMainSubscriber.add(withLatestOtherSubscriber);
                observableArr[i2].unsafeSubscribe(withLatestOtherSubscriber);
                i2 = i4;
            } else {
                return;
            }
        }
        this.main.unsafeSubscribe(withLatestMainSubscriber);
    }
}
