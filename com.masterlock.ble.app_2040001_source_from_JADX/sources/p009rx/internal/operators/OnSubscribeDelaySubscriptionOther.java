package p009rx.internal.operators;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.observers.Subscribers;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.SerialSubscription;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OnSubscribeDelaySubscriptionOther */
public final class OnSubscribeDelaySubscriptionOther<T, U> implements OnSubscribe<T> {
    final Observable<? extends T> main;
    final Observable<U> other;

    public OnSubscribeDelaySubscriptionOther(Observable<? extends T> observable, Observable<U> observable2) {
        this.main = observable;
        this.other = observable2;
    }

    public void call(Subscriber<? super T> subscriber) {
        final SerialSubscription serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        final Subscriber wrap = Subscribers.wrap(subscriber);
        C20311 r1 = new Subscriber<U>() {
            boolean done;

            public void onNext(U u) {
                onCompleted();
            }

            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaHooks.onError(th);
                    return;
                }
                this.done = true;
                wrap.onError(th);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    serialSubscription.set(Subscriptions.unsubscribed());
                    OnSubscribeDelaySubscriptionOther.this.main.unsafeSubscribe(wrap);
                }
            }
        };
        serialSubscription.set(r1);
        this.other.unsafeSubscribe(r1);
    }
}
