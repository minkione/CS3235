package p009rx.internal.operators;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

/* renamed from: rx.internal.operators.EmptyObservableHolder */
public enum EmptyObservableHolder implements OnSubscribe<Object> {
    INSTANCE;
    
    static final Observable<Object> EMPTY = null;

    static {
        EmptyObservableHolder emptyObservableHolder;
        EMPTY = Observable.unsafeCreate(emptyObservableHolder);
    }

    public static <T> Observable<T> instance() {
        return EMPTY;
    }

    public void call(Subscriber<? super Object> subscriber) {
        subscriber.onCompleted();
    }
}
