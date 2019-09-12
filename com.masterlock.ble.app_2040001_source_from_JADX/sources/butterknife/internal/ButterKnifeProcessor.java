package butterknife.internal;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Optional;
import butterknife.internal.ListenerClass.NONE;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public final class ButterKnifeProcessor extends AbstractProcessor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String ANDROID_PREFIX = "android.";
    public static final String JAVA_PREFIX = "java.";
    private static final List<Class<? extends Annotation>> LISTENERS = Arrays.asList(new Class[]{OnCheckedChanged.class, OnClick.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class, OnItemLongClick.class, OnItemSelected.class, OnLongClick.class, OnPageChange.class, OnTextChanged.class, OnTouch.class});
    private static final String LIST_TYPE = List.class.getCanonicalName();
    public static final String SUFFIX = "$$ViewInjector";
    static final String VIEW_TYPE = "android.view.View";
    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        ButterKnifeProcessor.super.init(processingEnvironment);
        this.elementUtils = processingEnvironment.getElementUtils();
        this.typeUtils = processingEnvironment.getTypeUtils();
        this.filer = processingEnvironment.getFiler();
    }

    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(InjectView.class.getCanonicalName());
        linkedHashSet.add(InjectViews.class.getCanonicalName());
        for (Class canonicalName : LISTENERS) {
            linkedHashSet.add(canonicalName.getCanonicalName());
        }
        return linkedHashSet;
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Entry entry : findAndParseTargets(roundEnvironment).entrySet()) {
            Element element = (TypeElement) entry.getKey();
            ViewInjector viewInjector = (ViewInjector) entry.getValue();
            try {
                Writer openWriter = this.filer.createSourceFile(viewInjector.getFqcn(), new Element[]{element}).openWriter();
                openWriter.write(viewInjector.brewJava());
                openWriter.flush();
                openWriter.close();
            } catch (IOException e) {
                error(element, "Unable to write injector for type %s: %s", element, e.getMessage());
            }
        }
        return true;
    }

    private Map<TypeElement, ViewInjector> findAndParseTargets(RoundEnvironment roundEnvironment) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(InjectView.class)) {
            try {
                parseInjectView(element, linkedHashMap, linkedHashSet);
            } catch (Exception e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                error(element, "Unable to generate view injector for @InjectView.\n\n%s", stringWriter);
            }
        }
        for (Element element2 : roundEnvironment.getElementsAnnotatedWith(InjectViews.class)) {
            try {
                parseInjectViews(element2, linkedHashMap, linkedHashSet);
            } catch (Exception e2) {
                StringWriter stringWriter2 = new StringWriter();
                e2.printStackTrace(new PrintWriter(stringWriter2));
                error(element2, "Unable to generate view injector for @InjectViews.\n\n%s", stringWriter2);
            }
        }
        for (Class findAndParseListener : LISTENERS) {
            findAndParseListener(roundEnvironment, findAndParseListener, linkedHashMap, linkedHashSet);
        }
        for (Entry entry : linkedHashMap.entrySet()) {
            String findParentFqcn = findParentFqcn((TypeElement) entry.getKey(), linkedHashSet);
            if (findParentFqcn != null) {
                ViewInjector viewInjector = (ViewInjector) entry.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append(findParentFqcn);
                sb.append(SUFFIX);
                viewInjector.setParentInjector(sb.toString());
            }
        }
        return linkedHashMap;
    }

    private boolean isValidForGeneratedCode(Class<? extends Annotation> cls, String str, Element element) {
        boolean z;
        TypeElement enclosingElement = element.getEnclosingElement();
        Set modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)", cls.getSimpleName(), str, enclosingElement.getQualifiedName(), element.getSimpleName());
            z = true;
        } else {
            z = false;
        }
        if (enclosingElement.getKind() != ElementKind.CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)", cls.getSimpleName(), str, enclosingElement.getQualifiedName(), element.getSimpleName());
            z = true;
        }
        if (!enclosingElement.getModifiers().contains(Modifier.PRIVATE)) {
            return z;
        }
        error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)", cls.getSimpleName(), str, enclosingElement.getQualifiedName(), element.getSimpleName());
        return true;
    }

    private boolean isBindingInWrongPackage(Class<? extends Annotation> cls, Element element) {
        String obj = element.getEnclosingElement().getQualifiedName().toString();
        if (obj.startsWith(ANDROID_PREFIX)) {
            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", cls.getSimpleName(), obj);
            return true;
        } else if (!obj.startsWith(JAVA_PREFIX)) {
            return false;
        } else {
            error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", cls.getSimpleName(), obj);
            return true;
        }
    }

    private void parseInjectView(Element element, Map<TypeElement, ViewInjector> map, Set<String> set) {
        boolean z;
        TypeElement enclosingElement = element.getEnclosingElement();
        TypeMirror asType = element.asType();
        if (asType instanceof TypeVariable) {
            asType = ((TypeVariable) asType).getUpperBound();
        }
        boolean z2 = false;
        if (!isSubtypeOfType(asType, VIEW_TYPE)) {
            error(element, "@InjectView fields must extend from View. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            z = true;
        } else {
            z = false;
        }
        boolean isValidForGeneratedCode = z | isValidForGeneratedCode(InjectView.class, "fields", element) | isBindingInWrongPackage(InjectView.class, element);
        if (element.getAnnotation(InjectViews.class) != null) {
            error(element, "Only one of @InjectView and @InjectViews is allowed. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            isValidForGeneratedCode = true;
        }
        if (!isValidForGeneratedCode) {
            String obj = element.getSimpleName().toString();
            int value = ((InjectView) element.getAnnotation(InjectView.class)).value();
            String typeMirror = asType.toString();
            if (element.getAnnotation(Optional.class) == null) {
                z2 = true;
            }
            getOrCreateTargetClass(map, enclosingElement).addView(value, new ViewBinding(obj, typeMirror, z2));
            set.add(enclosingElement.toString());
        }
    }

    private void parseInjectViews(Element element, Map<TypeElement, ViewInjector> map, Set<String> set) {
        Kind kind;
        boolean z;
        TypeElement enclosingElement = element.getEnclosingElement();
        ArrayType asType = element.asType();
        String doubleErasure = doubleErasure(asType);
        TypeMirror typeMirror = null;
        boolean z2 = false;
        if (asType.getKind() == TypeKind.ARRAY) {
            typeMirror = asType.getComponentType();
            kind = Kind.ARRAY;
            z = false;
        } else if (LIST_TYPE.equals(doubleErasure)) {
            List typeArguments = ((DeclaredType) asType).getTypeArguments();
            if (typeArguments.size() != 1) {
                error(element, "@InjectViews List must have a generic component. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                z = true;
            } else {
                typeMirror = (TypeMirror) typeArguments.get(0);
                z = false;
            }
            kind = Kind.LIST;
        } else {
            error(element, "@InjectViews must be a List or array. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            kind = null;
            z = true;
        }
        if (typeMirror instanceof TypeVariable) {
            typeMirror = ((TypeVariable) typeMirror).getUpperBound();
        }
        if (typeMirror != null && !isSubtypeOfType(typeMirror, VIEW_TYPE)) {
            error(element, "@InjectViews type must extend from View. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            z = true;
        }
        if (!(z | isValidForGeneratedCode(InjectViews.class, "fields", element)) && !isBindingInWrongPackage(InjectViews.class, element)) {
            String obj = element.getSimpleName().toString();
            int[] value = ((InjectViews) element.getAnnotation(InjectViews.class)).value();
            if (value.length == 0) {
                error(element, "@InjectViews must specify at least one ID. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                return;
            }
            String typeMirror2 = typeMirror.toString();
            if (element.getAnnotation(Optional.class) == null) {
                z2 = true;
            }
            getOrCreateTargetClass(map, enclosingElement).addCollection(value, new CollectionBinding(obj, typeMirror2, kind, z2));
            set.add(enclosingElement.toString());
        }
    }

    private String doubleErasure(TypeMirror typeMirror) {
        String typeMirror2 = this.typeUtils.erasure(typeMirror).toString();
        int indexOf = typeMirror2.indexOf(60);
        return indexOf != -1 ? typeMirror2.substring(0, indexOf) : typeMirror2;
    }

    private void findAndParseListener(RoundEnvironment roundEnvironment, Class<? extends Annotation> cls, Map<TypeElement, ViewInjector> map, Set<String> set) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(cls)) {
            try {
                parseListenerAnnotation(cls, element, map, set);
            } catch (Exception e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                error(element, "Unable to generate view injector for @%s.\n\n%s", cls.getSimpleName(), stringWriter.toString());
            }
        }
    }

    private void parseListenerAnnotation(Class<? extends Annotation> cls, Element element, Map<TypeElement, ViewInjector> map, Set<String> set) throws Exception {
        ListenerMethod listenerMethod;
        String[] parameters;
        Class<? extends Annotation> cls2 = cls;
        Element element2 = element;
        if (!(element2 instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
            throw new IllegalStateException(String.format("@%s annotation must be on a method.", new Object[]{cls.getSimpleName()}));
        }
        ExecutableElement executableElement = (ExecutableElement) element2;
        TypeElement enclosingElement = element.getEnclosingElement();
        Annotation annotation = element2.getAnnotation(cls2);
        Method declaredMethod = cls2.getDeclaredMethod("value", new Class[0]);
        if (declaredMethod.getReturnType() == int[].class) {
            int[] iArr = (int[]) declaredMethod.invoke(annotation, new Object[0]);
            String obj = executableElement.getSimpleName().toString();
            boolean z = element2.getAnnotation(Optional.class) == null;
            boolean isValidForGeneratedCode = isValidForGeneratedCode(cls2, "methods", element2) | isBindingInWrongPackage(cls, element);
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            boolean z2 = isValidForGeneratedCode;
            for (int i : iArr) {
                if (!linkedHashSet.add(Integer.valueOf(i))) {
                    error(element2, "@%s annotation for method contains duplicate ID %d. (%s.%s)", cls.getSimpleName(), Integer.valueOf(i), enclosingElement.getQualifiedName(), element.getSimpleName());
                    z2 = true;
                }
            }
            ListenerClass listenerClass = (ListenerClass) cls2.getAnnotation(ListenerClass.class);
            if (listenerClass != null) {
                ListenerMethod[] method = listenerClass.method();
                if (method.length <= 1) {
                    if (method.length != 1) {
                        Enum enumR = (Enum) cls2.getDeclaredMethod("callback", new Class[0]).invoke(annotation, new Object[0]);
                        listenerMethod = (ListenerMethod) enumR.getDeclaringClass().getField(enumR.name()).getAnnotation(ListenerMethod.class);
                        if (listenerMethod == null) {
                            throw new IllegalStateException(String.format("No @%s defined on @%s's %s.%s.", new Object[]{ListenerMethod.class.getSimpleName(), cls.getSimpleName(), enumR.getDeclaringClass().getSimpleName(), enumR.name()}));
                        }
                    } else if (listenerClass.callbacks() == NONE.class) {
                        listenerMethod = method[0];
                    } else {
                        throw new IllegalStateException(String.format("Both method() and callback() defined on @%s.", new Object[]{cls.getSimpleName()}));
                    }
                    List parameters2 = executableElement.getParameters();
                    if (parameters2.size() > listenerMethod.parameters().length) {
                        error(element2, "@%s methods can have at most %s parameter(s). (%s.%s)", cls.getSimpleName(), Integer.valueOf(listenerMethod.parameters().length), enclosingElement.getQualifiedName(), element.getSimpleName());
                        z2 = true;
                    }
                    TypeMirror returnType = executableElement.getReturnType();
                    if (returnType instanceof TypeVariable) {
                        returnType = ((TypeVariable) returnType).getUpperBound();
                    }
                    if (!returnType.toString().equals(listenerMethod.returnType())) {
                        error(element2, "@%s methods must have a '%s' return type. (%s.%s)", cls.getSimpleName(), listenerMethod.returnType(), enclosingElement.getQualifiedName(), element.getSimpleName());
                        z2 = true;
                    }
                    if (!z2) {
                        Parameter[] parameterArr = Parameter.NONE;
                        if (!parameters2.isEmpty()) {
                            parameterArr = new Parameter[parameters2.size()];
                            BitSet bitSet = new BitSet(parameters2.size());
                            String[] parameters3 = listenerMethod.parameters();
                            int i2 = 0;
                            while (i2 < parameters2.size()) {
                                TypeMirror asType = ((VariableElement) parameters2.get(i2)).asType();
                                if (asType instanceof TypeVariable) {
                                    asType = ((TypeVariable) asType).getUpperBound();
                                }
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= parameters3.length) {
                                        break;
                                    } else if (!bitSet.get(i3) && isSubtypeOfType(asType, parameters3[i3])) {
                                        parameterArr[i2] = new Parameter(i3, asType.toString());
                                        bitSet.set(i3);
                                        break;
                                    } else {
                                        i3++;
                                    }
                                }
                                if (parameterArr[i2] == null) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Unable to match @");
                                    sb.append(cls.getSimpleName());
                                    sb.append(" method arguments. (");
                                    sb.append(enclosingElement.getQualifiedName());
                                    sb.append('.');
                                    sb.append(element.getSimpleName());
                                    sb.append(')');
                                    int i4 = 0;
                                    while (i4 < parameterArr.length) {
                                        Parameter parameter = parameterArr[i4];
                                        sb.append("\n\n  Parameter #");
                                        int i5 = i4 + 1;
                                        sb.append(i5);
                                        sb.append(": ");
                                        sb.append(((VariableElement) parameters2.get(i4)).asType().toString());
                                        sb.append("\n    ");
                                        if (parameter == null) {
                                            sb.append("did not match any listener parameters");
                                        } else {
                                            sb.append("matched listener parameter #");
                                            sb.append(parameter.getListenerPosition() + 1);
                                            sb.append(": ");
                                            sb.append(parameter.getType());
                                        }
                                        i4 = i5;
                                    }
                                    sb.append("\n\nMethods may have up to ");
                                    sb.append(listenerMethod.parameters().length);
                                    sb.append(" parameter(s):\n");
                                    for (String str : listenerMethod.parameters()) {
                                        sb.append("\n  ");
                                        sb.append(str);
                                    }
                                    sb.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
                                    error(executableElement, sb.toString(), new Object[0]);
                                    return;
                                }
                                i2++;
                                Class<? extends Annotation> cls3 = cls;
                                Element element3 = element;
                            }
                        }
                        ListenerBinding listenerBinding = new ListenerBinding(obj, Arrays.asList(parameterArr), z);
                        ViewInjector orCreateTargetClass = getOrCreateTargetClass(map, enclosingElement);
                        for (int i6 : iArr) {
                            if (!orCreateTargetClass.addListener(i6, listenerClass, listenerMethod, listenerBinding)) {
                                error(element, "Multiple @%s methods declared for ID %s in %s.", cls.getSimpleName(), Integer.valueOf(i6), enclosingElement.getQualifiedName());
                                return;
                            }
                            Element element4 = element;
                        }
                        set.add(enclosingElement.toString());
                        return;
                    }
                    return;
                }
                throw new IllegalStateException(String.format("Multiple listener methods specified on @%s.", new Object[]{cls.getSimpleName()}));
            }
            throw new IllegalStateException(String.format("No @%s defined on @%s.", new Object[]{ListenerClass.class.getSimpleName(), cls.getSimpleName()}));
        }
        throw new IllegalStateException(String.format("@%s annotation value() type not int[].", new Object[]{cls}));
    }

    private boolean isSubtypeOfType(TypeMirror typeMirror, String str) {
        if (str.equals(typeMirror.toString())) {
            return true;
        }
        if (!(typeMirror instanceof DeclaredType)) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder sb = new StringBuilder(declaredType.asElement().toString());
            sb.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('?');
            }
            sb.append('>');
            if (sb.toString().equals(str)) {
                return true;
            }
        }
        TypeElement asElement = declaredType.asElement();
        if (!(asElement instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = asElement;
        if (isSubtypeOfType(typeElement.getSuperclass(), str)) {
            return true;
        }
        for (TypeMirror isSubtypeOfType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(isSubtypeOfType, str)) {
                return true;
            }
        }
        return false;
    }

    private ViewInjector getOrCreateTargetClass(Map<TypeElement, ViewInjector> map, TypeElement typeElement) {
        ViewInjector viewInjector = (ViewInjector) map.get(typeElement);
        if (viewInjector != null) {
            return viewInjector;
        }
        String obj = typeElement.getQualifiedName().toString();
        String packageName = getPackageName(typeElement);
        StringBuilder sb = new StringBuilder();
        sb.append(getClassName(typeElement, packageName));
        sb.append(SUFFIX);
        ViewInjector viewInjector2 = new ViewInjector(packageName, sb.toString(), obj);
        map.put(typeElement, viewInjector2);
        return viewInjector2;
    }

    private static String getClassName(TypeElement typeElement, String str) {
        return typeElement.getQualifiedName().toString().substring(str.length() + 1).replace('.', '$');
    }

    private String findParentFqcn(TypeElement typeElement, Set<String> set) {
        do {
            DeclaredType superclass = typeElement.getSuperclass();
            if (superclass.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement = (TypeElement) superclass.asElement();
        } while (!set.contains(typeElement.toString()));
        String packageName = getPackageName(typeElement);
        StringBuilder sb = new StringBuilder();
        sb.append(packageName);
        sb.append(".");
        sb.append(getClassName(typeElement, packageName));
        return sb.toString();
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String str, Object... objArr) {
        if (objArr.length > 0) {
            str = String.format(str, objArr);
        }
        this.processingEnv.getMessager().printMessage(Kind.ERROR, str, element);
    }

    private String getPackageName(TypeElement typeElement) {
        return this.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }
}
