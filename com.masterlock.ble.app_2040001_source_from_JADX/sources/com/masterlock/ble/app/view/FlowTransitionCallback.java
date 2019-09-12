package com.masterlock.ble.app.view;

import flow.Flow.Direction;

public interface FlowTransitionCallback {
    void transitionComplete(Direction direction);

    void transitionStarted(Direction direction);
}
