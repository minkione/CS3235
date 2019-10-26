package p009rx.internal.operators;

import p009rx.Single;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.exceptions.Exceptions;
import p009rx.exceptions.OnErrorThrowable;
import p009rx.functions.Func1;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleOnSubscribeMap */
public final class SingleOnSubscribeMap<T, R> implements OnSubscribe<R> {
    final Single<T> source;
    final Func1<? super T, ? extends R> transformer;

    /* renamed from: rx.internal.operators.SingleOnSubscribeMap$MapSubscriber */
    static final class MapSubscriber<T, R> extends SingleSubscriber<T> {
        final SingleSubscriber<? super R> actual;
        boolean done;
        final Func1<? super T, ? extends R> mapper;

        public MapSubscriber(SingleSubscriber<? super R> singleSubscriber, Func1<? super T, ? extends R> func1) {
            this.actual = singleSubscriber;
            this.mapper = func1;
        }

        public void onSuccess(T t) {
            try {
                this.actual.onSuccess(this.mapper.call(t));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(th, t));
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }
    }

    public SingleOnSubscribeMap(Single<T> single, Func1<? super T, ? extends R> func1) {
        this.source = single;
        this.transformer = func1;
    }

    public void call(SingleSubscriber<? super R> singleSubscriber) {
        MapSubscriber mapSubscriber = new MapSubscriber(singleSubscriber, this.transformer);
        singleSubscriber.add(mapSubscriber);
        this.source.subscribe((SingleSubscriber<? super T>) mapSubscriber);
    }
}
