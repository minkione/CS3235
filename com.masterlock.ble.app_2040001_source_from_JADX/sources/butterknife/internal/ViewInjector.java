package butterknife.internal;

import com.google.android.gms.analytics.ecommerce.Promotion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class ViewInjector {
    private final String className;
    private final String classPackage;
    private final Map<CollectionBinding, int[]> collectionBindings = new LinkedHashMap();
    private String parentInjector;
    private final String targetClass;
    private final Map<Integer, ViewInjection> viewIdMap = new LinkedHashMap();

    ViewInjector(String str, String str2, String str3) {
        this.classPackage = str;
        this.className = str2;
        this.targetClass = str3;
    }

    /* access modifiers changed from: 0000 */
    public void addView(int i, ViewBinding viewBinding) {
        getOrCreateViewInjection(i).addViewBinding(viewBinding);
    }

    /* access modifiers changed from: 0000 */
    public boolean addListener(int i, ListenerClass listenerClass, ListenerMethod listenerMethod, ListenerBinding listenerBinding) {
        ViewInjection orCreateViewInjection = getOrCreateViewInjection(i);
        if (orCreateViewInjection.hasListenerBinding(listenerClass, listenerMethod)) {
            return false;
        }
        orCreateViewInjection.addListenerBinding(listenerClass, listenerMethod, listenerBinding);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void addCollection(int[] iArr, CollectionBinding collectionBinding) {
        this.collectionBindings.put(collectionBinding, iArr);
    }

    /* access modifiers changed from: 0000 */
    public void setParentInjector(String str) {
        this.parentInjector = str;
    }

    private ViewInjection getOrCreateViewInjection(int i) {
        ViewInjection viewInjection = (ViewInjection) this.viewIdMap.get(Integer.valueOf(i));
        if (viewInjection != null) {
            return viewInjection;
        }
        ViewInjection viewInjection2 = new ViewInjection(i);
        this.viewIdMap.put(Integer.valueOf(i), viewInjection2);
        return viewInjection2;
    }

    /* access modifiers changed from: 0000 */
    public String getFqcn() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.classPackage);
        sb.append(".");
        sb.append(this.className);
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public String brewJava() {
        StringBuilder sb = new StringBuilder();
        sb.append("// Generated code from Butter Knife. Do not modify!\n");
        sb.append("package ");
        sb.append(this.classPackage);
        sb.append(";\n\n");
        sb.append("import android.view.View;\n");
        sb.append("import butterknife.ButterKnife.Finder;\n\n");
        sb.append("public class ");
        sb.append(this.className);
        sb.append(" {\n");
        emitInject(sb);
        sb.append(10);
        emitReset(sb);
        sb.append("}\n");
        return sb.toString();
    }

    private void emitInject(StringBuilder sb) {
        sb.append("  public static void inject(Finder finder, final ");
        sb.append(this.targetClass);
        sb.append(" target, Object source) {\n");
        if (this.parentInjector != null) {
            sb.append("    ");
            sb.append(this.parentInjector);
            sb.append(".inject(finder, target, source);\n\n");
        }
        sb.append("    View view;\n");
        for (ViewInjection emitViewInjection : this.viewIdMap.values()) {
            emitViewInjection(sb, emitViewInjection);
        }
        for (Entry entry : this.collectionBindings.entrySet()) {
            emitCollectionBinding(sb, (CollectionBinding) entry.getKey(), (int[]) entry.getValue());
        }
        sb.append("  }\n");
    }

    private void emitCollectionBinding(StringBuilder sb, CollectionBinding collectionBinding, int[] iArr) {
        sb.append("    target.");
        sb.append(collectionBinding.getName());
        sb.append(" = ");
        switch (collectionBinding.getKind()) {
            case ARRAY:
                sb.append("Finder.arrayOf(");
                break;
            case LIST:
                sb.append("Finder.listOf(");
                break;
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown kind: ");
                sb2.append(collectionBinding.getKind());
                throw new IllegalStateException(sb2.toString());
        }
        for (int i = 0; i < iArr.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append("\n        ");
            emitCastIfNeeded(sb, collectionBinding.getType());
            if (collectionBinding.isRequired()) {
                sb.append("finder.findRequiredView(source, ");
                sb.append(iArr[i]);
                sb.append(", \"");
                sb.append(collectionBinding.getName());
                sb.append("\")");
            } else {
                sb.append("finder.findOptionalView(source, ");
                sb.append(iArr[i]);
                sb.append(")");
            }
        }
        sb.append("\n    );");
    }

    private void emitViewInjection(StringBuilder sb, ViewInjection viewInjection) {
        sb.append("    view = ");
        List requiredBindings = viewInjection.getRequiredBindings();
        if (requiredBindings.isEmpty()) {
            sb.append("finder.findOptionalView(source, ");
            sb.append(viewInjection.getId());
            sb.append(");\n");
        } else {
            sb.append("finder.findRequiredView(source, ");
            sb.append(viewInjection.getId());
            sb.append(", \"");
            emitHumanDescription(sb, requiredBindings);
            sb.append("\");\n");
        }
        emitViewBindings(sb, viewInjection);
        emitListenerBindings(sb, viewInjection);
    }

    private void emitViewBindings(StringBuilder sb, ViewInjection viewInjection) {
        Collection<ViewBinding> viewBindings = viewInjection.getViewBindings();
        if (!viewBindings.isEmpty()) {
            for (ViewBinding viewBinding : viewBindings) {
                sb.append("    target.");
                sb.append(viewBinding.getName());
                sb.append(" = ");
                emitCastIfNeeded(sb, viewBinding.getType());
                sb.append("view;\n");
            }
        }
    }

    private void emitListenerBindings(StringBuilder sb, ViewInjection viewInjection) {
        Map listenerBindings = viewInjection.getListenerBindings();
        if (!listenerBindings.isEmpty()) {
            String str = "";
            boolean isEmpty = viewInjection.getRequiredBindings().isEmpty();
            if (isEmpty) {
                sb.append("    if (view != null) {\n");
                str = "  ";
            }
            for (Entry entry : listenerBindings.entrySet()) {
                ListenerClass listenerClass = (ListenerClass) entry.getKey();
                Map map = (Map) entry.getValue();
                boolean z = !"android.view.View".equals(listenerClass.targetType());
                sb.append(str);
                sb.append("    ");
                if (z) {
                    sb.append("((");
                    sb.append(listenerClass.targetType());
                    if (listenerClass.genericArguments() > 0) {
                        sb.append('<');
                        for (int i = 0; i < listenerClass.genericArguments(); i++) {
                            if (i > 0) {
                                sb.append(", ");
                            }
                            sb.append('?');
                        }
                        sb.append('>');
                    }
                    sb.append(") ");
                }
                sb.append(Promotion.ACTION_VIEW);
                if (z) {
                    sb.append(')');
                }
                sb.append('.');
                sb.append(listenerClass.setter());
                sb.append("(\n");
                sb.append(str);
                sb.append("      new ");
                sb.append(listenerClass.type());
                sb.append("() {\n");
                for (ListenerMethod listenerMethod : getListenerMethods(listenerClass)) {
                    sb.append(str);
                    sb.append("        @Override public ");
                    sb.append(listenerMethod.returnType());
                    sb.append(' ');
                    sb.append(listenerMethod.name());
                    sb.append("(\n");
                    String[] parameters = listenerMethod.parameters();
                    int length = parameters.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        sb.append(str);
                        sb.append("          ");
                        sb.append(parameters[i2]);
                        sb.append(" p");
                        sb.append(i2);
                        if (i2 < length - 1) {
                            sb.append(',');
                        }
                        sb.append(10);
                    }
                    sb.append(str);
                    sb.append("        ) {\n");
                    sb.append(str);
                    sb.append("          ");
                    boolean z2 = !"void".equals(listenerMethod.returnType());
                    if (z2) {
                        sb.append("return ");
                    }
                    if (map.containsKey(listenerMethod)) {
                        ListenerBinding listenerBinding = (ListenerBinding) map.get(listenerMethod);
                        sb.append("target.");
                        sb.append(listenerBinding.getName());
                        sb.append('(');
                        List parameters2 = listenerBinding.getParameters();
                        String[] parameters3 = listenerMethod.parameters();
                        int size = parameters2.size();
                        for (int i3 = 0; i3 < size; i3++) {
                            Parameter parameter = (Parameter) parameters2.get(i3);
                            int listenerPosition = parameter.getListenerPosition();
                            emitCastIfNeeded(sb, parameters3[listenerPosition], parameter.getType());
                            sb.append('p');
                            sb.append(listenerPosition);
                            if (i3 < size - 1) {
                                sb.append(", ");
                            }
                        }
                        sb.append(");");
                    } else if (z2) {
                        sb.append(listenerMethod.defaultReturn());
                        sb.append(';');
                    }
                    sb.append(10);
                    sb.append(str);
                    sb.append("        }\n");
                }
                sb.append(str);
                sb.append("      });\n");
            }
            if (isEmpty) {
                sb.append("    }\n");
            }
        }
    }

    static List<ListenerMethod> getListenerMethods(ListenerClass listenerClass) {
        if (listenerClass.method().length == 1) {
            return Arrays.asList(listenerClass.method());
        }
        try {
            ArrayList arrayList = new ArrayList();
            Class callbacks = listenerClass.callbacks();
            Enum[] enumArr = (Enum[]) callbacks.getEnumConstants();
            int length = enumArr.length;
            int i = 0;
            while (i < length) {
                Enum enumR = enumArr[i];
                ListenerMethod listenerMethod = (ListenerMethod) callbacks.getField(enumR.name()).getAnnotation(ListenerMethod.class);
                if (listenerMethod != null) {
                    arrayList.add(listenerMethod);
                    i++;
                } else {
                    throw new IllegalStateException(String.format("@%s's %s.%s missing @%s annotation.", new Object[]{callbacks.getEnclosingClass().getSimpleName(), callbacks.getSimpleName(), enumR.name(), ListenerMethod.class.getSimpleName()}));
                }
            }
            return arrayList;
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    private void emitReset(StringBuilder sb) {
        sb.append("  public static void reset(");
        sb.append(this.targetClass);
        sb.append(" target) {\n");
        if (this.parentInjector != null) {
            sb.append("    ");
            sb.append(this.parentInjector);
            sb.append(".reset(target);\n\n");
        }
        for (ViewInjection viewBindings : this.viewIdMap.values()) {
            for (ViewBinding viewBinding : viewBindings.getViewBindings()) {
                sb.append("    target.");
                sb.append(viewBinding.getName());
                sb.append(" = null;\n");
            }
        }
        for (CollectionBinding collectionBinding : this.collectionBindings.keySet()) {
            sb.append("    target.");
            sb.append(collectionBinding.getName());
            sb.append(" = null;\n");
        }
        sb.append("  }\n");
    }

    static void emitCastIfNeeded(StringBuilder sb, String str) {
        emitCastIfNeeded(sb, "android.view.View", str);
    }

    static void emitCastIfNeeded(StringBuilder sb, String str, String str2) {
        if (!str.equals(str2)) {
            sb.append('(');
            sb.append(str2);
            sb.append(") ");
        }
    }

    static void emitHumanDescription(StringBuilder sb, List<Binding> list) {
        switch (list.size()) {
            case 1:
                sb.append(((Binding) list.get(0)).getDescription());
                return;
            case 2:
                sb.append(((Binding) list.get(0)).getDescription());
                sb.append(" and ");
                sb.append(((Binding) list.get(1)).getDescription());
                return;
            default:
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    Binding binding = (Binding) list.get(i);
                    if (i != 0) {
                        sb.append(", ");
                    }
                    if (i == size - 1) {
                        sb.append("and ");
                    }
                    sb.append(binding.getDescription());
                }
                return;
        }
    }
}
