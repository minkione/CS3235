package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
final class Hashing {

    /* renamed from: C1 */
    private static final int f119C1 = -862048943;

    /* renamed from: C2 */
    private static final int f120C2 = 461845907;
    private static int MAX_TABLE_SIZE = 1073741824;

    private Hashing() {
    }

    static int smear(int i) {
        return Integer.rotateLeft(i * f119C1, 15) * f120C2;
    }

    static int smearedHash(@Nullable Object obj) {
        return smear(obj == null ? 0 : obj.hashCode());
    }

    static int closedTableSize(int i, double d) {
        int max = Math.max(i, 2);
        int highestOneBit = Integer.highestOneBit(max);
        double d2 = (double) highestOneBit;
        Double.isNaN(d2);
        if (max <= ((int) (d * d2))) {
            return highestOneBit;
        }
        int i2 = highestOneBit << 1;
        if (i2 <= 0) {
            i2 = MAX_TABLE_SIZE;
        }
        return i2;
    }

    static boolean needsResizing(int i, int i2, double d) {
        double d2 = (double) i;
        double d3 = (double) i2;
        Double.isNaN(d3);
        return d2 > d * d3 && i2 < MAX_TABLE_SIZE;
    }
}
