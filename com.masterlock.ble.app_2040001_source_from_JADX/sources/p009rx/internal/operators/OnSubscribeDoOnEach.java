package p009rx.internal.operators;

import java.util.Arrays;
import java.util.Collection;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Subscriber;
import p009rx.exceptions.CompositeException;
import p009rx.exceptions.Exceptions;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeDoOnEach */
public class OnSubscribeDoOnEach<T> implements OnSubscribe<T> {
    private final Observer<? super T> doOnEachObserver;
    private final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeDoOnEach$DoOnEachSubscriber */
    private static final class DoOnEachSubscriber<T> extends Subscriber<T> {
        private final Observer<? super T> doOnEachObserver;
        private boolean done;
        private final Subscriber<? super T> subscriber;

        DoOnEachSubscriber(Subscriber<? super T> subscriber2, Observer<? super T> observer) {
            super(subscriber2);
            this.subscriber = subscriber2;
            this.doOnEachObserver = observer;
        }

        public void onCompleted() {
            if (!this.done) {
                try {
                    this.doOnEachObserver.onCompleted();
                    this.done = true;
                    this.subscriber.onCompleted();
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer<?>) this);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            this.done = true;
            try {
                this.doOnEachObserver.onError(th);
                this.subscriber.onError(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.subscriber.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{th, th2})));
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.doOnEachObserver.onNext(t);
                    this.subscriber.onNext(t);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer<?>) this, (Object) t);
                }
            }
        }
    }

    public OnSubscribeDoOnEach(Observable<T> observable, Observer<? super T> observer) {
        this.source = observable;
        this.doOnEachObserver = observer;
    }

    public void call(Subscriber<? super T> subscriber) {
        this.source.unsafeSubscribe(new DoOnEachSubscriber(subscriber, this.doOnEachObserver));
    }
}
