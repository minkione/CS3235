package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {

    @Beta
    protected class StandardKeySet extends SortedKeySet<K, V> {
        public StandardKeySet() {
            super(ForwardingSortedMap.this);
        }
    }

    /* access modifiers changed from: protected */
    public abstract SortedMap<K, V> delegate();

    protected ForwardingSortedMap() {
    }

    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    public K firstKey() {
        return delegate().firstKey();
    }

    public SortedMap<K, V> headMap(K k) {
        return delegate().headMap(k);
    }

    public K lastKey() {
        return delegate().lastKey();
    }

    public SortedMap<K, V> subMap(K k, K k2) {
        return delegate().subMap(k, k2);
    }

    public SortedMap<K, V> tailMap(K k) {
        return delegate().tailMap(k);
    }

    private int unsafeCompare(Object obj, Object obj2) {
        Comparator comparator = comparator();
        if (comparator == null) {
            return ((Comparable) obj).compareTo(obj2);
        }
        return comparator.compare(obj, obj2);
    }

    /* access modifiers changed from: protected */
    @Beta
    public boolean standardContainsKey(@Nullable Object obj) {
        boolean z = false;
        try {
            if (unsafeCompare(tailMap(obj).firstKey(), obj) == 0) {
                z = true;
            }
            return z;
        } catch (ClassCastException unused) {
            return false;
        } catch (NoSuchElementException unused2) {
            return false;
        } catch (NullPointerException unused3) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    @Beta
    public V standardRemove(@Nullable Object obj) {
        try {
            Iterator it = tailMap(obj).entrySet().iterator();
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (unsafeCompare(entry.getKey(), obj) == 0) {
                    V value = entry.getValue();
                    it.remove();
                    return value;
                }
            }
            return null;
        } catch (ClassCastException unused) {
            return null;
        } catch (NullPointerException unused2) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    @Beta
    public SortedMap<K, V> standardSubMap(K k, K k2) {
        Preconditions.checkArgument(unsafeCompare(k, k2) <= 0, "fromKey must be <= toKey");
        return tailMap(k).headMap(k2);
    }
}
