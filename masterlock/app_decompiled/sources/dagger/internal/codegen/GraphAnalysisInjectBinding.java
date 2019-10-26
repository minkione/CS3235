package dagger.internal.codegen;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class GraphAnalysisInjectBinding extends Binding<Object> {
    private final Binding<?>[] bindings;
    private final List<String> keys;
    private final String supertypeKey;
    private final TypeElement type;

    /* renamed from: dagger.internal.codegen.GraphAnalysisInjectBinding$1 */
    static /* synthetic */ class C17761 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                javax.lang.model.element.ElementKind[] r0 = javax.lang.model.element.ElementKind.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$lang$model$element$ElementKind = r0
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x0014 }
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.FIELD     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x001f }
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.CONSTRUCTOR     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.GraphAnalysisInjectBinding.C17761.<clinit>():void");
        }
    }

    private GraphAnalysisInjectBinding(String str, String str2, TypeElement typeElement, List<String> list, String str3) {
        super(str, str2, typeElement.getAnnotation(Singleton.class) != null, typeElement.getQualifiedName().toString());
        this.type = typeElement;
        this.keys = list;
        this.bindings = new Binding[list.size()];
        this.supertypeKey = str3;
    }

    static GraphAnalysisInjectBinding create(TypeElement typeElement, boolean z) {
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        boolean z3 = false;
        for (VariableElement variableElement : typeElement.getEnclosedElements()) {
            switch (C17761.$SwitchMap$javax$lang$model$element$ElementKind[variableElement.getKind().ordinal()]) {
                case 1:
                    if (hasAtInject(variableElement) && !variableElement.getModifiers().contains(Modifier.STATIC)) {
                        arrayList.add(GeneratorKeys.get(variableElement));
                        break;
                    }
                case 2:
                    List<VariableElement> parameters = ((ExecutableElement) variableElement).getParameters();
                    if (!hasAtInject(variableElement)) {
                        if (!parameters.isEmpty()) {
                            break;
                        } else {
                            z3 = true;
                            break;
                        }
                    } else if (hasAtSingleton(variableElement)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Singleton annotations have no effect on constructors. Did you mean to annotate the class? ");
                        sb.append(typeElement.getQualifiedName().toString());
                        throw new IllegalArgumentException(sb.toString());
                    } else if (!z2) {
                        for (VariableElement variableElement2 : parameters) {
                            arrayList.add(GeneratorKeys.get(variableElement2));
                        }
                        z2 = true;
                        break;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Too many injectable constructors on ");
                        sb2.append(typeElement.getQualifiedName().toString());
                        throw new IllegalArgumentException(sb2.toString());
                    }
                default:
                    if (!hasAtInject(variableElement)) {
                        break;
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Unexpected @Inject annotation on ");
                        sb3.append(variableElement);
                        throw new IllegalArgumentException(sb3.toString());
                    }
            }
        }
        if (z2 || !arrayList.isEmpty() || !z) {
            TypeMirror applicationSupertype = Util.getApplicationSupertype(typeElement);
            GraphAnalysisInjectBinding graphAnalysisInjectBinding = new GraphAnalysisInjectBinding((z2 || (z3 && !arrayList.isEmpty())) ? GeneratorKeys.get(typeElement.asType()) : null, GeneratorKeys.rawMembersKey(typeElement.asType()), typeElement, arrayList, applicationSupertype != null ? GeneratorKeys.rawMembersKey(applicationSupertype) : null);
            return graphAnalysisInjectBinding;
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("No injectable members on ");
        sb4.append(typeElement.getQualifiedName().toString());
        sb4.append(". Do you want to add an injectable constructor?");
        throw new IllegalArgumentException(sb4.toString());
    }

    private static boolean hasAtInject(Element element) {
        return element.getAnnotation(Inject.class) != null;
    }

    private static boolean hasAtSingleton(Element element) {
        return element.getAnnotation(Singleton.class) != null;
    }

    public void attach(Linker linker) {
        String obj = this.type.getQualifiedName().toString();
        for (int i = 0; i < this.keys.size(); i++) {
            this.bindings[i] = linker.requestBinding((String) this.keys.get(i), obj, getClass().getClassLoader());
        }
        String str = this.supertypeKey;
        if (str != null) {
            linker.requestBinding(str, obj, getClass().getClassLoader(), false, true);
        }
    }

    public Object get() {
        throw new AssertionError("Compile-time binding should never be called to inject.");
    }

    public void injectMembers(Object obj) {
        throw new AssertionError("Compile-time binding should never be called to inject.");
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        Collections.addAll(set, this.bindings);
    }
}
