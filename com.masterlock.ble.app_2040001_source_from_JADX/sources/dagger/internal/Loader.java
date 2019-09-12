package dagger.internal;

public abstract class Loader {
    private final Memoizer<ClassLoader, Memoizer<String, Class<?>>> caches = new Memoizer<ClassLoader, Memoizer<String, Class<?>>>() {
        /* access modifiers changed from: protected */
        public Memoizer<String, Class<?>> create(final ClassLoader classLoader) {
            return new Memoizer<String, Class<?>>() {
                /* access modifiers changed from: protected */
                public Class<?> create(String str) {
                    try {
                        return classLoader.loadClass(str);
                    } catch (ClassNotFoundException unused) {
                        return Void.class;
                    }
                }
            };
        }
    };

    public abstract Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z);

    public abstract <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls);

    public abstract StaticInjection getStaticInjection(Class<?> cls);

    /* access modifiers changed from: protected */
    public Class<?> loadClass(ClassLoader classLoader, String str) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return (Class) ((Memoizer) this.caches.get(classLoader)).get(str);
    }

    /* access modifiers changed from: protected */
    public <T> T instantiate(String str, ClassLoader classLoader) {
        try {
            Class<Void> loadClass = loadClass(classLoader, str);
            if (loadClass == Void.class) {
                return null;
            }
            return loadClass.newInstance();
        } catch (InstantiationException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to initialize ");
            sb.append(str);
            throw new RuntimeException(sb.toString(), e);
        } catch (IllegalAccessException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to initialize ");
            sb2.append(str);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }
}
