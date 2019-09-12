package com.square.flow.appflow;

import android.content.Context;
import flow.Screen;

public interface ScreenContextFactory {
    Context createContext(Screen screen, Context context);

    void destroyContext(Context context);
}
