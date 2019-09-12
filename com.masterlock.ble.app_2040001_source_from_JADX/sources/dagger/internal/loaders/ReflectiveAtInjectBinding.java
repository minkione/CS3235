package dagger.internal.loaders;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

public final class ReflectiveAtInjectBinding<T> extends Binding<T> {
    private final Constructor<T> constructor;
    private final Binding<?>[] fieldBindings;
    private final Field[] fields;
    private final String[] keys;
    private final ClassLoader loader;
    private final Binding<?>[] parameterBindings;
    private final Class<?> supertype;
    private Binding<? super T> supertypeBinding;

    private ReflectiveAtInjectBinding(String str, String str2, boolean z, Class<?> cls, Field[] fieldArr, Constructor<T> constructor2, int i, Class<?> cls2, String[] strArr) {
        super(str, str2, z, cls);
        this.constructor = constructor2;
        this.fields = fieldArr;
        this.supertype = cls2;
        this.keys = strArr;
        this.parameterBindings = new Binding[i];
        this.fieldBindings = new Binding[fieldArr.length];
        this.loader = cls.getClassLoader();
    }

    public void attach(Linker linker) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            Field[] fieldArr = this.fields;
            if (i2 >= fieldArr.length) {
                break;
            }
            Binding<?>[] bindingArr = this.fieldBindings;
            if (bindingArr[i2] == null) {
                bindingArr[i2] = linker.requestBinding(this.keys[i3], fieldArr[i2], this.loader);
            }
            i3++;
            i2++;
        }
        if (this.constructor != null) {
            while (true) {
                Binding<?>[] bindingArr2 = this.parameterBindings;
                if (i >= bindingArr2.length) {
                    break;
                }
                if (bindingArr2[i] == null) {
                    bindingArr2[i] = linker.requestBinding(this.keys[i3], this.constructor, this.loader);
                }
                i3++;
                i++;
            }
        }
        if (this.supertype != null && this.supertypeBinding == null) {
            this.supertypeBinding = linker.requestBinding(this.keys[i3], this.membersKey, this.loader, false, true);
        }
    }

    public T get() {
        if (this.constructor != null) {
            Object[] objArr = new Object[this.parameterBindings.length];
            int i = 0;
            while (true) {
                Binding<?>[] bindingArr = this.parameterBindings;
                if (i < bindingArr.length) {
                    objArr[i] = bindingArr[i].get();
                    i++;
                } else {
                    try {
                        T newInstance = this.constructor.newInstance(objArr);
                        injectMembers(newInstance);
                        return newInstance;
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        throw (cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause));
                    } catch (IllegalAccessException e2) {
                        throw new AssertionError(e2);
                    } catch (InstantiationException e3) {
                        throw new RuntimeException(e3);
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void injectMembers(T t) {
        int i = 0;
        while (i < this.fields.length) {
            try {
                this.fields[i].set(t, this.fieldBindings[i].get());
                i++;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
        if (this.supertypeBinding != null) {
            this.supertypeBinding.injectMembers(t);
        }
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        Binding<?>[] bindingArr = this.parameterBindings;
        if (bindingArr != null) {
            Collections.addAll(set, bindingArr);
        }
        Collections.addAll(set2, this.fieldBindings);
        Binding<? super T> binding = this.supertypeBinding;
        if (binding != null) {
            set2.add(binding);
        }
    }

    public String toString() {
        return this.provideKey != null ? this.provideKey : this.membersKey;
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0106  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> dagger.internal.Binding<T> create(java.lang.Class<T> r12, boolean r13) {
        /*
            java.lang.Class<javax.inject.Singleton> r0 = javax.inject.Singleton.class
            boolean r4 = r12.isAnnotationPresent(r0)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = r12
        L_0x0011:
            java.lang.Class<java.lang.Object> r3 = java.lang.Object.class
            r5 = 1
            r6 = 0
            if (r2 == r3) goto L_0x0070
            java.lang.reflect.Field[] r3 = r2.getDeclaredFields()
            int r7 = r3.length
        L_0x001c:
            if (r6 >= r7) goto L_0x006b
            r8 = r3[r6]
            java.lang.Class<javax.inject.Inject> r9 = javax.inject.Inject.class
            boolean r9 = r8.isAnnotationPresent(r9)
            if (r9 == 0) goto L_0x0068
            int r9 = r8.getModifiers()
            boolean r9 = java.lang.reflect.Modifier.isStatic(r9)
            if (r9 == 0) goto L_0x0033
            goto L_0x0068
        L_0x0033:
            int r9 = r8.getModifiers()
            r9 = r9 & 2
            if (r9 != 0) goto L_0x0051
            r8.setAccessible(r5)
            r1.add(r8)
            java.lang.reflect.Type r9 = r8.getGenericType()
            java.lang.annotation.Annotation[] r10 = r8.getAnnotations()
            java.lang.String r8 = dagger.internal.Keys.get(r9, r10, r8)
            r0.add(r8)
            goto L_0x0068
        L_0x0051:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "Can't inject private field: "
            r13.append(r0)
            r13.append(r8)
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0068:
            int r6 = r6 + 1
            goto L_0x001c
        L_0x006b:
            java.lang.Class r2 = r2.getSuperclass()
            goto L_0x0011
        L_0x0070:
            java.lang.reflect.Constructor[] r2 = getConstructorsForType(r12)
            int r3 = r2.length
            r7 = 0
            r9 = r7
            r8 = 0
        L_0x0078:
            if (r8 >= r3) goto L_0x0097
            r10 = r2[r8]
            java.lang.Class<javax.inject.Inject> r11 = javax.inject.Inject.class
            boolean r11 = r10.isAnnotationPresent(r11)
            if (r11 != 0) goto L_0x0085
            goto L_0x0088
        L_0x0085:
            if (r9 != 0) goto L_0x008b
            r9 = r10
        L_0x0088:
            int r8 = r8 + 1
            goto L_0x0078
        L_0x008b:
            dagger.internal.Binding$InvalidBindingException r13 = new dagger.internal.Binding$InvalidBindingException
            java.lang.String r12 = r12.getName()
            java.lang.String r0 = "has too many injectable constructors"
            r13.<init>(r12, r0)
            throw r13
        L_0x0097:
            if (r9 != 0) goto L_0x00b5
            boolean r2 = r1.isEmpty()
            if (r2 != 0) goto L_0x00a6
            java.lang.Class[] r13 = new java.lang.Class[r6]     // Catch:{ NoSuchMethodException -> 0x00b5 }
            java.lang.reflect.Constructor r13 = r12.getDeclaredConstructor(r13)     // Catch:{ NoSuchMethodException -> 0x00b5 }
            goto L_0x00b6
        L_0x00a6:
            if (r13 != 0) goto L_0x00a9
            goto L_0x00b5
        L_0x00a9:
            dagger.internal.Binding$InvalidBindingException r13 = new dagger.internal.Binding$InvalidBindingException
            java.lang.String r12 = r12.getName()
            java.lang.String r0 = "has no injectable members. Do you want to add an injectable constructor?"
            r13.<init>(r12, r0)
            throw r13
        L_0x00b5:
            r13 = r9
        L_0x00b6:
            if (r13 == 0) goto L_0x00fc
            int r2 = r13.getModifiers()
            r2 = r2 & 2
            if (r2 != 0) goto L_0x00e5
            java.lang.String r2 = dagger.internal.Keys.get(r12)
            r13.setAccessible(r5)
            java.lang.reflect.Type[] r3 = r13.getGenericParameterTypes()
            int r5 = r3.length
            if (r5 == 0) goto L_0x00e3
            java.lang.annotation.Annotation[][] r8 = r13.getParameterAnnotations()
        L_0x00d2:
            int r9 = r3.length
            if (r6 >= r9) goto L_0x00e3
            r9 = r3[r6]
            r10 = r8[r6]
            java.lang.String r9 = dagger.internal.Keys.get(r9, r10, r13)
            r0.add(r9)
            int r6 = r6 + 1
            goto L_0x00d2
        L_0x00e3:
            r8 = r5
            goto L_0x0100
        L_0x00e5:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Can't inject private constructor: "
            r0.append(r1)
            r0.append(r13)
            java.lang.String r13 = r0.toString()
            r12.<init>(r13)
            throw r12
        L_0x00fc:
            if (r4 != 0) goto L_0x0141
            r2 = r7
            r8 = 0
        L_0x0100:
            java.lang.Class r3 = r12.getSuperclass()
            if (r3 == 0) goto L_0x0119
            java.lang.String r5 = r3.getName()
            boolean r5 = dagger.internal.Keys.isPlatformType(r5)
            if (r5 == 0) goto L_0x0112
            r9 = r7
            goto L_0x011a
        L_0x0112:
            java.lang.String r5 = dagger.internal.Keys.getMembersKey(r3)
            r0.add(r5)
        L_0x0119:
            r9 = r3
        L_0x011a:
            java.lang.String r3 = dagger.internal.Keys.getMembersKey(r12)
            dagger.internal.loaders.ReflectiveAtInjectBinding r11 = new dagger.internal.loaders.ReflectiveAtInjectBinding
            int r5 = r1.size()
            java.lang.reflect.Field[] r5 = new java.lang.reflect.Field[r5]
            java.lang.Object[] r1 = r1.toArray(r5)
            r6 = r1
            java.lang.reflect.Field[] r6 = (java.lang.reflect.Field[]) r6
            int r1 = r0.size()
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.Object[] r0 = r0.toArray(r1)
            r10 = r0
            java.lang.String[] r10 = (java.lang.String[]) r10
            r1 = r11
            r5 = r12
            r7 = r13
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10)
            return r11
        L_0x0141:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "No injectable constructor on @Singleton "
            r0.append(r1)
            java.lang.String r12 = r12.getName()
            r0.append(r12)
            java.lang.String r12 = r0.toString()
            r13.<init>(r12)
            throw r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dagger.internal.loaders.ReflectiveAtInjectBinding.create(java.lang.Class, boolean):dagger.internal.Binding");
    }

    private static <T> Constructor<T>[] getConstructorsForType(Class<T> cls) {
        return cls.getDeclaredConstructors();
    }
}
