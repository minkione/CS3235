package dagger.internal;

public abstract class ProvidesBinding<T> extends Binding<T> {
    protected final String methodName;
    protected final String moduleClass;

    public abstract T get();

    public ProvidesBinding(String str, boolean z, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(".");
        sb.append(str3);
        sb.append("()");
        super(str, null, z, sb.toString());
        this.moduleClass = str2;
        this.methodName = str3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[key=");
        sb.append(this.provideKey);
        sb.append(" method=");
        sb.append(this.moduleClass);
        sb.append(".");
        sb.append(this.methodName);
        sb.append("()");
        sb.append("]");
        return sb.toString();
    }
}
