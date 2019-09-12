package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class Memoizer<K, V> {
    private final Map<K, V> map = new LinkedHashMap();
    private final Lock readLock;
    private final Lock writeLock;

    /* access modifiers changed from: protected */
    public abstract V create(K k);

    public Memoizer() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    public final V get(K k) {
        if (k != null) {
            this.readLock.lock();
            try {
                V v = this.map.get(k);
                if (v != null) {
                    return v;
                }
                this.readLock.unlock();
                V create = create(k);
                if (create != null) {
                    this.writeLock.lock();
                    try {
                        this.map.put(k, create);
                        return create;
                    } finally {
                        this.writeLock.unlock();
                    }
                } else {
                    throw new NullPointerException("create returned null");
                }
            } finally {
                this.readLock.unlock();
            }
        } else {
            throw new NullPointerException("key == null");
        }
    }

    public final String toString() {
        this.readLock.lock();
        try {
            return this.map.toString();
        } finally {
            this.readLock.unlock();
        }
    }
}
