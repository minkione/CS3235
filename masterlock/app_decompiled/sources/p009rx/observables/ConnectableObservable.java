package p009rx.observables;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.functions.Actions;
import p009rx.internal.operators.OnSubscribeAutoConnect;
import p009rx.internal.operators.OnSubscribeRefCount;

/* renamed from: rx.observables.ConnectableObservable */
public abstract class ConnectableObservable<T> extends Observable<T> {
    public abstract void connect(Action1<? super Subscription> action1);

    protected ConnectableObservable(OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
    }

    public final Subscription connect() {
        final Subscription[] subscriptionArr = new Subscription[1];
        connect(new Action1<Subscription>() {
            public void call(Subscription subscription) {
                subscriptionArr[0] = subscription;
            }
        });
        return subscriptionArr[0];
    }

    public Observable<T> refCount() {
        return unsafeCreate(new OnSubscribeRefCount(this));
    }

    public Observable<T> autoConnect() {
        return autoConnect(1);
    }

    public Observable<T> autoConnect(int i) {
        return autoConnect(i, Actions.empty());
    }

    public Observable<T> autoConnect(int i, Action1<? super Subscription> action1) {
        if (i > 0) {
            return unsafeCreate(new OnSubscribeAutoConnect(this, i, action1));
        }
        connect(action1);
        return this;
    }
}
