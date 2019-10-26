package com.masterlock.ble.app.presenter.nav.settings;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.NotificationItemRvAdapter.NotificationEventViewHolder;

/* renamed from: com.masterlock.ble.app.presenter.nav.settings.NotificationItemRvAdapter$NotificationEventViewHolder$$ViewInjector */
public class C1300x71475762 {
    public static void inject(Finder finder, NotificationEventViewHolder notificationEventViewHolder, Object obj) {
        notificationEventViewHolder.tVEventName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_event_name, "field 'tVEventName'");
        notificationEventViewHolder.iVEmailNotification = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.ib_email_notification, "field 'iVEmailNotification'");
        notificationEventViewHolder.iVSMSNotification = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.ib_sms_notification, "field 'iVSMSNotification'");
    }

    public static void reset(NotificationEventViewHolder notificationEventViewHolder) {
        notificationEventViewHolder.tVEventName = null;
        notificationEventViewHolder.iVEmailNotification = null;
        notificationEventViewHolder.iVSMSNotification = null;
    }
}
