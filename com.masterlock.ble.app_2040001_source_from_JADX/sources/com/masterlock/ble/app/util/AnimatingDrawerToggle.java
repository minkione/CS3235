package com.masterlock.ble.app.util;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.support.p000v4.widget.DrawerLayout;
import android.support.p003v7.app.ActionBarDrawerToggle;
import android.support.p003v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

public class AnimatingDrawerToggle extends ActionBarDrawerToggle {
    private static final float ARROW_POSITION = 1.0f;
    private static final float MENU_POSITION = 0.0f;
    private final Activity activity;
    private State currentState = State.HOME;
    private final DrawerLayout drawerLayout;
    private final int duration;

    public enum State {
        UP,
        HOME
    }

    public AnimatingDrawerToggle(Activity activity2, DrawerLayout drawerLayout2, Toolbar toolbar, int i, int i2) {
        super(activity2, drawerLayout2, toolbar, i, i2);
        this.duration = activity2.getResources().getInteger(17694720);
        this.drawerLayout = drawerLayout2;
        this.activity = activity2;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void animateToUp() {
        if (this.currentState != State.UP) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, ARROW_POSITION});
            ofFloat.addUpdateListener(new AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AnimatingDrawerToggle.this.onDrawerSlide(AnimatingDrawerToggle.this.drawerLayout, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.setDuration((long) this.duration);
            ofFloat.start();
            this.currentState = State.UP;
        }
    }

    public void animateToHome() {
        if (this.currentState != State.HOME) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{ARROW_POSITION, 0.0f});
            ofFloat.addUpdateListener(new AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AnimatingDrawerToggle.this.onDrawerSlide(AnimatingDrawerToggle.this.drawerLayout, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.setDuration((long) this.duration);
            ofFloat.start();
            this.currentState = State.HOME;
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332 || this.currentState != State.UP) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.activity.onBackPressed();
        return true;
    }

    public void syncState() {
        super.syncState();
        if (this.currentState == State.UP) {
            onDrawerSlide(this.drawerLayout, ARROW_POSITION);
        }
    }
}
