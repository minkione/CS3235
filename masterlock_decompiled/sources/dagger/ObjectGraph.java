package dagger;

import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.FailoverLoader;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.Loader;
import dagger.internal.ModuleAdapter;
import dagger.internal.Modules;
import dagger.internal.ProblemDetector;
import dagger.internal.SetBinding;
import dagger.internal.StaticInjection;
import dagger.internal.ThrowingErrorHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ObjectGraph {

    static class DaggerObjectGraph extends ObjectGraph {
        private final DaggerObjectGraph base;
        private final Map<String, Class<?>> injectableTypes;
        private final Linker linker;
        private final Loader plugin;
        private final List<SetBinding<?>> setBindings;
        private final Map<Class<?>, StaticInjection> staticInjections;

        DaggerObjectGraph(DaggerObjectGraph daggerObjectGraph, Linker linker2, Loader loader, Map<Class<?>, StaticInjection> map, Map<String, Class<?>> map2, List<SetBinding<?>> list) {
            this.base = daggerObjectGraph;
            this.linker = (Linker) checkNotNull(linker2, "linker");
            this.plugin = (Loader) checkNotNull(loader, "plugin");
            this.staticInjections = (Map) checkNotNull(map, "staticInjections");
            this.injectableTypes = (Map) checkNotNull(map2, "injectableTypes");
            this.setBindings = (List) checkNotNull(list, "setBindings");
        }

        private static <T> T checkNotNull(T t, String str) {
            if (t != null) {
                return t;
            }
            throw new NullPointerException(str);
        }

        /* access modifiers changed from: private */
        public static ObjectGraph makeGraph(DaggerObjectGraph daggerObjectGraph, Loader loader, Object... objArr) {
            Linker linker2;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            StandardBindings standardBindings = daggerObjectGraph == null ? new StandardBindings() : new StandardBindings(daggerObjectGraph.setBindings);
            BindingsGroup overridesBindings = new OverridesBindings();
            Iterator it = Modules.loadModules(loader, objArr).entrySet().iterator();
            while (true) {
                linker2 = null;
                if (!it.hasNext()) {
                    break;
                }
                Entry entry = (Entry) it.next();
                ModuleAdapter moduleAdapter = (ModuleAdapter) entry.getKey();
                for (String put : moduleAdapter.injectableTypes) {
                    linkedHashMap.put(put, moduleAdapter.moduleClass);
                }
                for (Class<?> put2 : moduleAdapter.staticInjections) {
                    linkedHashMap2.put(put2, null);
                }
                try {
                    moduleAdapter.getBindings(moduleAdapter.overrides ? overridesBindings : standardBindings, entry.getValue());
                } catch (IllegalArgumentException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(moduleAdapter.moduleClass.getSimpleName());
                    sb.append(": ");
                    sb.append(e.getMessage());
                    throw new IllegalArgumentException(sb.toString(), e);
                }
            }
            if (daggerObjectGraph != null) {
                linker2 = daggerObjectGraph.linker;
            }
            Linker linker3 = new Linker(linker2, loader, new ThrowingErrorHandler());
            linker3.installBindings(standardBindings);
            linker3.installBindings(overridesBindings);
            DaggerObjectGraph daggerObjectGraph2 = new DaggerObjectGraph(daggerObjectGraph, linker3, loader, linkedHashMap2, linkedHashMap, standardBindings.setBindings);
            return daggerObjectGraph2;
        }

        public ObjectGraph plus(Object... objArr) {
            linkEverything();
            return makeGraph(this, this.plugin, objArr);
        }

        private void linkStaticInjections() {
            for (Entry entry : this.staticInjections.entrySet()) {
                StaticInjection staticInjection = (StaticInjection) entry.getValue();
                if (staticInjection == null) {
                    staticInjection = this.plugin.getStaticInjection((Class) entry.getKey());
                    entry.setValue(staticInjection);
                }
                staticInjection.attach(this.linker);
            }
        }

        private void linkInjectableTypes() {
            for (Entry entry : this.injectableTypes.entrySet()) {
                this.linker.requestBinding((String) entry.getKey(), entry.getValue(), ((Class) entry.getValue()).getClassLoader(), false, true);
            }
        }

        public void validate() {
            new ProblemDetector().detectProblems(linkEverything().values());
        }

        private Map<String, Binding<?>> linkEverything() {
            Map<String, Binding<?>> fullyLinkedBindings = this.linker.fullyLinkedBindings();
            if (fullyLinkedBindings != null) {
                return fullyLinkedBindings;
            }
            synchronized (this.linker) {
                Map<String, Binding<?>> fullyLinkedBindings2 = this.linker.fullyLinkedBindings();
                if (fullyLinkedBindings2 != null) {
                    return fullyLinkedBindings2;
                }
                linkStaticInjections();
                linkInjectableTypes();
                Map<String, Binding<?>> linkAll = this.linker.linkAll();
                return linkAll;
            }
        }

        public void injectStatics() {
            synchronized (this.linker) {
                linkStaticInjections();
                this.linker.linkRequested();
                linkStaticInjections();
            }
            for (Entry value : this.staticInjections.entrySet()) {
                ((StaticInjection) value.getValue()).inject();
            }
        }

        public <T> T get(Class<T> cls) {
            String str = Keys.get(cls);
            return getInjectableTypeBinding(cls.getClassLoader(), cls.isInterface() ? str : Keys.getMembersKey(cls), str).get();
        }

        public <T> T inject(T t) {
            String membersKey = Keys.getMembersKey(t.getClass());
            getInjectableTypeBinding(t.getClass().getClassLoader(), membersKey, membersKey).injectMembers(t);
            return t;
        }

        private Binding<?> getInjectableTypeBinding(ClassLoader classLoader, String str, String str2) {
            Binding<?> requestBinding;
            Class cls = null;
            for (DaggerObjectGraph daggerObjectGraph = this; daggerObjectGraph != null; daggerObjectGraph = daggerObjectGraph.base) {
                cls = (Class) daggerObjectGraph.injectableTypes.get(str);
                if (cls != null) {
                    break;
                }
            }
            if (cls != null) {
                synchronized (this.linker) {
                    requestBinding = this.linker.requestBinding(str2, cls, classLoader, false, true);
                    if (requestBinding == null || !requestBinding.isLinked()) {
                        this.linker.linkRequested();
                        requestBinding = this.linker.requestBinding(str2, cls, classLoader, false, true);
                    }
                }
                return requestBinding;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No inject registered for ");
            sb.append(str);
            sb.append(". You must explicitly add it to the 'injects' option in one of your modules.");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private static final class OverridesBindings extends BindingsGroup {
        OverridesBindings() {
        }

        public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
            throw new IllegalArgumentException("Module overrides cannot contribute set bindings.");
        }
    }

    private static final class StandardBindings extends BindingsGroup {
        /* access modifiers changed from: private */
        public final List<SetBinding<?>> setBindings;

        public StandardBindings() {
            this.setBindings = new ArrayList();
        }

        public StandardBindings(List<SetBinding<?>> list) {
            this.setBindings = new ArrayList(list.size());
            for (SetBinding setBinding : list) {
                SetBinding setBinding2 = new SetBinding(setBinding);
                this.setBindings.add(setBinding2);
                put(setBinding2.provideKey, setBinding2);
            }
        }

        public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
            this.setBindings.add(setBinding);
            return super.put(str, setBinding);
        }
    }

    public abstract <T> T get(Class<T> cls);

    public abstract <T> T inject(T t);

    public abstract void injectStatics();

    public abstract ObjectGraph plus(Object... objArr);

    public abstract void validate();

    ObjectGraph() {
    }

    public static ObjectGraph create(Object... objArr) {
        return DaggerObjectGraph.makeGraph(null, new FailoverLoader(), objArr);
    }

    static ObjectGraph createWith(Loader loader, Object... objArr) {
        return DaggerObjectGraph.makeGraph(null, loader, objArr);
    }
}
