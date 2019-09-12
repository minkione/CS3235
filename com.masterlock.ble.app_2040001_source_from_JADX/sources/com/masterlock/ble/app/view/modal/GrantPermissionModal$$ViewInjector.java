package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class GrantPermissionModal$$ViewInjector {
    public static void inject(Finder finder, GrantPermissionModal grantPermissionModal, Object obj) {
        grantPermissionModal.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'mTitle'");
        grantPermissionModal.mBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'mBody'");
        grantPermissionModal.mBodyContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.body_container, "field 'mBodyContainer'");
        grantPermissionModal.bTGrantPermission = (Button) finder.findRequiredView(obj, C1075R.C1077id.bt_grant_permission, "field 'bTGrantPermission'");
        grantPermissionModal.tVSkipForNow = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_skip_for_now, "field 'tVSkipForNow'");
        grantPermissionModal.iVPermission = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.iv_permission, "field 'iVPermission'");
    }

    public static void reset(GrantPermissionModal grantPermissionModal) {
        grantPermissionModal.mTitle = null;
        grantPermissionModal.mBody = null;
        grantPermissionModal.mBodyContainer = null;
        grantPermissionModal.bTGrantPermission = null;
        grantPermissionModal.tVSkipForNow = null;
        grantPermissionModal.iVPermission = null;
    }
}
