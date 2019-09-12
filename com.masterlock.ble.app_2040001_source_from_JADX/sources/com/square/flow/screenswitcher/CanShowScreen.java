package com.square.flow.screenswitcher;

import flow.Flow.Callback;
import flow.Flow.Direction;
import flow.Screen;

public interface CanShowScreen {
    void showScreen(Screen screen, Direction direction, Callback callback);
}
