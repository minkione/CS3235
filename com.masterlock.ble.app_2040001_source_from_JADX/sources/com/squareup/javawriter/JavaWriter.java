package com.squareup.javawriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;

public class JavaWriter implements Closeable {
    private static final String INDENT = "  ";
    private static final int MAX_SINGLE_LINE_ATTRIBUTES = 3;
    private static final EnumSet<Scope> METHOD_SCOPES = EnumSet.of(Scope.NON_ABSTRACT_METHOD, Scope.CONSTRUCTOR, Scope.CONTROL_FLOW, Scope.INITIALIZER);
    private static final Pattern TYPE_PATTERN = Pattern.compile("(?:[\\w$]+\\.)*([\\w\\.*$]+)");
    private final Map<String, String> importedTypes = new LinkedHashMap();
    private String indent = INDENT;
    private boolean isCompressingTypes = true;
    private final Writer out;
    private String packagePrefix;
    private final Deque<Scope> scopes = new ArrayDeque();
    private final Deque<String> types = new ArrayDeque();

    private enum Scope {
        TYPE_DECLARATION,
        INTERFACE_DECLARATION,
        ABSTRACT_METHOD,
        NON_ABSTRACT_METHOD,
        CONSTRUCTOR,
        CONTROL_FLOW,
        ANNOTATION_ATTRIBUTE,
        ANNOTATION_ARRAY_VALUE,
        INITIALIZER
    }

    public JavaWriter(Writer writer) {
        this.out = writer;
    }

    public void setCompressingTypes(boolean z) {
        this.isCompressingTypes = z;
    }

    public boolean isCompressingTypes() {
        return this.isCompressingTypes;
    }

    public void setIndent(String str) {
        this.indent = str;
    }

    public String getIndent() {
        return this.indent;
    }

    public JavaWriter emitPackage(String str) throws IOException {
        if (this.packagePrefix == null) {
            if (str.isEmpty()) {
                this.packagePrefix = "";
            } else {
                this.out.write("package ");
                this.out.write(str);
                this.out.write(";\n\n");
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(".");
                this.packagePrefix = sb.toString();
            }
            return this;
        }
        throw new IllegalStateException();
    }

    public JavaWriter emitImports(String... strArr) throws IOException {
        return emitImports((Collection<String>) Arrays.asList(strArr));
    }

    public JavaWriter emitImports(Class<?>... clsArr) throws IOException {
        ArrayList arrayList = new ArrayList(clsArr.length);
        for (Class<?> name : clsArr) {
            arrayList.add(name.getName());
        }
        return emitImports((Collection<String>) arrayList);
    }

    public JavaWriter emitImports(Collection<String> collection) throws IOException {
        Iterator it = new TreeSet(collection).iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Matcher matcher = TYPE_PATTERN.matcher(str);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(str);
            } else if (this.importedTypes.put(str, matcher.group(1)) == null) {
                this.out.write("import ");
                this.out.write(str);
                this.out.write(";\n");
            } else {
                throw new IllegalArgumentException(str);
            }
        }
        return this;
    }

    public JavaWriter emitStaticImports(String... strArr) throws IOException {
        return emitStaticImports((Collection<String>) Arrays.asList(strArr));
    }

    public JavaWriter emitStaticImports(Collection<String> collection) throws IOException {
        Iterator it = new TreeSet(collection).iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Matcher matcher = TYPE_PATTERN.matcher(str);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(str);
            } else if (this.importedTypes.put(str, matcher.group(1)) == null) {
                this.out.write("import static ");
                this.out.write(str);
                this.out.write(";\n");
            } else {
                throw new IllegalArgumentException(str);
            }
        }
        return this;
    }

    private JavaWriter emitCompressedType(String str) throws IOException {
        if (this.isCompressingTypes) {
            this.out.write(compressType(str));
        } else {
            this.out.write(str);
        }
        return this;
    }

    public String compressType(String str) {
        StringBuilder sb = new StringBuilder();
        if (this.packagePrefix != null) {
            Matcher matcher = TYPE_PATTERN.matcher(str);
            int i = 0;
            while (true) {
                boolean find = matcher.find(i);
                sb.append(str, i, find ? matcher.start() : str.length());
                if (!find) {
                    return sb.toString();
                }
                String group = matcher.group(0);
                String str2 = (String) this.importedTypes.get(group);
                if (str2 != null) {
                    sb.append(str2);
                } else if (isClassInPackage(group, this.packagePrefix)) {
                    String substring = group.substring(this.packagePrefix.length());
                    if (isAmbiguous(substring)) {
                        sb.append(group);
                    } else {
                        sb.append(substring);
                    }
                } else if (isClassInPackage(group, "java.lang.")) {
                    sb.append(group.substring(10));
                } else {
                    sb.append(group);
                }
                i = matcher.end();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private static boolean isClassInPackage(String str, String str2) {
        if (!str.startsWith(str2) || (str.indexOf(46, str2.length()) != -1 && !Character.isUpperCase(str.charAt(str2.length())))) {
            return false;
        }
        return true;
    }

    private boolean isAmbiguous(String str) {
        return this.importedTypes.values().contains(str);
    }

    public JavaWriter beginInitializer(boolean z) throws IOException {
        indent();
        if (z) {
            this.out.write("static");
            this.out.write(" {\n");
        } else {
            this.out.write("{\n");
        }
        this.scopes.push(Scope.INITIALIZER);
        return this;
    }

    public JavaWriter endInitializer() throws IOException {
        popScope(Scope.INITIALIZER);
        indent();
        this.out.write("}\n");
        return this;
    }

    public JavaWriter beginType(String str, String str2) throws IOException {
        return beginType(str, str2, EnumSet.noneOf(Modifier.class), null, new String[0]);
    }

    public JavaWriter beginType(String str, String str2, Set<Modifier> set) throws IOException {
        return beginType(str, str2, set, null, new String[0]);
    }

    public JavaWriter beginType(String str, String str2, Set<Modifier> set, String str3, String... strArr) throws IOException {
        indent();
        emitModifiers(set);
        this.out.write(str2);
        this.out.write(" ");
        emitCompressedType(str);
        if (str3 != null) {
            this.out.write(" extends ");
            emitCompressedType(str3);
        }
        if (strArr.length > 0) {
            this.out.write("\n");
            indent();
            this.out.write("    implements ");
            for (int i = 0; i < strArr.length; i++) {
                if (i != 0) {
                    this.out.write(", ");
                }
                emitCompressedType(strArr[i]);
            }
        }
        this.out.write(" {\n");
        this.scopes.push("interface".equals(str2) ? Scope.INTERFACE_DECLARATION : Scope.TYPE_DECLARATION);
        this.types.push(str);
        return this;
    }

    public JavaWriter endType() throws IOException {
        popScope(Scope.TYPE_DECLARATION, Scope.INTERFACE_DECLARATION);
        this.types.pop();
        indent();
        this.out.write("}\n");
        return this;
    }

    public JavaWriter emitField(String str, String str2) throws IOException {
        return emitField(str, str2, EnumSet.noneOf(Modifier.class), null);
    }

    public JavaWriter emitField(String str, String str2, Set<Modifier> set) throws IOException {
        return emitField(str, str2, set, null);
    }

    public JavaWriter emitField(String str, String str2, Set<Modifier> set, String str3) throws IOException {
        indent();
        emitModifiers(set);
        emitCompressedType(str);
        this.out.write(" ");
        this.out.write(str2);
        if (str3 != null) {
            this.out.write(" =");
            if (!str3.startsWith("\n")) {
                this.out.write(" ");
            }
            String[] split = str3.split("\n", -1);
            this.out.write(split[0]);
            for (int i = 1; i < split.length; i++) {
                this.out.write("\n");
                hangingIndent();
                this.out.write(split[i]);
            }
        }
        this.out.write(";\n");
        return this;
    }

    public JavaWriter beginMethod(String str, String str2, Set<Modifier> set, String... strArr) throws IOException {
        return beginMethod(str, str2, set, Arrays.asList(strArr), null);
    }

    public JavaWriter beginMethod(String str, String str2, Set<Modifier> set, List<String> list, List<String> list2) throws IOException {
        indent();
        emitModifiers(set);
        if (str != null) {
            emitCompressedType(str);
            this.out.write(" ");
            this.out.write(str2);
        } else {
            emitCompressedType(str2);
        }
        this.out.write("(");
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                if (i != 0) {
                    this.out.write(", ");
                }
                int i2 = i + 1;
                emitCompressedType((String) list.get(i));
                this.out.write(" ");
                i = i2 + 1;
                emitCompressedType((String) list.get(i2));
            }
        }
        this.out.write(")");
        if (list2 != null && list2.size() > 0) {
            this.out.write("\n");
            indent();
            this.out.write("    throws ");
            for (int i3 = 0; i3 < list2.size(); i3++) {
                if (i3 != 0) {
                    this.out.write(", ");
                }
                emitCompressedType((String) list2.get(i3));
            }
        }
        if (set.contains(Modifier.ABSTRACT) || Scope.INTERFACE_DECLARATION.equals(this.scopes.peek())) {
            this.out.write(";\n");
            this.scopes.push(Scope.ABSTRACT_METHOD);
        } else {
            this.out.write(" {\n");
            this.scopes.push(str == null ? Scope.CONSTRUCTOR : Scope.NON_ABSTRACT_METHOD);
        }
        return this;
    }

    public JavaWriter beginConstructor(Set<Modifier> set, String... strArr) throws IOException {
        beginMethod(null, rawType((String) this.types.peekFirst()), set, strArr);
        return this;
    }

    public JavaWriter beginConstructor(Set<Modifier> set, List<String> list, List<String> list2) throws IOException {
        beginMethod(null, rawType((String) this.types.peekFirst()), set, list, list2);
        return this;
    }

    public JavaWriter emitJavadoc(String str, Object... objArr) throws IOException {
        String[] split;
        String format = String.format(str, objArr);
        indent();
        this.out.write("/**\n");
        for (String str2 : format.split("\n")) {
            indent();
            this.out.write(" *");
            if (!str2.isEmpty()) {
                this.out.write(" ");
                this.out.write(str2);
            }
            this.out.write("\n");
        }
        indent();
        this.out.write(" */\n");
        return this;
    }

    public JavaWriter emitSingleLineComment(String str, Object... objArr) throws IOException {
        indent();
        this.out.write("// ");
        this.out.write(String.format(str, objArr));
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitEmptyLine() throws IOException {
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitEnumValue(String str) throws IOException {
        indent();
        this.out.write(str);
        this.out.write(",\n");
        return this;
    }

    public JavaWriter emitEnumValue(String str, boolean z) throws IOException {
        return z ? emitLastEnumValue(str) : emitEnumValue(str);
    }

    private JavaWriter emitLastEnumValue(String str) throws IOException {
        indent();
        this.out.write(str);
        this.out.write(";\n");
        return this;
    }

    public JavaWriter emitEnumValues(Iterable<String> iterable) throws IOException {
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (it.hasNext()) {
                emitEnumValue(str);
            } else {
                emitLastEnumValue(str);
            }
        }
        return this;
    }

    public JavaWriter emitAnnotation(String str) throws IOException {
        return emitAnnotation(str, Collections.emptyMap());
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> cls) throws IOException {
        return emitAnnotation(type(cls, new String[0]), Collections.emptyMap());
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> cls, Object obj) throws IOException {
        return emitAnnotation(type(cls, new String[0]), obj);
    }

    public JavaWriter emitAnnotation(String str, Object obj) throws IOException {
        indent();
        this.out.write("@");
        emitCompressedType(str);
        this.out.write("(");
        emitAnnotationValue(obj);
        this.out.write(")");
        this.out.write("\n");
        return this;
    }

    public JavaWriter emitAnnotation(Class<? extends Annotation> cls, Map<String, ?> map) throws IOException {
        return emitAnnotation(type(cls, new String[0]), map);
    }

    public JavaWriter emitAnnotation(String str, Map<String, ?> map) throws IOException {
        indent();
        this.out.write("@");
        emitCompressedType(str);
        switch (map.size()) {
            case 0:
                break;
            case 1:
                Entry entry = (Entry) map.entrySet().iterator().next();
                this.out.write("(");
                if (!"value".equals(entry.getKey())) {
                    this.out.write((String) entry.getKey());
                    this.out.write(" = ");
                }
                emitAnnotationValue(entry.getValue());
                this.out.write(")");
                break;
            default:
                boolean z = map.size() > 3 || containsArray(map.values());
                this.out.write("(");
                this.scopes.push(Scope.ANNOTATION_ATTRIBUTE);
                String str2 = z ? "\n" : "";
                for (Entry entry2 : map.entrySet()) {
                    this.out.write(str2);
                    str2 = z ? ",\n" : ", ";
                    if (z) {
                        indent();
                    }
                    this.out.write((String) entry2.getKey());
                    this.out.write(" = ");
                    emitAnnotationValue(entry2.getValue());
                }
                popScope(Scope.ANNOTATION_ATTRIBUTE);
                if (z) {
                    this.out.write("\n");
                    indent();
                }
                this.out.write(")");
                break;
        }
        this.out.write("\n");
        return this;
    }

    private boolean containsArray(Collection<?> collection) {
        for (Object obj : collection) {
            if (obj instanceof Object[]) {
                return true;
            }
        }
        return false;
    }

    private JavaWriter emitAnnotationValue(Object obj) throws IOException {
        Object[] objArr;
        if (obj instanceof Object[]) {
            this.out.write("{");
            this.scopes.push(Scope.ANNOTATION_ARRAY_VALUE);
            boolean z = true;
            for (Object obj2 : (Object[]) obj) {
                if (z) {
                    this.out.write("\n");
                    z = false;
                } else {
                    this.out.write(",\n");
                }
                indent();
                this.out.write(obj2.toString());
            }
            popScope(Scope.ANNOTATION_ARRAY_VALUE);
            this.out.write("\n");
            indent();
            this.out.write("}");
        } else {
            this.out.write(obj.toString());
        }
        return this;
    }

    public JavaWriter emitStatement(String str, Object... objArr) throws IOException {
        checkInMethod();
        String[] split = String.format(str, objArr).split("\n", -1);
        indent();
        this.out.write(split[0]);
        for (int i = 1; i < split.length; i++) {
            this.out.write("\n");
            hangingIndent();
            this.out.write(split[i]);
        }
        this.out.write(";\n");
        return this;
    }

    public JavaWriter beginControlFlow(String str, Object... objArr) throws IOException {
        checkInMethod();
        indent();
        this.out.write(String.format(str, objArr));
        this.out.write(" {\n");
        this.scopes.push(Scope.CONTROL_FLOW);
        return this;
    }

    public JavaWriter nextControlFlow(String str, Object... objArr) throws IOException {
        popScope(Scope.CONTROL_FLOW);
        indent();
        this.scopes.push(Scope.CONTROL_FLOW);
        this.out.write("} ");
        this.out.write(String.format(str, objArr));
        this.out.write(" {\n");
        return this;
    }

    public JavaWriter endControlFlow() throws IOException {
        return endControlFlow(null, new Object[0]);
    }

    public JavaWriter endControlFlow(String str, Object... objArr) throws IOException {
        popScope(Scope.CONTROL_FLOW);
        indent();
        if (str != null) {
            this.out.write("} ");
            this.out.write(String.format(str, objArr));
            this.out.write(";\n");
        } else {
            this.out.write("}\n");
        }
        return this;
    }

    public JavaWriter endMethod() throws IOException {
        Scope scope = (Scope) this.scopes.pop();
        if (scope == Scope.NON_ABSTRACT_METHOD || scope == Scope.CONSTRUCTOR) {
            indent();
            this.out.write("}\n");
        } else if (scope != Scope.ABSTRACT_METHOD) {
            throw new IllegalStateException();
        }
        return this;
    }

    public JavaWriter endConstructor() throws IOException {
        popScope(Scope.CONSTRUCTOR);
        indent();
        this.out.write("}\n");
        return this;
    }

    public static String stringLiteral(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"') {
                sb.append("\\\"");
            } else if (charAt != '\\') {
                switch (charAt) {
                    case 8:
                        sb.append("\\b");
                        break;
                    case 9:
                        sb.append("\\t");
                        break;
                    case 10:
                        sb.append("\\n");
                        break;
                    default:
                        switch (charAt) {
                            case 12:
                                sb.append("\\f");
                                break;
                            case 13:
                                sb.append("\\r");
                                break;
                            default:
                                if (!Character.isISOControl(charAt)) {
                                    sb.append(charAt);
                                    break;
                                } else {
                                    sb.append(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                                    break;
                                }
                        }
                }
            } else {
                sb.append("\\\\");
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    public static String type(Class<?> cls, String... strArr) {
        if (strArr.length == 0) {
            return cls.getCanonicalName();
        }
        if (cls.getTypeParameters().length == strArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append(cls.getCanonicalName());
            sb.append("<");
            sb.append(strArr[0]);
            for (int i = 1; i < strArr.length; i++) {
                sb.append(", ");
                sb.append(strArr[i]);
            }
            sb.append(">");
            return sb.toString();
        }
        throw new IllegalArgumentException();
    }

    public static String rawType(String str) {
        int indexOf = str.indexOf(60);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    public void close() throws IOException {
        this.out.close();
    }

    private void emitModifiers(Set<Modifier> set) throws IOException {
        if (!set.isEmpty()) {
            if (!(set instanceof EnumSet)) {
                set = EnumSet.copyOf(set);
            }
            for (Modifier modifier : set) {
                this.out.append(modifier.toString()).append(' ');
            }
        }
    }

    private void indent() throws IOException {
        int size = this.scopes.size();
        for (int i = 0; i < size; i++) {
            this.out.write(this.indent);
        }
    }

    private void hangingIndent() throws IOException {
        int size = this.scopes.size() + 2;
        for (int i = 0; i < size; i++) {
            this.out.write(this.indent);
        }
    }

    private void checkInMethod() {
        if (!METHOD_SCOPES.contains(this.scopes.peekFirst())) {
            throw new IllegalArgumentException();
        }
    }

    private void popScope(Scope... scopeArr) {
        if (!EnumSet.copyOf(Arrays.asList(scopeArr)).contains(this.scopes.pop())) {
            throw new IllegalStateException();
        }
    }
}
