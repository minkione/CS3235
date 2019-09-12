package dagger.internal.codegen;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Qualifier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class GeneratorKeys {
    private static final String SET_PREFIX;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Set.class.getCanonicalName());
        sb.append("<");
        SET_PREFIX = sb.toString();
    }

    private GeneratorKeys() {
    }

    public static String rawMembersKey(TypeMirror typeMirror) {
        StringBuilder sb = new StringBuilder();
        sb.append("members/");
        sb.append(Util.rawTypeToString(typeMirror, '$'));
        return sb.toString();
    }

    public static String get(TypeMirror typeMirror) {
        StringBuilder sb = new StringBuilder();
        Util.typeToString(typeMirror, sb, '$');
        return sb.toString();
    }

    public static String get(ExecutableElement executableElement) {
        StringBuilder sb = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(executableElement.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, sb);
        }
        Util.typeToString(executableElement.getReturnType(), sb, '$');
        return sb.toString();
    }

    public static String getSetKey(ExecutableElement executableElement) {
        StringBuilder sb = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(executableElement.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, sb);
        }
        sb.append(SET_PREFIX);
        Util.typeToString(executableElement.getReturnType(), sb, '$');
        sb.append(">");
        return sb.toString();
    }

    public static String get(VariableElement variableElement) {
        StringBuilder sb = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(variableElement.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, sb);
        }
        Util.typeToString(variableElement.asType(), sb, '$');
        return sb.toString();
    }

    private static void qualifierToString(AnnotationMirror annotationMirror, StringBuilder sb) {
        sb.append('@');
        Util.typeToString(annotationMirror.getAnnotationType(), sb, '$');
        sb.append('(');
        for (Entry entry : annotationMirror.getElementValues().entrySet()) {
            sb.append(((ExecutableElement) entry.getKey()).getSimpleName());
            sb.append('=');
            sb.append(((AnnotationValue) entry.getValue()).getValue());
        }
        sb.append(")/");
    }

    private static AnnotationMirror getQualifier(List<? extends AnnotationMirror> list) {
        AnnotationMirror annotationMirror = null;
        for (AnnotationMirror annotationMirror2 : list) {
            if (annotationMirror2.getAnnotationType().asElement().getAnnotation(Qualifier.class) != null) {
                annotationMirror = annotationMirror2;
            }
        }
        return annotationMirror;
    }
}
