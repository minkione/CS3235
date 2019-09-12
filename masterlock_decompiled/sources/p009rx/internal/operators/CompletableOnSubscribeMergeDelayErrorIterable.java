package p009rx.internal.operators;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Subscription;
import p009rx.internal.util.atomic.MpscLinkedAtomicQueue;
import p009rx.internal.util.unsafe.MpscLinkedQueue;
import p009rx.internal.util.unsafe.UnsafeAccess;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable */
public final class CompletableOnSubscribeMergeDelayErrorIterable implements OnSubscribe {
    final Iterable<? extends Completable> sources;

    public CompletableOnSubscribeMergeDelayErrorIterable(Iterable<? extends Completable> iterable) {
        this.sources = iterable;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        Queue queue;
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        completableSubscriber.onSubscribe(compositeSubscription);
        try {
            Iterator it = this.sources.iterator();
            if (it == null) {
                completableSubscriber.onError(new NullPointerException("The source iterator returned is null"));
                return;
            }
            AtomicInteger atomicInteger = new AtomicInteger(1);
            if (UnsafeAccess.isUnsafeAvailable()) {
                queue = new MpscLinkedQueue();
            } else {
                queue = new MpscLinkedAtomicQueue();
            }
            while (!compositeSubscription.isUnsubscribed()) {
                try {
                    if (!it.hasNext()) {
                        if (atomicInteger.decrementAndGet() == 0) {
                            if (queue.isEmpty()) {
                                completableSubscriber.onCompleted();
                            } else {
                                completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(queue));
                            }
                        }
                        return;
                    } else if (!compositeSubscription.isUnsubscribed()) {
                        try {
                            Completable completable = (Completable) it.next();
                            if (!compositeSubscription.isUnsubscribed()) {
                                if (completable == null) {
                                    queue.offer(new NullPointerException("A completable source is null"));
                                    if (atomicInteger.decrementAndGet() == 0) {
                                        if (queue.isEmpty()) {
                                            completableSubscriber.onCompleted();
                                        } else {
                                            completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(queue));
                                        }
                                    }
                                    return;
                                }
                                atomicInteger.getAndIncrement();
                                final CompositeSubscription compositeSubscription2 = compositeSubscription;
                                final Queue queue2 = queue;
                                final AtomicInteger atomicInteger2 = atomicInteger;
                                final CompletableSubscriber completableSubscriber2 = completableSubscriber;
                                C20191 r0 = new CompletableSubscriber() {
                                    public void onSubscribe(Subscription subscription) {
                                        compositeSubscription2.add(subscription);
                                    }

                                    public void onError(Throwable th) {
                                        queue2.offer(th);
                                        tryTerminate();
                                    }

                                    public void onCompleted() {
                                        tryTerminate();
                                    }

                                    /* access modifiers changed from: 0000 */
                                    public void tryTerminate() {
                                        if (atomicInteger2.decrementAndGet() != 0) {
                                            return;
                                        }
                                        if (queue2.isEmpty()) {
                                            completableSubscriber2.onCompleted();
                                        } else {
                                            completableSubscriber2.onError(CompletableOnSubscribeMerge.collectErrors(queue2));
                                        }
                                    }
                                };
                                completable.unsafeSubscribe((CompletableSubscriber) r0);
                            } else {
                                return;
                            }
                        } catch (Throwable th) {
                            queue.offer(th);
                            if (atomicInteger.decrementAndGet() == 0) {
                                if (queue.isEmpty()) {
                                    completableSubscriber.onCompleted();
                                } else {
                                    completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(queue));
                                }
                            }
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th2) {
                    queue.offer(th2);
                    if (atomicInteger.decrementAndGet() == 0) {
                        if (queue.isEmpty()) {
                            completableSubscriber.onCompleted();
                        } else {
                            completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(queue));
                        }
                    }
                    return;
                }
            }
        } catch (Throwable th3) {
            completableSubscriber.onError(th3);
        }
    }
}
