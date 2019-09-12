package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements Strategy {
    MURMUR128_MITZ_32 {
        public <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long asLong = Hashing.murmur3_128().hashObject(t, funnel).asLong();
            int i2 = (int) asLong;
            int i3 = (int) (asLong >>> 32);
            boolean z = false;
            for (int i4 = 1; i4 <= i; i4++) {
                int i5 = (i4 * i3) + i2;
                if (i5 < 0) {
                    i5 ^= -1;
                }
                z |= bitArray.set(i5 % bitArray.bitSize());
            }
            return z;
        }

        public <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long asLong = Hashing.murmur3_128().hashObject(t, funnel).asLong();
            int i2 = (int) asLong;
            int i3 = (int) (asLong >>> 32);
            for (int i4 = 1; i4 <= i; i4++) {
                int i5 = (i4 * i3) + i2;
                if (i5 < 0) {
                    i5 ^= -1;
                }
                if (!bitArray.get(i5 % bitArray.bitSize())) {
                    return false;
                }
            }
            return true;
        }
    };

    static class BitArray {
        int bitCount;
        final long[] data;

        BitArray(long j) {
            this(new long[Ints.checkedCast(LongMath.divide(j, 64, RoundingMode.CEILING))]);
        }

        BitArray(long[] jArr) {
            Preconditions.checkArgument(jArr.length > 0, "data length is zero!");
            this.data = jArr;
            int i = 0;
            for (long bitCount2 : jArr) {
                i += Long.bitCount(bitCount2);
            }
            this.bitCount = i;
        }

        /* access modifiers changed from: 0000 */
        public boolean set(int i) {
            if (get(i)) {
                return false;
            }
            long[] jArr = this.data;
            int i2 = i >> 6;
            jArr[i2] = jArr[i2] | (1 << i);
            this.bitCount++;
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean get(int i) {
            return (this.data[i >> 6] & (1 << i)) != 0;
        }

        /* access modifiers changed from: 0000 */
        public int bitSize() {
            return this.data.length * 64;
        }

        /* access modifiers changed from: 0000 */
        public int bitCount() {
            return this.bitCount;
        }

        /* access modifiers changed from: 0000 */
        public BitArray copy() {
            return new BitArray((long[]) this.data.clone());
        }

        /* access modifiers changed from: 0000 */
        public void putAll(BitArray bitArray) {
            int i = 0;
            Preconditions.checkArgument(this.data.length == bitArray.data.length, "BitArrays must be of equal length (%s != %s)", Integer.valueOf(this.data.length), Integer.valueOf(bitArray.data.length));
            this.bitCount = 0;
            while (true) {
                long[] jArr = this.data;
                if (i < jArr.length) {
                    jArr[i] = jArr[i] | bitArray.data[i];
                    this.bitCount += Long.bitCount(jArr[i]);
                    i++;
                } else {
                    return;
                }
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof BitArray)) {
                return false;
            }
            return Arrays.equals(this.data, ((BitArray) obj).data);
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
}
