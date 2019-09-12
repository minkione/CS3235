package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;

class DescendingImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    private final ImmutableSortedSet<E> forward;

    DescendingImmutableSortedSet(ImmutableSortedSet<E> immutableSortedSet) {
        super(Ordering.from(immutableSortedSet.comparator()).reverse());
        this.forward = immutableSortedSet;
    }

    public int size() {
        return this.forward.size();
    }

    public UnmodifiableIterator<E> iterator() {
        return this.forward.descendingIterator();
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return this.forward.tailSet(e, z).descendingSet();
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return this.forward.subSet(e2, z2, e, z).descendingSet();
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return this.forward.headSet(e, z).descendingSet();
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> descendingSet() {
        return this.forward;
    }

    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return this.forward.iterator();
    }

    /* access modifiers changed from: 0000 */
    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> createDescendingSet() {
        throw new AssertionError("should never be called");
    }

    public E lower(E e) {
        return this.forward.higher(e);
    }

    public E floor(E e) {
        return this.forward.ceiling(e);
    }

    public E ceiling(E e) {
        return this.forward.floor(e);
    }

    public E higher(E e) {
        return this.forward.lower(e);
    }

    /* access modifiers changed from: 0000 */
    public int indexOf(@Nullable Object obj) {
        int indexOf = this.forward.indexOf(obj);
        if (indexOf == -1) {
            return indexOf;
        }
        return (size() - 1) - indexOf;
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return this.forward.isPartialView();
    }
}