package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Action0;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorDoAfterTerminate */
public final class OperatorDoAfterTerminate<T> implements Operator<T, T> {
    final Action0 action;

    public OperatorDoAfterTerminate(Action0 action0) {
        if (action0 != null) {
            this.action = action0;
            return;
        }
        throw new NullPointerException("Action can not be null");
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onNext(T t) {
                subscriber.onNext(t);
            }

            public void onError(Throwable th) {
                try {
                    subscriber.onError(th);
                } finally {
                    callAction();
                }
            }

            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    callAction();
                }
            }

            /* access modifiers changed from: 0000 */
            public void callAction() {
                try {
                    OperatorDoAfterTerminate.this.action.call();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaHooks.onError(th);
                }
            }
        };
    }
}
