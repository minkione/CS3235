package org.zff.ble.communication;

import org.zff.ble.communication.message.CommMessage;

public class GattCommInterfaces {

    public interface OnGattInputStream {
        void onReadRssi(int i);

        void onReceiverData(CommMessage commMessage);
    }

    public interface OnGattOutputStream {
        void onSendData(CommMessage commMessage);
    }

    public interface OnGattStateListener {
        void onConnected();

        void onDisconnected();

        void onServiceDiscoverFail();

        void onServiceDiscoverSuccess();
    }
}
