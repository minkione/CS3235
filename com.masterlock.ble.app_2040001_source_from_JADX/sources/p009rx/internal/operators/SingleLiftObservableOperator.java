package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Single.OnSubscribe;
import p009rx.SingleSubscriber;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.internal.producers.SingleProducer;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleLiftObservableOperator */
public final class SingleLiftObservableOperator<T, R> implements OnSubscribe<R> {
    final Operator<? extends R, ? super T> lift;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleLiftObservableOperator$WrapSubscriberIntoSingle */
    static final class WrapSubscriberIntoSingle<T> extends SingleSubscriber<T> {
        final Subscriber<? super T> actual;

        WrapSubscriberIntoSingle(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        public void onSuccess(T t) {
            Subscriber<? super T> subscriber = this.actual;
            subscriber.setProducer(new SingleProducer(subscriber, t));
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }
    }

    public SingleLiftObservableOperator(OnSubscribe<T> onSubscribe, Operator<? extends R, ? super T> operator) {
        this.source = onSubscribe;
        this.lift = operator;
    }

    public void call(SingleSubscriber<? super R> singleSubscriber) {
        WrapSingleIntoSubscriber wrapSingleIntoSubscriber = new WrapSingleIntoSubscriber(singleSubscriber);
        singleSubscriber.add(wrapSingleIntoSubscriber);
        try {
            Subscriber subscriber = (Subscriber) RxJavaHooks.onSingleLift(this.lift).call(wrapSingleIntoSubscriber);
            SingleSubscriber wrap = wrap(subscriber);
            subscriber.onStart();
            this.source.call(wrap);
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, singleSubscriber);
        }
    }

    public static <T> SingleSubscriber<T> wrap(Subscriber<T> subscriber) {
        WrapSubscriberIntoSingle wrapSubscriberIntoSingle = new WrapSubscriberIntoSingle(subscriber);
        subscriber.add(wrapSubscriberIntoSingle);
        return wrapSubscriberIntoSingle;
    }
}
