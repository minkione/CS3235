package com.google.android.gms.common;

import java.util.concurrent.Callable;

final class zzo extends zzm {
    private final Callable<String> zzae;

    private zzo(Callable<String> callable) {
        super(false, null, null);
        this.zzae = callable;
    }

    /* access modifiers changed from: 0000 */
    public final String getErrorMessage() {
        try {
            return (String) this.zzae.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
