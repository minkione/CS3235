package dagger.internal.codegen;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Scope;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"*"})
public final class ValidationProcessor extends AbstractProcessor {

    /* renamed from: dagger.internal.codegen.ValidationProcessor$1 */
    static /* synthetic */ class C17861 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
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
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.METHOD     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x002a }
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.PARAMETER     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x0035 }
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.CLASS     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.ValidationProcessor.C17861.<clinit>():void");
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ArrayList<Element> arrayList = new ArrayList<>();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        getAllElements(roundEnvironment, arrayList, linkedHashMap);
        for (Element element : arrayList) {
            try {
                validateProvides(element);
                validateScoping(element);
                validateQualifiers(element, linkedHashMap);
            } catch (CodeGenerationIncompleteException unused) {
            }
        }
        return false;
    }

    private void validateProvides(Element element) {
        if (element.getAnnotation(Provides.class) != null && Util.getAnnotation(Module.class, element.getEnclosingElement()) == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("@Provides methods must be declared in modules: ");
            sb.append(Util.elementToString(element));
            error(sb.toString(), element);
        }
    }

    private void validateQualifiers(Element element, Map<Element, Element> map) {
        int i = 0;
        boolean z = element.getAnnotation(SuppressWarnings.class) != null && Arrays.asList(((SuppressWarnings) element.getAnnotation(SuppressWarnings.class)).value()).contains("qualifiers");
        for (AnnotationMirror annotationType : element.getAnnotationMirrors()) {
            if (annotationType.getAnnotationType().asElement().getAnnotation(Qualifier.class) != null) {
                switch (C17861.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
                    case 1:
                        i++;
                        if (element.getAnnotation(Inject.class) == null && !z) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Dagger will ignore qualifier annotations on fields that are not annotated with @Inject: ");
                            sb.append(Util.elementToString(element));
                            warning(sb.toString(), element);
                            break;
                        }
                    case 2:
                        i++;
                        if (!isProvidesMethod(element) && !z) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Dagger will ignore qualifier annotations on methods that are not @Provides methods: ");
                            sb2.append(Util.elementToString(element));
                            warning(sb2.toString(), element);
                            break;
                        }
                    case 3:
                        i++;
                        if (!isInjectableConstructorParameter(element, map) && !isProvidesMethodParameter(element, map) && !z) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Dagger will ignore qualifier annotations on parameters that are not @Inject constructor parameters or @Provides method parameters: ");
                            sb3.append(Util.elementToString(element));
                            warning(sb3.toString(), element);
                            break;
                        }
                    default:
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Qualifier annotations are only allowed on fields, methods, and parameters: ");
                        sb4.append(Util.elementToString(element));
                        error(sb4.toString(), element);
                        break;
                }
            }
        }
        if (i > 1) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Only one qualifier annotation is allowed per element: ");
            sb5.append(Util.elementToString(element));
            error(sb5.toString(), element);
        }
    }

    private void validateScoping(Element element) {
        int i = 0;
        boolean z = element.getAnnotation(SuppressWarnings.class) != null && Arrays.asList(((SuppressWarnings) element.getAnnotation(SuppressWarnings.class)).value()).contains("scoping");
        for (AnnotationMirror annotationType : element.getAnnotationMirrors()) {
            if (annotationType.getAnnotationType().asElement().getAnnotation(Scope.class) != null) {
                int i2 = C17861.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()];
                if (i2 == 2) {
                    i++;
                    if (!isProvidesMethod(element) && !z) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Dagger will ignore scoping annotations on methods that are not @Provides methods: ");
                        sb.append(Util.elementToString(element));
                        warning(sb.toString(), element);
                    }
                } else if (i2 == 4 && !element.getModifiers().contains(Modifier.ABSTRACT)) {
                    i++;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Scoping annotations are only allowed on concrete types and @Provides methods: ");
                    sb2.append(Util.elementToString(element));
                    error(sb2.toString(), element);
                }
            }
        }
        if (i > 1) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Only one scoping annotation is allowed per element: ");
            sb3.append(Util.elementToString(element));
            error(sb3.toString(), element);
        }
    }

    private void getAllElements(RoundEnvironment roundEnvironment, List<Element> list, Map<Element, Element> map) {
        for (Element addAllEnclosed : roundEnvironment.getRootElements()) {
            addAllEnclosed(addAllEnclosed, list, map);
        }
    }

    private void addAllEnclosed(Element element, List<Element> list, Map<Element, Element> map) {
        list.add(element);
        for (Element element2 : element.getEnclosedElements()) {
            addAllEnclosed(element2, list, map);
            if (element2.getKind() == ElementKind.METHOD || element2.getKind() == ElementKind.CONSTRUCTOR) {
                for (Element element3 : ((ExecutableElement) element2).getParameters()) {
                    list.add(element3);
                    map.put(element3, element2);
                }
            }
        }
    }

    private boolean isProvidesMethod(Element element) {
        return element.getKind() == ElementKind.METHOD && element.getAnnotation(Provides.class) != null;
    }

    private boolean isProvidesMethodParameter(Element element, Map<Element, Element> map) {
        return ((Element) map.get(element)).getAnnotation(Provides.class) != null;
    }

    private boolean isInjectableConstructorParameter(Element element, Map<Element, Element> map) {
        return ((Element) map.get(element)).getKind() == ElementKind.CONSTRUCTOR && ((Element) map.get(element)).getAnnotation(Inject.class) != null;
    }

    private void error(String str, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, str, element);
    }

    private void warning(String str, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.WARNING, str, element);
    }
}
