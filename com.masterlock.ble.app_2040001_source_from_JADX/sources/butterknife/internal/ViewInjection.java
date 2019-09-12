package butterknife.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ViewInjection {

    /* renamed from: id */
    private final int f29id;
    private final Map<ListenerClass, Map<ListenerMethod, ListenerBinding>> listenerBindings = new LinkedHashMap();
    private final Set<ViewBinding> viewBindings = new LinkedHashSet();

    ViewInjection(int i) {
        this.f29id = i;
    }

    public int getId() {
        return this.f29id;
    }

    public Collection<ViewBinding> getViewBindings() {
        return this.viewBindings;
    }

    public Map<ListenerClass, Map<ListenerMethod, ListenerBinding>> getListenerBindings() {
        return this.listenerBindings;
    }

    public boolean hasListenerBinding(ListenerClass listenerClass, ListenerMethod listenerMethod) {
        Map map = (Map) this.listenerBindings.get(listenerClass);
        return map != null && map.containsKey(listenerMethod);
    }

    public void addListenerBinding(ListenerClass listenerClass, ListenerMethod listenerMethod, ListenerBinding listenerBinding) {
        Map map = (Map) this.listenerBindings.get(listenerClass);
        if (map == null) {
            map = new LinkedHashMap();
            this.listenerBindings.put(listenerClass, map);
        }
        ListenerBinding listenerBinding2 = (ListenerBinding) map.get(listenerMethod);
        if (listenerBinding2 == null) {
            map.put(listenerMethod, listenerBinding);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(this.f29id);
        sb.append(" already has listener binding for ");
        sb.append(listenerClass.type());
        sb.append(".");
        sb.append(listenerMethod.name());
        sb.append(" on ");
        sb.append(listenerBinding2.getDescription());
        throw new IllegalStateException(sb.toString());
    }

    public void addViewBinding(ViewBinding viewBinding) {
        this.viewBindings.add(viewBinding);
    }

    public List<Binding> getRequiredBindings() {
        ArrayList arrayList = new ArrayList();
        for (ViewBinding viewBinding : this.viewBindings) {
            if (viewBinding.isRequired()) {
                arrayList.add(viewBinding);
            }
        }
        for (Map values : this.listenerBindings.values()) {
            for (ListenerBinding listenerBinding : values.values()) {
                if (listenerBinding.isRequired()) {
                    arrayList.add(listenerBinding);
                }
            }
        }
        return arrayList;
    }
}
