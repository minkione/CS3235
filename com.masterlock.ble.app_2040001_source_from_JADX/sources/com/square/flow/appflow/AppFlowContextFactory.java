package com.square.flow.appflow;

import android.content.Context;
import flow.Screen;
import javax.annotation.Nullable;

public final class AppFlowContextFactory implements ScreenContextFactory {
    @Nullable
    private final ScreenContextFactory delegate;

    public AppFlowContextFactory() {
        this.delegate = null;
    }

    public AppFlowContextFactory(ScreenContextFactory screenContextFactory) {
        this.delegate = screenContextFactory;
    }

    public Context createContext(Screen screen, Context context) {
        ScreenContextFactory screenContextFactory = this.delegate;
        if (screenContextFactory != null) {
            context = screenContextFactory.createContext(screen, context);
        }
        return AppFlow.setScreen(context, screen);
    }

    public void destroyContext(Context context) {
        ScreenContextFactory screenContextFactory = this.delegate;
        if (screenContextFactory != null) {
            screenContextFactory.destroyContext(context);
        }
    }
}
