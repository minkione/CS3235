package p009rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import p009rx.internal.util.UtilityFunctions;

/* renamed from: rx.internal.operators.BackpressureUtils */
public final class BackpressureUtils {
    static final long COMPLETED_MASK = Long.MIN_VALUE;
    static final long REQUESTED_MASK = Long.MAX_VALUE;

    public static long addCap(long j, long j2) {
        long j3 = j + j2;
        return j3 < 0 ? REQUESTED_MASK : j3;
    }

    private BackpressureUtils() {
        throw new IllegalStateException("No instances!");
    }

    public static long getAndAddRequest(AtomicLong atomicLong, long j) {
        long j2;
        do {
            j2 = atomicLong.get();
        } while (!atomicLong.compareAndSet(j2, addCap(j2, j)));
        return j2;
    }

    public static long multiplyCap(long j, long j2) {
        long j3 = j * j2;
        return (((j | j2) >>> 31) == 0 || j2 == 0 || j3 / j2 == j) ? j3 : REQUESTED_MASK;
    }

    public static <T> void postCompleteDone(AtomicLong atomicLong, Queue<T> queue, Subscriber<? super T> subscriber) {
        postCompleteDone(atomicLong, queue, subscriber, UtilityFunctions.identity());
    }

    public static <T> boolean postCompleteRequest(AtomicLong atomicLong, long j, Queue<T> queue, Subscriber<? super T> subscriber) {
        return postCompleteRequest(atomicLong, j, queue, subscriber, UtilityFunctions.identity());
    }

    public static <T, R> void postCompleteDone(AtomicLong atomicLong, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1) {
        long j;
        do {
            j = atomicLong.get();
            if ((j & COMPLETED_MASK) != 0) {
                return;
            }
        } while (!atomicLong.compareAndSet(j, COMPLETED_MASK | j));
        if (j != 0) {
            postCompleteDrain(atomicLong, queue, subscriber, func1);
        }
    }

    public static <T, R> boolean postCompleteRequest(AtomicLong atomicLong, long j, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1) {
        long j2;
        long j3;
        AtomicLong atomicLong2 = atomicLong;
        long j4 = j;
        if (j4 >= 0) {
            boolean z = true;
            if (j4 == 0) {
                if ((atomicLong.get() & COMPLETED_MASK) != 0) {
                    z = false;
                }
                return z;
            }
            while (true) {
                j2 = atomicLong.get();
                j3 = j2 & COMPLETED_MASK;
                if (atomicLong.compareAndSet(j2, addCap(REQUESTED_MASK & j2, j4) | j3)) {
                    break;
                }
                Queue<T> queue2 = queue;
                Subscriber<? super R> subscriber2 = subscriber;
                Func1<? super T, ? extends R> func12 = func1;
            }
            if (j2 == COMPLETED_MASK) {
                postCompleteDrain(atomicLong, queue, subscriber, func1);
                return false;
            }
            if (j3 != 0) {
                z = false;
            }
            return z;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("n >= 0 required but it was ");
        sb.append(j4);
        throw new IllegalArgumentException(sb.toString());
    }

    static <T, R> void postCompleteDrain(AtomicLong atomicLong, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1) {
        long j = atomicLong.get();
        if (j == REQUESTED_MASK) {
            while (!subscriber.isUnsubscribed()) {
                Object poll = queue.poll();
                if (poll == null) {
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onNext(func1.call(poll));
            }
            return;
        }
        long j2 = j;
        long j3 = Long.MIN_VALUE;
        while (true) {
            if (j3 == j2) {
                if (j3 == j2) {
                    if (!subscriber.isUnsubscribed()) {
                        if (queue.isEmpty()) {
                            subscriber.onCompleted();
                            return;
                        }
                    } else {
                        return;
                    }
                }
                j2 = atomicLong.get();
                if (j2 == j3) {
                    j2 = atomicLong.addAndGet(-(j3 & REQUESTED_MASK));
                    if (j2 != COMPLETED_MASK) {
                        j3 = Long.MIN_VALUE;
                    } else {
                        return;
                    }
                } else {
                    continue;
                }
            } else if (!subscriber.isUnsubscribed()) {
                Object poll2 = queue.poll();
                if (poll2 == null) {
                    subscriber.onCompleted();
                    return;
                } else {
                    subscriber.onNext(func1.call(poll2));
                    j3++;
                }
            } else {
                return;
            }
        }
    }

    public static long produced(AtomicLong atomicLong, long j) {
        long j2;
        long j3;
        do {
            j2 = atomicLong.get();
            if (j2 == REQUESTED_MASK) {
                return REQUESTED_MASK;
            }
            j3 = j2 - j;
            if (j3 < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("More produced than requested: ");
                sb.append(j3);
                throw new IllegalStateException(sb.toString());
            }
        } while (!atomicLong.compareAndSet(j2, j3));
        return j3;
    }

    public static boolean validate(long j) {
        if (j >= 0) {
            return j != 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("n >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }
}
