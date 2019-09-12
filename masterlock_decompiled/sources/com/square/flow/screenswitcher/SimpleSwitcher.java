package com.square.flow.screenswitcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.masterlock.ble.app.view.FlowTransitionCallback;
import com.square.flow.appflow.ScreenContextFactory;
import com.square.flow.util.Utils;
import com.square.flow.util.Utils.OnMeasuredCallback;
import flow.Flow.Callback;
import flow.Flow.Direction;
import flow.Screen;

public class SimpleSwitcher extends ScreenSwitcher {
    /* access modifiers changed from: private */
    public final ScreenContextFactory contextFactory;

    public static final class Factory extends com.square.flow.screenswitcher.ScreenSwitcher.Factory {
        public Factory(int i, ScreenContextFactory screenContextFactory) {
            super(i, screenContextFactory);
        }

        public ScreenSwitcher createScreenSwitcher(ScreenSwitcherView screenSwitcherView) {
            return new SimpleSwitcher(screenSwitcherView, this.tagKey, this.contextFactory);
        }
    }

    SimpleSwitcher(ScreenSwitcherView screenSwitcherView, int i, ScreenContextFactory screenContextFactory) {
        super(screenSwitcherView, i);
        this.contextFactory = screenContextFactory;
    }

    /* access modifiers changed from: protected */
    public void transition(ViewGroup viewGroup, Screen screen, Screen screen2, Direction direction, Callback callback) {
        final PathContext pathContext;
        final View view;
        Tag ensureTag = ensureTag(viewGroup);
        if (viewGroup.getChildCount() > 0) {
            pathContext = PathContext.get(viewGroup.getChildAt(0).getContext());
        } else {
            pathContext = PathContext.empty(viewGroup.getContext());
        }
        final PathContext create = PathContext.create(pathContext, screen2, this.contextFactory);
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(create).cloneInContext(create).inflate(getLayout(screen2), viewGroup, false);
        ensureTag.setNextScreen(screen2);
        if (ensureTag.fromScreen != null) {
            View childAt = viewGroup.getChildAt(0);
            SparseArray sparseArray = new SparseArray();
            childAt.saveHierarchyState(sparseArray);
            ensureTag.fromScreen.setViewState(sparseArray);
            view = childAt;
        } else {
            view = null;
        }
        if (view == null || direction == Direction.REPLACE) {
            viewGroup.removeAllViews();
            viewGroup.addView(viewGroup2);
            ensureTag.toScreen.restoreHierarchyState(viewGroup.getChildAt(0));
            pathContext.destroyNotIn(create, this.contextFactory);
            callback.onComplete();
            if (viewGroup2 instanceof FlowTransitionCallback) {
                FlowTransitionCallback flowTransitionCallback = (FlowTransitionCallback) viewGroup2;
                flowTransitionCallback.transitionStarted(Direction.FORWARD);
                flowTransitionCallback.transitionComplete(Direction.FORWARD);
                return;
            }
            return;
        }
        viewGroup.addView(viewGroup2);
        if (direction == Direction.BACKWARD) {
            view.bringToFront();
        }
        ensureTag.toScreen.restoreHierarchyState(viewGroup.getChildAt(0));
        final ViewGroup viewGroup3 = viewGroup;
        final Direction direction2 = direction;
        final Callback callback2 = callback;
        C16911 r2 = new OnMeasuredCallback() {
            public void onMeasured(View view, int i, int i2) {
                SimpleSwitcher.this.runAnimation(viewGroup3, view, view, direction2, new Callback() {
                    public void onComplete() {
                        viewGroup3.removeView(view);
                        pathContext.destroyNotIn(create, SimpleSwitcher.this.contextFactory);
                        callback2.onComplete();
                    }
                });
            }
        };
        Utils.waitForMeasure(viewGroup2, r2);
    }

    /* access modifiers changed from: private */
    public void runAnimation(ViewGroup viewGroup, View view, View view2, Direction direction, Callback callback) {
        Animator createSegue = createSegue(view, view2, direction);
        final View view3 = view2;
        final View view4 = view;
        final ViewGroup viewGroup2 = viewGroup;
        final Callback callback2 = callback;
        C16932 r0 = new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                View view = view3;
                if (view instanceof FlowTransitionCallback) {
                    ((FlowTransitionCallback) view).transitionStarted(Direction.FORWARD);
                }
                View view2 = view4;
                if (view2 instanceof FlowTransitionCallback) {
                    ((FlowTransitionCallback) view2).transitionStarted(Direction.BACKWARD);
                }
            }

            public void onAnimationEnd(Animator animator) {
                View view = view3;
                if (view instanceof FlowTransitionCallback) {
                    ((FlowTransitionCallback) view).transitionComplete(Direction.FORWARD);
                }
                View view2 = view4;
                if (view2 instanceof FlowTransitionCallback) {
                    ((FlowTransitionCallback) view2).transitionComplete(Direction.BACKWARD);
                }
                viewGroup2.removeView(view4);
                callback2.onComplete();
            }
        };
        createSegue.addListener(r0);
        createSegue.start();
    }

    private Animator createSegue(View view, View view2, Direction direction) {
        boolean z = direction == Direction.BACKWARD;
        int width = z ? view.getWidth() : -view.getWidth();
        int width2 = z ? -view2.getWidth() : view2.getWidth();
        AnimatorSet animatorSet = new AnimatorSet();
        if (z) {
            animatorSet.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, new float[]{(float) width}));
            animatorSet.play(ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{((float) width2) / 3.0f, 0.0f}));
        } else {
            animatorSet.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, new float[]{((float) width) / 3.0f}));
            animatorSet.play(ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{(float) width2, 0.0f}));
        }
        return animatorSet;
    }
}
