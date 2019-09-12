package p009rx.internal.schedulers;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Subscription;
import p009rx.exceptions.OnErrorNotImplementedException;
import p009rx.functions.Action0;
import p009rx.internal.util.SubscriptionList;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.schedulers.ScheduledAction */
public final class ScheduledAction extends AtomicReference<Thread> implements Runnable, Subscription {
    private static final long serialVersionUID = -3962399486978279857L;
    final Action0 action;
    final SubscriptionList cancel;

    /* renamed from: rx.internal.schedulers.ScheduledAction$FutureCompleter */
    final class FutureCompleter implements Subscription {

        /* renamed from: f */
        private final Future<?> f257f;

        FutureCompleter(Future<?> future) {
            this.f257f = future;
        }

        public void unsubscribe() {
            if (ScheduledAction.this.get() != Thread.currentThread()) {
                this.f257f.cancel(true);
            } else {
                this.f257f.cancel(false);
            }
        }

        public boolean isUnsubscribed() {
            return this.f257f.isCancelled();
        }
    }

    /* renamed from: rx.internal.schedulers.ScheduledAction$Remover */
    static final class Remover extends AtomicBoolean implements Subscription {
        private static final long serialVersionUID = 247232374289553518L;
        final CompositeSubscription parent;

        /* renamed from: s */
        final ScheduledAction f258s;

        public Remover(ScheduledAction scheduledAction, CompositeSubscription compositeSubscription) {
            this.f258s = scheduledAction;
            this.parent = compositeSubscription;
        }

        public boolean isUnsubscribed() {
            return this.f258s.isUnsubscribed();
        }

        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                this.parent.remove(this.f258s);
            }
        }
    }

    /* renamed from: rx.internal.schedulers.ScheduledAction$Remover2 */
    static final class Remover2 extends AtomicBoolean implements Subscription {
        private static final long serialVersionUID = 247232374289553518L;
        final SubscriptionList parent;

        /* renamed from: s */
        final ScheduledAction f259s;

        public Remover2(ScheduledAction scheduledAction, SubscriptionList subscriptionList) {
            this.f259s = scheduledAction;
            this.parent = subscriptionList;
        }

        public boolean isUnsubscribed() {
            return this.f259s.isUnsubscribed();
        }

        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                this.parent.remove(this.f259s);
            }
        }
    }

    public ScheduledAction(Action0 action0) {
        this.action = action0;
        this.cancel = new SubscriptionList();
    }

    public ScheduledAction(Action0 action0, CompositeSubscription compositeSubscription) {
        this.action = action0;
        this.cancel = new SubscriptionList((Subscription) new Remover(this, compositeSubscription));
    }

    public ScheduledAction(Action0 action0, SubscriptionList subscriptionList) {
        this.action = action0;
        this.cancel = new SubscriptionList((Subscription) new Remover2(this, subscriptionList));
    }

    public void run() {
        try {
            lazySet(Thread.currentThread());
            this.action.call();
        } catch (OnErrorNotImplementedException e) {
            signalError(new IllegalStateException("Exception thrown on Scheduler.Worker thread. Add `onError` handling.", e));
        } catch (Throwable th) {
            unsubscribe();
            throw th;
        }
        unsubscribe();
    }

    /* access modifiers changed from: 0000 */
    public void signalError(Throwable th) {
        RxJavaHooks.onError(th);
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }

    public boolean isUnsubscribed() {
        return this.cancel.isUnsubscribed();
    }

    public void unsubscribe() {
        if (!this.cancel.isUnsubscribed()) {
            this.cancel.unsubscribe();
        }
    }

    public void add(Subscription subscription) {
        this.cancel.add(subscription);
    }

    public void add(Future<?> future) {
        this.cancel.add(new FutureCompleter(future));
    }

    public void addParent(CompositeSubscription compositeSubscription) {
        this.cancel.add(new Remover(this, compositeSubscription));
    }

    public void addParent(SubscriptionList subscriptionList) {
        this.cancel.add(new Remover2(this, subscriptionList));
    }
}
