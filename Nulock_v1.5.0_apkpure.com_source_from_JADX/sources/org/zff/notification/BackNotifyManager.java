package org.zff.notification;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import java.util.HashMap;
import java.util.Map;

public class BackNotifyManager {
    private final String TAG = "BackNotifyManager";
    private final String TITLE = "MyTitle";
    private String infoContent = "";
    private int infoFlag = 0;
    private String infoName = "";
    private Context mContext = null;
    private Map<String, Integer> mMapFlag = new HashMap();
    private Map<String, Integer> mMapPosition = new HashMap();
    private Notification mNotification = null;
    private NotificationManager mNotifyManager = null;
    private String mNotifyTestContent = "hello";

    public BackNotifyManager(Context context) {
        this.mContext = context;
        this.mNotifyManager = (NotificationManager) context.getSystemService("notification");
    }

    public boolean showNotification(String notifyName, int newStateFlag, String notifyTitle, String notifyContentText, int notifySmarllIcon, PendingIntent pendingIntent) {
        if (!this.mMapFlag.containsKey(notifyName)) {
            this.mMapFlag.put(notifyName, Integer.valueOf(newStateFlag));
            this.mMapPosition.put(notifyName, Integer.valueOf(this.mMapFlag.size()));
        } else if (((Integer) this.mMapFlag.get(notifyName)).intValue() == newStateFlag) {
            return false;
        } else {
            this.mMapFlag.put(notifyName, Integer.valueOf(newStateFlag));
        }
        int position = ((Integer) this.mMapPosition.get(notifyName)).intValue();
        this.mNotifyManager.cancel(position);
        this.mNotifyManager.notify(position, createNotification(notifyTitle, notifyContentText, notifySmarllIcon, pendingIntent));
        return true;
    }

    private Notification createNotification(String notifyTitle, String notifyContentText, int notifySmarllIcon, PendingIntent pendingIntent) {
        Builder builder = new Builder(this.mContext);
        builder.setAutoCancel(true);
        builder.setContentTitle(notifyTitle);
        builder.setContentText(notifyContentText);
        builder.setSmallIcon(notifySmarllIcon);
        builder.setDefaults(1);
        builder.setTicker(SmartLockDatabase.TABLE);
        builder.setContentIntent(pendingIntent);
        Notification nf = builder.build();
        Log.i("BackNotifyManager", "createNotification-->");
        return nf;
    }

    public void cancelNotification(String name) {
        int position = 0;
        if (this.mMapPosition.containsKey(name)) {
            position = ((Integer) this.mMapPosition.get(name)).intValue();
        }
        this.mNotifyManager.cancel(position);
        Log.i("BackNotifyManager", "cancel-->" + this.mNotification);
    }

    public void cancelAllNotification() {
        for (Integer intValue : this.mMapPosition.values()) {
            this.mNotifyManager.cancel(intValue.intValue());
        }
    }
}
