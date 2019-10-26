package p007fr.castorflex.android.circularprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.UiThread;
import p007fr.castorflex.android.circularprogressbar.CircularProgressDrawable.OnEndListener;

/* renamed from: fr.castorflex.android.circularprogressbar.PBDelegate */
interface PBDelegate {
    @UiThread
    void draw(Canvas canvas, Paint paint);

    @UiThread
    void progressiveStop(OnEndListener onEndListener);

    @UiThread
    void start();

    @UiThread
    void stop();
}
