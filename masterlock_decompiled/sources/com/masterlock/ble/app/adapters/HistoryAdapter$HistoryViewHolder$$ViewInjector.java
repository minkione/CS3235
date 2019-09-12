package com.masterlock.ble.app.adapters;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.HistoryAdapter.HistoryViewHolder;

public class HistoryAdapter$HistoryViewHolder$$ViewInjector {
    public static void inject(Finder finder, HistoryViewHolder historyViewHolder, Object obj) {
        historyViewHolder.container = finder.findRequiredView(obj, C1075R.C1077id.container, "field 'container'");
        historyViewHolder.name = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_history_name, "field 'name'");
        historyViewHolder.message = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_history_message, "field 'message'");
        historyViewHolder.image = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_history_icon, "field 'image'");
    }

    public static void reset(HistoryViewHolder historyViewHolder) {
        historyViewHolder.container = null;
        historyViewHolder.name = null;
        historyViewHolder.message = null;
        historyViewHolder.image = null;
    }
}
