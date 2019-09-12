package com.masterlock.ble.app.view.lock.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class SecondaryCodeUpdateKeySafeView$$ViewInjector {
    public static void reset(SecondaryCodeUpdateKeySafeView secondaryCodeUpdateKeySafeView) {
    }

    public static void inject(Finder finder, final SecondaryCodeUpdateKeySafeView secondaryCodeUpdateKeySafeView, Object obj) {
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_code, "method 'saveSecondaryCode'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.saveSecondaryCode();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_1, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_2, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_3, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_4, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_5, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_6, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_7, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_8, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_9, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.d_pad_button_0, "method 'onDPadButtonClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.onDPadButtonClicked(view);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.secondary_code_delete_button, "method 'deleteCodeEntry'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                secondaryCodeUpdateKeySafeView.deleteCodeEntry();
            }
        });
    }
}
