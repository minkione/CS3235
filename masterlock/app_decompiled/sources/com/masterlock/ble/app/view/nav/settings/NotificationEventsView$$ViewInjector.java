package com.masterlock.ble.app.view.nav.settings;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class NotificationEventsView$$ViewInjector {
    public static void inject(Finder finder, final NotificationEventsView notificationEventsView, Object obj) {
        notificationEventsView.rVNotificationEvents = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.rv_notification_events, "field 'rVNotificationEvents'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save, "method 'updateNotificationSettings'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                notificationEventsView.updateNotificationSettings();
            }
        });
    }

    public static void reset(NotificationEventsView notificationEventsView) {
        notificationEventsView.rVNotificationEvents = null;
    }
}
