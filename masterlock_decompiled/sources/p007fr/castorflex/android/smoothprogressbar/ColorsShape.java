package p007fr.castorflex.android.smoothprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/* renamed from: fr.castorflex.android.smoothprogressbar.ColorsShape */
public class ColorsShape extends Shape {
    private int[] mColors;
    private float mStrokeWidth;

    public ColorsShape(float f, int[] iArr) {
        this.mStrokeWidth = f;
        this.mColors = iArr;
    }

    public float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
    }

    public int[] getColors() {
        return this.mColors;
    }

    public void setColors(int[] iArr) {
        this.mColors = iArr;
    }

    public void draw(Canvas canvas, Paint paint) {
        float length = 1.0f / ((float) this.mColors.length);
        paint.setStrokeWidth(this.mStrokeWidth);
        int i = 0;
        for (int color : this.mColors) {
            paint.setColor(color);
            i++;
            canvas.drawLine(((float) i) * length * getWidth(), getHeight() / 2.0f, ((float) i) * length * getWidth(), getHeight() / 2.0f, paint);
        }
    }
}
