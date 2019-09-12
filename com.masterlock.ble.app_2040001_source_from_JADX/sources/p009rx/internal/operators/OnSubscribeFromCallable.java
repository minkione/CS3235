package p009rx.internal.operators;

import java.util.concurrent.Callable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.internal.producers.SingleDelayedProducer;

/* renamed from: rx.internal.operators.OnSubscribeFromCallable */
public final class OnSubscribeFromCallable<T> implements OnSubscribe<T> {
    private final Callable<? extends T> resultFactory;

    public OnSubscribeFromCallable(Callable<? extends T> callable) {
        this.resultFactory = callable;
    }

    public void call(Subscriber<? super T> subscriber) {
        SingleDelayedProducer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        subscriber.setProducer(singleDelayedProducer);
        try {
            singleDelayedProducer.setValue(this.resultFactory.call());
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer<?>) subscriber);
        }
    }
}
