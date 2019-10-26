package dagger.internal.codegen;

import com.squareup.javawriter.JavaWriter;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import dagger.internal.loaders.GeneratedAdapters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
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
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"javax.inject.Inject"})
public final class InjectAdapterProcessor extends AbstractProcessor {
    private final Set<String> remainingTypeNames = new LinkedHashSet();

    /* renamed from: dagger.internal.codegen.InjectAdapterProcessor$1 */
    static /* synthetic */ class C17811 {
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
            throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.InjectAdapterProcessor.C17811.<clinit>():void");
        }
    }

    static class InjectedClass {
        final ExecutableElement constructor;
        final List<Element> fields;
        final List<Element> staticFields;
        final TypeElement type;

        InjectedClass(TypeElement typeElement, List<Element> list, ExecutableElement executableElement, List<Element> list2) {
            this.type = typeElement;
            this.staticFields = list;
            this.constructor = executableElement;
            this.fields = list2;
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        this.remainingTypeNames.addAll(findInjectedClassNames(roundEnvironment));
        Iterator it = this.remainingTypeNames.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            InjectedClass createInjectedClass = createInjectedClass((String) it.next());
            if (!allTypesExist(createInjectedClass.fields) || ((createInjectedClass.constructor != null && !allTypesExist(createInjectedClass.constructor.getParameters())) || !allTypesExist(createInjectedClass.staticFields))) {
                z = true;
            }
            if (!z) {
                try {
                    generateInjectionsForClass(createInjectedClass);
                } catch (IOException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Code gen failed: ");
                    sb.append(e);
                    error(sb.toString(), createInjectedClass.type);
                }
                it.remove();
            }
        }
        if (roundEnvironment.processingOver() && !this.remainingTypeNames.isEmpty()) {
            Messager messager = this.processingEnv.getMessager();
            Kind kind = Kind.ERROR;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not find injection type required by ");
            sb2.append(this.remainingTypeNames);
            messager.printMessage(kind, sb2.toString());
        }
        return false;
    }

    private void generateInjectionsForClass(InjectedClass injectedClass) throws IOException {
        if (injectedClass.constructor != null || !injectedClass.fields.isEmpty()) {
            generateInjectAdapter(injectedClass.type, injectedClass.constructor, injectedClass.fields);
        }
        if (!injectedClass.staticFields.isEmpty()) {
            generateStaticInjection(injectedClass.type, injectedClass.staticFields);
        }
    }

    private boolean allTypesExist(Collection<? extends Element> collection) {
        for (Element asType : collection) {
            if (asType.asType().getKind() == TypeKind.ERROR) {
                return false;
            }
        }
        return true;
    }

    private Set<String> findInjectedClassNames(RoundEnvironment roundEnvironment) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Inject.class)) {
            if (validateInjectable(element)) {
                linkedHashSet.add(Util.rawTypeToString(element.getEnclosingElement().asType(), '.'));
            }
        }
        return linkedHashSet;
    }

    private boolean validateInjectable(Element element) {
        Element enclosingElement = element.getEnclosingElement();
        if (element.getKind() == ElementKind.CLASS) {
            StringBuilder sb = new StringBuilder();
            sb.append("@Inject is not valid on a class: ");
            sb.append(Util.elementToString(element));
            error(sb.toString(), element);
            return false;
        } else if (element.getKind() == ElementKind.METHOD) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Method injection is not supported: ");
            sb2.append(Util.elementToString(element));
            error(sb2.toString(), element);
            return false;
        } else if (element.getKind() == ElementKind.FIELD && element.getModifiers().contains(Modifier.FINAL)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Can't inject a final field: ");
            sb3.append(Util.elementToString(element));
            error(sb3.toString(), element);
            return false;
        } else if (element.getKind() == ElementKind.FIELD && element.getModifiers().contains(Modifier.PRIVATE)) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Can't inject a private field: ");
            sb4.append(Util.elementToString(element));
            error(sb4.toString(), element);
            return false;
        } else if (element.getKind() != ElementKind.CONSTRUCTOR || !element.getModifiers().contains(Modifier.PRIVATE)) {
            ElementKind kind = enclosingElement.getEnclosingElement().getKind();
            boolean z = kind.isClass() || kind.isInterface();
            boolean contains = enclosingElement.getModifiers().contains(Modifier.STATIC);
            if (!z || contains) {
                return true;
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Can't inject a non-static inner class: ");
            sb5.append(Util.elementToString(element));
            error(sb5.toString(), enclosingElement);
            return false;
        } else {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Can't inject a private constructor: ");
            sb6.append(Util.elementToString(element));
            error(sb6.toString(), element);
            return false;
        }
    }

    private InjectedClass createInjectedClass(String str) {
        ExecutableElement executableElement;
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(str);
        boolean contains = typeElement.getModifiers().contains(Modifier.ABSTRACT);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ExecutableElement executableElement2 = null;
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getAnnotation(Inject.class) != null) {
                switch (C17811.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
                    case 1:
                        if (!element.getModifiers().contains(Modifier.STATIC)) {
                            arrayList2.add(element);
                            break;
                        } else {
                            arrayList.add(element);
                            break;
                        }
                    case 2:
                        if (executableElement2 != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Too many injectable constructors on ");
                            sb.append(typeElement.getQualifiedName());
                            error(sb.toString(), element);
                        } else if (contains) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Abstract class ");
                            sb2.append(typeElement.getQualifiedName());
                            sb2.append(" must not have an @Inject-annotated constructor.");
                            error(sb2.toString(), element);
                        }
                        executableElement2 = (ExecutableElement) element;
                        break;
                    default:
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Cannot inject ");
                        sb3.append(Util.elementToString(element));
                        error(sb3.toString(), element);
                        break;
                }
            }
        }
        if (executableElement2 != null || contains) {
            executableElement = executableElement2;
        } else {
            executableElement = Util.getNoArgsConstructor(typeElement);
            if (executableElement != null && !Util.isCallableConstructor(executableElement)) {
                executableElement = null;
            }
        }
        return new InjectedClass(typeElement, arrayList, executableElement, arrayList2);
    }

    private void generateInjectAdapter(TypeElement typeElement, ExecutableElement executableElement, List<Element> list) throws IOException {
        JavaWriter javaWriter;
        TypeElement typeElement2 = typeElement;
        ExecutableElement executableElement2 = executableElement;
        String obj = Util.getPackage(typeElement).getQualifiedName().toString();
        String strippedTypeName = strippedTypeName(typeElement.getQualifiedName().toString(), obj);
        TypeMirror applicationSupertype = Util.getApplicationSupertype(typeElement);
        String adapterName = Util.adapterName(typeElement2, GeneratedAdapters.INJECT_ADAPTER_SUFFIX);
        boolean z = true;
        JavaWriter javaWriter2 = new JavaWriter(this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{typeElement2}).openWriter());
        boolean contains = typeElement.getModifiers().contains(Modifier.ABSTRACT);
        boolean z2 = !list.isEmpty() || applicationSupertype != null;
        boolean z3 = !list.isEmpty() && executableElement2 != null && !executableElement.getParameters().isEmpty();
        boolean z4 = z2 || (executableElement2 != null && !executableElement.getParameters().isEmpty());
        javaWriter2.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        javaWriter2.emitPackage(obj);
        javaWriter2.emitImports((Collection<String>) findImports(z4, z2, executableElement2 != null));
        javaWriter2.emitEmptyLine();
        javaWriter2.emitJavadoc(AdapterJavadocs.bindingTypeDocs(strippedTypeName, contains, z2, z4), new Object[0]);
        String str = "class";
        EnumSet of = EnumSet.of(Modifier.PUBLIC, Modifier.FINAL);
        String type = JavaWriter.type(Binding.class, strippedTypeName);
        if (executableElement2 == null) {
            z = false;
        }
        String[] implementedInterfaces = implementedInterfaces(strippedTypeName, z2, z);
        boolean z5 = z4;
        boolean z6 = z3;
        javaWriter2.beginType(adapterName, str, of, type, implementedInterfaces);
        writeMemberBindingsFields(javaWriter2, list, z6);
        if (executableElement2 != null) {
            writeParameterBindingsFields(javaWriter2, executableElement2, z6);
        }
        if (applicationSupertype != null) {
            writeSupertypeInjectorField(javaWriter2, applicationSupertype);
        }
        javaWriter2.emitEmptyLine();
        boolean z7 = z6;
        boolean z8 = z2;
        writeInjectAdapterConstructor(javaWriter2, executableElement, typeElement, strippedTypeName, adapterName);
        if (z5) {
            ExecutableElement executableElement3 = executableElement;
            List<Element> list2 = list;
            boolean z9 = z7;
            javaWriter = javaWriter2;
            writeAttachMethod(javaWriter2, executableElement3, list2, z9, strippedTypeName, applicationSupertype, true);
            writeGetDependenciesMethod(javaWriter, executableElement3, list2, z9, applicationSupertype, true);
        } else {
            javaWriter = javaWriter2;
        }
        if (executableElement2 != null) {
            writeGetMethod(javaWriter, executableElement, z7, z8, strippedTypeName);
        }
        if (z8) {
            writeMembersInjectMethod(javaWriter, list, z7, strippedTypeName, applicationSupertype);
        }
        javaWriter.endType();
        javaWriter.close();
    }

    private void generateStaticInjection(TypeElement typeElement, List<Element> list) throws IOException {
        String obj = typeElement.getQualifiedName().toString();
        String adapterName = Util.adapterName(typeElement, GeneratedAdapters.STATIC_INJECTION_SUFFIX);
        JavaWriter javaWriter = new JavaWriter(this.processingEnv.getFiler().createSourceFile(adapterName, new Element[]{typeElement}).openWriter());
        javaWriter.emitSingleLineComment("Code generated by dagger-compiler.  Do not edit.", new Object[0]);
        javaWriter.emitPackage(Util.getPackage(typeElement).getQualifiedName().toString());
        javaWriter.emitImports((Collection<String>) Arrays.asList(new String[]{StaticInjection.class.getName(), Binding.class.getName(), Linker.class.getName()}));
        javaWriter.emitEmptyLine();
        javaWriter.emitJavadoc("A manager for {@code %s}'s injections into static fields.", typeElement.getSimpleName());
        JavaWriter javaWriter2 = javaWriter;
        javaWriter2.beginType(adapterName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), StaticInjection.class.getSimpleName(), new String[0]);
        writeMemberBindingsFields(javaWriter, list, false);
        javaWriter.emitEmptyLine();
        writeAttachMethod(javaWriter2, null, list, false, obj, null, true);
        writeStaticInjectMethod(javaWriter, list, obj);
        javaWriter.endType();
        javaWriter.close();
    }

    private void writeMemberBindingsFields(JavaWriter javaWriter, List<Element> list, boolean z) throws IOException {
        for (Element element : list) {
            javaWriter.emitField(JavaWriter.type(Binding.class, Util.typeToString(element.asType())), fieldName(z, element), EnumSet.of(Modifier.PRIVATE));
        }
    }

    private void writeParameterBindingsFields(JavaWriter javaWriter, ExecutableElement executableElement, boolean z) throws IOException {
        for (VariableElement variableElement : executableElement.getParameters()) {
            javaWriter.emitField(JavaWriter.type(Binding.class, Util.typeToString(variableElement.asType())), parameterName(z, variableElement), EnumSet.of(Modifier.PRIVATE));
        }
    }

    private void writeSupertypeInjectorField(JavaWriter javaWriter, TypeMirror typeMirror) throws IOException {
        javaWriter.emitField(JavaWriter.type(Binding.class, Util.rawTypeToString(typeMirror, '.')), "supertype", EnumSet.of(Modifier.PRIVATE));
    }

    private void writeInjectAdapterConstructor(JavaWriter javaWriter, ExecutableElement executableElement, TypeElement typeElement, String str, String str2) throws IOException {
        String str3 = null;
        javaWriter.beginMethod(null, str2, EnumSet.of(Modifier.PUBLIC), new String[0]);
        if (executableElement != null) {
            str3 = JavaWriter.stringLiteral(GeneratorKeys.get(typeElement.asType()));
        }
        String stringLiteral = JavaWriter.stringLiteral(GeneratorKeys.rawMembersKey(typeElement.asType()));
        boolean z = typeElement.getAnnotation(Singleton.class) != null;
        String str4 = "super(%s, %s, %s, %s.class)";
        Object[] objArr = new Object[4];
        objArr[0] = str3;
        objArr[1] = stringLiteral;
        objArr[2] = z ? "IS_SINGLETON" : "NOT_SINGLETON";
        objArr[3] = str;
        javaWriter.emitStatement(str4, objArr);
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private void writeAttachMethod(JavaWriter javaWriter, ExecutableElement executableElement, List<Element> list, boolean z, String str, TypeMirror typeMirror, boolean z2) throws IOException {
        JavaWriter javaWriter2 = javaWriter;
        boolean z3 = z;
        TypeMirror typeMirror2 = typeMirror;
        javaWriter2.emitJavadoc("Used internally to link bindings/providers together at run time\naccording to their dependency graph.", new Object[0]);
        if (z2) {
            javaWriter2.emitAnnotation(Override.class);
        }
        javaWriter2.emitAnnotation(SuppressWarnings.class, (Object) JavaWriter.stringLiteral("unchecked"));
        javaWriter2.beginMethod("void", "attach", EnumSet.of(Modifier.PUBLIC), Linker.class.getCanonicalName(), "linker");
        if (executableElement != null) {
            for (VariableElement variableElement : executableElement.getParameters()) {
                javaWriter2.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", parameterName(z3, variableElement), javaWriter2.compressType(JavaWriter.type(Binding.class, Util.typeToString(variableElement.asType()))), JavaWriter.stringLiteral(GeneratorKeys.get(variableElement)), str);
            }
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            VariableElement variableElement2 = (Element) it.next();
            javaWriter2.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader())", fieldName(z3, variableElement2), javaWriter2.compressType(JavaWriter.type(Binding.class, Util.typeToString(variableElement2.asType()))), JavaWriter.stringLiteral(GeneratorKeys.get(variableElement2)), str);
        }
        if (typeMirror2 != null) {
            javaWriter2.emitStatement("%s = (%s) linker.requestBinding(%s, %s.class, getClass().getClassLoader(), false, true)", "supertype", javaWriter2.compressType(JavaWriter.type(Binding.class, Util.rawTypeToString(typeMirror2, '.'))), JavaWriter.stringLiteral(GeneratorKeys.rawMembersKey(typeMirror)), str);
        }
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private void writeGetDependenciesMethod(JavaWriter javaWriter, ExecutableElement executableElement, List<Element> list, boolean z, TypeMirror typeMirror, boolean z2) throws IOException {
        javaWriter.emitJavadoc("Used internally obtain dependency information, such as for cyclical\ngraph detection.", new Object[0]);
        if (z2) {
            javaWriter.emitAnnotation(Override.class);
        }
        String type = JavaWriter.type(Set.class, "Binding<?>");
        javaWriter.beginMethod("void", "getDependencies", EnumSet.of(Modifier.PUBLIC), type, "getBindings", type, "injectMembersBindings");
        if (executableElement != null) {
            for (Element parameterName : executableElement.getParameters()) {
                javaWriter.emitStatement("getBindings.add(%s)", parameterName(z, parameterName));
            }
        }
        for (Element fieldName : list) {
            javaWriter.emitStatement("injectMembersBindings.add(%s)", fieldName(z, fieldName));
        }
        if (typeMirror != null) {
            javaWriter.emitStatement("injectMembersBindings.add(%s)", "supertype");
        }
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private void writeGetMethod(JavaWriter javaWriter, ExecutableElement executableElement, boolean z, boolean z2, String str) throws IOException {
        boolean z3 = true;
        javaWriter.emitJavadoc("Returns the fully provisioned instance satisfying the contract for\n{@code Provider<%s>}.", str);
        javaWriter.emitAnnotation(Override.class);
        javaWriter.beginMethod(str, "get", EnumSet.of(Modifier.PUBLIC), new String[0]);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" result = new ");
        sb.append(str);
        sb.append('(');
        for (VariableElement variableElement : executableElement.getParameters()) {
            if (!z3) {
                sb.append(", ");
            } else {
                z3 = false;
            }
            sb.append(parameterName(z, variableElement));
            sb.append(".get()");
        }
        sb.append(')');
        javaWriter.emitStatement(sb.toString(), new Object[0]);
        if (z2) {
            javaWriter.emitStatement("injectMembers(result)", new Object[0]);
        }
        javaWriter.emitStatement("return result", new Object[0]);
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private void writeMembersInjectMethod(JavaWriter javaWriter, List<Element> list, boolean z, String str, TypeMirror typeMirror) throws IOException {
        javaWriter.emitJavadoc("Injects any {@code @Inject} annotated fields in the given instance,\nsatisfying the contract for {@code Provider<%s>}.", str);
        javaWriter.emitAnnotation(Override.class);
        javaWriter.beginMethod("void", "injectMembers", EnumSet.of(Modifier.PUBLIC), str, "object");
        for (Element element : list) {
            javaWriter.emitStatement("object.%s = %s.get()", element.getSimpleName(), fieldName(z, element));
        }
        if (typeMirror != null) {
            javaWriter.emitStatement("supertype.injectMembers(object)", new Object[0]);
        }
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private void writeStaticInjectMethod(JavaWriter javaWriter, List<Element> list, String str) throws IOException {
        javaWriter.emitEmptyLine();
        javaWriter.emitJavadoc("Performs the injections of dependencies into static fields when requested by\nthe {@code dagger.ObjectGraph}.", new Object[0]);
        javaWriter.emitAnnotation(Override.class);
        javaWriter.beginMethod("void", "inject", EnumSet.of(Modifier.PUBLIC), new String[0]);
        for (Element element : list) {
            javaWriter.emitStatement("%s.%s = %s.get()", javaWriter.compressType(str), element.getSimpleName().toString(), fieldName(false, element));
        }
        javaWriter.endMethod();
        javaWriter.emitEmptyLine();
    }

    private Set<String> findImports(boolean z, boolean z2, boolean z3) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(Binding.class.getCanonicalName());
        if (z) {
            linkedHashSet.add(Linker.class.getCanonicalName());
            linkedHashSet.add(Set.class.getCanonicalName());
        }
        if (z2) {
            linkedHashSet.add(MembersInjector.class.getCanonicalName());
        }
        if (z3) {
            linkedHashSet.add(Provider.class.getCanonicalName());
        }
        return linkedHashSet;
    }

    private String[] implementedInterfaces(String str, boolean z, boolean z2) {
        ArrayList arrayList = new ArrayList();
        if (z2) {
            arrayList.add(JavaWriter.type(Provider.class, str));
        }
        if (z) {
            arrayList.add(JavaWriter.type(MembersInjector.class, str));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private String strippedTypeName(String str, String str2) {
        return str.substring(str2.isEmpty() ? 0 : str2.length() + 1);
    }

    private String fieldName(boolean z, Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "field_" : "");
        sb.append(element.getSimpleName().toString());
        return sb.toString();
    }

    private String parameterName(boolean z, Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "parameter_" : "");
        sb.append(element.getSimpleName().toString());
        return sb.toString();
    }

    private void error(String str, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, str, element);
    }
}
