package p009rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Subscription;
import p009rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray */
public final class CompletableOnSubscribeMergeDelayErrorArray implements OnSubscribe {
    final Completable[] sources;

    public CompletableOnSubscribeMergeDelayErrorArray(Completable[] completableArr) {
        this.sources = completableArr;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AtomicInteger atomicInteger = new AtomicInteger(this.sources.length + 1);
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        completableSubscriber.onSubscribe(compositeSubscription);
        Completable[] completableArr = this.sources;
        int length = completableArr.length;
        int i = 0;
        while (i < length) {
            Completable completable = completableArr[i];
            if (!compositeSubscription.isUnsubscribed()) {
                if (completable == null) {
                    concurrentLinkedQueue.offer(new NullPointerException("A completable source is null"));
                    atomicInteger.decrementAndGet();
                } else {
                    final CompositeSubscription compositeSubscription2 = compositeSubscription;
                    final ConcurrentLinkedQueue concurrentLinkedQueue2 = concurrentLinkedQueue;
                    final AtomicInteger atomicInteger2 = atomicInteger;
                    final CompletableSubscriber completableSubscriber2 = completableSubscriber;
                    C20181 r0 = new CompletableSubscriber() {
                        public void onSubscribe(Subscription subscription) {
                            compositeSubscription2.add(subscription);
                        }

                        public void onError(Throwable th) {
                            concurrentLinkedQueue2.offer(th);
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
                            if (concurrentLinkedQueue2.isEmpty()) {
                                completableSubscriber2.onCompleted();
                            } else {
                                completableSubscriber2.onError(CompletableOnSubscribeMerge.collectErrors(concurrentLinkedQueue2));
                            }
                        }
                    };
                    completable.unsafeSubscribe((CompletableSubscriber) r0);
                }
                i++;
            } else {
                return;
            }
        }
        if (atomicInteger.decrementAndGet() == 0) {
            if (concurrentLinkedQueue.isEmpty()) {
                completableSubscriber.onCompleted();
            } else {
                completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(concurrentLinkedQueue));
            }
        }
    }
}
