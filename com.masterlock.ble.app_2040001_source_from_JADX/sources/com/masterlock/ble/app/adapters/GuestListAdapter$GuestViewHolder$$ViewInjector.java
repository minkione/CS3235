package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.GuestListAdapter.GuestViewHolder;

public class GuestListAdapter$GuestViewHolder$$ViewInjector {
    public static void inject(Finder finder, final GuestViewHolder guestViewHolder, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.txt_guest_name, "field 'mGuestName' and method 'onClick'");
        guestViewHolder.mGuestName = (TextView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                guestViewHolder.onClick(view);
            }
        });
    }

    public static void reset(GuestViewHolder guestViewHolder) {
        guestViewHolder.mGuestName = null;
    }
}
