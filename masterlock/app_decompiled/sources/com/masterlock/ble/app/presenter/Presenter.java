package com.masterlock.ble.app.presenter;

public abstract class Presenter<M, V> {
    protected M model;
    protected long modelId;
    protected V view;

    public abstract void start();

    protected Presenter(V v) {
        if (v != null) {
            this.view = v;
            return;
        }
        throw new IllegalArgumentException("View must not be null");
    }

    protected Presenter(long j, V v) {
        this(v);
        if (j > 0) {
            this.modelId = j;
            return;
        }
        throw new IllegalArgumentException("Model Id must be greater than 0");
    }

    protected Presenter(M m, V v) {
        this(v);
        if (m != null) {
            this.model = m;
            return;
        }
        throw new IllegalArgumentException("Model must be not be null");
    }

    public void finish() {
        this.model = null;
        this.view = null;
    }
}
