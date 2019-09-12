package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BindingsGroup {
    private final Map<String, Binding<?>> bindings = new LinkedHashMap();

    public abstract Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding);

    public Binding<?> contributeProvidesBinding(String str, ProvidesBinding<?> providesBinding) {
        return put(str, providesBinding);
    }

    /* access modifiers changed from: protected */
    public Binding<?> put(String str, Binding<?> binding) {
        Binding binding2 = (Binding) this.bindings.put(str, binding);
        if (binding2 == null) {
            return null;
        }
        this.bindings.put(str, binding2);
        StringBuilder sb = new StringBuilder();
        sb.append("Duplicate:\n    ");
        sb.append(binding2);
        sb.append("\n    ");
        sb.append(binding);
        throw new IllegalArgumentException(sb.toString());
    }

    public Binding<?> get(String str) {
        return (Binding) this.bindings.get(str);
    }

    public final Set<Entry<String, Binding<?>>> entrySet() {
        return this.bindings.entrySet();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(this.bindings.toString());
        return sb.toString();
    }
}
