package com.masterlock.mlclientapi;

public class MlUser {
    private MlKey mKey;
    private MlNonce mNonce;

    public MlUser(MlKey mlKey) {
        this.mKey = mlKey;
    }

    public MlKey getKey() {
        return this.mKey;
    }

    public void setKey(MlKey mlKey) {
        this.mKey = mlKey;
    }

    public MlNonce getNonce() {
        return this.mNonce;
    }

    public void setNonce(MlNonce mlNonce) {
        this.mNonce = mlNonce;
    }

    public void advanceNonce() {
        this.mNonce.advance();
    }
}
