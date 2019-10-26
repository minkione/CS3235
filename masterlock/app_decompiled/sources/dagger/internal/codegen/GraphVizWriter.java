package dagger.internal.codegen;

import com.squareup.javawriter.JavaWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GraphVizWriter implements Closeable {
    private static final String INDENT = "  ";
    private final Map<String, String> generatedNames = new LinkedHashMap();
    private int indent = 0;
    private int nextName = 1;
    private final Writer out;

    public GraphVizWriter(Writer writer) {
        this.out = writer;
    }

    public void beginGraph(String... strArr) throws IOException {
        indent();
        String str = this.indent == 0 ? "digraph " : "subgraph ";
        String nextName2 = nextName(this.indent == 0 ? "G" : "cluster");
        this.out.write(str);
        this.out.write(nextName2);
        this.out.write(" {\n");
        this.indent++;
        attributes(strArr);
    }

    public void endGraph() throws IOException {
        this.indent--;
        indent();
        this.out.write("}\n");
    }

    public void node(String str, String... strArr) throws IOException {
        String nodeName = nodeName(str);
        indent();
        this.out.write(nodeName);
        inlineAttributes(strArr);
        this.out.write(";\n");
    }

    public void edge(String str, String str2, String... strArr) throws IOException {
        String nodeName = nodeName(str);
        String nodeName2 = nodeName(str2);
        indent();
        this.out.write(nodeName);
        this.out.write(" -> ");
        this.out.write(nodeName2);
        inlineAttributes(strArr);
        this.out.write(";\n");
    }

    public void nodeDefaults(String... strArr) throws IOException {
        if (strArr.length != 0) {
            indent();
            this.out.write("node");
            inlineAttributes(strArr);
            this.out.write(";\n");
        }
    }

    public void edgeDefaults(String... strArr) throws IOException {
        if (strArr.length != 0) {
            indent();
            this.out.write("edge");
            inlineAttributes(strArr);
            this.out.write(";\n");
        }
    }

    private void attributes(String[] strArr) throws IOException {
        if (strArr.length != 0) {
            if (strArr.length % 2 == 0) {
                for (int i = 0; i < strArr.length; i += 2) {
                    indent();
                    this.out.write(strArr[i]);
                    this.out.write(" = ");
                    this.out.write(literal(strArr[i + 1]));
                    this.out.write(";\n");
                }
                return;
            }
            throw new IllegalArgumentException();
        }
    }

    private void inlineAttributes(String[] strArr) throws IOException {
        if (strArr.length != 0) {
            if (strArr.length % 2 == 0) {
                this.out.write(" [");
                for (int i = 0; i < strArr.length; i += 2) {
                    if (i != 0) {
                        this.out.write(";");
                    }
                    this.out.write(strArr[i]);
                    this.out.write("=");
                    this.out.write(literal(strArr[i + 1]));
                }
                this.out.write("]");
                return;
            }
            throw new IllegalArgumentException();
        }
    }

    private String nodeName(String str) throws IOException {
        if (str.matches("\\w+")) {
            return str;
        }
        String str2 = (String) this.generatedNames.get(str);
        if (str2 != null) {
            return str2;
        }
        String nextName2 = nextName("n");
        this.generatedNames.put(str, nextName2);
        node(nextName2, "label", str);
        return nextName2;
    }

    private String literal(String str) {
        return str.matches("\\w+") ? str : JavaWriter.stringLiteral(str);
    }

    private void indent() throws IOException {
        for (int i = 0; i < this.indent; i++) {
            this.out.write(INDENT);
        }
    }

    private String nextName(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        int i = this.nextName;
        this.nextName = i + 1;
        sb.append(i);
        return sb.toString();
    }

    public void close() throws IOException {
        this.out.close();
    }
}
