package dagger.internal;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class ProblemDetector {

    static class ArraySet<T> extends AbstractSet<T> {
        private final ArrayList<T> list = new ArrayList<>();

        ArraySet() {
        }

        public boolean add(T t) {
            this.list.add(t);
            return true;
        }

        public Iterator<T> iterator() {
            return this.list.iterator();
        }

        public int size() {
            throw new UnsupportedOperationException();
        }
    }

    public void detectCircularDependencies(Collection<Binding<?>> collection) {
        detectCircularDependencies(collection, new ArrayList());
    }

    public void detectUnusedBinding(Collection<Binding<?>> collection) {
        ArrayList arrayList = new ArrayList();
        for (Binding binding : collection) {
            if (!binding.library() && !binding.dependedOn()) {
                arrayList.add(binding);
            }
        }
        if (!arrayList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("You have these unused @Provider methods:");
            int i = 0;
            while (i < arrayList.size()) {
                sb.append("\n    ");
                int i2 = i + 1;
                sb.append(i2);
                sb.append(". ");
                sb.append(((Binding) arrayList.get(i)).requiredBy);
                i = i2;
            }
            sb.append("\n    Set library=true in your module to disable this check.");
            throw new IllegalStateException(sb.toString());
        }
    }

    private static void detectCircularDependencies(Collection<Binding<?>> collection, List<Binding<?>> list) {
        for (Binding binding : collection) {
            if (!binding.isCycleFree()) {
                if (binding.isVisiting()) {
                    int indexOf = list.indexOf(binding);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Dependency cycle:");
                    for (int i = indexOf; i < list.size(); i++) {
                        sb.append("\n    ");
                        sb.append(i - indexOf);
                        sb.append(". ");
                        sb.append(((Binding) list.get(i)).provideKey);
                        sb.append(" bound by ");
                        sb.append(list.get(i));
                    }
                    sb.append("\n    ");
                    sb.append(0);
                    sb.append(". ");
                    sb.append(binding.provideKey);
                    throw new IllegalStateException(sb.toString());
                }
                binding.setVisiting(true);
                list.add(binding);
                try {
                    ArraySet arraySet = new ArraySet();
                    binding.getDependencies(arraySet, arraySet);
                    detectCircularDependencies(arraySet, list);
                    binding.setCycleFree(true);
                } finally {
                    list.remove(list.size() - 1);
                    binding.setVisiting(false);
                }
            }
        }
    }

    public void detectProblems(Collection<Binding<?>> collection) {
        detectCircularDependencies(collection);
        detectUnusedBinding(collection);
    }
}
