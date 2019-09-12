package com.masterlock.ble.app.view.lock.dialspeed;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class DialSpeedDetailsView$$ViewInjector {
    public static void inject(Finder finder, final DialSpeedDetailsView dialSpeedDetailsView, Object obj) {
        dialSpeedDetailsView.mLockNameTV = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockNameTV'");
        dialSpeedDetailsView.mCodesRV = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.dialspeed_rv, "field 'mCodesRV'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.dialspeed_edit_codes_btn, "field 'mEditCodesBtn' and method 'goToEditCodes'");
        dialSpeedDetailsView.mEditCodesBtn = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialSpeedDetailsView.goToEditCodes();
            }
        });
    }

    public static void reset(DialSpeedDetailsView dialSpeedDetailsView) {
        dialSpeedDetailsView.mLockNameTV = null;
        dialSpeedDetailsView.mCodesRV = null;
        dialSpeedDetailsView.mEditCodesBtn = null;
    }
}
