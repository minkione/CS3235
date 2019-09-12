package com.masterlock.ble.app.gamma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import com.masterlock.ble.app.C1075R;

public class FillImageProgressBar extends View {
    private static final int DOWN_TOP = 1;
    private static final int LEFT_RIGHT = 2;
    private static final String LOG_TAG = "FILL IMAGE PROGRESS BAR";
    private static final int RIGHT_LEFT = 3;
    private static final int TOP_DOWN = 0;
    private int fillOrientation;
    private int filledSectionColor;
    int height;
    /* access modifiers changed from: private */
    public int imageSource;
    private Bitmap mBitmap;
    private Context mContext;
    private IndeterminateProgress mIndeterminateProgress;
    private Paint mProgressPaint;
    /* access modifiers changed from: private */
    public int progress;
    int progressEndX = 0;
    int progressEndY = 0;
    /* access modifiers changed from: private */
    public int progressMax;
    float scale = 0.0f;
    private int unfilledSectionColor;
    int width;

    private class IndeterminateProgress extends AsyncTask<Void, Integer, Void> {
        int loopCount;

        private IndeterminateProgress() {
            this.loopCount = 0;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            FillImageProgressBar.this.setProgressMax(10);
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            FillImageProgressBar.this.setProgress(numArr[0].intValue());
            FillImageProgressBar.this.invalidate();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            int i = 0;
            while (true) {
                try {
                    if (i <= FillImageProgressBar.this.progressMax) {
                        int i2 = i + 1;
                        publishProgress(new Integer[]{Integer.valueOf(i)});
                        Thread.sleep(100);
                        i = i2;
                    } else {
                        i = 0;
                    }
                } catch (Exception unused) {
                    return null;
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled(Void voidR) {
            super.onCancelled(voidR);
            FillImageProgressBar fillImageProgressBar = FillImageProgressBar.this;
            fillImageProgressBar.resetToFixed(fillImageProgressBar.imageSource);
            FillImageProgressBar fillImageProgressBar2 = FillImageProgressBar.this;
            fillImageProgressBar2.progress = fillImageProgressBar2.progressMax;
            FillImageProgressBar.this.invalidate();
        }
    }

    public void setProgress(int i) {
        this.progress = i;
        invalidate();
    }

    public void setProgressMax(int i) {
        this.progressMax = i;
    }

    public int getProgressMax() {
        return this.progressMax;
    }

    public void setFilledSectionColor(int i) {
        this.filledSectionColor = i;
    }

    public void setUnfilledSectionColor(int i) {
        this.unfilledSectionColor = i;
    }

    public void setFillOrientation(int i) {
        this.fillOrientation = i;
    }

    public void setImageSource(int i) {
        this.imageSource = i;
    }

    public FillImageProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setAttributes(attributeSet);
    }

    private void setAttributes(AttributeSet attributeSet) {
        this.mProgressPaint = new Paint();
        this.mProgressPaint.setStyle(Style.FILL_AND_STROKE);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, C1075R.styleable.FillImageProgressBar, 0, 0);
        try {
            setFilledSectionColor(obtainStyledAttributes.getColor(1, -16776961));
            setUnfilledSectionColor(obtainStyledAttributes.getColor(3, ViewCompat.MEASURED_STATE_MASK));
            setFillOrientation(obtainStyledAttributes.getInt(0, 0));
            setImageSource(obtainStyledAttributes.getResourceId(2, 0));
            setBackgroundColor(this.unfilledSectionColor);
            this.mBitmap = BitmapFactory.decodeResource(getResources(), this.imageSource);
        } catch (Exception unused) {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        int size3 = MeasureSpec.getSize(this.mBitmap.getWidth());
        int size4 = MeasureSpec.getSize(this.mBitmap.getHeight());
        if (mode == Integer.MIN_VALUE) {
            this.width = Math.min(size, size3);
        } else if (mode != 1073741824) {
            this.width = size3;
        } else {
            this.width = size;
        }
        if (mode2 == Integer.MIN_VALUE) {
            this.height = Math.min(size2, size4);
        } else if (mode2 != 1073741824) {
            this.height = size4;
        } else {
            this.height = size2;
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Measured: width: ");
        sb.append(this.width);
        sb.append(", height: ");
        sb.append(this.height);
        Log.d(str, sb.toString());
        setMeasuredDimension(this.width, this.height);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        try {
            switch (this.fillOrientation) {
                case 0:
                    Log.d(LOG_TAG, "Vertical filling top_down");
                    this.scale = ((float) this.height) / ((float) this.progressMax);
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Scale: ");
                    sb.append(this.scale);
                    sb.append(", height: ");
                    sb.append(this.height);
                    sb.append(", progressMax: ");
                    sb.append(this.progressMax);
                    Log.d(str, sb.toString());
                    this.progressEndY = Math.round(this.scale) * this.progress;
                    this.mProgressPaint.setStrokeWidth((float) this.width);
                    this.mProgressPaint.setColor(this.filledSectionColor);
                    canvas.drawLine((float) (getWidth() / 2), 0.0f, (float) (getWidth() / 2), (float) this.progressEndY, this.mProgressPaint);
                    this.mProgressPaint.setStrokeWidth((float) this.width);
                    this.mProgressPaint.setColor(this.unfilledSectionColor);
                    canvas.drawLine((float) (getWidth() / 2), (float) this.progressEndY, (float) (getWidth() / 2), (float) getHeight(), this.mProgressPaint);
                    break;
                case 1:
                    Log.d(LOG_TAG, "Vertical filling down_top");
                    this.scale = ((float) this.height) / ((float) this.progressMax);
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Scale: ");
                    sb2.append(this.scale);
                    sb2.append(", height: ");
                    sb2.append(this.height);
                    sb2.append(", progressMax: ");
                    sb2.append(this.progressMax);
                    Log.d(str2, sb2.toString());
                    this.progressEndY = Math.round(this.scale) * this.progress;
                    this.mProgressPaint.setStrokeWidth((float) this.width);
                    this.mProgressPaint.setColor(this.filledSectionColor);
                    canvas.drawLine((float) (getWidth() / 2), (float) getHeight(), (float) (getWidth() / 2), (float) (getHeight() - this.progressEndY), this.mProgressPaint);
                    this.mProgressPaint.setStrokeWidth((float) this.width);
                    this.mProgressPaint.setColor(this.unfilledSectionColor);
                    canvas.drawLine((float) (getWidth() / 2), 0.0f, (float) (getWidth() / 2), (float) (getHeight() - this.progressEndY), this.mProgressPaint);
                    break;
                case 2:
                    Log.d(LOG_TAG, "Horizontal filling left_right");
                    this.scale = ((float) this.width) / ((float) this.progressMax);
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Scale: ");
                    sb3.append(this.scale);
                    sb3.append(", height: ");
                    sb3.append(this.height);
                    sb3.append(", progressMax: ");
                    sb3.append(this.progressMax);
                    Log.d(str3, sb3.toString());
                    this.progressEndY = Math.round(this.scale) * this.progress;
                    this.mProgressPaint.setStrokeWidth((float) this.height);
                    this.mProgressPaint.setColor(this.filledSectionColor);
                    canvas.drawLine(0.0f, (float) (getHeight() / 2), (float) this.progressEndX, (float) (getHeight() / 2), this.mProgressPaint);
                    this.mProgressPaint.setStrokeWidth((float) this.height);
                    this.mProgressPaint.setColor(this.unfilledSectionColor);
                    canvas.drawLine((float) this.progressEndX, (float) (getHeight() / 2), (float) getWidth(), (float) (getHeight() / 2), this.mProgressPaint);
                    break;
                case 3:
                    Log.d(LOG_TAG, "Horizontal filling right_left");
                    this.scale = ((float) this.width) / ((float) this.progressMax);
                    String str4 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Scale: ");
                    sb4.append(this.scale);
                    sb4.append(", height: ");
                    sb4.append(this.height);
                    sb4.append(", progressMax: ");
                    sb4.append(this.progressMax);
                    Log.d(str4, sb4.toString());
                    this.progressEndY = Math.round(this.scale) * this.progress;
                    this.mProgressPaint.setStrokeWidth((float) this.height);
                    this.mProgressPaint.setColor(this.filledSectionColor);
                    canvas.drawLine((float) getWidth(), (float) (getHeight() / 2), (float) (getWidth() - this.progressEndX), (float) (getHeight() / 2), this.mProgressPaint);
                    this.mProgressPaint.setStrokeWidth((float) this.height);
                    this.mProgressPaint.setColor(this.unfilledSectionColor);
                    canvas.drawLine((float) (getWidth() - this.progressEndX), (float) (getHeight() / 2), 0.0f, (float) (getHeight() / 2), this.mProgressPaint);
                    break;
            }
        } catch (Exception unused) {
        }
        String str5 = LOG_TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Progress x: ");
        sb5.append(this.progressEndX);
        sb5.append(", progress y: ");
        sb5.append(this.progressEndY);
        Log.d(str5, sb5.toString());
        canvas.drawBitmap(this.mBitmap, (float) ((canvas.getWidth() - this.mBitmap.getWidth()) / 2), (float) ((canvas.getHeight() - this.mBitmap.getHeight()) / 2), null);
    }

    public void resetToFixed(int i) {
        this.progress = 0;
        this.mBitmap = BitmapFactory.decodeResource(getResources(), i);
        invalidate();
    }

    public void resetToProgress(int i) {
        this.progress = 0;
        this.mBitmap = BitmapFactory.decodeResource(getResources(), i);
        invalidate();
    }

    public void fillToMax() {
        if (!this.mIndeterminateProgress.isCancelled()) {
            this.mIndeterminateProgress.cancel(true);
        }
    }

    public void setIndeterminate(boolean z) {
        if (!z) {
            IndeterminateProgress indeterminateProgress = this.mIndeterminateProgress;
            if (indeterminateProgress != null) {
                indeterminateProgress.cancel(true);
                this.mIndeterminateProgress = null;
            }
        } else if (this.mIndeterminateProgress == null) {
            this.mIndeterminateProgress = new IndeterminateProgress();
            this.mIndeterminateProgress.execute(new Void[0]);
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(NotificationCompat.CATEGORY_PROGRESS, this.progress);
        bundle.putParcelable("superState", super.onSaveInstanceState());
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            setProgress(bundle.getInt(NotificationCompat.CATEGORY_PROGRESS));
            parcelable = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(parcelable);
    }
}
