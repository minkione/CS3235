package com.akta.android.util;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.InputStream;

public class ImageUtils {
    public static int getPowerOfTwoForSampleRatio(double d) {
        int highestOneBit = Integer.highestOneBit((int) Math.floor(d));
        if (highestOneBit == 0) {
            return 1;
        }
        return highestOneBit;
    }

    public static Options optionsForImage(InputStream inputStream) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Config.ARGB_8888;
        BitmapFactory.decodeStream(inputStream, null, options);
        return options;
    }
}
