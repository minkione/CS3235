package com.square.flow.screenswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.masterlock.ble.app.C1075R;
import com.square.flow.appflow.AppFlow;
import com.square.flow.appflow.AppFlowContextFactory;
import com.square.flow.screenswitcher.SimpleSwitcher.Factory;
import flow.Flow.Callback;
import flow.Flow.Direction;
import flow.Screen;

public class FrameScreenSwitcherView extends FrameLayout implements HandlesBack, HandlesUp, ScreenSwitcherView {
    private final ScreenSwitcher container;
    /* access modifiers changed from: private */
    public boolean disabled;
    /* access modifiers changed from: private */
    public OnScreenSwitchedListener listener;
    private final UpAndBackHandler upAndBackHandler;

    public interface OnScreenSwitchedListener {
        void onComplete();
    }

    public ViewGroup getContainerView() {
        return this;
    }

    public FrameScreenSwitcherView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, new Factory(C1075R.C1077id.screen_switcher_tag, new AppFlowContextFactory()));
    }

    protected FrameScreenSwitcherView(Context context, AttributeSet attributeSet, ScreenSwitcher.Factory factory) {
        super(context, attributeSet);
        this.container = factory.createScreenSwitcher(this);
        this.upAndBackHandler = new UpAndBackHandler(AppFlow.get(context));
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return !this.disabled && super.dispatchTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public void showScreen(Screen screen, Direction direction, final Callback callback) {
        this.disabled = true;
        this.container.showScreen(screen, direction, new Callback() {
            public void onComplete() {
                if (FrameScreenSwitcherView.this.listener != null) {
                    FrameScreenSwitcherView.this.listener.onComplete();
                }
                callback.onComplete();
                FrameScreenSwitcherView.this.disabled = false;
            }
        });
    }

    public void setOnScreenSwitchedListener(OnScreenSwitchedListener onScreenSwitchedListener) {
        this.listener = onScreenSwitchedListener;
    }

    public boolean onUpPressed() {
        return this.upAndBackHandler.onUpPressed(getCurrentChild());
    }

    public boolean onBackPressed() {
        return this.upAndBackHandler.onBackPressed(getCurrentChild());
    }

    public ViewGroup getCurrentChild() {
        return (ViewGroup) getContainerView().getChildAt(0);
    }
}
