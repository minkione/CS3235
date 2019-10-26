package p009rx.observers;

import p009rx.Observer;
import p009rx.Subscriber;

/* renamed from: rx.observers.SerializedSubscriber */
public class SerializedSubscriber<T> extends Subscriber<T> {

    /* renamed from: s */
    private final Observer<T> f274s;

    public SerializedSubscriber(Subscriber<? super T> subscriber) {
        this(subscriber, true);
    }

    public SerializedSubscriber(Subscriber<? super T> subscriber, boolean z) {
        super(subscriber, z);
        this.f274s = new SerializedObserver(subscriber);
    }

    public void onCompleted() {
        this.f274s.onCompleted();
    }

    public void onError(Throwable th) {
        this.f274s.onError(th);
    }

    public void onNext(T t) {
        this.f274s.onNext(t);
    }
}
