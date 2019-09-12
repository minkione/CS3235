package p009rx.schedulers;

/* renamed from: rx.schedulers.Timestamped */
public final class Timestamped<T> {
    private final long timestampMillis;
    private final T value;

    public Timestamped(long j, T t) {
        this.value = t;
        this.timestampMillis = j;
    }

    public long getTimestampMillis() {
        return this.timestampMillis;
    }

    public T getValue() {
        return this.value;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        if (r2.equals(r8) == false) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r8) {
        /*
            r7 = this;
            r0 = 1
            if (r7 != r8) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r8 != 0) goto L_0x0008
            return r1
        L_0x0008:
            boolean r2 = r8 instanceof p009rx.schedulers.Timestamped
            if (r2 != 0) goto L_0x000d
            return r1
        L_0x000d:
            rx.schedulers.Timestamped r8 = (p009rx.schedulers.Timestamped) r8
            long r2 = r7.timestampMillis
            long r4 = r8.timestampMillis
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0026
            T r2 = r7.value
            T r8 = r8.value
            if (r2 == r8) goto L_0x0027
            if (r2 == 0) goto L_0x0026
            boolean r8 = r2.equals(r8)
            if (r8 == 0) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r0 = 0
        L_0x0027:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.schedulers.Timestamped.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        long j = this.timestampMillis;
        int i = (((int) (j ^ (j >>> 32))) + 31) * 31;
        T t = this.value;
        return i + (t == null ? 0 : t.hashCode());
    }

    public String toString() {
        return String.format("Timestamped(timestampMillis = %d, value = %s)", new Object[]{Long.valueOf(this.timestampMillis), this.value.toString()});
    }
}
