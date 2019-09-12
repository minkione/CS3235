package p009rx.internal.operators;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func0;
import p009rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeDefer */
public final class OnSubscribeDefer<T> implements OnSubscribe<T> {
    final Func0<? extends Observable<? extends T>> observableFactory;

    public OnSubscribeDefer(Func0<? extends Observable<? extends T>> func0) {
        this.observableFactory = func0;
    }

    public void call(Subscriber<? super T> subscriber) {
        try {
            ((Observable) this.observableFactory.call()).unsafeSubscribe(Subscribers.wrap(subscriber));
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer<?>) subscriber);
        }
    }
}
