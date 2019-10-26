package com.masterlock.ble.app.adapters;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.LockAdapter.HeaderViewHolder;

public class LockAdapter$HeaderViewHolder$$ViewInjector {
    public static void inject(Finder finder, HeaderViewHolder headerViewHolder, Object obj) {
        headerViewHolder.header = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_header_title, "field 'header'");
    }

    public static void reset(HeaderViewHolder headerViewHolder) {
        headerViewHolder.header = null;
    }
}
