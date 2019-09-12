package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ShowRelockTimeKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final ShowRelockTimeKeySafeView showRelockTimeKeySafeView, Object obj) {
        showRelockTimeKeySafeView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time, "field 'mRelockTime'");
        showRelockTimeKeySafeView.buttonBar = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'buttonBar'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_change_relock_time, "method 'changeRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                showRelockTimeKeySafeView.changeRelockTime();
            }
        });
    }

    public static void reset(ShowRelockTimeKeySafeView showRelockTimeKeySafeView) {
        showRelockTimeKeySafeView.mRelockTime = null;
        showRelockTimeKeySafeView.buttonBar = null;
    }
}
