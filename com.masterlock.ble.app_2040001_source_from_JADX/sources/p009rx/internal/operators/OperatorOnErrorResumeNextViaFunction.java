package p009rx.internal.operators;

import p009rx.Observable;
import p009rx.Observable.Operator;
import p009rx.Observer;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;
import p009rx.internal.producers.ProducerArbiter;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction */
public final class OperatorOnErrorResumeNextViaFunction<T> implements Operator<T, T> {
    final Func1<? super Throwable, ? extends Observable<? extends T>> resumeFunction;

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withSingle(final Func1<? super Throwable, ? extends T> func1) {
        return new OperatorOnErrorResumeNextViaFunction<>(new Func1<Throwable, Observable<? extends T>>() {
            public Observable<? extends T> call(Throwable th) {
                return Observable.just(func1.call(th));
            }
        });
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withOther(final Observable<? extends T> observable) {
        return new OperatorOnErrorResumeNextViaFunction<>(new Func1<Throwable, Observable<? extends T>>() {
            public Observable<? extends T> call(Throwable th) {
                return observable;
            }
        });
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withException(final Observable<? extends T> observable) {
        return new OperatorOnErrorResumeNextViaFunction<>(new Func1<Throwable, Observable<? extends T>>() {
            public Observable<? extends T> call(Throwable th) {
                if (th instanceof Exception) {
                    return observable;
                }
                return Observable.error(th);
            }
        });
    }

    public OperatorOnErrorResumeNextViaFunction(Func1<? super Throwable, ? extends Observable<? extends T>> func1) {
        this.resumeFunction = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final ProducerArbiter producerArbiter = new ProducerArbiter();
        final SerialSubscription serialSubscription = new SerialSubscription();
        C20974 r2 = new Subscriber<T>() {
            private boolean done;
            long produced;

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    subscriber.onCompleted();
                }
            }

            public void onError(Throwable th) {
                if (this.done) {
                    Exceptions.throwIfFatal(th);
                    RxJavaHooks.onError(th);
                    return;
                }
                this.done = true;
                try {
                    unsubscribe();
                    C20981 r0 = new Subscriber<T>() {
                        public void onNext(T t) {
                            subscriber.onNext(t);
                        }

                        public void onError(Throwable th) {
                            subscriber.onError(th);
                        }

                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        public void setProducer(Producer producer) {
                            producerArbiter.setProducer(producer);
                        }
                    };
                    serialSubscription.set(r0);
                    long j = this.produced;
                    if (j != 0) {
                        producerArbiter.produced(j);
                    }
                    ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(th)).unsafeSubscribe(r0);
                } catch (Throwable th2) {
                    Exceptions.throwOrReport(th2, (Observer<?>) subscriber);
                }
            }

            public void onNext(T t) {
                if (!this.done) {
                    this.produced++;
                    subscriber.onNext(t);
                }
            }

            public void setProducer(Producer producer) {
                producerArbiter.setProducer(producer);
            }
        };
        serialSubscription.set(r2);
        subscriber.add(serialSubscription);
        subscriber.setProducer(producerArbiter);
        return r2;
    }
}
