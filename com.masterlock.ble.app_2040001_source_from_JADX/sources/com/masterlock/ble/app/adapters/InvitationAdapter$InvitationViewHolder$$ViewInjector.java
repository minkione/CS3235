package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.daimajia.swipe.SwipeLayout;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.InvitationAdapter.InvitationViewHolder;

public class InvitationAdapter$InvitationViewHolder$$ViewInjector {
    public static void inject(Finder finder, final InvitationViewHolder invitationViewHolder, Object obj) {
        invitationViewHolder.container = finder.findRequiredView(obj, C1075R.C1077id.guest_list_item_container, "field 'container'");
        invitationViewHolder.name = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_guest_name, "field 'name'");
        invitationViewHolder.coowner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_coowner, "field 'coowner'");
        invitationViewHolder.activity = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_guest_activity, "field 'activity'");
        invitationViewHolder.access = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_access, "field 'access'");
        invitationViewHolder.swipe = (SwipeLayout) finder.findRequiredView(obj, C1075R.C1077id.swipe, "field 'swipe'");
        invitationViewHolder.delete = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.img_delete, "field 'delete'");
        finder.findRequiredView(obj, C1075R.C1077id.frame, "method 'click'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                invitationViewHolder.click(view);
            }
        });
    }

    public static void reset(InvitationViewHolder invitationViewHolder) {
        invitationViewHolder.container = null;
        invitationViewHolder.name = null;
        invitationViewHolder.coowner = null;
        invitationViewHolder.activity = null;
        invitationViewHolder.access = null;
        invitationViewHolder.swipe = null;
        invitationViewHolder.delete = null;
    }
}
