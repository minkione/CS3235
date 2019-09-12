package com.masterlock.ble.app.adapters;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.DialSpeedCodesAdapter.DialCodeViewHolder;

public class DialSpeedCodesAdapter$DialCodeViewHolder$$ViewInjector {
    public static void inject(Finder finder, DialCodeViewHolder dialCodeViewHolder, Object obj) {
        dialCodeViewHolder.codeNameTV = (TextView) finder.findRequiredView(obj, C1075R.C1077id.code_name_tv, "field 'codeNameTV'");
        dialCodeViewHolder.codeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.backup_code_container, "field 'codeContainer'");
    }

    public static void reset(DialCodeViewHolder dialCodeViewHolder) {
        dialCodeViewHolder.codeNameTV = null;
        dialCodeViewHolder.codeContainer = null;
    }
}
