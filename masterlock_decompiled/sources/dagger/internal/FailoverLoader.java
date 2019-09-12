package dagger.internal;

import dagger.internal.loaders.GeneratedAdapters;
import dagger.internal.loaders.ReflectiveAtInjectBinding;
import dagger.internal.loaders.ReflectiveStaticInjection;

public final class FailoverLoader extends Loader {
    private final Memoizer<Class<?>, ModuleAdapter<?>> loadedAdapters = new Memoizer<Class<?>, ModuleAdapter<?>>() {
        /* access modifiers changed from: protected */
        public ModuleAdapter<?> create(Class<?> cls) {
            ModuleAdapter<?> moduleAdapter = (ModuleAdapter) FailoverLoader.this.instantiate(cls.getName().concat(GeneratedAdapters.MODULE_ADAPTER_SUFFIX), cls.getClassLoader());
            if (moduleAdapter != null) {
                return moduleAdapter;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Module adapter for ");
            sb.append(cls);
            sb.append(" could not be loaded. ");
            sb.append("Please ensure that code generation was run for this module.");
            throw new IllegalStateException(sb.toString());
        }
    };

    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls) {
        return (ModuleAdapter) this.loadedAdapters.get(cls);
    }

    public Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z) {
        Binding<?> binding = (Binding) instantiate(str2.concat(GeneratedAdapters.INJECT_ADAPTER_SUFFIX), classLoader);
        if (binding != null) {
            return binding;
        }
        Class loadClass = loadClass(classLoader, str2);
        if (loadClass.equals(Void.class)) {
            throw new IllegalStateException(String.format("Could not load class %s needed for binding %s", new Object[]{str2, str}));
        } else if (loadClass.isInterface()) {
            return null;
        } else {
            return ReflectiveAtInjectBinding.create(loadClass, z);
        }
    }

    public StaticInjection getStaticInjection(Class<?> cls) {
        StaticInjection staticInjection = (StaticInjection) instantiate(cls.getName().concat(GeneratedAdapters.STATIC_INJECTION_SUFFIX), cls.getClassLoader());
        if (staticInjection != null) {
            return staticInjection;
        }
        return ReflectiveStaticInjection.create(cls);
    }
}
