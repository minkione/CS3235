package com.google.android.gms.dynamite;

import butterknife.internal.ButterKnifeProcessor;
import dalvik.system.PathClassLoader;

final class zzh extends PathClassLoader {
    zzh(String str, ClassLoader classLoader) {
        super(str, classLoader);
    }

    /* access modifiers changed from: protected */
    public final Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        if (!str.startsWith(ButterKnifeProcessor.JAVA_PREFIX) && !str.startsWith(ButterKnifeProcessor.ANDROID_PREFIX)) {
            try {
                return findClass(str);
            } catch (ClassNotFoundException unused) {
            }
        }
        return super.loadClass(str, z);
    }
}
