package com.masterlock.ble.app.view.guest;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class InvitationListView$$ViewInjector {
    public static void inject(Finder finder, final InvitationListView invitationListView, Object obj) {
        invitationListView.mLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockName'");
        invitationListView.mDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'mDeviceId'");
        invitationListView.mEmptyView = finder.findRequiredView(obj, C1075R.C1077id.empty, "field 'mEmptyView'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.button_add_guest, "field 'mAddGuest' and method 'click'");
        invitationListView.mAddGuest = findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                invitationListView.click(view);
            }
        });
        invitationListView.mListView = (ListView) finder.findRequiredView(obj, C1075R.C1077id.list, "field 'mListView'");
    }

    public static void reset(InvitationListView invitationListView) {
        invitationListView.mLockName = null;
        invitationListView.mDeviceId = null;
        invitationListView.mEmptyView = null;
        invitationListView.mAddGuest = null;
        invitationListView.mListView = null;
    }
}
