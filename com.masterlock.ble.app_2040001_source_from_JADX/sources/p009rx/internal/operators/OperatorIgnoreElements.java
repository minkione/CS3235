package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorIgnoreElements */
public class OperatorIgnoreElements<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorIgnoreElements$Holder */
    static final class Holder {
        static final OperatorIgnoreElements<?> INSTANCE = new OperatorIgnoreElements<>();

        Holder() {
        }
    }

    public static <T> OperatorIgnoreElements<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorIgnoreElements() {
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        C20831 r0 = new Subscriber<T>() {
            public void onNext(T t) {
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }
        };
        subscriber.add(r0);
        return r0;
    }
}
