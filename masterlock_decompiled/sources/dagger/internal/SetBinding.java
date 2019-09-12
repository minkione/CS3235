package dagger.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SetBinding<T> extends Binding<Set<T>> {
    private final List<Binding<?>> contributors;
    private final SetBinding<T> parent;

    public static <T> void add(BindingsGroup bindingsGroup, String str, Binding<?> binding) {
        prepareSetBinding(bindingsGroup, str, binding).contributors.add(Linker.scope(binding));
    }

    private static <T> SetBinding<T> prepareSetBinding(BindingsGroup bindingsGroup, String str, Binding<?> binding) {
        Binding binding2 = bindingsGroup.get(str);
        if (binding2 instanceof SetBinding) {
            SetBinding<T> setBinding = (SetBinding) binding2;
            setBinding.setLibrary(setBinding.library() && binding.library());
            return setBinding;
        } else if (binding2 == null) {
            SetBinding setBinding2 = new SetBinding(str, binding.requiredBy);
            setBinding2.setLibrary(binding.library());
            bindingsGroup.contributeSetBinding(str, setBinding2);
            return (SetBinding) bindingsGroup.get(str);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Duplicate:\n    ");
            sb.append(binding2);
            sb.append("\n    ");
            sb.append(binding);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public SetBinding(String str, Object obj) {
        super(str, null, false, obj);
        this.parent = null;
        this.contributors = new ArrayList();
    }

    public SetBinding(SetBinding<T> setBinding) {
        super(setBinding.provideKey, null, false, setBinding.requiredBy);
        this.parent = setBinding;
        setLibrary(setBinding.library());
        setDependedOn(setBinding.dependedOn());
        this.contributors = new ArrayList();
    }

    public void attach(Linker linker) {
        for (Binding attach : this.contributors) {
            attach.attach(linker);
        }
    }

    public int size() {
        int i = 0;
        for (SetBinding setBinding = this; setBinding != null; setBinding = setBinding.parent) {
            i += setBinding.contributors.size();
        }
        return i;
    }

    public Set<T> get() {
        ArrayList arrayList = new ArrayList();
        for (SetBinding setBinding = this; setBinding != null; setBinding = setBinding.parent) {
            int size = setBinding.contributors.size();
            for (int i = 0; i < size; i++) {
                Binding binding = (Binding) setBinding.contributors.get(i);
                Object obj = binding.get();
                if (binding.provideKey.equals(this.provideKey)) {
                    arrayList.addAll((Set) obj);
                } else {
                    arrayList.add(obj);
                }
            }
        }
        return Collections.unmodifiableSet(new LinkedHashSet(arrayList));
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        for (SetBinding setBinding = this; setBinding != null; setBinding = setBinding.parent) {
            set.addAll(setBinding.contributors);
        }
    }

    public void injectMembers(Set<T> set) {
        throw new UnsupportedOperationException("Cannot inject members on a contributed Set<T>.");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SetBinding[");
        boolean z = true;
        SetBinding setBinding = this;
        while (setBinding != null) {
            int size = setBinding.contributors.size();
            boolean z2 = z;
            int i = 0;
            while (i < size) {
                if (!z2) {
                    sb.append(",");
                }
                sb.append(setBinding.contributors.get(i));
                i++;
                z2 = false;
            }
            setBinding = setBinding.parent;
            z = z2;
        }
        sb.append("]");
        return sb.toString();
    }
}
