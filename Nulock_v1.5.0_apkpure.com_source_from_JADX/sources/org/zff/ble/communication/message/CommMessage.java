package org.zff.ble.communication.message;

public abstract class CommMessage {
    public int mCmdId = 0;
    public int mStreamId = 0;
    public byte[] sendBuffer = null;

    public abstract void receiverData(byte[] bArr);

    public abstract void sendData(byte[] bArr);
}
