package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.daimajia.swipe.SwipeLayout;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.LockAdapter.LockViewHolder;

public class LockAdapter$LockViewHolder$$ViewInjector {
    public static void inject(Finder finder, final LockViewHolder lockViewHolder, Object obj) {
        lockViewHolder.container = finder.findRequiredView(obj, C1075R.C1077id.lock_list_item_container, "field 'container'");
        lockViewHolder.image = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_lock, "field 'image'");
        lockViewHolder.name = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'name'");
        lockViewHolder.coowner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_coowner, "field 'coowner'");
        lockViewHolder.activity = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_activity, "field 'activity'");
        lockViewHolder.batteryText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_battery_low, "field 'batteryText'");
        lockViewHolder.batteryIndicator = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.battery_indicator, "field 'batteryIndicator'");
        lockViewHolder.swipeImageContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.img_swipe_container, "field 'swipeImageContainer'");
        lockViewHolder.timerText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_timer, "field 'timerText'");
        lockViewHolder.swipe = (SwipeLayout) finder.findRequiredView(obj, C1075R.C1077id.swipe, "field 'swipe'");
        lockViewHolder.check = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_check, "field 'check'");
        lockViewHolder.swipeSmall = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_swipe_small, "field 'swipeSmall'");
        lockViewHolder.swipeMedium = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_swipe_medium, "field 'swipeMedium'");
        lockViewHolder.swipeLarge = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_swipe_large, "field 'swipeLarge'");
        lockViewHolder.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_device_Id, "field 'deviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.frame, "method 'click'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockViewHolder.click(view);
            }
        });
    }

    public static void reset(LockViewHolder lockViewHolder) {
        lockViewHolder.container = null;
        lockViewHolder.image = null;
        lockViewHolder.name = null;
        lockViewHolder.coowner = null;
        lockViewHolder.activity = null;
        lockViewHolder.batteryText = null;
        lockViewHolder.batteryIndicator = null;
        lockViewHolder.swipeImageContainer = null;
        lockViewHolder.timerText = null;
        lockViewHolder.swipe = null;
        lockViewHolder.check = null;
        lockViewHolder.swipeSmall = null;
        lockViewHolder.swipeMedium = null;
        lockViewHolder.swipeLarge = null;
        lockViewHolder.deviceId = null;
    }
}
