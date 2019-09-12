package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Absent extends Optional<Object> {
    static final Absent INSTANCE = new Absent();
    private static final long serialVersionUID = 0;

    public boolean equals(@Nullable Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return 1502476572;
    }

    public boolean isPresent() {
        return false;
    }

    @Nullable
    public Object orNull() {
        return null;
    }

    public String toString() {
        return "Optional.absent()";
    }

    private Absent() {
    }

    public Object get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    /* renamed from: or */
    public Object mo12171or(Object obj) {
        return Preconditions.checkNotNull(obj, "use Optional.orNull() instead of Optional.or(null)");
    }

    /* renamed from: or */
    public Optional<Object> mo12169or(Optional<?> optional) {
        return (Optional) Preconditions.checkNotNull(optional);
    }

    /* renamed from: or */
    public Object mo12170or(Supplier<?> supplier) {
        return Preconditions.checkNotNull(supplier.get(), "use Optional.orNull() instead of a Supplier that returns null");
    }

    public Set<Object> asSet() {
        return Collections.emptySet();
    }

    public <V> Optional<V> transform(Function<Object, V> function) {
        Preconditions.checkNotNull(function);
        return Optional.absent();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
