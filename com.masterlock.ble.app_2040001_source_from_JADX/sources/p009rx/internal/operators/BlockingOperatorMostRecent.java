package p009rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import p009rx.Observable;
import p009rx.Subscriber;
import p009rx.exceptions.Exceptions;

/* renamed from: rx.internal.operators.BlockingOperatorMostRecent */
public final class BlockingOperatorMostRecent {

    /* renamed from: rx.internal.operators.BlockingOperatorMostRecent$MostRecentObserver */
    static final class MostRecentObserver<T> extends Subscriber<T> {
        volatile Object value;

        MostRecentObserver(T t) {
            this.value = NotificationLite.next(t);
        }

        public void onCompleted() {
            this.value = NotificationLite.completed();
        }

        public void onError(Throwable th) {
            this.value = NotificationLite.error(th);
        }

        public void onNext(T t) {
            this.value = NotificationLite.next(t);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                private Object buf;

                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !NotificationLite.isCompleted(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentObserver.this.value;
                        }
                        if (NotificationLite.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (!NotificationLite.isError(this.buf)) {
                            T value = NotificationLite.getValue(this.buf);
                            this.buf = obj;
                            return value;
                        } else {
                            throw Exceptions.propagate(NotificationLite.getError(this.buf));
                        }
                    } finally {
                        this.buf = obj;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }

    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> observable, final T t) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                MostRecentObserver mostRecentObserver = new MostRecentObserver(t);
                observable.subscribe((Subscriber<? super T>) mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }
}
