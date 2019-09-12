package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangeTimeZoneView$$ViewInjector {
    public static void inject(Finder finder, final ChangeTimeZoneView changeTimeZoneView, Object obj) {
        changeTimeZoneView.mTimezoneSpinner = (Spinner) finder.findRequiredView(obj, C1075R.C1077id.lock_timezones, "field 'mTimezoneSpinner'");
        ((CompoundButton) finder.findRequiredView(obj, C1075R.C1077id.sc_change_all_timezones, "method 'onCheckedChange'")).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                changeTimeZoneView.onCheckedChange(z);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.bt_save, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeTimeZoneView.onSaveClicked();
            }
        });
    }

    public static void reset(ChangeTimeZoneView changeTimeZoneView) {
        changeTimeZoneView.mTimezoneSpinner = null;
    }
}
