package dagger.internal;

import butterknife.internal.ButterKnifeProcessor;
import dagger.Lazy;
import dagger.MembersInjector;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Provider;
import javax.inject.Qualifier;

public final class Keys {
    private static final Memoizer<Class<? extends Annotation>, Boolean> IS_QUALIFIER_ANNOTATION = new Memoizer<Class<? extends Annotation>, Boolean>() {
        /* access modifiers changed from: protected */
        public Boolean create(Class<? extends Annotation> cls) {
            return Boolean.valueOf(cls.isAnnotationPresent(Qualifier.class));
        }
    };
    private static final String LAZY_PREFIX;
    private static final String MEMBERS_INJECTOR_PREFIX;
    private static final String PROVIDER_PREFIX;
    private static final String SET_PREFIX;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Provider.class.getCanonicalName());
        sb.append("<");
        PROVIDER_PREFIX = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(MembersInjector.class.getCanonicalName());
        sb2.append("<");
        MEMBERS_INJECTOR_PREFIX = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(Lazy.class.getCanonicalName());
        sb3.append("<");
        LAZY_PREFIX = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(Set.class.getCanonicalName());
        sb4.append("<");
        SET_PREFIX = sb4.toString();
    }

    Keys() {
    }

    public static String get(Type type) {
        return get(type, null);
    }

    public static String getMembersKey(Class<?> cls) {
        return "members/".concat(cls.getName());
    }

    private static String get(Type type, Annotation annotation) {
        Type boxIfPrimitive = boxIfPrimitive(type);
        if (annotation == null && (boxIfPrimitive instanceof Class)) {
            Class cls = (Class) boxIfPrimitive;
            if (!cls.isArray()) {
                return cls.getName();
            }
        }
        StringBuilder sb = new StringBuilder();
        if (annotation != null) {
            sb.append(annotation);
            sb.append("/");
        }
        typeToString(boxIfPrimitive, sb, true);
        return sb.toString();
    }

    public static String getSetKey(Type type, Annotation[] annotationArr, Object obj) {
        Annotation extractQualifier = extractQualifier(annotationArr, obj);
        Type boxIfPrimitive = boxIfPrimitive(type);
        StringBuilder sb = new StringBuilder();
        if (extractQualifier != null) {
            sb.append(extractQualifier);
            sb.append("/");
        }
        sb.append(SET_PREFIX);
        typeToString(boxIfPrimitive, sb, true);
        sb.append(">");
        return sb.toString();
    }

    public static String get(Type type, Annotation[] annotationArr, Object obj) {
        return get(type, extractQualifier(annotationArr, obj));
    }

    private static Annotation extractQualifier(Annotation[] annotationArr, Object obj) {
        Annotation annotation = null;
        for (Annotation annotation2 : annotationArr) {
            if (((Boolean) IS_QUALIFIER_ANNOTATION.get(annotation2.annotationType())).booleanValue()) {
                if (annotation == null) {
                    annotation = annotation2;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Too many qualifier annotations on ");
                    sb.append(obj);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
        }
        return annotation;
    }

    private static void typeToString(Type type, StringBuilder sb, boolean z) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                typeToString(cls.getComponentType(), sb, false);
                sb.append("[]");
            } else if (!cls.isPrimitive()) {
                sb.append(cls.getName());
            } else if (!z) {
                sb.append(cls.getName());
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Uninjectable type ");
                sb2.append(cls.getName());
                throw new UnsupportedOperationException(sb2.toString());
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            typeToString(parameterizedType.getRawType(), sb, true);
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            sb.append("<");
            for (int i = 0; i < actualTypeArguments.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                typeToString(actualTypeArguments[i], sb, true);
            }
            sb.append(">");
        } else if (type instanceof GenericArrayType) {
            typeToString(((GenericArrayType) type).getGenericComponentType(), sb, false);
            sb.append("[]");
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Uninjectable type ");
            sb3.append(type);
            throw new UnsupportedOperationException(sb3.toString());
        }
    }

    static String getBuiltInBindingsKey(String str) {
        int startOfType = startOfType(str);
        if (substringStartsWith(str, startOfType, PROVIDER_PREFIX)) {
            return extractKey(str, startOfType, str.substring(0, startOfType), PROVIDER_PREFIX);
        }
        if (substringStartsWith(str, startOfType, MEMBERS_INJECTOR_PREFIX)) {
            return extractKey(str, startOfType, "members/", MEMBERS_INJECTOR_PREFIX);
        }
        return null;
    }

    static String getLazyKey(String str) {
        int startOfType = startOfType(str);
        if (substringStartsWith(str, startOfType, LAZY_PREFIX)) {
            return extractKey(str, startOfType, str.substring(0, startOfType), LAZY_PREFIX);
        }
        return null;
    }

    private static int startOfType(String str) {
        if (str.startsWith("@")) {
            return str.lastIndexOf(47) + 1;
        }
        return 0;
    }

    private static String extractKey(String str, int i, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(str.substring(i + str3.length(), str.length() - 1));
        return sb.toString();
    }

    private static boolean substringStartsWith(String str, int i, String str2) {
        return str.regionMatches(i, str2, 0, str2.length());
    }

    public static boolean isAnnotated(String str) {
        return str.startsWith("@");
    }

    public static String getClassName(String str) {
        int i;
        if (str.startsWith("@") || str.startsWith("members/")) {
            i = str.lastIndexOf(47) + 1;
        } else {
            i = 0;
        }
        if (str.indexOf(60, i) == -1 && str.indexOf(91, i) == -1) {
            return str.substring(i);
        }
        return null;
    }

    public static boolean isPlatformType(String str) {
        return str.startsWith(ButterKnifeProcessor.JAVA_PREFIX) || str.startsWith("javax.") || str.startsWith(ButterKnifeProcessor.ANDROID_PREFIX);
    }

    private static Type boxIfPrimitive(Type type) {
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Character.TYPE) {
            return Character.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Double.TYPE) {
            return Double.class;
        }
        return type == Void.TYPE ? Void.class : type;
    }
}
