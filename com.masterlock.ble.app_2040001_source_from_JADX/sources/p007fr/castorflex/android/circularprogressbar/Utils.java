package p007fr.castorflex.android.circularprogressbar;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import java.util.Locale;

/* renamed from: fr.castorflex.android.circularprogressbar.Utils */
class Utils {
    private Utils() {
    }

    static void checkSpeed(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Speed must be >= 0");
        }
    }

    static void checkColors(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            throw new IllegalArgumentException("You must provide at least 1 color");
        }
    }

    static void checkAngle(int i) {
        if (i < 0 || i > 360) {
            throw new IllegalArgumentException(String.format(Locale.US, "Illegal angle %d: must be >=0 and <=360", new Object[]{Integer.valueOf(i)}));
        }
    }

    static void checkPositiveOrZero(float f, String str) {
        if (f < 0.0f) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s %f must be positive", new Object[]{str, Float.valueOf(f)}));
        }
    }

    static void checkPositive(int i, String str) {
        if (i <= 0) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s must not be null", new Object[]{str}));
        }
    }

    static void checkNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s must be not null", new Object[]{str}));
        }
    }

    static float getAnimatedFraction(ValueAnimator valueAnimator) {
        if (VERSION.SDK_INT >= 23) {
            return valueAnimator.getAnimatedFraction();
        }
        return valueAnimator.getInterpolator().getInterpolation(Math.min(valueAnimator.getDuration() > 0 ? ((float) valueAnimator.getCurrentPlayTime()) / ((float) valueAnimator.getDuration()) : 0.0f, 1.0f));
    }

    @TargetApi(21)
    static boolean isPowerSaveModeEnabled(@NonNull PowerManager powerManager) {
        if (VERSION.SDK_INT < 21) {
            return false;
        }
        try {
            return powerManager.isPowerSaveMode();
        } catch (Exception unused) {
            return false;
        }
    }

    static PowerManager powerManager(Context context) {
        return (PowerManager) context.getSystemService("power");
    }
}
