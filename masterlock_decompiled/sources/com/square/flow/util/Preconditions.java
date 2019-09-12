package com.square.flow.util;

public final class Preconditions {
    private Preconditions() {
    }

    public static <T> T checkNotNull(T t, String str, Object... objArr) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.format(str, objArr));
    }

    public static void checkArgument(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalArgumentException(String.format(str, objArr));
        }
    }

    public static String checkNotNullOrBlank(String str, String str2, Object... objArr) {
        checkNotNull(str, str2, objArr);
        String trim = str.trim();
        if (!trim.isEmpty()) {
            return trim;
        }
        throw new IllegalArgumentException(String.format(str2, objArr));
    }

    public static void checkState(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalStateException(String.format(str, objArr));
        }
    }
}
