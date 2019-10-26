package dagger.internal.codegen;

import dagger.internal.Keys;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

final class Util {
    private static final AnnotationValueVisitor<Object, Void> VALUE_EXTRACTOR = new SimpleAnnotationValueVisitor6<Object, Void>() {
        /* access modifiers changed from: protected */
        public Object defaultAction(Object obj, Void voidR) {
            return obj;
        }

        public Object visitType(TypeMirror typeMirror, Void voidR) {
            return typeMirror;
        }

        public Object visitString(String str, Void voidR) {
            if ("<error>".equals(str)) {
                throw new CodeGenerationIncompleteException("Unknown type returned as <error>.");
            } else if (!"<any>".equals(str)) {
                return str;
            } else {
                throw new CodeGenerationIncompleteException("Unknown type returned as <any>.");
            }
        }

        public Object visitArray(List<? extends AnnotationValue> list, Void voidR) {
            Object[] objArr = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                objArr[i] = ((AnnotationValue) list.get(i)).accept(this, null);
            }
            return objArr;
        }
    };

    /* renamed from: dagger.internal.codegen.Util$3 */
    static /* synthetic */ class C17853 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$type$TypeKind = new int[TypeKind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|25|26|27|28|29|30|32) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|25|26|27|28|29|30|32) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|25|26|27|28|29|30|32) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0035 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0081 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x008b */
        static {
            /*
                javax.lang.model.type.TypeKind[] r0 = javax.lang.model.type.TypeKind.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$lang$model$type$TypeKind = r0
                r0 = 1
                int[] r1 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x0014 }
                javax.lang.model.type.TypeKind r2 = javax.lang.model.type.TypeKind.BYTE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x001f }
                javax.lang.model.type.TypeKind r3 = javax.lang.model.type.TypeKind.SHORT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x002a }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.INT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x0035 }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.LONG     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r5 = 4
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x0040 }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.FLOAT     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r5 = 5
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x004b }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.DOUBLE     // Catch:{ NoSuchFieldError -> 0x004b }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r5 = 6
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x0056 }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.BOOLEAN     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r5 = 7
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x0062 }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.CHAR     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r5 = 8
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r3 = $SwitchMap$javax$lang$model$type$TypeKind     // Catch:{ NoSuchFieldError -> 0x006e }
                javax.lang.model.type.TypeKind r4 = javax.lang.model.type.TypeKind.VOID     // Catch:{ NoSuchFieldError -> 0x006e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x006e }
                r5 = 9
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x006e }
            L_0x006e:
                javax.lang.model.element.ElementKind[] r3 = javax.lang.model.element.ElementKind.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$javax$lang$model$element$ElementKind = r3
                int[] r3 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x0081 }
                javax.lang.model.element.ElementKind r4 = javax.lang.model.element.ElementKind.FIELD     // Catch:{ NoSuchFieldError -> 0x0081 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0081 }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x0081 }
            L_0x0081:
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x008b }
                javax.lang.model.element.ElementKind r3 = javax.lang.model.element.ElementKind.CONSTRUCTOR     // Catch:{ NoSuchFieldError -> 0x008b }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x008b }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x008b }
            L_0x008b:
                int[] r0 = $SwitchMap$javax$lang$model$element$ElementKind     // Catch:{ NoSuchFieldError -> 0x0095 }
                javax.lang.model.element.ElementKind r1 = javax.lang.model.element.ElementKind.METHOD     // Catch:{ NoSuchFieldError -> 0x0095 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0095 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0095 }
            L_0x0095:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: dagger.internal.codegen.Util.C17853.<clinit>():void");
        }
    }

    static final class CodeGenerationIncompleteException extends IllegalStateException {
        public CodeGenerationIncompleteException(String str) {
            super(str);
        }
    }

    private Util() {
    }

    public static PackageElement getPackage(Element element) {
        while (element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }
        return (PackageElement) element;
    }

    public static TypeMirror getApplicationSupertype(TypeElement typeElement) {
        TypeMirror superclass = typeElement.getSuperclass();
        if (Keys.isPlatformType(superclass.toString())) {
            return null;
        }
        return superclass;
    }

    public static String adapterName(TypeElement typeElement, String str) {
        StringBuilder sb = new StringBuilder();
        rawTypeToString(sb, typeElement, '$');
        sb.append(str);
        return sb.toString();
    }

    public static String typeToString(TypeMirror typeMirror) {
        StringBuilder sb = new StringBuilder();
        typeToString(typeMirror, sb, '.');
        return sb.toString();
    }

    public static String rawTypeToString(TypeMirror typeMirror, char c) {
        if (typeMirror instanceof DeclaredType) {
            StringBuilder sb = new StringBuilder();
            rawTypeToString(sb, ((DeclaredType) typeMirror).asElement(), c);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unexpected type: ");
        sb2.append(typeMirror);
        throw new IllegalArgumentException(sb2.toString());
    }

    public static void typeToString(final TypeMirror typeMirror, final StringBuilder sb, final char c) {
        typeMirror.accept(new SimpleTypeVisitor6<Void, Void>() {
            public Void visitDeclared(DeclaredType declaredType, Void voidR) {
                Util.rawTypeToString(sb, declaredType.asElement(), c);
                List typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    sb.append("<");
                    for (int i = 0; i < typeArguments.size(); i++) {
                        if (i != 0) {
                            sb.append(", ");
                        }
                        Util.typeToString((TypeMirror) typeArguments.get(i), sb, c);
                    }
                    sb.append(">");
                }
                return null;
            }

            public Void visitPrimitive(PrimitiveType primitiveType, Void voidR) {
                sb.append(Util.box(typeMirror).getName());
                return null;
            }

            public Void visitArray(ArrayType arrayType, Void voidR) {
                TypeMirror componentType = arrayType.getComponentType();
                if (componentType instanceof PrimitiveType) {
                    sb.append(componentType.toString());
                } else {
                    Util.typeToString(arrayType.getComponentType(), sb, c);
                }
                sb.append("[]");
                return null;
            }

            public Void visitTypeVariable(TypeVariable typeVariable, Void voidR) {
                sb.append(typeVariable.asElement().getSimpleName());
                return null;
            }

            public Void visitError(ErrorType errorType, Void voidR) {
                if (!"<any>".equals(errorType.toString())) {
                    sb.append(errorType.toString());
                    return null;
                }
                throw new CodeGenerationIncompleteException("Type reported as <any> is likely a not-yet generated parameterized type.");
            }

            /* access modifiers changed from: protected */
            public Void defaultAction(TypeMirror typeMirror, Void voidR) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected TypeKind ");
                sb.append(typeMirror.getKind());
                sb.append(" for ");
                sb.append(typeMirror);
                throw new UnsupportedOperationException(sb.toString());
            }
        }, null);
    }

    public static Map<String, Object> getAnnotation(Class<?> cls, Element element) {
        Method[] methods;
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (rawTypeToString(annotationMirror.getAnnotationType(), '$').equals(cls.getName())) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Method method : cls.getMethods()) {
                    linkedHashMap.put(method.getName(), method.getDefaultValue());
                }
                for (Entry entry : annotationMirror.getElementValues().entrySet()) {
                    String obj = ((ExecutableElement) entry.getKey()).getSimpleName().toString();
                    Object accept = ((AnnotationValue) entry.getValue()).accept(VALUE_EXTRACTOR, null);
                    Object obj2 = linkedHashMap.get(obj);
                    if (!lenientIsInstance(obj2.getClass(), accept)) {
                        Object[] objArr = new Object[5];
                        objArr[0] = cls;
                        objArr[1] = obj;
                        objArr[2] = accept.getClass().getName();
                        objArr[3] = obj2.getClass().getName();
                        if (accept instanceof Object[]) {
                            accept = Arrays.toString((Object[]) accept);
                        }
                        objArr[4] = accept;
                        throw new IllegalStateException(String.format("Value of %s.%s is a %s but expected a %s\n    value: %s", objArr));
                    }
                    linkedHashMap.put(obj, accept);
                }
                return linkedHashMap;
            }
        }
        return null;
    }

    private static boolean lenientIsInstance(Class<?> cls, Object obj) {
        boolean z = true;
        if (cls.isArray()) {
            Class componentType = cls.getComponentType();
            if (!(obj instanceof Object[])) {
                return false;
            }
            for (Object lenientIsInstance : (Object[]) obj) {
                if (!lenientIsInstance(componentType, lenientIsInstance)) {
                    return false;
                }
            }
            return true;
        } else if (cls == Class.class) {
            return obj instanceof TypeMirror;
        } else {
            if (cls != obj.getClass()) {
                z = false;
            }
            return z;
        }
    }

    static String elementToString(Element element) {
        switch (C17853.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
            case 1:
            case 2:
            case 3:
                StringBuilder sb = new StringBuilder();
                sb.append(element.getEnclosingElement());
                sb.append(".");
                sb.append(element);
                return sb.toString();
            default:
                return element.toString();
        }
    }

    static void rawTypeToString(StringBuilder sb, TypeElement typeElement, char c) {
        String obj = getPackage(typeElement).getQualifiedName().toString();
        String obj2 = typeElement.getQualifiedName().toString();
        if (obj.isEmpty()) {
            sb.append(obj2.replace('.', c));
            return;
        }
        sb.append(obj);
        sb.append('.');
        sb.append(obj2.substring(obj.length() + 1).replace('.', c));
    }

    /* access modifiers changed from: private */
    public static Class<?> box(PrimitiveType primitiveType) {
        switch (C17853.$SwitchMap$javax$lang$model$type$TypeKind[primitiveType.getKind().ordinal()]) {
            case 1:
                return Byte.class;
            case 2:
                return Short.class;
            case 3:
                return Integer.class;
            case 4:
                return Long.class;
            case 5:
                return Float.class;
            case 6:
                return Double.class;
            case 7:
                return Boolean.class;
            case 8:
                return Character.class;
            case 9:
                return Void.class;
            default:
                throw new AssertionError();
        }
    }

    public static ExecutableElement getNoArgsConstructor(TypeElement typeElement) {
        for (ExecutableElement executableElement : typeElement.getEnclosedElements()) {
            if (executableElement.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement executableElement2 = executableElement;
                if (executableElement2.getParameters().isEmpty()) {
                    return executableElement2;
                }
            }
        }
        return null;
    }

    public static boolean isCallableConstructor(ExecutableElement executableElement) {
        boolean z = false;
        if (executableElement.getModifiers().contains(Modifier.PRIVATE)) {
            return false;
        }
        TypeElement enclosingElement = executableElement.getEnclosingElement();
        if (enclosingElement.getEnclosingElement().getKind() == ElementKind.PACKAGE || enclosingElement.getModifiers().contains(Modifier.STATIC)) {
            z = true;
        }
        return z;
    }

    public static String className(ExecutableElement executableElement) {
        return executableElement.getEnclosingElement().getQualifiedName().toString();
    }

    public static boolean isInterface(TypeMirror typeMirror) {
        return (typeMirror instanceof DeclaredType) && ((DeclaredType) typeMirror).asElement().getKind() == ElementKind.INTERFACE;
    }

    static boolean isStatic(Element element) {
        for (Modifier equals : element.getModifiers()) {
            if (equals.equals(Modifier.STATIC)) {
                return true;
            }
        }
        return false;
    }
}
