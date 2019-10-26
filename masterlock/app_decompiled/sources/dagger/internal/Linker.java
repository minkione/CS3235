package dagger.internal;

import dagger.internal.Binding.InvalidBindingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public final class Linker {
    /* access modifiers changed from: private */
    public static final Object UNINITIALIZED = new Object();
    private boolean attachSuccess = true;
    private final Linker base;
    private final Map<String, Binding<?>> bindings = new HashMap();
    private final ErrorHandler errorHandler;
    private final List<String> errors = new ArrayList();
    private volatile Map<String, Binding<?>> linkedBindings = null;
    private final Loader plugin;
    private final Queue<Binding<?>> toLink = new ArrayQueue();

    private static class DeferredBinding extends Binding<Object> {
        final ClassLoader classLoader;
        final String deferredKey;
        final boolean mustHaveInjections;

        private DeferredBinding(String str, ClassLoader classLoader2, Object obj, boolean z) {
            super(null, null, false, obj);
            this.deferredKey = str;
            this.classLoader = classLoader2;
            this.mustHaveInjections = z;
        }

        public void injectMembers(Object obj) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DeferredBinding[deferredKey=");
            sb.append(this.deferredKey);
            sb.append("]");
            return sb.toString();
        }
    }

    public interface ErrorHandler {
        public static final ErrorHandler NULL = new ErrorHandler() {
            public void handleErrors(List<String> list) {
            }
        };

        void handleErrors(List<String> list);
    }

    private static class SingletonBinding<T> extends Binding<T> {
        private final Binding<T> binding;
        private volatile Object onlyInstance;

        /* access modifiers changed from: protected */
        public boolean isSingleton() {
            return true;
        }

        private SingletonBinding(Binding<T> binding2) {
            super(binding2.provideKey, binding2.membersKey, true, binding2.requiredBy);
            this.onlyInstance = Linker.UNINITIALIZED;
            this.binding = binding2;
        }

        public void attach(Linker linker) {
            this.binding.attach(linker);
        }

        public void injectMembers(T t) {
            this.binding.injectMembers(t);
        }

        public T get() {
            if (this.onlyInstance == Linker.UNINITIALIZED) {
                synchronized (this) {
                    if (this.onlyInstance == Linker.UNINITIALIZED) {
                        this.onlyInstance = this.binding.get();
                    }
                }
            }
            return this.onlyInstance;
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            this.binding.getDependencies(set, set2);
        }

        public boolean isCycleFree() {
            return this.binding.isCycleFree();
        }

        public boolean isLinked() {
            return this.binding.isLinked();
        }

        public boolean isVisiting() {
            return this.binding.isVisiting();
        }

        public boolean library() {
            return this.binding.library();
        }

        public boolean dependedOn() {
            return this.binding.dependedOn();
        }

        public void setCycleFree(boolean z) {
            this.binding.setCycleFree(z);
        }

        public void setVisiting(boolean z) {
            this.binding.setVisiting(z);
        }

        public void setLibrary(boolean z) {
            this.binding.setLibrary(true);
        }

        public void setDependedOn(boolean z) {
            this.binding.setDependedOn(z);
        }

        /* access modifiers changed from: protected */
        public void setLinked() {
            this.binding.setLinked();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("@Singleton/");
            sb.append(this.binding.toString());
            return sb.toString();
        }
    }

    public Linker(Linker linker, Loader loader, ErrorHandler errorHandler2) {
        if (loader == null) {
            throw new NullPointerException("plugin");
        } else if (errorHandler2 != null) {
            this.base = linker;
            this.plugin = loader;
            this.errorHandler = errorHandler2;
        } else {
            throw new NullPointerException("errorHandler");
        }
    }

    public void installBindings(BindingsGroup bindingsGroup) {
        if (this.linkedBindings == null) {
            for (Entry entry : bindingsGroup.entrySet()) {
                this.bindings.put(entry.getKey(), scope((Binding) entry.getValue()));
            }
            return;
        }
        throw new IllegalStateException("Cannot install further bindings after calling linkAll().");
    }

    public Map<String, Binding<?>> linkAll() {
        assertLockHeld();
        if (this.linkedBindings != null) {
            return this.linkedBindings;
        }
        for (Binding binding : this.bindings.values()) {
            if (!binding.isLinked()) {
                this.toLink.add(binding);
            }
        }
        linkRequested();
        this.linkedBindings = Collections.unmodifiableMap(this.bindings);
        return this.linkedBindings;
    }

    public Map<String, Binding<?>> fullyLinkedBindings() {
        return this.linkedBindings;
    }

    public void linkRequested() {
        assertLockHeld();
        while (true) {
            Binding binding = (Binding) this.toLink.poll();
            if (binding == null) {
                try {
                    this.errorHandler.handleErrors(this.errors);
                    return;
                } finally {
                    this.errors.clear();
                }
            } else if (binding instanceof DeferredBinding) {
                DeferredBinding deferredBinding = (DeferredBinding) binding;
                String str = deferredBinding.deferredKey;
                boolean z = deferredBinding.mustHaveInjections;
                if (this.bindings.containsKey(str)) {
                    continue;
                } else {
                    try {
                        Binding createBinding = createBinding(str, binding.requiredBy, deferredBinding.classLoader, z);
                        createBinding.setLibrary(binding.library());
                        createBinding.setDependedOn(binding.dependedOn());
                        if (!str.equals(createBinding.provideKey)) {
                            if (!str.equals(createBinding.membersKey)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Unable to create binding for ");
                                sb.append(str);
                                throw new IllegalStateException(sb.toString());
                            }
                        }
                        Binding scope = scope(createBinding);
                        this.toLink.add(scope);
                        putBinding(scope);
                    } catch (InvalidBindingException e) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(e.type);
                        sb2.append(" ");
                        sb2.append(e.getMessage());
                        sb2.append(" required by ");
                        sb2.append(binding.requiredBy);
                        addError(sb2.toString());
                        this.bindings.put(str, Binding.UNRESOLVED);
                    } catch (UnsupportedOperationException e2) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Unsupported: ");
                        sb3.append(e2.getMessage());
                        sb3.append(" required by ");
                        sb3.append(binding.requiredBy);
                        addError(sb3.toString());
                        this.bindings.put(str, Binding.UNRESOLVED);
                    } catch (IllegalArgumentException e3) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(e3.getMessage());
                        sb4.append(" required by ");
                        sb4.append(binding.requiredBy);
                        addError(sb4.toString());
                        this.bindings.put(str, Binding.UNRESOLVED);
                    } catch (RuntimeException e4) {
                        throw e4;
                    } catch (Exception e5) {
                        throw new RuntimeException(e5);
                    }
                }
            } else {
                this.attachSuccess = true;
                binding.attach(this);
                if (this.attachSuccess) {
                    binding.setLinked();
                } else {
                    this.toLink.add(binding);
                }
            }
        }
    }

    private void assertLockHeld() {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
    }

    private Binding<?> createBinding(String str, Object obj, ClassLoader classLoader, boolean z) {
        String builtInBindingsKey = Keys.getBuiltInBindingsKey(str);
        if (builtInBindingsKey != null) {
            return new BuiltInBinding(str, obj, classLoader, builtInBindingsKey);
        }
        String lazyKey = Keys.getLazyKey(str);
        if (lazyKey != null) {
            return new LazyBinding(str, obj, classLoader, lazyKey);
        }
        String className = Keys.getClassName(str);
        if (className == null || Keys.isAnnotated(str)) {
            throw new IllegalArgumentException(str);
        }
        Binding<?> atInjectBinding = this.plugin.getAtInjectBinding(str, className, classLoader, z);
        if (atInjectBinding != null) {
            return atInjectBinding;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("could not be bound with key ");
        sb.append(str);
        throw new InvalidBindingException(className, sb.toString());
    }

    @Deprecated
    public Binding<?> requestBinding(String str, Object obj) {
        return requestBinding(str, obj, getClass().getClassLoader(), true, true);
    }

    public Binding<?> requestBinding(String str, Object obj, ClassLoader classLoader) {
        return requestBinding(str, obj, classLoader, true, true);
    }

    @Deprecated
    public Binding<?> requestBinding(String str, Object obj, boolean z, boolean z2) {
        return requestBinding(str, obj, getClass().getClassLoader(), z, z2);
    }

    public Binding<?> requestBinding(String str, Object obj, ClassLoader classLoader, boolean z, boolean z2) {
        assertLockHeld();
        Linker linker = this;
        Binding binding = null;
        while (true) {
            if (linker == null) {
                break;
            }
            binding = (Binding) linker.bindings.get(str);
            if (binding == null) {
                linker = linker.base;
            } else if (linker != this && !binding.isLinked()) {
                throw new AssertionError();
            }
        }
        if (binding == null) {
            DeferredBinding deferredBinding = new DeferredBinding(str, classLoader, obj, z);
            deferredBinding.setLibrary(z2);
            deferredBinding.setDependedOn(true);
            this.toLink.add(deferredBinding);
            this.attachSuccess = false;
            return null;
        }
        if (!binding.isLinked()) {
            this.toLink.add(binding);
        }
        binding.setLibrary(z2);
        binding.setDependedOn(true);
        return binding;
    }

    private <T> void putBinding(Binding<T> binding) {
        if (binding.provideKey != null) {
            putIfAbsent(this.bindings, binding.provideKey, binding);
        }
        if (binding.membersKey != null) {
            putIfAbsent(this.bindings, binding.membersKey, binding);
        }
    }

    static <T> Binding<T> scope(Binding<T> binding) {
        return (!binding.isSingleton() || (binding instanceof SingletonBinding)) ? binding : new SingletonBinding(binding);
    }

    private <K, V> void putIfAbsent(Map<K, V> map, K k, V v) {
        Object put = map.put(k, v);
        if (put != null) {
            map.put(k, put);
        }
    }

    private void addError(String str) {
        this.errors.add(str);
    }
}
