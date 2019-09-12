package com.masterlock.ble.app.bus;

import com.masterlock.ble.app.presenter.Presenter;

public class LastLocationEvent {
    private int mapId;
    private Presenter presenterOnMapReady;

    public Presenter getPresenterOnMapReady() {
        return this.presenterOnMapReady;
    }

    public int getMapId() {
        return this.mapId;
    }

    public LastLocationEvent(Presenter presenter, int i) {
        this.presenterOnMapReady = presenter;
        this.mapId = i;
    }
}
