package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class FluentIterable<E> implements Iterable<E> {
    private final Iterable<E> iterable;

    private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
        private FromIterableFunction() {
        }

        public FluentIterable<E> apply(Iterable<E> iterable) {
            return FluentIterable.from(iterable);
        }
    }

    protected FluentIterable() {
        this.iterable = this;
    }

    FluentIterable(Iterable<E> iterable2) {
        this.iterable = (Iterable) Preconditions.checkNotNull(iterable2);
    }

    public static <E> FluentIterable<E> from(final Iterable<E> iterable2) {
        return iterable2 instanceof FluentIterable ? (FluentIterable) iterable2 : new FluentIterable<E>(iterable2) {
            public Iterator<E> iterator() {
                return iterable2.iterator();
            }
        };
    }

    @Deprecated
    public static <E> FluentIterable<E> from(FluentIterable<E> fluentIterable) {
        return (FluentIterable) Preconditions.checkNotNull(fluentIterable);
    }

    public String toString() {
        return Iterables.toString(this.iterable);
    }

    public final int size() {
        return Iterables.size(this.iterable);
    }

    public final boolean contains(@Nullable Object obj) {
        return Iterables.contains(this.iterable, obj);
    }

    @CheckReturnValue
    public final FluentIterable<E> cycle() {
        return from(Iterables.cycle(this.iterable));
    }

    @CheckReturnValue
    public final FluentIterable<E> filter(Predicate<? super E> predicate) {
        return from(Iterables.filter(this.iterable, predicate));
    }

    @CheckReturnValue
    @GwtIncompatible("Class.isInstance")
    public final <T> FluentIterable<T> filter(Class<T> cls) {
        return from(Iterables.filter(this.iterable, cls));
    }

    public final boolean anyMatch(Predicate<? super E> predicate) {
        return Iterables.any(this.iterable, predicate);
    }

    public final boolean allMatch(Predicate<? super E> predicate) {
        return Iterables.all(this.iterable, predicate);
    }

    public final Optional<E> firstMatch(Predicate<? super E> predicate) {
        return Iterables.tryFind(this.iterable, predicate);
    }

    public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
        return from(Iterables.transform(this.iterable, function));
    }

    public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
        return from(Iterables.concat((Iterable<? extends Iterable<? extends T>>) transform(function)));
    }

    public final Optional<E> first() {
        Iterator it = this.iterable.iterator();
        return it.hasNext() ? Optional.m41of(it.next()) : Optional.absent();
    }

    public final Optional<E> last() {
        Object next;
        Iterable<E> iterable2 = this.iterable;
        if (iterable2 instanceof List) {
            List list = (List) iterable2;
            if (list.isEmpty()) {
                return Optional.absent();
            }
            return Optional.m41of(list.get(list.size() - 1));
        }
        Iterator it = iterable2.iterator();
        if (!it.hasNext()) {
            return Optional.absent();
        }
        Iterable<E> iterable3 = this.iterable;
        if (iterable3 instanceof SortedSet) {
            return Optional.m41of(((SortedSet) iterable3).last());
        }
        do {
            next = it.next();
        } while (it.hasNext());
        return Optional.m41of(next);
    }

    @CheckReturnValue
    public final FluentIterable<E> skip(int i) {
        return from(Iterables.skip(this.iterable, i));
    }

    @CheckReturnValue
    public final FluentIterable<E> limit(int i) {
        return from(Iterables.limit(this.iterable, i));
    }

    public final boolean isEmpty() {
        return !this.iterable.iterator().hasNext();
    }

    public final ImmutableList<E> toList() {
        return ImmutableList.copyOf(this.iterable);
    }

    @Beta
    public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
        return Ordering.from(comparator).immutableSortedCopy(this.iterable);
    }

    public final ImmutableSet<E> toSet() {
        return ImmutableSet.copyOf(this.iterable);
    }

    public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
        return ImmutableSortedSet.copyOf(comparator, this.iterable);
    }

    public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> function) {
        return Maps.toMap(this.iterable, function);
    }

    public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> function) {
        return Multimaps.index(this.iterable, function);
    }

    public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> function) {
        return Maps.uniqueIndex(this.iterable, function);
    }

    @GwtIncompatible("Array.newArray(Class, int)")
    public final E[] toArray(Class<E> cls) {
        return Iterables.toArray(this.iterable, cls);
    }

    public final <C extends Collection<? super E>> C copyInto(C c) {
        Preconditions.checkNotNull(c);
        Iterable<E> iterable2 = this.iterable;
        if (iterable2 instanceof Collection) {
            c.addAll(Collections2.cast(iterable2));
        } else {
            for (E add : iterable2) {
                c.add(add);
            }
        }
        return c;
    }

    public final E get(int i) {
        return Iterables.get(this.iterable, i);
    }
}
