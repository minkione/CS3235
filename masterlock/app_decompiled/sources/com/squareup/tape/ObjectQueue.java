package com.squareup.tape;

public interface ObjectQueue<T> {

    public interface Listener<T> {
        void onAdd(ObjectQueue<T> objectQueue, T t);

        void onRemove(ObjectQueue<T> objectQueue);
    }

    void add(T t);

    T peek();

    void remove();

    void setListener(Listener<T> listener);

    int size();
}
