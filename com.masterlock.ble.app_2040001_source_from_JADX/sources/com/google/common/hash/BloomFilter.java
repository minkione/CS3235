package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
    /* access modifiers changed from: private */
    public final BitArray bits;
    /* access modifiers changed from: private */
    public final Funnel<T> funnel;
    /* access modifiers changed from: private */
    public final int numHashFunctions;
    /* access modifiers changed from: private */
    public final Strategy strategy;

    private static class SerialForm<T> implements Serializable {
        private static final long serialVersionUID = 1;
        final long[] data;
        final Funnel<T> funnel;
        final int numHashFunctions;
        final Strategy strategy;

        SerialForm(BloomFilter<T> bloomFilter) {
            this.data = bloomFilter.bits.data;
            this.numHashFunctions = bloomFilter.numHashFunctions;
            this.funnel = bloomFilter.funnel;
            this.strategy = bloomFilter.strategy;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            BloomFilter bloomFilter = new BloomFilter(new BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
            return bloomFilter;
        }
    }

    interface Strategy extends Serializable {
        <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray);

        int ordinal();

        <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray);
    }

    private BloomFilter(BitArray bitArray, int i, Funnel<T> funnel2, Strategy strategy2) {
        Preconditions.checkArgument(i > 0, "numHashFunctions (%s) must be > 0", Integer.valueOf(i));
        Preconditions.checkArgument(i <= 255, "numHashFunctions (%s) must be <= 255", Integer.valueOf(i));
        this.bits = (BitArray) Preconditions.checkNotNull(bitArray);
        this.numHashFunctions = i;
        this.funnel = (Funnel) Preconditions.checkNotNull(funnel2);
        this.strategy = (Strategy) Preconditions.checkNotNull(strategy2);
    }

    public BloomFilter<T> copy() {
        return new BloomFilter<>(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }

    public boolean mightContain(T t) {
        return this.strategy.mightContain(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public boolean apply(T t) {
        return mightContain(t);
    }

    public boolean put(T t) {
        return this.strategy.put(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public double expectedFpp() {
        double bitCount = (double) this.bits.bitCount();
        double bitSize = (double) bitSize();
        Double.isNaN(bitCount);
        Double.isNaN(bitSize);
        return Math.pow(bitCount / bitSize, (double) this.numHashFunctions);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public long bitSize() {
        return (long) this.bits.bitSize();
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=com.google.common.hash.BloomFilter<T>, code=com.google.common.hash.BloomFilter, for r6v0, types: [com.google.common.hash.BloomFilter<T>, com.google.common.hash.BloomFilter, java.lang.Object] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isCompatible(com.google.common.hash.BloomFilter r6) {
        /*
            r5 = this;
            com.google.common.base.Preconditions.checkNotNull(r6)
            if (r5 == r6) goto L_0x002d
            int r0 = r5.numHashFunctions
            int r1 = r6.numHashFunctions
            if (r0 != r1) goto L_0x002d
            long r0 = r5.bitSize()
            long r2 = r6.bitSize()
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x002d
            com.google.common.hash.BloomFilter$Strategy r0 = r5.strategy
            com.google.common.hash.BloomFilter$Strategy r1 = r6.strategy
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002d
            com.google.common.hash.Funnel<T> r0 = r5.funnel
            com.google.common.hash.Funnel<T> r6 = r6.funnel
            boolean r6 = r0.equals(r6)
            if (r6 == 0) goto L_0x002d
            r6 = 1
            goto L_0x002e
        L_0x002d:
            r6 = 0
        L_0x002e:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.BloomFilter.isCompatible(com.google.common.hash.BloomFilter):boolean");
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=com.google.common.hash.BloomFilter<T>, code=com.google.common.hash.BloomFilter, for r9v0, types: [com.google.common.hash.BloomFilter<T>, com.google.common.hash.BloomFilter, java.lang.Object] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void putAll(com.google.common.hash.BloomFilter r9) {
        /*
            r8 = this;
            com.google.common.base.Preconditions.checkNotNull(r9)
            r0 = 1
            r1 = 0
            if (r8 == r9) goto L_0x0009
            r2 = 1
            goto L_0x000a
        L_0x0009:
            r2 = 0
        L_0x000a:
            java.lang.String r3 = "Cannot combine a BloomFilter with itself."
            com.google.common.base.Preconditions.checkArgument(r2, r3)
            int r2 = r8.numHashFunctions
            int r3 = r9.numHashFunctions
            if (r2 != r3) goto L_0x0017
            r2 = 1
            goto L_0x0018
        L_0x0017:
            r2 = 0
        L_0x0018:
            java.lang.String r3 = "BloomFilters must have the same number of hash functions (%s != %s)"
            r4 = 2
            java.lang.Object[] r5 = new java.lang.Object[r4]
            int r6 = r8.numHashFunctions
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r5[r1] = r6
            int r6 = r9.numHashFunctions
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r5[r0] = r6
            com.google.common.base.Preconditions.checkArgument(r2, r3, r5)
            long r2 = r8.bitSize()
            long r5 = r9.bitSize()
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x003e
            r2 = 1
            goto L_0x003f
        L_0x003e:
            r2 = 0
        L_0x003f:
            java.lang.String r3 = "BloomFilters must have the same size underlying bit arrays (%s != %s)"
            java.lang.Object[] r5 = new java.lang.Object[r4]
            long r6 = r8.bitSize()
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r5[r1] = r6
            long r6 = r9.bitSize()
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r5[r0] = r6
            com.google.common.base.Preconditions.checkArgument(r2, r3, r5)
            com.google.common.hash.BloomFilter$Strategy r2 = r8.strategy
            com.google.common.hash.BloomFilter$Strategy r3 = r9.strategy
            boolean r2 = r2.equals(r3)
            java.lang.String r3 = "BloomFilters must have equal strategies (%s != %s)"
            java.lang.Object[] r5 = new java.lang.Object[r4]
            com.google.common.hash.BloomFilter$Strategy r6 = r8.strategy
            r5[r1] = r6
            com.google.common.hash.BloomFilter$Strategy r6 = r9.strategy
            r5[r0] = r6
            com.google.common.base.Preconditions.checkArgument(r2, r3, r5)
            com.google.common.hash.Funnel<T> r2 = r8.funnel
            com.google.common.hash.Funnel<T> r3 = r9.funnel
            boolean r2 = r2.equals(r3)
            java.lang.String r3 = "BloomFilters must have equal funnels (%s != %s)"
            java.lang.Object[] r4 = new java.lang.Object[r4]
            com.google.common.hash.Funnel<T> r5 = r8.funnel
            r4[r1] = r5
            com.google.common.hash.Funnel<T> r1 = r9.funnel
            r4[r0] = r1
            com.google.common.base.Preconditions.checkArgument(r2, r3, r4)
            com.google.common.hash.BloomFilterStrategies$BitArray r0 = r8.bits
            com.google.common.hash.BloomFilterStrategies$BitArray r9 = r9.bits
            r0.putAll(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.BloomFilter.putAll(com.google.common.hash.BloomFilter):void");
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BloomFilter)) {
            return false;
        }
        BloomFilter bloomFilter = (BloomFilter) obj;
        if (this.numHashFunctions != bloomFilter.numHashFunctions || !this.funnel.equals(bloomFilter.funnel) || !this.bits.equals(bloomFilter.bits) || !this.strategy.equals(bloomFilter.strategy)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits);
    }

    public static <T> BloomFilter<T> create(Funnel<T> funnel2, int i, double d) {
        Preconditions.checkNotNull(funnel2);
        Preconditions.checkArgument(i >= 0, "Expected insertions (%s) must be >= 0", Integer.valueOf(i));
        Preconditions.checkArgument(d > 0.0d, "False positive probability (%s) must be > 0.0", Double.valueOf(d));
        Preconditions.checkArgument(d < 1.0d, "False positive probability (%s) must be < 1.0", Double.valueOf(d));
        if (i == 0) {
            i = 1;
        }
        long j = (long) i;
        long optimalNumOfBits = optimalNumOfBits(j, d);
        try {
            return new BloomFilter<>(new BitArray(optimalNumOfBits), optimalNumOfHashFunctions(j, optimalNumOfBits), funnel2, BloomFilterStrategies.MURMUR128_MITZ_32);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not create BloomFilter of ");
            sb.append(optimalNumOfBits);
            sb.append(" bits");
            throw new IllegalArgumentException(sb.toString(), e);
        }
    }

    public static <T> BloomFilter<T> create(Funnel<T> funnel2, int i) {
        return create(funnel2, i, 0.03d);
    }

    @VisibleForTesting
    static int optimalNumOfHashFunctions(long j, long j2) {
        double d = (double) (j2 / j);
        double log = Math.log(2.0d);
        Double.isNaN(d);
        return Math.max(1, (int) Math.round(d * log));
    }

    @VisibleForTesting
    static long optimalNumOfBits(long j, double d) {
        if (d == 0.0d) {
            d = Double.MIN_VALUE;
        }
        double d2 = (double) (-j);
        double log = Math.log(d);
        Double.isNaN(d2);
        return (long) ((d2 * log) / (Math.log(2.0d) * Math.log(2.0d)));
    }

    private Object writeReplace() {
        return new SerialForm(this);
    }
}
