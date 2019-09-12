package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AdjustRelockTimeKeySafeView$$ViewInjector {
    public static void reset(AdjustRelockTimeKeySafeView adjustRelockTimeKeySafeView) {
    }

    public static void inject(Finder finder, final AdjustRelockTimeKeySafeView adjustRelockTimeKeySafeView, Object obj) {
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_relock_time, "method 'saveRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                adjustRelockTimeKeySafeView.saveRelockTime();
            }
        });
    }
}
