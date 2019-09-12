package com.masterlock.ble.app.view.guest;

import android.support.p003v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ExistingGuestListView$$ViewInjector {
    public static void inject(Finder finder, ExistingGuestListView existingGuestListView, Object obj) {
        existingGuestListView.mLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockName'");
        existingGuestListView.mLockId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_id, "field 'mLockId'");
        existingGuestListView.mRecycler = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.recycler_view, "field 'mRecycler'");
    }

    public static void reset(ExistingGuestListView existingGuestListView) {
        existingGuestListView.mLockName = null;
        existingGuestListView.mLockId = null;
        existingGuestListView.mRecycler = null;
    }
}
