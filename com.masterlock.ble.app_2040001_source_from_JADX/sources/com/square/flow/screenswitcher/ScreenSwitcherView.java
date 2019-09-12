package com.square.flow.screenswitcher;

import android.content.Context;
import android.view.ViewGroup;

public interface ScreenSwitcherView extends CanShowScreen {
    ViewGroup getContainerView();

    Context getContext();

    ViewGroup getCurrentChild();
}
