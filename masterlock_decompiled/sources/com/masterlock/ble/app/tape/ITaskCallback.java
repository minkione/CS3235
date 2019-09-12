package com.masterlock.ble.app.tape;

public interface ITaskCallback {
    void onFailure(Throwable th);

    void onSuccess();
}
