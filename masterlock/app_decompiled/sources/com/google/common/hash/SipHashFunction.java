package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

final class SipHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c */
    private final int f129c;

    /* renamed from: d */
    private final int f130d;

    /* renamed from: k0 */
    private final long f131k0;

    /* renamed from: k1 */
    private final long f132k1;

    private static final class SipHasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;

        /* renamed from: b */
        private long f133b = 0;

        /* renamed from: c */
        private final int f134c;

        /* renamed from: d */
        private final int f135d;
        private long finalM = 0;

        /* renamed from: v0 */
        private long f136v0 = 8317987319222330741L;

        /* renamed from: v1 */
        private long f137v1 = 7237128888997146477L;

        /* renamed from: v2 */
        private long f138v2 = 7816392313619706465L;

        /* renamed from: v3 */
        private long f139v3 = 8387220255154660723L;

        SipHasher(int i, int i2, long j, long j2) {
            super(8);
            this.f134c = i;
            this.f135d = i2;
            this.f136v0 ^= j;
            this.f137v1 ^= j2;
            this.f138v2 ^= j;
            this.f139v3 ^= j2;
        }

        /* access modifiers changed from: protected */
        public void process(ByteBuffer byteBuffer) {
            this.f133b += 8;
            processM(byteBuffer.getLong());
        }

        /* access modifiers changed from: protected */
        public void processRemaining(ByteBuffer byteBuffer) {
            this.f133b += (long) byteBuffer.remaining();
            int i = 0;
            while (byteBuffer.hasRemaining()) {
                this.finalM ^= (((long) byteBuffer.get()) & 255) << i;
                i += 8;
            }
        }

        public HashCode makeHash() {
            this.finalM ^= this.f133b << 56;
            processM(this.finalM);
            this.f138v2 ^= 255;
            sipRound(this.f135d);
            return HashCode.fromLong(((this.f136v0 ^ this.f137v1) ^ this.f138v2) ^ this.f139v3);
        }

        private void processM(long j) {
            this.f139v3 ^= j;
            sipRound(this.f134c);
            this.f136v0 = j ^ this.f136v0;
        }

        private void sipRound(int i) {
            for (int i2 = 0; i2 < i; i2++) {
                long j = this.f136v0;
                long j2 = this.f137v1;
                this.f136v0 = j + j2;
                this.f138v2 += this.f139v3;
                this.f137v1 = Long.rotateLeft(j2, 13);
                this.f139v3 = Long.rotateLeft(this.f139v3, 16);
                long j3 = this.f137v1;
                long j4 = this.f136v0;
                this.f137v1 = j3 ^ j4;
                this.f139v3 ^= this.f138v2;
                this.f136v0 = Long.rotateLeft(j4, 32);
                long j5 = this.f138v2;
                long j6 = this.f137v1;
                this.f138v2 = j5 + j6;
                this.f136v0 += this.f139v3;
                this.f137v1 = Long.rotateLeft(j6, 17);
                this.f139v3 = Long.rotateLeft(this.f139v3, 21);
                long j7 = this.f137v1;
                long j8 = this.f138v2;
                this.f137v1 = j7 ^ j8;
                this.f139v3 ^= this.f136v0;
                this.f138v2 = Long.rotateLeft(j8, 32);
            }
        }
    }

    public int bits() {
        return 64;
    }

    SipHashFunction(int i, int i2, long j, long j2) {
        Preconditions.checkArgument(i > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", Integer.valueOf(i));
        Preconditions.checkArgument(i2 > 0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", Integer.valueOf(i2));
        this.f129c = i;
        this.f130d = i2;
        this.f131k0 = j;
        this.f132k1 = j2;
    }

    public Hasher newHasher() {
        SipHasher sipHasher = new SipHasher(this.f129c, this.f130d, this.f131k0, this.f132k1);
        return sipHasher;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hashing.sipHash");
        sb.append(this.f129c);
        sb.append("");
        sb.append(this.f130d);
        sb.append("(");
        sb.append(this.f131k0);
        sb.append(", ");
        sb.append(this.f132k1);
        sb.append(")");
        return sb.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof SipHashFunction)) {
            return false;
        }
        SipHashFunction sipHashFunction = (SipHashFunction) obj;
        if (this.f129c == sipHashFunction.f129c && this.f130d == sipHashFunction.f130d && this.f131k0 == sipHashFunction.f131k0 && this.f132k1 == sipHashFunction.f132k1) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (int) ((((long) ((getClass().hashCode() ^ this.f129c) ^ this.f130d)) ^ this.f131k0) ^ this.f132k1);
    }
}
