package p009rx.internal.operators;

import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;

/* renamed from: rx.internal.operators.SingleOnErrorReturn */
public final class SingleOnErrorReturn<T> implements OnSubscribe<T> {
    final Func1<Throwable, ? extends T> resumeFunction;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleOnErrorReturn$OnErrorReturnsSingleSubscriber */
    static final class OnErrorReturnsSingleSubscriber<T> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final Func1<Throwable, ? extends T> resumeFunction;

        public OnErrorReturnsSingleSubscriber(SingleSubscriber<? super T> singleSubscriber, Func1<Throwable, ? extends T> func1) {
            this.actual = singleSubscriber;
            this.resumeFunction = func1;
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
        }

        public void onError(Throwable th) {
            try {
                this.actual.onSuccess(this.resumeFunction.call(th));
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(th2);
            }
        }
    }

    public SingleOnErrorReturn(OnSubscribe<T> onSubscribe, Func1<Throwable, ? extends T> func1) {
        this.source = onSubscribe;
        this.resumeFunction = func1;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        OnErrorReturnsSingleSubscriber onErrorReturnsSingleSubscriber = new OnErrorReturnsSingleSubscriber(singleSubscriber, this.resumeFunction);
        singleSubscriber.add(onErrorReturnsSingleSubscriber);
        this.source.call(onErrorReturnsSingleSubscriber);
    }
}
