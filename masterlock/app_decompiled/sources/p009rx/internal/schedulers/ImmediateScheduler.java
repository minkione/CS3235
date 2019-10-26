package p009rx.internal.schedulers;

import java.util.concurrent.TimeUnit;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.subscriptions.BooleanSubscription;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.schedulers.ImmediateScheduler */
public final class ImmediateScheduler extends Scheduler {
    public static final ImmediateScheduler INSTANCE = new ImmediateScheduler();

    /* renamed from: rx.internal.schedulers.ImmediateScheduler$InnerImmediateScheduler */
    final class InnerImmediateScheduler extends Worker implements Subscription {
        final BooleanSubscription innerSubscription = new BooleanSubscription();

        InnerImmediateScheduler() {
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            return schedule(new SleepingAction(action0, this, ImmediateScheduler.this.now() + timeUnit.toMillis(j)));
        }

        public Subscription schedule(Action0 action0) {
            action0.call();
            return Subscriptions.unsubscribed();
        }

        public void unsubscribe() {
            this.innerSubscription.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }
    }

    private ImmediateScheduler() {
    }

    public Worker createWorker() {
        return new InnerImmediateScheduler();
    }
}
