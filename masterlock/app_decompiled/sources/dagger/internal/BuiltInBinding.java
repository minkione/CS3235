package dagger.internal;

final class BuiltInBinding<T> extends Binding<T> {
    private final ClassLoader classLoader;
    private Binding<?> delegate;
    private final String delegateKey;

    public BuiltInBinding(String str, Object obj, ClassLoader classLoader2, String str2) {
        super(str, null, false, obj);
        this.classLoader = classLoader2;
        this.delegateKey = str2;
    }

    public void attach(Linker linker) {
        this.delegate = linker.requestBinding(this.delegateKey, this.requiredBy, this.classLoader);
    }

    public void injectMembers(T t) {
        throw new UnsupportedOperationException();
    }

    public T get() {
        return this.delegate;
    }

    public Binding<?> getDelegate() {
        return this.delegate;
    }
}
