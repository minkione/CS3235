package dagger.internal;

import dagger.MembersInjector;
import java.util.Set;
import javax.inject.Provider;

public abstract class Binding<T> implements Provider<T>, MembersInjector<T> {
    private static final int CYCLE_FREE = 8;
    private static final int DEPENDED_ON = 16;
    protected static final boolean IS_SINGLETON = true;
    private static final int LIBRARY = 32;
    private static final int LINKED = 2;
    protected static final boolean NOT_SINGLETON = false;
    private static final int SINGLETON = 1;
    public static final Binding<Object> UNRESOLVED = new Binding<Object>(null, null, false, null) {
        public Object get() {
            throw new AssertionError("Unresolved binding should never be called to inject.");
        }

        public void injectMembers(Object obj) {
            throw new AssertionError("Unresolved binding should never be called to inject.");
        }
    };
    private static final int VISITING = 4;
    private int bits;
    public final String membersKey;
    public final String provideKey;
    public final Object requiredBy;

    public static class InvalidBindingException extends RuntimeException {
        public final String type;

        public InvalidBindingException(String str, String str2) {
            super(str2);
            this.type = str;
        }

        public InvalidBindingException(String str, String str2, Throwable th) {
            StringBuilder sb = new StringBuilder();
            sb.append("Binding for ");
            sb.append(str);
            sb.append(" was invalid: ");
            sb.append(str2);
            super(sb.toString(), th);
            this.type = str;
        }
    }

    public void attach(Linker linker) {
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
    }

    public void injectMembers(T t) {
    }

    protected Binding(String str, String str2, boolean z, Object obj) {
        if (!z || str != null) {
            this.provideKey = str;
            this.membersKey = str2;
            this.requiredBy = obj;
            this.bits = z ? 1 : 0;
            return;
        }
        throw new InvalidBindingException(Keys.getClassName(str2), "is exclusively members injected and therefore cannot be scoped");
    }

    public T get() {
        StringBuilder sb = new StringBuilder();
        sb.append("No injectable constructor on ");
        sb.append(getClass().getName());
        throw new UnsupportedOperationException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void setLinked() {
        this.bits |= 2;
    }

    public boolean isLinked() {
        return (this.bits & 2) != 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean isSingleton() {
        return (this.bits & 1) != 0;
    }

    public boolean isVisiting() {
        return (this.bits & 4) != 0;
    }

    public void setVisiting(boolean z) {
        this.bits = z ? this.bits | 4 : this.bits & -5;
    }

    public boolean isCycleFree() {
        return (this.bits & 8) != 0;
    }

    public void setCycleFree(boolean z) {
        this.bits = z ? this.bits | 8 : this.bits & -9;
    }

    public void setLibrary(boolean z) {
        this.bits = z ? this.bits | 32 : this.bits & -33;
    }

    public boolean library() {
        return (this.bits & 32) != 0;
    }

    public void setDependedOn(boolean z) {
        this.bits = z ? this.bits | 16 : this.bits & -17;
    }

    public boolean dependedOn() {
        return (this.bits & 16) != 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[provideKey=\"");
        sb.append(this.provideKey);
        sb.append("\", memberskey=\"");
        sb.append(this.membersKey);
        sb.append("\"]");
        return sb.toString();
    }
}
