package p009rx.internal.util.unsafe;

import p009rx.internal.util.SuppressAnimalSniffer;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.SpmcArrayQueueProducerIndexCacheField */
/* compiled from: SpmcArrayQueue */
abstract class SpmcArrayQueueProducerIndexCacheField<E> extends SpmcArrayQueueMidPad<E> {
    private volatile long producerIndexCache;

    public SpmcArrayQueueProducerIndexCacheField(int i) {
        super(i);
    }

    /* access modifiers changed from: protected */
    public final long lvProducerIndexCache() {
        return this.producerIndexCache;
    }

    /* access modifiers changed from: protected */
    public final void svProducerIndexCache(long j) {
        this.producerIndexCache = j;
    }
}
