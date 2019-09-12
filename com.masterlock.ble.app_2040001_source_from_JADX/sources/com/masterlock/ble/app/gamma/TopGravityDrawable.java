package com.masterlock.ble.app.gamma;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class TopGravityDrawable extends BitmapDrawable {
    final int topPadding = 4;

    public TopGravityDrawable(Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
    }

    public void draw(Canvas canvas) {
        int ceil = (int) Math.ceil((double) (((float) canvas.getHeight()) / 2.0f));
        int ceil2 = (int) Math.ceil((double) (((float) getIntrinsicHeight()) / 2.0f));
        canvas.save();
        canvas.translate(0.0f, (float) ((-ceil) + ceil2 + 4));
        super.draw(canvas);
        canvas.restore();
    }
}
