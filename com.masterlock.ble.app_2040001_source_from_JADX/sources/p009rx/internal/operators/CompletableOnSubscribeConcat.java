package p009rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Observable;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.MissingBackpressureException;
import p009rx.internal.subscriptions.SequentialSubscription;
import p009rx.internal.util.unsafe.SpscArrayQueue;
import p009rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.CompletableOnSubscribeConcat */
public final class CompletableOnSubscribeConcat implements OnSubscribe {
    final int prefetch;
    final Observable<Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeConcat$CompletableConcatSubscriber */
    static final class CompletableConcatSubscriber extends Subscriber<Completable> {
        volatile boolean active;
        final CompletableSubscriber actual;
        volatile boolean done;
        final ConcatInnerSubscriber inner = new ConcatInnerSubscriber();
        final AtomicBoolean once = new AtomicBoolean();
        final SpscArrayQueue<Completable> queue;

        /* renamed from: sr */
        final SequentialSubscription f236sr = new SequentialSubscription();

        /* renamed from: rx.internal.operators.CompletableOnSubscribeConcat$CompletableConcatSubscriber$ConcatInnerSubscriber */
        final class ConcatInnerSubscriber extends AtomicInteger implements CompletableSubscriber {
            private static final long serialVersionUID = 7233503139645205620L;

            ConcatInnerSubscriber() {
            }

            public void onSubscribe(Subscription subscription) {
                CompletableConcatSubscriber.this.f236sr.set(subscription);
            }

            public void onError(Throwable th) {
                CompletableConcatSubscriber.this.innerError(th);
            }

            public void onCompleted() {
                CompletableConcatSubscriber.this.innerComplete();
            }
        }

        public CompletableConcatSubscriber(CompletableSubscriber completableSubscriber, int i) {
            this.actual = completableSubscriber;
            this.queue = new SpscArrayQueue<>(i);
            add(this.f236sr);
            request((long) i);
        }

        public void onNext(Completable completable) {
            if (!this.queue.offer(completable)) {
                onError(new MissingBackpressureException());
            } else {
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.once.compareAndSet(false, true)) {
                this.actual.onError(th);
            } else {
                RxJavaHooks.onError(th);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable th) {
            unsubscribe();
            onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            this.active = false;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            ConcatInnerSubscriber concatInnerSubscriber = this.inner;
            if (concatInnerSubscriber.getAndIncrement() == 0) {
                while (!isUnsubscribed()) {
                    if (!this.active) {
                        boolean z = this.done;
                        Completable completable = (Completable) this.queue.poll();
                        boolean z2 = completable == null;
                        if (z && z2) {
                            this.actual.onCompleted();
                            return;
                        } else if (!z2) {
                            this.active = true;
                            completable.subscribe((CompletableSubscriber) concatInnerSubscriber);
                            request(1);
                        }
                    }
                    if (concatInnerSubscriber.decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeConcat(Observable<? extends Completable> observable, int i) {
        this.sources = observable;
        this.prefetch = i;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompletableConcatSubscriber completableConcatSubscriber = new CompletableConcatSubscriber(completableSubscriber, this.prefetch);
        completableSubscriber.onSubscribe(completableConcatSubscriber);
        this.sources.unsafeSubscribe(completableConcatSubscriber);
    }
}
