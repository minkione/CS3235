package p009rx.observables;

import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

/* renamed from: rx.observables.GroupedObservable */
public class GroupedObservable<K, T> extends Observable<T> {
    private final K key;

    public static <K, T> GroupedObservable<K, T> from(K k, final Observable<T> observable) {
        return new GroupedObservable<>(k, new OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
                observable.unsafeSubscribe(subscriber);
            }
        });
    }

    public static <K, T> GroupedObservable<K, T> create(K k, OnSubscribe<T> onSubscribe) {
        return new GroupedObservable<>(k, onSubscribe);
    }

    protected GroupedObservable(K k, OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
        this.key = k;
    }

    public K getKey() {
        return this.key;
    }
}
