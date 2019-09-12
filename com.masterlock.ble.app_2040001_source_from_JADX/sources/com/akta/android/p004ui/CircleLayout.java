package com.akta.android.p004ui;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import com.akta.android.util.MathUtils;

/* renamed from: com.akta.android.ui.CircleLayout */
public class CircleLayout extends EllipseLayout {
    public CircleLayout(Context context) {
        super(context);
    }

    public CircleLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Point pointOnEdge(double d) {
        LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        PointF pointOnCircle = MathUtils.pointOnCircle((float) d, ((float) right()) - ((float) layoutParams.width), ((float) bottom()) - ((float) layoutParams.height));
        return new Point(Math.round(pointOnCircle.x), Math.round(pointOnCircle.y));
    }
}
