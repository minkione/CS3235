package p009rx.internal.producers;

import p009rx.Producer;

/* renamed from: rx.internal.producers.ProducerArbiter */
public final class ProducerArbiter implements Producer {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long j) {
        }
    };
    Producer currentProducer;
    boolean emitting;
    long missedProduced;
    Producer missedProducer;
    long missedRequested;
    long requested;

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2 = r5.requested + r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0020, code lost:
        if (r2 >= 0) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0022, code lost:
        r2 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0027, code lost:
        r5.requested = r2;
        r0 = r5.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
        if (r0 == null) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
        r0.request(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0030, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0033, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0034, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0035, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r5.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003a, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r6) {
        /*
            r5 = this;
            r0 = 0
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x0041
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x000b
            return
        L_0x000b:
            monitor-enter(r5)
            boolean r2 = r5.emitting     // Catch:{ all -> 0x003e }
            if (r2 == 0) goto L_0x0017
            long r0 = r5.missedRequested     // Catch:{ all -> 0x003e }
            long r0 = r0 + r6
            r5.missedRequested = r0     // Catch:{ all -> 0x003e }
            monitor-exit(r5)     // Catch:{ all -> 0x003e }
            return
        L_0x0017:
            r2 = 1
            r5.emitting = r2     // Catch:{ all -> 0x003e }
            monitor-exit(r5)     // Catch:{ all -> 0x003e }
            long r2 = r5.requested     // Catch:{ all -> 0x0034 }
            long r2 = r2 + r6
            int r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r4 >= 0) goto L_0x0027
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x0027:
            r5.requested = r2     // Catch:{ all -> 0x0034 }
            rx.Producer r0 = r5.currentProducer     // Catch:{ all -> 0x0034 }
            if (r0 == 0) goto L_0x0030
            r0.request(r6)     // Catch:{ all -> 0x0034 }
        L_0x0030:
            r5.emitLoop()     // Catch:{ all -> 0x0034 }
            return
        L_0x0034:
            r6 = move-exception
            monitor-enter(r5)
            r7 = 0
            r5.emitting = r7     // Catch:{ all -> 0x003b }
            monitor-exit(r5)     // Catch:{ all -> 0x003b }
            throw r6
        L_0x003b:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003b }
            throw r6
        L_0x003e:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003e }
            throw r6
        L_0x0041:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "n >= 0 required"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.producers.ProducerArbiter.request(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2 = r7.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        if (r2 == Long.MAX_VALUE) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        r2 = r2 - r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
        if (r2 < 0) goto L_0x0029;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0026, code lost:
        r7.requested = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
        throw new java.lang.IllegalStateException("more items arrived than were requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0034, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0035, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0036, code lost:
        monitor-enter(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r7.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x003b, code lost:
        throw r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void produced(long r8) {
        /*
            r7 = this;
            r0 = 0
            int r2 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r2 <= 0) goto L_0x0042
            monitor-enter(r7)
            boolean r2 = r7.emitting     // Catch:{ all -> 0x003f }
            if (r2 == 0) goto L_0x0012
            long r0 = r7.missedProduced     // Catch:{ all -> 0x003f }
            long r0 = r0 + r8
            r7.missedProduced = r0     // Catch:{ all -> 0x003f }
            monitor-exit(r7)     // Catch:{ all -> 0x003f }
            return
        L_0x0012:
            r2 = 1
            r7.emitting = r2     // Catch:{ all -> 0x003f }
            monitor-exit(r7)     // Catch:{ all -> 0x003f }
            long r2 = r7.requested     // Catch:{ all -> 0x0035 }
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 == 0) goto L_0x0031
            long r2 = r2 - r8
            int r8 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r8 < 0) goto L_0x0029
            r7.requested = r2     // Catch:{ all -> 0x0035 }
            goto L_0x0031
        L_0x0029:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0035 }
            java.lang.String r9 = "more items arrived than were requested"
            r8.<init>(r9)     // Catch:{ all -> 0x0035 }
            throw r8     // Catch:{ all -> 0x0035 }
        L_0x0031:
            r7.emitLoop()     // Catch:{ all -> 0x0035 }
            return
        L_0x0035:
            r8 = move-exception
            monitor-enter(r7)
            r9 = 0
            r7.emitting = r9     // Catch:{ all -> 0x003c }
            monitor-exit(r7)     // Catch:{ all -> 0x003c }
            throw r8
        L_0x003c:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x003c }
            throw r8
        L_0x003f:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x003f }
            throw r8
        L_0x0042:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            java.lang.String r9 = "n > 0 required"
            r8.<init>(r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.producers.ProducerArbiter.produced(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2.currentProducer = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0013, code lost:
        if (r3 == null) goto L_0x001a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0015, code lost:
        r3.request(r2.requested);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001a, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001e, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x001f, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r2.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0024, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(p009rx.Producer r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.emitting     // Catch:{ all -> 0x0028 }
            if (r0 == 0) goto L_0x000d
            if (r3 != 0) goto L_0x0009
            rx.Producer r3 = NULL_PRODUCER     // Catch:{ all -> 0x0028 }
        L_0x0009:
            r2.missedProducer = r3     // Catch:{ all -> 0x0028 }
            monitor-exit(r2)     // Catch:{ all -> 0x0028 }
            return
        L_0x000d:
            r0 = 1
            r2.emitting = r0     // Catch:{ all -> 0x0028 }
            monitor-exit(r2)     // Catch:{ all -> 0x0028 }
            r2.currentProducer = r3     // Catch:{ all -> 0x001e }
            if (r3 == 0) goto L_0x001a
            long r0 = r2.requested     // Catch:{ all -> 0x001e }
            r3.request(r0)     // Catch:{ all -> 0x001e }
        L_0x001a:
            r2.emitLoop()     // Catch:{ all -> 0x001e }
            return
        L_0x001e:
            r3 = move-exception
            monitor-enter(r2)
            r0 = 0
            r2.emitting = r0     // Catch:{ all -> 0x0025 }
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            throw r3
        L_0x0025:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            throw r3
        L_0x0028:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0028 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.producers.ProducerArbiter.setProducer(rx.Producer):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        r8 = r13.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        if (r8 == Long.MAX_VALUE) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        r8 = r8 + r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r8 < 0) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r8 != Long.MAX_VALUE) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        r8 = r8 - r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
        if (r8 < 0) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003a, code lost:
        r13.requested = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0044, code lost:
        throw new java.lang.IllegalStateException("more produced than requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0045, code lost:
        r13.requested = Long.MAX_VALUE;
        r8 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        if (r4 == null) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r4 != NULL_PRODUCER) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
        r13.currentProducer = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0051, code lost:
        r13.currentProducer = r4;
        r4.request(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0057, code lost:
        r2 = r13.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0059, code lost:
        if (r2 == null) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005d, code lost:
        if (r0 == 0) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005f, code lost:
        r2.request(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitLoop() {
        /*
            r13 = this;
        L_0x0000:
            monitor-enter(r13)
            long r0 = r13.missedRequested     // Catch:{ all -> 0x0063 }
            long r2 = r13.missedProduced     // Catch:{ all -> 0x0063 }
            rx.Producer r4 = r13.missedProducer     // Catch:{ all -> 0x0063 }
            r5 = 0
            int r7 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x0018
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x0018
            if (r4 != 0) goto L_0x0018
            r0 = 0
            r13.emitting = r0     // Catch:{ all -> 0x0063 }
            monitor-exit(r13)     // Catch:{ all -> 0x0063 }
            return
        L_0x0018:
            r13.missedRequested = r5     // Catch:{ all -> 0x0063 }
            r13.missedProduced = r5     // Catch:{ all -> 0x0063 }
            r7 = 0
            r13.missedProducer = r7     // Catch:{ all -> 0x0063 }
            monitor-exit(r13)     // Catch:{ all -> 0x0063 }
            long r8 = r13.requested
            r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r12 == 0) goto L_0x0048
            long r8 = r8 + r0
            int r12 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0045
            int r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r12 != 0) goto L_0x0035
            goto L_0x0045
        L_0x0035:
            long r8 = r8 - r2
            int r2 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r2 < 0) goto L_0x003d
            r13.requested = r8
            goto L_0x0048
        L_0x003d:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "more produced than requested"
            r0.<init>(r1)
            throw r0
        L_0x0045:
            r13.requested = r10
            r8 = r10
        L_0x0048:
            if (r4 == 0) goto L_0x0057
            rx.Producer r0 = NULL_PRODUCER
            if (r4 != r0) goto L_0x0051
            r13.currentProducer = r7
            goto L_0x0000
        L_0x0051:
            r13.currentProducer = r4
            r4.request(r8)
            goto L_0x0000
        L_0x0057:
            rx.Producer r2 = r13.currentProducer
            if (r2 == 0) goto L_0x0000
            int r3 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x0000
            r2.request(r0)
            goto L_0x0000
        L_0x0063:
            r0 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x0063 }
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.internal.producers.ProducerArbiter.emitLoop():void");
    }
}
