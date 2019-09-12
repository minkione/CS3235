package com.square.flow.screenswitcher;

import android.view.ViewGroup;
import com.square.flow.appflow.ScreenContextFactory;
import com.square.flow.util.ObjectUtils;
import com.square.flow.util.Preconditions;
import flow.Flow.Callback;
import flow.Flow.Direction;
import flow.Layout;
import flow.Screen;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ScreenSwitcher {
    private static final Map<Class, Integer> SCREEN_LAYOUT_CACHE = new LinkedHashMap();
    protected final int tagKey;
    private final ScreenSwitcherView view;

    public static abstract class Factory {
        protected final ScreenContextFactory contextFactory;
        protected final int tagKey;

        public abstract ScreenSwitcher createScreenSwitcher(ScreenSwitcherView screenSwitcherView);

        public Factory(int i, ScreenContextFactory screenContextFactory) {
            this.tagKey = i;
            this.contextFactory = screenContextFactory;
        }
    }

    protected static final class Tag {
        public Screen fromScreen;
        public Screen toScreen;

        protected Tag() {
        }

        public void setNextScreen(Screen screen) {
            this.fromScreen = this.toScreen;
            this.toScreen = screen;
        }
    }

    /* access modifiers changed from: protected */
    public abstract void transition(ViewGroup viewGroup, Screen screen, Screen screen2, Direction direction, Callback callback);

    protected ScreenSwitcher(ScreenSwitcherView screenSwitcherView, int i) {
        this.view = screenSwitcherView;
        this.tagKey = i;
    }

    public void showScreen(Screen screen, Direction direction, Callback callback) {
        Screen screen2;
        ViewGroup currentChild = this.view.getCurrentChild();
        if (currentChild != null) {
            Screen screen3 = (Screen) Preconditions.checkNotNull(((Tag) this.view.getContainerView().getTag(this.tagKey)).toScreen, "Container view has child %s with no screen", currentChild.toString());
            if (screen3.getName().equals(screen.getName())) {
                callback.onComplete();
                return;
            }
            screen2 = screen3;
        } else {
            screen2 = null;
        }
        transition(this.view.getContainerView(), screen2, screen, direction, callback);
    }

    /* access modifiers changed from: protected */
    public Tag ensureTag(ViewGroup viewGroup) {
        Tag tag = (Tag) viewGroup.getTag(this.tagKey);
        if (tag != null) {
            return tag;
        }
        Tag tag2 = new Tag();
        viewGroup.setTag(this.tagKey, tag2);
        return tag2;
    }

    protected static int getLayout(Object obj) {
        Class cls = ObjectUtils.getClass(obj);
        Integer num = (Integer) SCREEN_LAYOUT_CACHE.get(cls);
        if (num == null) {
            Layout layout = (Layout) cls.getAnnotation(Layout.class);
            Preconditions.checkNotNull(layout, "@%s annotation not found on class %s", Layout.class.getSimpleName(), cls.getName());
            num = Integer.valueOf(layout.value());
            SCREEN_LAYOUT_CACHE.put(cls, num);
        }
        return num.intValue();
    }
}
