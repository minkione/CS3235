package p009rx.internal.operators;

import p009rx.Observable.Operator;
import p009rx.Producer;
import p009rx.Scheduler;
import p009rx.Scheduler.Worker;
import p009rx.Subscriber;
import p009rx.functions.Action0;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorUnsubscribeOn */
public class OperatorUnsubscribeOn<T> implements Operator<T, T> {
    final Scheduler scheduler;

    public OperatorUnsubscribeOn(Scheduler scheduler2) {
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final C21481 r0 = new Subscriber<T>() {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T t) {
                subscriber.onNext(t);
            }

            public void setProducer(Producer producer) {
                subscriber.setProducer(producer);
            }
        };
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                final Worker createWorker = OperatorUnsubscribeOn.this.scheduler.createWorker();
                createWorker.schedule(new Action0() {
                    public void call() {
                        r0.unsubscribe();
                        createWorker.unsubscribe();
                    }
                });
            }
        }));
        return r0;
    }
}
