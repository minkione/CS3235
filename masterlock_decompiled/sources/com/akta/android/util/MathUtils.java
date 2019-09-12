package com.akta.android.util;

import android.graphics.Point;
import android.graphics.PointF;

public class MathUtils {
    private static final int MAX_ANGLE = 360;

    public static double normalizeAngle(double d) {
        if (d <= 360.0d) {
            return d < 0.0d ? 360.0d - Math.abs(d) : d;
        }
        double d2 = (double) ((((int) d) / MAX_ANGLE) * MAX_ANGLE);
        Double.isNaN(d2);
        return d - d2;
    }

    public static Point pointOnEllipse(double d, int i, int i2) {
        Point point = new Point();
        double radians = Math.toRadians(d);
        double d2 = (double) (i * i2);
        double d3 = (double) i2;
        double cos = Math.cos(radians);
        Double.isNaN(d3);
        double d4 = cos * d3;
        double cos2 = Math.cos(radians);
        Double.isNaN(d3);
        double d5 = d4 * cos2 * d3;
        double d6 = (double) i;
        double sin = Math.sin(radians);
        Double.isNaN(d6);
        double d7 = sin * d6;
        double sin2 = Math.sin(radians);
        Double.isNaN(d6);
        double sqrt = Math.sqrt(d5 + (d7 * sin2 * d6));
        Double.isNaN(d2);
        double d8 = d2 / sqrt;
        Double.isNaN(d6);
        double d9 = d8 / 2.0d;
        point.x = (int) ((d6 / 2.0d) + (Math.cos(radians) * d9));
        Double.isNaN(d3);
        point.y = (int) ((d3 / 2.0d) + (d9 * Math.sin(radians)));
        return point;
    }

    public static PointF pointOnCircle(float f, float f2, float f3) {
        PointF pointF = new PointF();
        PointF pointF2 = new PointF();
        float radians = (float) Math.toRadians((double) f);
        float f4 = f2 <= f3 ? f2 / 2.0f : f3 / 2.0f;
        pointF2.x = f2 / 2.0f;
        pointF2.y = f3 / 2.0f;
        double d = (double) radians;
        pointF.x = pointF2.x + (((float) Math.cos(d)) * f4);
        pointF.y = pointF2.y + (f4 * ((float) Math.sin(d)));
        return pointF;
    }
}
