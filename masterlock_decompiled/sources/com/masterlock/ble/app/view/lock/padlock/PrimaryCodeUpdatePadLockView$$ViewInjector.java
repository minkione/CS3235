package com.masterlock.ble.app.view.lock.padlock;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class PrimaryCodeUpdatePadLockView$$ViewInjector {
    public static void reset(PrimaryCodeUpdatePadLockView primaryCodeUpdatePadLockView) {
    }

    public static void inject(Finder finder, final PrimaryCodeUpdatePadLockView primaryCodeUpdatePadLockView, Object obj) {
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_code, "method 'savePrimaryCode'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                primaryCodeUpdatePadLockView.savePrimaryCode();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.primary_code_delete_button, "method 'deleteCodeEntry'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                primaryCodeUpdatePadLockView.deleteCodeEntry();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_right, "method 'dPadButtonRightClicked'").setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return primaryCodeUpdatePadLockView.dPadButtonRightClicked(motionEvent);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_down, "method 'dPadButtonDownClicked'").setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return primaryCodeUpdatePadLockView.dPadButtonDownClicked(motionEvent);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_left, "method 'dPadButtonLeftClicked'").setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return primaryCodeUpdatePadLockView.dPadButtonLeftClicked(motionEvent);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_up, "method 'dPadButtonUpClicked'").setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return primaryCodeUpdatePadLockView.dPadButtonUpClicked(motionEvent);
            }
        });
    }
}
