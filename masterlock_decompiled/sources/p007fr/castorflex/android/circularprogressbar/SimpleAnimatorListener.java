package p007fr.castorflex.android.circularprogressbar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.support.annotation.CallSuper;

/* renamed from: fr.castorflex.android.circularprogressbar.SimpleAnimatorListener */
abstract class SimpleAnimatorListener implements AnimatorListener {
    private boolean mCancelled = false;
    private boolean mStarted = false;

    public void onAnimationRepeat(Animator animator) {
    }

    /* access modifiers changed from: protected */
    public void onPreAnimationEnd(Animator animator) {
    }

    SimpleAnimatorListener() {
    }

    @CallSuper
    public void onAnimationStart(Animator animator) {
        this.mCancelled = false;
        this.mStarted = true;
    }

    public final void onAnimationEnd(Animator animator) {
        onPreAnimationEnd(animator);
        this.mStarted = false;
    }

    @CallSuper
    public void onAnimationCancel(Animator animator) {
        this.mCancelled = true;
    }

    public boolean isStartedAndNotCancelled() {
        return this.mStarted && !this.mCancelled;
    }
}
