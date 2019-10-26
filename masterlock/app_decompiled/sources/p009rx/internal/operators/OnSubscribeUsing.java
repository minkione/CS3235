package p009rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Observer;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.CompositeException;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Action0;
import p009rx.functions.Action1;
import p009rx.functions.Func0;
import p009rx.functions.Func1;
import p009rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeUsing */
public final class OnSubscribeUsing<T, Resource> implements OnSubscribe<T> {
    private final Action1<? super Resource> dispose;
    private final boolean disposeEagerly;
    private final Func1<? super Resource, ? extends Observable<? extends T>> observableFactory;
    private final Func0<Resource> resourceFactory;

    /* renamed from: rx.internal.operators.OnSubscribeUsing$DisposeAction */
    static final class DisposeAction<Resource> extends AtomicBoolean implements Action0, Subscription {
        private static final long serialVersionUID = 4262875056400218316L;
        private Action1<? super Resource> dispose;
        private Resource resource;

        DisposeAction(Action1<? super Resource> action1, Resource resource2) {
            this.dispose = action1;
            this.resource = resource2;
            lazySet(false);
        }

        public void call() {
            if (compareAndSet(false, true)) {
                try {
                    this.dispose.call(this.resource);
                } finally {
                    this.resource = null;
                    this.dispose = null;
                }
            }
        }

        public boolean isUnsubscribed() {
            return get();
        }

        public void unsubscribe() {
            call();
        }
    }

    public OnSubscribeUsing(Func0<Resource> func0, Func1<? super Resource, ? extends Observable<? extends T>> func1, Action1<? super Resource> action1, boolean z) {
        this.resourceFactory = func0;
        this.observableFactory = func1;
        this.dispose = action1;
        this.disposeEagerly = z;
    }

    public void call(Subscriber<? super T> subscriber) {
        DisposeAction disposeAction;
        Observable observable;
        try {
            Object call = this.resourceFactory.call();
            disposeAction = new DisposeAction(this.dispose, call);
            subscriber.add(disposeAction);
            try {
                Observable observable2 = (Observable) this.observableFactory.call(call);
                if (this.disposeEagerly) {
                    observable = observable2.doOnTerminate(disposeAction);
                } else {
                    observable = observable2.doAfterTerminate(disposeAction);
                }
                observable.unsafeSubscribe(Subscribers.wrap(subscriber));
            } catch (Throwable th) {
                Throwable dispose2 = dispose(disposeAction);
                Exceptions.throwIfFatal(th);
                Exceptions.throwIfFatal(dispose2);
                if (dispose2 != null) {
                    subscriber.onError(new CompositeException(th, dispose2));
                } else {
                    subscriber.onError(th);
                }
            }
        } catch (Throwable th2) {
            Exceptions.throwOrReport(th2, (Observer<?>) subscriber);
        }
    }

    private Throwable dispose(Action0 action0) {
        try {
            action0.call();
            return null;
        } catch (Throwable th) {
            return th;
        }
    }
}
