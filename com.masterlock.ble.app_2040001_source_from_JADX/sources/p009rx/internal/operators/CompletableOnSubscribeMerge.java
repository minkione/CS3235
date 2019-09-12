package p009rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Observable;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.exceptions.CompositeException;
import p009rx.plugins.RxJavaHooks;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMerge */
public final class CompletableOnSubscribeMerge implements OnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMerge$CompletableMergeSubscriber */
    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        final CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicReference<Queue<Throwable>> errors = new AtomicReference<>();
        final AtomicBoolean once = new AtomicBoolean();
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);

        public CompletableMergeSubscriber(CompletableSubscriber completableSubscriber, int i, boolean z) {
            this.actual = completableSubscriber;
            this.delayErrors = z;
            if (i == Integer.MAX_VALUE) {
                request(Long.MAX_VALUE);
            } else {
                request((long) i);
            }
        }

        /* access modifiers changed from: 0000 */
        public Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> queue = (Queue) this.errors.get();
            if (queue != null) {
                return queue;
            }
            ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
            if (this.errors.compareAndSet(null, concurrentLinkedQueue)) {
                return concurrentLinkedQueue;
            }
            return (Queue) this.errors.get();
        }

        public void onNext(Completable completable) {
            if (!this.done) {
                this.wip.getAndIncrement();
                completable.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {

                    /* renamed from: d */
                    Subscription f239d;
                    boolean innerDone;

                    public void onSubscribe(Subscription subscription) {
                        this.f239d = subscription;
                        CompletableMergeSubscriber.this.set.add(subscription);
                    }

                    public void onError(Throwable th) {
                        if (this.innerDone) {
                            RxJavaHooks.onError(th);
                            return;
                        }
                        this.innerDone = true;
                        CompletableMergeSubscriber.this.set.remove(this.f239d);
                        CompletableMergeSubscriber.this.getOrCreateErrors().offer(th);
                        CompletableMergeSubscriber.this.terminate();
                        if (CompletableMergeSubscriber.this.delayErrors && !CompletableMergeSubscriber.this.done) {
                            CompletableMergeSubscriber.this.request(1);
                        }
                    }

                    public void onCompleted() {
                        if (!this.innerDone) {
                            this.innerDone = true;
                            CompletableMergeSubscriber.this.set.remove(this.f239d);
                            CompletableMergeSubscriber.this.terminate();
                            if (!CompletableMergeSubscriber.this.done) {
                                CompletableMergeSubscriber.this.request(1);
                            }
                        }
                    }
                });
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaHooks.onError(th);
                return;
            }
            getOrCreateErrors().offer(th);
            this.done = true;
            terminate();
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                terminate();
            }
        }

        /* access modifiers changed from: 0000 */
        public void terminate() {
            if (this.wip.decrementAndGet() == 0) {
                Queue queue = (Queue) this.errors.get();
                if (queue == null || queue.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                Throwable collectErrors = CompletableOnSubscribeMerge.collectErrors(queue);
                if (this.once.compareAndSet(false, true)) {
                    this.actual.onError(collectErrors);
                } else {
                    RxJavaHooks.onError(collectErrors);
                }
            } else if (!this.delayErrors) {
                Queue queue2 = (Queue) this.errors.get();
                if (queue2 != null && !queue2.isEmpty()) {
                    Throwable collectErrors2 = CompletableOnSubscribeMerge.collectErrors(queue2);
                    if (this.once.compareAndSet(false, true)) {
                        this.actual.onError(collectErrors2);
                    } else {
                        RxJavaHooks.onError(collectErrors2);
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeMerge(Observable<? extends Completable> observable, int i, boolean z) {
        this.source = observable;
        this.maxConcurrency = i;
        this.delayErrors = z;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompletableMergeSubscriber completableMergeSubscriber = new CompletableMergeSubscriber(completableSubscriber, this.maxConcurrency, this.delayErrors);
        completableSubscriber.onSubscribe(completableMergeSubscriber);
        this.source.unsafeSubscribe(completableMergeSubscriber);
    }

    public static Throwable collectErrors(Queue<Throwable> queue) {
        ArrayList arrayList = new ArrayList();
        while (true) {
            Throwable th = (Throwable) queue.poll();
            if (th == null) {
                break;
            }
            arrayList.add(th);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        if (arrayList.size() == 1) {
            return (Throwable) arrayList.get(0);
        }
        return new CompositeException((Collection<? extends Throwable>) arrayList);
    }
}
