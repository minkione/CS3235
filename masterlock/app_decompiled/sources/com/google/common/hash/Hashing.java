package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.Nullable;
import p008io.fabric.sdk.android.services.common.CommonUtils;

@Beta
public final class Hashing {
    private static final HashFunction ADLER_32 = checksumHashFunction(ChecksumType.ADLER_32, "Hashing.adler32()");
    private static final HashFunction CRC_32 = checksumHashFunction(ChecksumType.CRC_32, "Hashing.crc32()");
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = murmur3_128(GOOD_FAST_HASH_SEED);
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = murmur3_32(GOOD_FAST_HASH_SEED);
    private static final int GOOD_FAST_HASH_SEED = ((int) System.currentTimeMillis());
    private static final HashFunction MD5 = new MessageDigestHashFunction(CommonUtils.MD5_INSTANCE, "Hashing.md5()");
    private static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
    private static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
    private static final HashFunction SHA_1 = new MessageDigestHashFunction(CommonUtils.SHA1_INSTANCE, "Hashing.sha1()");
    private static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
    private static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
    private static final HashFunction SIP_HASH_24;

    enum ChecksumType implements Supplier<Checksum> {
        CRC_32(32) {
            public Checksum get() {
                return new CRC32();
            }
        },
        ADLER_32(32) {
            public Checksum get() {
                return new Adler32();
            }
        };
        
        /* access modifiers changed from: private */
        public final int bits;

        public abstract Checksum get();

        private ChecksumType(int i) {
            this.bits = i;
        }
    }

    @VisibleForTesting
    static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
        private final int bits;

        ConcatenatedHashFunction(HashFunction... hashFunctionArr) {
            super(hashFunctionArr);
            int i = 0;
            for (HashFunction bits2 : hashFunctionArr) {
                i += bits2.bits();
            }
            this.bits = i;
        }

        /* access modifiers changed from: 0000 */
        public HashCode makeHash(Hasher[] hasherArr) {
            byte[] bArr = new byte[(this.bits / 8)];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            for (Hasher hash : hasherArr) {
                wrap.put(hash.hash().asBytes());
            }
            return HashCode.fromBytesNoCopy(bArr);
        }

        public int bits() {
            return this.bits;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ConcatenatedHashFunction)) {
                return false;
            }
            ConcatenatedHashFunction concatenatedHashFunction = (ConcatenatedHashFunction) obj;
            if (this.bits != concatenatedHashFunction.bits || this.functions.length != concatenatedHashFunction.functions.length) {
                return false;
            }
            for (int i = 0; i < this.functions.length; i++) {
                if (!this.functions[i].equals(concatenatedHashFunction.functions[i])) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int i = this.bits;
            for (HashFunction hashCode : this.functions) {
                i ^= hashCode.hashCode();
            }
            return i;
        }
    }

    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long j) {
            this.state = j;
        }

        public double nextDouble() {
            this.state = (this.state * 2862933555777941757L) + 1;
            double d = (double) (((int) (this.state >>> 33)) + 1);
            Double.isNaN(d);
            return d / 2.147483648E9d;
        }
    }

    public static HashFunction goodFastHash(int i) {
        int checkPositiveAndMakeMultipleOf32 = checkPositiveAndMakeMultipleOf32(i);
        if (checkPositiveAndMakeMultipleOf32 == 32) {
            return GOOD_FAST_HASH_FUNCTION_32;
        }
        if (checkPositiveAndMakeMultipleOf32 <= 128) {
            return GOOD_FAST_HASH_FUNCTION_128;
        }
        int i2 = (checkPositiveAndMakeMultipleOf32 + 127) / 128;
        HashFunction[] hashFunctionArr = new HashFunction[i2];
        hashFunctionArr[0] = GOOD_FAST_HASH_FUNCTION_128;
        int i3 = GOOD_FAST_HASH_SEED;
        for (int i4 = 1; i4 < i2; i4++) {
            i3 += 1500450271;
            hashFunctionArr[i4] = murmur3_128(i3);
        }
        return new ConcatenatedHashFunction(hashFunctionArr);
    }

    static {
        SipHashFunction sipHashFunction = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
        SIP_HASH_24 = sipHashFunction;
    }

    public static HashFunction murmur3_32(int i) {
        return new Murmur3_32HashFunction(i);
    }

    public static HashFunction murmur3_32() {
        return MURMUR3_32;
    }

    public static HashFunction murmur3_128(int i) {
        return new Murmur3_128HashFunction(i);
    }

    public static HashFunction murmur3_128() {
        return MURMUR3_128;
    }

    public static HashFunction sipHash24() {
        return SIP_HASH_24;
    }

    public static HashFunction sipHash24(long j, long j2) {
        SipHashFunction sipHashFunction = new SipHashFunction(2, 4, j, j2);
        return sipHashFunction;
    }

    public static HashFunction md5() {
        return MD5;
    }

    public static HashFunction sha1() {
        return SHA_1;
    }

    public static HashFunction sha256() {
        return SHA_256;
    }

    public static HashFunction sha512() {
        return SHA_512;
    }

    public static HashFunction crc32() {
        return CRC_32;
    }

    public static HashFunction adler32() {
        return ADLER_32;
    }

    private static HashFunction checksumHashFunction(ChecksumType checksumType, String str) {
        return new ChecksumHashFunction(checksumType, checksumType.bits, str);
    }

    public static int consistentHash(HashCode hashCode, int i) {
        return consistentHash(hashCode.padToLong(), i);
    }

    public static int consistentHash(long j, int i) {
        int i2 = 0;
        Preconditions.checkArgument(i > 0, "buckets must be positive: %s", Integer.valueOf(i));
        LinearCongruentialGenerator linearCongruentialGenerator = new LinearCongruentialGenerator(j);
        while (true) {
            double d = (double) (i2 + 1);
            double nextDouble = linearCongruentialGenerator.nextDouble();
            Double.isNaN(d);
            int i3 = (int) (d / nextDouble);
            if (i3 < 0 || i3 >= i) {
                return i2;
            }
            i2 = i3;
        }
        return i2;
    }

    public static HashCode combineOrdered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) ((bArr[i] * 37) ^ asBytes2[i]);
            }
        }
        return HashCode.fromBytesNoCopy(bArr);
    }

    public static HashCode combineUnordered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) (bArr[i] + asBytes2[i]);
            }
        }
        return HashCode.fromBytesNoCopy(bArr);
    }

    static int checkPositiveAndMakeMultipleOf32(int i) {
        Preconditions.checkArgument(i > 0, "Number of bits must be positive");
        return (i + 31) & -32;
    }

    private Hashing() {
    }
}
