package dagger.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ArrayQueue<E> extends AbstractCollection<E> implements Queue<E>, Cloneable, Serializable {
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 2340985798034038923L;
    /* access modifiers changed from: private */
    public transient Object[] elements;
    /* access modifiers changed from: private */
    public transient int head;
    /* access modifiers changed from: private */
    public transient int tail;

    private class QueueIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private QueueIterator() {
            this.cursor = ArrayQueue.this.head;
            this.fence = ArrayQueue.this.tail;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (this.cursor != this.fence) {
                E e = ArrayQueue.this.elements[this.cursor];
                if (ArrayQueue.this.tail != this.fence || e == null) {
                    throw new ConcurrentModificationException();
                }
                int i = this.cursor;
                this.lastRet = i;
                this.cursor = (i + 1) & (ArrayQueue.this.elements.length - 1);
                return e;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.lastRet;
            if (i >= 0) {
                if (ArrayQueue.this.delete(i)) {
                    this.cursor = (this.cursor - 1) & (ArrayQueue.this.elements.length - 1);
                    this.fence = ArrayQueue.this.tail;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }

    private void allocateElements(int i) {
        int i2 = 8;
        if (i >= 8) {
            int i3 = i | (i >>> 1);
            int i4 = i3 | (i3 >>> 2);
            int i5 = i4 | (i4 >>> 4);
            int i6 = i5 | (i5 >>> 8);
            i2 = (i6 | (i6 >>> 16)) + 1;
            if (i2 < 0) {
                i2 >>>= 1;
            }
        }
        this.elements = new Object[i2];
    }

    private void doubleCapacity() {
        int i = this.head;
        Object[] objArr = this.elements;
        int length = objArr.length;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            Object[] objArr2 = new Object[i3];
            System.arraycopy(objArr, i, objArr2, 0, i2);
            System.arraycopy(this.elements, 0, objArr2, i2, i);
            this.elements = objArr2;
            this.head = 0;
            this.tail = length;
            return;
        }
        throw new IllegalStateException("Sorry, queue too big");
    }

    public ArrayQueue() {
        this.elements = new Object[16];
    }

    public ArrayQueue(int i) {
        allocateElements(i);
    }

    public ArrayQueue(Collection<? extends E> collection) {
        allocateElements(collection.size());
        addAll(collection);
    }

    public boolean add(E e) {
        if (e != null) {
            Object[] objArr = this.elements;
            int i = this.tail;
            objArr[i] = e;
            int length = (objArr.length - 1) & (i + 1);
            this.tail = length;
            if (length == this.head) {
                doubleCapacity();
            }
            return true;
        }
        throw new NullPointerException("e == null");
    }

    public boolean offer(E e) {
        return add(e);
    }

    public E remove() {
        E poll = poll();
        if (poll != null) {
            return poll;
        }
        throw new NoSuchElementException();
    }

    public E poll() {
        int i = this.head;
        E[] eArr = this.elements;
        E e = eArr[i];
        if (e == null) {
            return null;
        }
        eArr[i] = null;
        this.head = (i + 1) & (eArr.length - 1);
        return e;
    }

    public E element() {
        E e = this.elements[this.head];
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }

    public E peek() {
        return this.elements[this.head];
    }

    /* access modifiers changed from: private */
    public boolean delete(int i) {
        Object[] objArr = this.elements;
        int length = objArr.length - 1;
        int i2 = this.head;
        int i3 = this.tail;
        int i4 = (i - i2) & length;
        int i5 = (i3 - i) & length;
        if (i4 >= ((i3 - i2) & length)) {
            throw new ConcurrentModificationException();
        } else if (i4 < i5) {
            if (i2 <= i) {
                System.arraycopy(objArr, i2, objArr, i2 + 1, i4);
            } else {
                System.arraycopy(objArr, 0, objArr, 1, i);
                objArr[0] = objArr[length];
                System.arraycopy(objArr, i2, objArr, i2 + 1, length - i2);
            }
            objArr[i2] = null;
            this.head = (i2 + 1) & length;
            return false;
        } else {
            if (i < i3) {
                System.arraycopy(objArr, i + 1, objArr, i, i5);
                this.tail = i3 - 1;
            } else {
                System.arraycopy(objArr, i + 1, objArr, i, length - i);
                objArr[length] = objArr[0];
                System.arraycopy(objArr, 1, objArr, 0, i3);
                this.tail = (i3 - 1) & length;
            }
            return true;
        }
    }

    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                delete(i);
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public void clear() {
        int i = this.head;
        int i2 = this.tail;
        if (i != i2) {
            this.tail = 0;
            this.head = 0;
            int length = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & length;
            } while (i != i2);
        }
    }

    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    public <T> T[] toArray(T[] tArr) {
        int size = size();
        if (tArr.length < size) {
            tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
        }
        int i = this.head;
        int i2 = this.tail;
        if (i < i2) {
            System.arraycopy(this.elements, i, tArr, 0, size());
        } else if (i > i2) {
            Object[] objArr = this.elements;
            int length = objArr.length - i;
            System.arraycopy(objArr, i, tArr, 0, length);
            System.arraycopy(this.elements, 0, tArr, length, this.tail);
        }
        if (tArr.length > size) {
            tArr[size] = null;
        }
        return tArr;
    }

    public ArrayQueue<E> clone() {
        try {
            ArrayQueue<E> arrayQueue = (ArrayQueue) super.clone();
            Object[] objArr = (Object[]) Array.newInstance(this.elements.getClass().getComponentType(), this.elements.length);
            System.arraycopy(this.elements, 0, objArr, 0, this.elements.length);
            arrayQueue.elements = objArr;
            return arrayQueue;
        } catch (CloneNotSupportedException unused) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        int length = this.elements.length - 1;
        for (int i = this.head; i != this.tail; i = (i + 1) & length) {
            objectOutputStream.writeObject(this.elements[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        allocateElements(readInt);
        this.head = 0;
        this.tail = readInt;
        for (int i = 0; i < readInt; i++) {
            this.elements[i] = objectInputStream.readObject();
        }
    }
}
