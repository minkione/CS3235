package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Subscriber;
import p009rx.functions.Action0;
import p009rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OperatorDoOnSubscribe */
public class OperatorDoOnSubscribe<T> implements Operator<T, T> {
    private final Action0 subscribe;

    public OperatorDoOnSubscribe(Action0 action0) {
        this.subscribe = action0;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        this.subscribe.call();
        return Subscribers.wrap(subscriber);
    }
}
