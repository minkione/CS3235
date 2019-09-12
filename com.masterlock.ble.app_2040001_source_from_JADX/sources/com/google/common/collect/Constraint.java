package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Deprecated
@Beta
public interface Constraint<E> {
    E checkElement(E e);

    String toString();
}
