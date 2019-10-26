package dagger.internal.codegen;

import com.squareup.javawriter.JavaWriter;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Provides.Type;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import dagger.internal.SetBinding;
import dagger.internal.loaders.GeneratedAdapters;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"*"})
public final class ModuleAdapterProcessor extends AbstractProcessor {
    private static final String BINDINGS_MAP = JavaWriter.type(BindingsGroup.class, new String[0]);
    private static final List<String> INVALID_RETURN_TYPES = Arrays.asList(new String[]{Provider.class.getCanonicalName(), Lazy.class.getCanonicalName()});
    private final LinkedHashMap<String, List<ExecutableElement>> remainingTypes = new LinkedHashMap<>();

    /* renamed from: dagger.internal.codegen.ModuleAdapterProcessor$1 */
    static /* synthetic */ class C17821 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(11:0|1|2|3|4|5|6|7|9|10|12) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                dagger.Provides$Type[] r0 = dagger.Provides.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$dagger$Provides$Type = r0
                r0 = 1
                int[] r1 = $SwitchMap$dagger$Provides$Type     // Catch:{ NoSuchFieldError -> 0x0014 }
                dagger.Provides$Type r2 = dagger.Provides.Type.UNIQUE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r1 = $SwitchMap$dagger$Provides$Type     // Catch:{ NoSuchFieldError -> 0x001f }
                dagger.Provides$Type r2 = dagger.Provides.Type.SET     // Catch:{ NoSuchFieldError -> 0x001f }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r3 = 2
                r1[r2] = r3     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r1 = $SwitchMap$dagger$Provides$Type     // Catch:{ NoSuchFieldError -> 0x002a }
                dagger.Provides$Type r2 = dagger.Provides.Type.SET_VALUES     // Catch:{ NoSuchFieldError -> 0x002a }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3 = 3
                r1[r2] = r3     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                javax.lang.model.element.ElementKind[] r1 = javax.lang.model.element.ElementKind.values()
                int r1 = r1.length
                int[] r1 = new int[r1]
                $SwitchMap$javax$lang$model$element$ElementKind = r1
                int[] r1 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x003d }
                javax.lang.model.element.ElementKind r2 = javax.lang.model.element.ElementKind.CLASS     // Catch:{ NoSuchFieldError -> 0x003d }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.ModuleAdapterProcessor.C17821.<clinit>():void");
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        this.remainingTypes.putAll(providerMethodsByClass(roundEnvironment));
        Iterator it = this.remainingTypes.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(str);
            List list = (List) this.remainingTypes.get(str);
            try {
                Map annotation = Util.getAnnotation(Module.class, typeElement);
                StringWriter stringWriter = new StringWriter();
                String adapterName = Util.adapterName(typeElement, GeneratedAdapters.MODULE_ADAPTER_SUFFIX);
                generateModuleAdapter(stringWriter, adapterName, typeElement, annotation, list);
                Writer openWriter = this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{typeElement}).openWriter();
                openWriter.append(stringWriter.getBuffer());
                openWriter.close();
            } catch (CodeGenerationIncompleteException unused) {
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Code gen failed: ");
                sb.append(e);
                error(sb.toString(), typeElement);
            }
            it.remove();
        }
        if (roundEnvironment.processingOver() && this.remainingTypes.size() > 0) {
            Messager messager = this.processingEnv.getMessager();
            Kind kind = Kind.ERROR;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not find types required by provides methods for ");
            sb2.append(this.remainingTypes.keySet());
            messager.printMessage(kind, sb2.toString());
        }
        return false;
    }

    private void error(String str, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, str, element);
    }

    private Map<String, List<ExecutableElement>> providerMethodsByClass(RoundEnvironment roundEnvironment) {
        Elements elementUtils = this.processingEnv.getElementUtils();
        Types typeUtils = this.processingEnv.getTypeUtils();
        HashMap hashMap = new HashMap();
        for (Element element : findProvidesMethods(roundEnvironment)) {
            if (C17821.$SwitchMap$javax$lang$model$element$ElementKind[element.getEnclosingElement().getKind().ordinal()] != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected @Provides on ");
                sb.append(Util.elementToString(element));
                error(sb.toString(), element);
            } else {
                TypeElement enclosingElement = element.getEnclosingElement();
                Set modifiers = enclosingElement.getModifiers();
                if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.ABSTRACT)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Classes declaring @Provides methods must not be private or abstract: ");
                    sb2.append(enclosingElement.getQualifiedName());
                    error(sb2.toString(), enclosingElement);
                } else {
                    Set modifiers2 = element.getModifiers();
                    if (modifiers2.contains(Modifier.PRIVATE) || modifiers2.contains(Modifier.ABSTRACT) || modifiers2.contains(Modifier.STATIC)) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("@Provides methods must not be private, abstract or static: ");
                        sb3.append(enclosingElement.getQualifiedName());
                        sb3.append(".");
                        sb3.append(element);
                        error(sb3.toString(), element);
                    } else {
                        ExecutableElement executableElement = (ExecutableElement) element;
                        if (!executableElement.getThrownTypes().isEmpty()) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("@Provides methods must not have a throws clause: ");
                            sb4.append(enclosingElement.getQualifiedName());
                            sb4.append(".");
                            sb4.append(element);
                            error(sb4.toString(), element);
                        } else {
                            TypeMirror erasure = typeUtils.erasure(executableElement.getReturnType());
                            if (!erasure.getKind().equals(TypeKind.ERROR)) {
                                Iterator it = INVALID_RETURN_TYPES.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    TypeElement typeElement = elementUtils.getTypeElement((String) it.next());
                                    if (typeElement != null && typeUtils.isSameType(erasure, typeUtils.erasure(typeElement.asType()))) {
                                        error(String.format("@Provides method must not return %s directly: %s.%s", new Object[]{typeElement, enclosingElement.getQualifiedName(), element}), element);
                                        break;
                                    }
                                }
                            }
                            List list = (List) hashMap.get(enclosingElement.getQualifiedName().toString());
                            if (list == null) {
                                list = new ArrayList();
                                hashMap.put(enclosingElement.getQualifiedName().toString(), list);
                            }
                            list.add(executableElement);
                        }
                    }
                }
            }
        }
        TypeMirror asType = elementUtils.getTypeElement("java.lang.Object").asType();
        for (Element element2 : roundEnvironment.getElementsAnnotatedWith(Module.class)) {
            if (!element2.getKind().equals(ElementKind.CLASS)) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Modules must be classes: ");
                sb5.append(Util.elementToString(element2));
                error(sb5.toString(), element2);
            } else {
                TypeElement typeElement2 = (TypeElement) element2;
                if (!typeElement2.getSuperclass().equals(asType)) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Modules must not extend from other classes: ");
                    sb6.append(Util.elementToString(element2));
                    error(sb6.toString(), element2);
                }
                String obj = typeElement2.getQualifiedName().toString();
                if (!hashMap.containsKey(obj)) {
                    hashMap.put(obj, new ArrayList());
                }
            }
        }
        return hashMap;
    }

    private Set<? extends Element> findProvidesMethods(RoundEnvironment roundEnvironment) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(roundEnvironment.getElementsAnnotatedWith(Provides.class));
        return linkedHashSet;
    }

    private void generateModuleAdapter(Writer writer, String str, TypeElement typeElement, Map<String, Object> map, List<ExecutableElement> list) throws IOException {
        Provides provides;
        TypeElement typeElement2 = typeElement;
        Map<String, Object> map2 = map;
        List<ExecutableElement> list2 = list;
        if (map2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(typeElement2);
            sb.append(" has @Provides methods but no @Module annotation");
            error(sb.toString(), typeElement2);
            return;
        }
        Object[] objArr = (Object[]) map2.get("staticInjections");
        Object[] objArr2 = (Object[]) map2.get("injects");
        Object[] objArr3 = (Object[]) map2.get("includes");
        boolean booleanValue = ((Boolean) map2.get("overrides")).booleanValue();
        boolean booleanValue2 = ((Boolean) map2.get("complete")).booleanValue();
        boolean booleanValue3 = ((Boolean) map2.get("library")).booleanValue();
        JavaWriter javaWriter = new JavaWriter(writer);
        boolean checkForMultibindings = checkForMultibindings(list2);
        boolean checkForDependencies = checkForDependencies(list2);
        javaWriter.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        javaWriter.emitPackage(Util.getPackage(typeElement).getQualifiedName().toString());
        javaWriter.emitImports((Collection<String>) findImports(checkForMultibindings, !list.isEmpty(), checkForDependencies));
        String obj = typeElement.getQualifiedName().toString();
        javaWriter.emitEmptyLine();
        javaWriter.emitJavadoc("A manager of modules and provides adapters allowing for proper linking and\ninstance provision of types served by {@code @Provides} methods.", new Object[0]);
        String str2 = obj;
        boolean z = booleanValue3;
        javaWriter.beginType(str, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), JavaWriter.type(ModuleAdapter.class, obj), new String[0]);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("{ ");
        for (Object obj2 : objArr2) {
            TypeMirror typeMirror = (TypeMirror) obj2;
            sb2.append(JavaWriter.stringLiteral(Util.isInterface(typeMirror) ? GeneratorKeys.get(typeMirror) : GeneratorKeys.rawMembersKey(typeMirror)));
            sb2.append(", ");
        }
        sb2.append("}");
        javaWriter.emitField("String[]", "INJECTS", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("{ ");
        for (Object obj3 : objArr) {
            sb3.append(Util.typeToString((TypeMirror) obj3));
            sb3.append(".class, ");
        }
        sb3.append("}");
        javaWriter.emitField("Class<?>[]", "STATIC_INJECTIONS", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("{ ");
        for (Object obj4 : objArr3) {
            if (!(obj4 instanceof TypeMirror)) {
                Messager messager = this.processingEnv.getMessager();
                Kind kind = Kind.WARNING;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Unexpected value: ");
                sb5.append(obj4);
                sb5.append(" in includes of ");
                sb5.append(typeElement2);
                messager.printMessage(kind, sb5.toString(), typeElement2);
            } else {
                sb4.append(Util.typeToString((TypeMirror) obj4));
                sb4.append(".class, ");
            }
        }
        sb4.append("}");
        javaWriter.emitField("Class<?>[]", "INCLUDES", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), sb4.toString());
        javaWriter.emitEmptyLine();
        javaWriter.beginMethod(null, str, EnumSet.of(Modifier.PUBLIC), new String[0]);
        javaWriter.emitStatement("super(%s.class, INJECTS, STATIC_INJECTIONS, %s /*overrides*/, INCLUDES, %s /*complete*/, %s /*library*/)", str2, Boolean.valueOf(booleanValue), Boolean.valueOf(booleanValue2), Boolean.valueOf(z));
        javaWriter.endMethod();
        ExecutableElement noArgsConstructor = Util.getNoArgsConstructor(typeElement);
        if (noArgsConstructor != null && Util.isCallableConstructor(noArgsConstructor)) {
            javaWriter.emitEmptyLine();
            javaWriter.emitAnnotation(Override.class);
            javaWriter.beginMethod(str2, "newModule", EnumSet.of(Modifier.PUBLIC), new String[0]);
            javaWriter.emitStatement("return new %s()", str2);
            javaWriter.endMethod();
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        if (!list.isEmpty()) {
            javaWriter.emitEmptyLine();
            javaWriter.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
            javaWriter.emitAnnotation(Override.class);
            javaWriter.beginMethod("void", "getBindings", EnumSet.of(Modifier.PUBLIC), BINDINGS_MAP, "bindings", str2, "module");
            for (ExecutableElement executableElement : list) {
                switch (((Provides) executableElement.getAnnotation(Provides.class)).type()) {
                    case UNIQUE:
                        javaWriter.emitStatement("bindings.contributeProvidesBinding(%s, new %s(module))", JavaWriter.stringLiteral(GeneratorKeys.get(executableElement)), bindingClassName(executableElement, linkedHashMap, linkedHashMap2));
                        break;
                    case SET:
                        javaWriter.emitStatement("SetBinding.add(bindings, %s, new %s(module))", JavaWriter.stringLiteral(GeneratorKeys.getSetKey(executableElement)), bindingClassName(executableElement, linkedHashMap, linkedHashMap2));
                        break;
                    case SET_VALUES:
                        javaWriter.emitStatement("SetBinding.add(bindings, %s, new %s(module))", JavaWriter.stringLiteral(GeneratorKeys.get(executableElement)), bindingClassName(executableElement, linkedHashMap, linkedHashMap2));
                        break;
                    default:
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Unknown @Provides type ");
                        sb6.append(provides.type());
                        throw new AssertionError(sb6.toString());
                }
            }
            javaWriter.endMethod();
        }
        for (ExecutableElement generateProvidesAdapter : list) {
            JavaWriter javaWriter2 = javaWriter;
            generateProvidesAdapter(javaWriter, generateProvidesAdapter, linkedHashMap, linkedHashMap2, z);
        }
        JavaWriter javaWriter3 = javaWriter;
        javaWriter3.endType();
        javaWriter3.close();
    }

    private Set<String> findImports(boolean z, boolean z2, boolean z3) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(ModuleAdapter.class.getCanonicalName());
        if (z2) {
            linkedHashSet.add(BindingsGroup.class.getCanonicalName());
            linkedHashSet.add(Provider.class.getCanonicalName());
            linkedHashSet.add(ProvidesBinding.class.getCanonicalName());
        }
        if (z3) {
            linkedHashSet.add(Linker.class.getCanonicalName());
            linkedHashSet.add(Set.class.getCanonicalName());
            linkedHashSet.add(Binding.class.getCanonicalName());
        }
        if (z) {
            linkedHashSet.add(SetBinding.class.getCanonicalName());
        }
        return linkedHashSet;
    }

    private boolean checkForDependencies(List<ExecutableElement> list) {
        for (ExecutableElement parameters : list) {
            if (!parameters.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForMultibindings(List<ExecutableElement> list) {
        for (ExecutableElement annotation : list) {
            Type type = ((Provides) annotation.getAnnotation(Provides.class)).type();
            if (type != Type.SET) {
                if (type == Type.SET_VALUES) {
                }
            }
            return true;
        }
        return false;
    }

    private String bindingClassName(ExecutableElement executableElement, Map<ExecutableElement, String> map, Map<String, AtomicInteger> map2) {
        String str = (String) map.get(executableElement);
        if (str != null) {
            return str;
        }
        String obj = executableElement.getSimpleName().toString();
        String str2 = "";
        AtomicInteger atomicInteger = (AtomicInteger) map2.get(obj);
        if (atomicInteger == null) {
            map2.put(obj, new AtomicInteger(2));
        } else {
            str2 = atomicInteger.toString();
            atomicInteger.incrementAndGet();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(obj.charAt(0)));
        sb.append(obj.substring(1));
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("ProvidesAdapter");
        sb3.append(str2);
        String sb4 = sb3.toString();
        map.put(executableElement, sb4);
        return sb4;
    }

    private void generateProvidesAdapter(JavaWriter javaWriter, ExecutableElement executableElement, Map<ExecutableElement, String> map, Map<String, AtomicInteger> map2, boolean z) throws IOException {
        JavaWriter javaWriter2 = javaWriter;
        ExecutableElement executableElement2 = executableElement;
        String obj = executableElement.getSimpleName().toString();
        String typeToString = Util.typeToString(executableElement.getEnclosingElement().asType());
        String bindingClassName = bindingClassName(executableElement2, map, map2);
        String typeToString2 = Util.typeToString(executableElement.getReturnType());
        List<Element> parameters = executableElement.getParameters();
        boolean z2 = !parameters.isEmpty();
        javaWriter.emitEmptyLine();
        javaWriter2.emitJavadoc(AdapterJavadocs.bindingTypeDocs(typeToString2, false, false, z2), new Object[0]);
        boolean z3 = z2;
        javaWriter.beginType(bindingClassName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), JavaWriter.type(ProvidesBinding.class, typeToString2), JavaWriter.type(Provider.class, typeToString2));
        javaWriter2.emitField(typeToString, "module", EnumSet.of(Modifier.PRIVATE, Modifier.FINAL));
        for (Element element : parameters) {
            javaWriter2.emitField(JavaWriter.type(Binding.class, Util.typeToString(element.asType())), parameterName(element), EnumSet.of(Modifier.PRIVATE));
        }
        javaWriter.emitEmptyLine();
        javaWriter2.beginMethod(null, bindingClassName, EnumSet.of(Modifier.PUBLIC), typeToString, "module");
        boolean z4 = executableElement2.getAnnotation(Singleton.class) != null;
        String str = "super(%s, %s, %s, %s)";
        int i = 4;
        Object[] objArr = new Object[4];
        objArr[0] = JavaWriter.stringLiteral(GeneratorKeys.get(executableElement));
        objArr[1] = z4 ? "IS_SINGLETON" : "NOT_SINGLETON";
        objArr[2] = JavaWriter.stringLiteral(typeToString);
        objArr[3] = JavaWriter.stringLiteral(obj);
        javaWriter2.emitStatement(str, objArr);
        javaWriter2.emitStatement("this.module = module", new Object[0]);
        javaWriter2.emitStatement("setLibrary(%s)", Boolean.valueOf(z));
        javaWriter.endMethod();
        if (z3) {
            javaWriter.emitEmptyLine();
            javaWriter2.emitJavadoc("Used internally to link bindings/providers together at run time\naccording to their dependency graph.", new Object[0]);
            javaWriter2.emitAnnotation(Override.class);
            javaWriter2.emitAnnotation(SuppressWarnings.class, (Object) JavaWriter.stringLiteral("unchecked"));
            javaWriter2.beginMethod("void", "attach", EnumSet.of(Modifier.PUBLIC), Linker.class.getCanonicalName(), "linker");
            Iterator it = parameters.iterator();
            while (it.hasNext()) {
                VariableElement variableElement = (VariableElement) it.next();
                String str2 = GeneratorKeys.get(variableElement);
                Object[] objArr2 = new Object[i];
                objArr2[0] = parameterName(variableElement);
                objArr2[1] = javaWriter2.compressType(JavaWriter.type(Binding.class, Util.typeToString(variableElement.asType())));
                objArr2[2] = JavaWriter.stringLiteral(str2);
                objArr2[3] = javaWriter2.compressType(typeToString);
                javaWriter2.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", objArr2);
                i = 4;
            }
            javaWriter.endMethod();
            javaWriter.emitEmptyLine();
            javaWriter2.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
            javaWriter2.emitAnnotation(Override.class);
            String type = JavaWriter.type(Set.class, "Binding<?>");
            javaWriter2.beginMethod("void", "getDependencies", EnumSet.of(Modifier.PUBLIC), type, "getBindings", type, "injectMembersBindings");
            for (Element parameterName : parameters) {
                javaWriter2.emitStatement("getBindings.add(%s)", parameterName(parameterName));
            }
            javaWriter.endMethod();
        }
        javaWriter.emitEmptyLine();
        javaWriter2.emitJavadoc("Returns the fully provisioned instance satisfying the contract for\n{@code Provider<%s>}.", typeToString2);
        javaWriter2.emitAnnotation(Override.class);
        javaWriter2.beginMethod(typeToString2, "get", EnumSet.of(Modifier.PUBLIC), new String[0]);
        StringBuilder sb = new StringBuilder();
        boolean z5 = true;
        for (Element element2 : parameters) {
            if (!z5) {
                sb.append(", ");
            } else {
                z5 = false;
            }
            sb.append(String.format("%s.get()", new Object[]{parameterName(element2)}));
        }
        javaWriter2.emitStatement("return module.%s(%s)", obj, sb.toString());
        javaWriter.endMethod();
        javaWriter.endType();
    }

    private String parameterName(Element element) {
        if (!element.getSimpleName().contentEquals("module")) {
            return element.getSimpleName().toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parameter_");
        sb.append(element.getSimpleName().toString());
        return sb.toString();
    }
}
