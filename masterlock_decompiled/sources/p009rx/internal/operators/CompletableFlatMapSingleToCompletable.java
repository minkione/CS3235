package p009rx.internal.operators;

import p009rx.Completable;
import p009rx.Completable.OnSubscribe;
import p009rx.CompletableSubscriber;
import p009rx.Single;
import p009rx.SingleSubscriber;
import p009rx.Subscription;
import p009rx.exceptions.Exceptions;
import p009rx.functions.Func1;

/* renamed from: rx.internal.operators.CompletableFlatMapSingleToCompletable */
public final class CompletableFlatMapSingleToCompletable<T> implements OnSubscribe {
    final Func1<? super T, ? extends Completable> mapper;
    final Single<T> source;

    /* renamed from: rx.internal.operators.CompletableFlatMapSingleToCompletable$SourceSubscriber */
    static final class SourceSubscriber<T> extends SingleSubscriber<T> implements CompletableSubscriber {
        final CompletableSubscriber actual;
        final Func1<? super T, ? extends Completable> mapper;

        public SourceSubscriber(CompletableSubscriber completableSubscriber, Func1<? super T, ? extends Completable> func1) {
            this.actual = completableSubscriber;
            this.mapper = func1;
        }

        public void onSuccess(T t) {
            try {
                Completable completable = (Completable) this.mapper.call(t);
                if (completable == null) {
                    onError(new NullPointerException("The mapper returned a null Completable"));
                } else {
                    completable.subscribe((CompletableSubscriber) this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                onError(th);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        public void onSubscribe(Subscription subscription) {
            add(subscription);
        }
    }

    public CompletableFlatMapSingleToCompletable(Single<T> single, Func1<? super T, ? extends Completable> func1) {
        this.source = single;
        this.mapper = func1;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        SourceSubscriber sourceSubscriber = new SourceSubscriber(completableSubscriber, this.mapper);
        completableSubscriber.onSubscribe(sourceSubscriber);
        this.source.subscribe((SingleSubscriber<? super T>) sourceSubscriber);
    }
}
