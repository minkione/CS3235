package com.masterlock.ble.app.view.lock.generic;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class GenericLockDetailsView$$ViewInjector {
    public static void inject(Finder finder, final GenericLockDetailsView genericLockDetailsView, Object obj) {
        genericLockDetailsView.mCombinationTV = (TextView) finder.findRequiredView(obj, C1075R.C1077id.generic_details_combination_tv, "field 'mCombinationTV'");
        genericLockDetailsView.mLockNameTV = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockNameTV'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_edit, "method 'goToEdit'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                genericLockDetailsView.goToEdit();
            }
        });
    }

    public static void reset(GenericLockDetailsView genericLockDetailsView) {
        genericLockDetailsView.mCombinationTV = null;
        genericLockDetailsView.mLockNameTV = null;
    }
}
