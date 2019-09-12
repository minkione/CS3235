package dagger.internal.codegen;

import dagger.Module;
import dagger.Provides;
import dagger.Provides.Type;
import dagger.internal.Binding;
import dagger.internal.Binding.InvalidBindingException;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.Linker.ErrorHandler;
import dagger.internal.ProblemDetector;
import dagger.internal.ProvidesBinding;
import dagger.internal.SetBinding;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Singleton;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes({"dagger.Module"})
public final class GraphAnalysisProcessor extends AbstractProcessor {
    private static final Set<String> ERROR_NAMES_TO_PROPAGATE = new LinkedHashSet(Arrays.asList(new String[]{"com.sun.tools.javac.code.Symbol$CompletionFailure"}));
    private final Set<String> delayedModuleNames = new LinkedHashSet();

    static class ModuleValidationException extends IllegalStateException {
        final Element source;

        public ModuleValidationException(String str, Element element) {
            super(str);
            this.source = element;
        }
    }

    static class ProviderMethodBinding extends ProvidesBinding<Object> {
        private final ExecutableElement method;
        private final Binding<?>[] parameters;

        protected ProviderMethodBinding(String str, ExecutableElement executableElement, boolean z) {
            super(str, executableElement.getAnnotation(Singleton.class) != null, Util.className(executableElement), executableElement.getSimpleName().toString());
            this.method = executableElement;
            this.parameters = new Binding[executableElement.getParameters().size()];
            setLibrary(z);
        }

        public void attach(Linker linker) {
            for (int i = 0; i < this.method.getParameters().size(); i++) {
                this.parameters[i] = linker.requestBinding(GeneratorKeys.get((VariableElement) this.method.getParameters().get(i)), this.method.toString(), getClass().getClassLoader());
            }
        }

        public Object get() {
            throw new AssertionError("Compile-time binding should never be called to inject.");
        }

        public void injectMembers(Object obj) {
            throw new AssertionError("Compile-time binding should never be called to inject.");
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            Collections.addAll(set, this.parameters);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ProvidesBinding[key=");
            sb.append(this.provideKey);
            sb.append(" method=");
            sb.append(this.moduleClass);
            sb.append(".");
            sb.append(this.method.getSimpleName());
            sb.append("()");
            return sb.toString();
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            for (TypeElement typeElement : roundEnvironment.getElementsAnnotatedWith(Module.class)) {
                if (!(typeElement instanceof TypeElement)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("@Module applies to a type, ");
                    sb.append(typeElement.getSimpleName());
                    sb.append(" is a ");
                    sb.append(typeElement.getKind());
                    error(sb.toString(), typeElement);
                } else {
                    this.delayedModuleNames.add(typeElement.getQualifiedName().toString());
                }
            }
            return false;
        }
        LinkedHashSet<TypeElement> linkedHashSet = new LinkedHashSet<>();
        for (String typeElement2 : this.delayedModuleNames) {
            linkedHashSet.add(elements().getTypeElement(typeElement2));
        }
        for (TypeElement typeElement3 : linkedHashSet) {
            try {
                Map annotation = Util.getAnnotation(Module.class, typeElement3);
                TypeElement typeElement4 = typeElement3;
                if (annotation == null) {
                    error("Missing @Module annotation.", typeElement4);
                } else {
                    if (annotation.get("complete").equals(Boolean.TRUE)) {
                        try {
                            Map processCompleteModule = processCompleteModule(typeElement4, false);
                            new ProblemDetector().detectCircularDependencies(processCompleteModule.values());
                            try {
                                writeDotFile(typeElement4, processCompleteModule);
                            } catch (IOException e) {
                                StringWriter stringWriter = new StringWriter();
                                e.printStackTrace(new PrintWriter(stringWriter));
                                Messager messager = this.processingEnv.getMessager();
                                Kind kind = Kind.WARNING;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Graph visualization failed. Please report this as a bug.\n\n");
                                sb2.append(stringWriter);
                                messager.printMessage(kind, sb2.toString(), typeElement4);
                            }
                        } catch (ModuleValidationException e2) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Graph validation failed: ");
                            sb3.append(e2.getMessage());
                            error(sb3.toString(), e2.source);
                        } catch (InvalidBindingException e3) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Graph validation failed: ");
                            sb4.append(e3.getMessage());
                            error(sb4.toString(), elements().getTypeElement(e3.type));
                        } catch (RuntimeException e4) {
                            if (!ERROR_NAMES_TO_PROPAGATE.contains(e4.getClass().getName())) {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Unknown error ");
                                sb5.append(e4.getClass().getName());
                                sb5.append(" thrown by javac in graph validation: ");
                                sb5.append(e4.getMessage());
                                error(sb5.toString(), typeElement4);
                            } else {
                                throw e4;
                            }
                        }
                    }
                    if (annotation.get("library").equals(Boolean.FALSE)) {
                        try {
                            new ProblemDetector().detectUnusedBinding(processCompleteModule(typeElement4, true).values());
                        } catch (IllegalStateException e5) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("Graph validation failed: ");
                            sb6.append(e5.getMessage());
                            error(sb6.toString(), typeElement4);
                        }
                    }
                }
            } catch (CodeGenerationIncompleteException unused) {
            }
        }
        return false;
    }

    private void error(String str, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, str, element);
    }

    private Map<String, Binding<?>> processCompleteModule(TypeElement typeElement, boolean z) {
        Map<String, Binding<?>> linkAll;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        collectIncludesRecursively(typeElement, linkedHashMap, new LinkedList());
        ArrayList arrayList = new ArrayList();
        Linker linker = new Linker(null, new GraphAnalysisLoader(this.processingEnv), z ? ErrorHandler.NULL : new GraphAnalysisErrorHandler(this.processingEnv, typeElement.getQualifiedName().toString()));
        synchronized (linker) {
            BindingsGroup r3 = new BindingsGroup() {
                public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
                    return super.put(str, setBinding);
                }
            };
            BindingsGroup r11 = new BindingsGroup() {
                public Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding) {
                    throw new IllegalStateException("Module overrides cannot contribute set bindings.");
                }
            };
            Iterator it = linkedHashMap.values().iterator();
            while (it.hasNext()) {
                TypeElement typeElement2 = (TypeElement) it.next();
                Map annotation = Util.getAnnotation(Module.class, typeElement2);
                boolean booleanValue = ((Boolean) annotation.get("overrides")).booleanValue();
                boolean booleanValue2 = ((Boolean) annotation.get("library")).booleanValue();
                BindingsGroup bindingsGroup = booleanValue ? r11 : r3;
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                Object[] objArr = (Object[]) annotation.get("injects");
                int length = objArr.length;
                int i = 0;
                while (i < length) {
                    TypeMirror typeMirror = (TypeMirror) objArr[i];
                    Iterator it2 = it;
                    String str = GeneratorKeys.get(typeMirror);
                    linkedHashSet.add(str);
                    if (!Util.isInterface(typeMirror)) {
                        str = GeneratorKeys.rawMembersKey(typeMirror);
                    }
                    int i2 = i;
                    String str2 = str;
                    Object[] objArr2 = objArr;
                    int i3 = length;
                    LinkedHashSet linkedHashSet2 = linkedHashSet;
                    BindingsGroup bindingsGroup2 = r11;
                    BindingsGroup bindingsGroup3 = bindingsGroup;
                    linker.requestBinding(str2, typeElement2.getQualifiedName().toString(), getClass().getClassLoader(), false, true);
                    i = i2 + 1;
                    length = i3;
                    linkedHashSet = linkedHashSet2;
                    bindingsGroup = bindingsGroup3;
                    objArr = objArr2;
                    r11 = bindingsGroup2;
                    it = it2;
                }
                Iterator it3 = it;
                LinkedHashSet linkedHashSet3 = linkedHashSet;
                BindingsGroup bindingsGroup4 = r11;
                BindingsGroup bindingsGroup5 = bindingsGroup;
                for (Object obj : (Object[]) annotation.get("staticInjections")) {
                    arrayList.add(new GraphAnalysisStaticInjection(this.processingEnv.getTypeUtils().asElement((TypeMirror) obj)));
                }
                for (ExecutableElement executableElement : typeElement2.getEnclosedElements()) {
                    Provides provides = (Provides) executableElement.getAnnotation(Provides.class);
                    if (provides != null) {
                        ExecutableElement executableElement2 = executableElement;
                        String str3 = GeneratorKeys.get(executableElement2);
                        ProviderMethodBinding providerMethodBinding = new ProviderMethodBinding(str3, executableElement2, booleanValue2);
                        Binding binding = bindingsGroup5.get(str3);
                        if (binding != null) {
                            if ((provides.type() != Type.SET && provides.type() != Type.SET_VALUES) || !(binding instanceof SetBinding)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Duplicate bindings for ");
                                sb.append(str3);
                                String sb2 = sb.toString();
                                if (booleanValue) {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(sb2);
                                    sb3.append(" in override module(s) - cannot override an override");
                                    sb2 = sb3.toString();
                                }
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(sb2);
                                sb4.append(":\n    ");
                                sb4.append(binding.requiredBy);
                                sb4.append("\n    ");
                                sb4.append(providerMethodBinding.requiredBy);
                                error(sb4.toString(), executableElement2);
                            }
                        }
                        switch (provides.type()) {
                            case UNIQUE:
                                if (linkedHashSet3.contains(providerMethodBinding.provideKey)) {
                                    providerMethodBinding.setDependedOn(true);
                                }
                                try {
                                    bindingsGroup5.contributeProvidesBinding(str3, providerMethodBinding);
                                    break;
                                } catch (IllegalStateException e) {
                                    throw new ModuleValidationException(e.getMessage(), executableElement2);
                                }
                            case SET:
                                SetBinding.add(bindingsGroup5, GeneratorKeys.getSetKey(executableElement2), providerMethodBinding);
                                break;
                            case SET_VALUES:
                                SetBinding.add(bindingsGroup5, str3, providerMethodBinding);
                                break;
                            default:
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Unknown @Provides type ");
                                sb5.append(provides.type());
                                throw new AssertionError(sb5.toString());
                        }
                    }
                }
                it = it3;
                r11 = bindingsGroup4;
            }
            BindingsGroup bindingsGroup6 = r11;
            linker.installBindings(r3);
            linker.installBindings(bindingsGroup6);
            Iterator it4 = arrayList.iterator();
            while (it4.hasNext()) {
                ((GraphAnalysisStaticInjection) it4.next()).attach(linker);
            }
            linkAll = linker.linkAll();
        }
        return linkAll;
    }

    private Elements elements() {
        return this.processingEnv.getElementUtils();
    }

    /* access modifiers changed from: 0000 */
    public void collectIncludesRecursively(TypeElement typeElement, Map<String, TypeElement> map, Deque<String> deque) {
        Map annotation = Util.getAnnotation(Module.class, typeElement);
        if (annotation != null) {
            String obj = typeElement.getQualifiedName().toString();
            if (deque.contains(obj)) {
                StringBuilder sb = new StringBuilder("Module Inclusion Cycle: ");
                if (deque.size() != 1) {
                    String str = obj;
                    int i = 0;
                    while (deque.size() > 0) {
                        String str2 = (String) deque.pop();
                        sb.append("\n");
                        sb.append(i);
                        sb.append(". ");
                        sb.append(str);
                        sb.append(" included by ");
                        sb.append(str2);
                        i++;
                        str = str2;
                    }
                    sb.append("\n0. ");
                    sb.append(obj);
                } else {
                    sb.append(obj);
                    sb.append(" includes itself directly.");
                }
                throw new ModuleValidationException(sb.toString(), typeElement);
            }
            map.put(obj, typeElement);
            Types typeUtils = this.processingEnv.getTypeUtils();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList((Object[]) annotation.get("includes")));
            if (!annotation.get("addsTo").equals(Void.class)) {
                arrayList.add(annotation.get("addsTo"));
            }
            for (Object next : arrayList) {
                if (!(next instanceof TypeMirror)) {
                    Messager messager = this.processingEnv.getMessager();
                    Kind kind = Kind.WARNING;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected value for include: ");
                    sb2.append(next);
                    sb2.append(" in ");
                    sb2.append(typeElement);
                    messager.printMessage(kind, sb2.toString(), typeElement);
                } else {
                    TypeElement asElement = typeUtils.asElement((TypeMirror) next);
                    deque.push(obj);
                    collectIncludesRecursively(asElement, map, deque);
                    deque.pop();
                }
            }
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("No @Module on ");
        sb3.append(typeElement);
        throw new ModuleValidationException(sb3.toString(), typeElement);
    }

    /* access modifiers changed from: 0000 */
    public void writeDotFile(TypeElement typeElement, Map<String, Binding<?>> map) throws IOException {
        StandardLocation standardLocation = StandardLocation.SOURCE_OUTPUT;
        String obj = Util.getPackage(typeElement).getQualifiedName().toString();
        StringBuilder sb = new StringBuilder();
        sb.append(typeElement.getQualifiedName().toString().substring(obj.length() + 1));
        sb.append(".dot");
        String sb2 = sb.toString();
        GraphVizWriter graphVizWriter = new GraphVizWriter(this.processingEnv.getFiler().createResource(standardLocation, obj, sb2, new Element[]{typeElement}).openWriter());
        new GraphVisualizer().write(map, graphVizWriter);
        graphVizWriter.close();
    }
}
