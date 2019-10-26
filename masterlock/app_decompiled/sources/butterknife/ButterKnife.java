package butterknife;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.util.Property;
import android.view.View;
import butterknife.internal.ButterKnifeProcessor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ButterKnife {
    static final Map<Class<?>, Method> INJECTORS = new LinkedHashMap();
    static final Method NO_OP = null;
    static final Map<Class<?>, Method> RESETTERS = new LinkedHashMap();
    private static final String TAG = "ButterKnife";
    private static boolean debug = false;

    public interface Action<T extends View> {
        void apply(T t, int i);
    }

    public enum Finder {
        VIEW {
            public View findOptionalView(Object obj, int i) {
                return ((View) obj).findViewById(i);
            }
        },
        ACTIVITY {
            public View findOptionalView(Object obj, int i) {
                return ((Activity) obj).findViewById(i);
            }
        },
        DIALOG {
            public View findOptionalView(Object obj, int i) {
                return ((Dialog) obj).findViewById(i);
            }
        };

        public static <T extends View> T[] arrayOf(T... tArr) {
            return tArr;
        }

        public abstract View findOptionalView(Object obj, int i);

        public static <T extends View> List<T> listOf(T... tArr) {
            return new ImmutableViewList(tArr);
        }

        public View findRequiredView(Object obj, int i, String str) {
            View findOptionalView = findOptionalView(obj, i);
            if (findOptionalView != null) {
                return findOptionalView;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Required view with id '");
            sb.append(i);
            sb.append("' for ");
            sb.append(str);
            sb.append(" was not found. If this view is optional add '@Optional' annotation.");
            throw new IllegalStateException(sb.toString());
        }
    }

    public interface Setter<T extends View, V> {
        void set(T t, V v, int i);
    }

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public static void setDebug(boolean z) {
        debug = z;
    }

    public static void inject(Activity activity) {
        inject(activity, activity, Finder.ACTIVITY);
    }

    public static void inject(View view) {
        inject(view, view, Finder.VIEW);
    }

    public static void inject(Dialog dialog) {
        inject(dialog, dialog, Finder.DIALOG);
    }

    public static void inject(Object obj, Activity activity) {
        inject(obj, activity, Finder.ACTIVITY);
    }

    public static void inject(Object obj, View view) {
        inject(obj, view, Finder.VIEW);
    }

    public static void inject(Object obj, Dialog dialog) {
        inject(obj, dialog, Finder.DIALOG);
    }

    public static void reset(Object obj) {
        Class cls = obj.getClass();
        try {
            if (debug) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Looking up view injector for ");
                sb.append(cls.getName());
                Log.d(str, sb.toString());
            }
            Method findResettersForClass = findResettersForClass(cls);
            if (findResettersForClass != null) {
                findResettersForClass.invoke(null, new Object[]{obj});
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            e = e2;
            if (e instanceof InvocationTargetException) {
                e = e.getCause();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to reset views for ");
            sb2.append(obj);
            throw new RuntimeException(sb2.toString(), e);
        }
    }

    static void inject(Object obj, Object obj2, Finder finder) {
        Class cls = obj.getClass();
        try {
            if (debug) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Looking up view injector for ");
                sb.append(cls.getName());
                Log.d(str, sb.toString());
            }
            Method findInjectorForClass = findInjectorForClass(cls);
            if (findInjectorForClass != null) {
                findInjectorForClass.invoke(null, new Object[]{finder, obj, obj2});
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            e = e2;
            if (e instanceof InvocationTargetException) {
                e = e.getCause();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to inject views for ");
            sb2.append(obj);
            throw new RuntimeException(sb2.toString(), e);
        }
    }

    private static Method findInjectorForClass(Class<?> cls) throws NoSuchMethodException {
        Method method;
        Method method2 = (Method) INJECTORS.get(cls);
        if (method2 != null) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in injector map.");
            }
            return method2;
        }
        String name = cls.getName();
        if (name.startsWith(ButterKnifeProcessor.ANDROID_PREFIX) || name.startsWith(ButterKnifeProcessor.JAVA_PREFIX)) {
            if (debug) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return NO_OP;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(ButterKnifeProcessor.SUFFIX);
            method = Class.forName(sb.toString()).getMethod("inject", new Class[]{Finder.class, cls, Object.class});
            if (debug) {
                Log.d(TAG, "HIT: Class loaded injection class.");
            }
        } catch (ClassNotFoundException unused) {
            if (debug) {
                String str = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not found. Trying superclass ");
                sb2.append(cls.getSuperclass().getName());
                Log.d(str, sb2.toString());
            }
            method = findInjectorForClass(cls.getSuperclass());
        }
        INJECTORS.put(cls, method);
        return method;
    }

    private static Method findResettersForClass(Class<?> cls) throws NoSuchMethodException {
        Method method;
        Method method2 = (Method) RESETTERS.get(cls);
        if (method2 != null) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in injector map.");
            }
            return method2;
        }
        String name = cls.getName();
        if (name.startsWith(ButterKnifeProcessor.ANDROID_PREFIX) || name.startsWith(ButterKnifeProcessor.JAVA_PREFIX)) {
            if (debug) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return NO_OP;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(ButterKnifeProcessor.SUFFIX);
            method = Class.forName(sb.toString()).getMethod("reset", new Class[]{cls});
            if (debug) {
                Log.d(TAG, "HIT: Class loaded injection class.");
            }
        } catch (ClassNotFoundException unused) {
            if (debug) {
                String str = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not found. Trying superclass ");
                sb2.append(cls.getSuperclass().getName());
                Log.d(str, sb2.toString());
            }
            method = findResettersForClass(cls.getSuperclass());
        }
        RESETTERS.put(cls, method);
        return method;
    }

    public static <T extends View> void apply(List<T> list, Action<? super T> action) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            action.apply((View) list.get(i), i);
        }
    }

    public static <T extends View, V> void apply(List<T> list, Setter<? super T, V> setter, V v) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            setter.set((View) list.get(i), v, i);
        }
    }

    @TargetApi(14)
    public static <T extends View, V> void apply(List<T> list, Property<? super T, V> property, V v) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            property.set(list.get(i), v);
        }
    }

    public static <T extends View> T findById(View view, int i) {
        return view.findViewById(i);
    }

    public static <T extends View> T findById(Activity activity, int i) {
        return activity.findViewById(i);
    }
}
