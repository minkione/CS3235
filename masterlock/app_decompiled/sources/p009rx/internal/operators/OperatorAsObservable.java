package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorAsObservable */
public final class OperatorAsObservable<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorAsObservable$Holder */
    static final class Holder {
        static final OperatorAsObservable<Object> INSTANCE = new OperatorAsObservable<>();

        Holder() {
        }
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return subscriber;
    }

    public static <T> OperatorAsObservable<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorAsObservable() {
    }
}
