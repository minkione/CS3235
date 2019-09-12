package p009rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Subscription;
import p009rx.internal.subscriptions.SequentialSubscription;
import p009rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.CompletableOnSubscribeConcatIterable */
public final class CompletableOnSubscribeConcatIterable implements OnSubscribe {
    final Iterable<? extends Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeConcatIterable$ConcatInnerSubscriber */
    static final class ConcatInnerSubscriber extends AtomicInteger implements CompletableSubscriber {
        private static final long serialVersionUID = -7965400327305809232L;
        final CompletableSubscriber actual;

        /* renamed from: sd */
        final SequentialSubscription f238sd = new SequentialSubscription();
        final Iterator<? extends Completable> sources;

        public ConcatInnerSubscriber(CompletableSubscriber completableSubscriber, Iterator<? extends Completable> it) {
            this.actual = completableSubscriber;
            this.sources = it;
        }

        public void onSubscribe(Subscription subscription) {
            this.f238sd.replace(subscription);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onCompleted() {
            next();
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            if (!this.f238sd.isUnsubscribed() && getAndIncrement() == 0) {
                Iterator<? extends Completable> it = this.sources;
                while (!this.f238sd.isUnsubscribed()) {
                    try {
                        if (!it.hasNext()) {
                            this.actual.onCompleted();
                            return;
                        }
                        try {
                            Completable completable = (Completable) it.next();
                            if (completable == null) {
                                this.actual.onError(new NullPointerException("The completable returned is null"));
                                return;
                            }
                            completable.unsafeSubscribe((CompletableSubscriber) this);
                            if (decrementAndGet() == 0) {
                                return;
                            }
                        } catch (Throwable th) {
                            this.actual.onError(th);
                            return;
                        }
                    } catch (Throwable th2) {
                        this.actual.onError(th2);
                        return;
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeConcatIterable(Iterable<? extends Completable> iterable) {
        this.sources = iterable;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        try {
            Iterator it = this.sources.iterator();
            if (it == null) {
                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                completableSubscriber.onError(new NullPointerException("The iterator returned is null"));
                return;
            }
            ConcatInnerSubscriber concatInnerSubscriber = new ConcatInnerSubscriber(completableSubscriber, it);
            completableSubscriber.onSubscribe(concatInnerSubscriber.f238sd);
            concatInnerSubscriber.next();
        } catch (Throwable th) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
            completableSubscriber.onError(th);
        }
    }
}
