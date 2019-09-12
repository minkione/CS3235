package p009rx.internal.operators;

import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Action0;

/* renamed from: rx.internal.operators.SingleDoOnSubscribe */
public final class SingleDoOnSubscribe<T> implements OnSubscribe<T> {
    final Action0 onSubscribe;
    final OnSubscribe<T> source;

    public SingleDoOnSubscribe(OnSubscribe<T> onSubscribe2, Action0 action0) {
        this.source = onSubscribe2;
        this.onSubscribe = action0;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        try {
            this.onSubscribe.call();
            this.source.call(singleSubscriber);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            singleSubscriber.onError(th);
        }
    }
}
