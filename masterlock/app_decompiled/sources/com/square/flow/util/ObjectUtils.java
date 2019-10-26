package com.square.flow.util;

public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> Class<T> getClass(Object obj) {
        return obj.getClass();
    }

    public static String getHumanClassName(Object obj) {
        Class cls = getClass(obj);
        String simpleName = cls.getSimpleName();
        if (!cls.isMemberClass()) {
            return simpleName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getDeclaringClass().getSimpleName());
        sb.append(".");
        sb.append(simpleName);
        return sb.toString();
    }
}
