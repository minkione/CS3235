package com.masterlock.ble.app.view.lock.keysafe;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangeSecondaryCodesKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final ChangeSecondaryCodesKeySafeView changeSecondaryCodesKeySafeView, Object obj) {
        changeSecondaryCodesKeySafeView.recyclerView = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.recycler_secondary_codes, "field 'recyclerView'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_add_secondary_code, "field 'btnAddSecondaryCode' and method 'onClickContinueButton'");
        changeSecondaryCodesKeySafeView.btnAddSecondaryCode = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeSecondaryCodesKeySafeView.onClickContinueButton();
            }
        });
        changeSecondaryCodesKeySafeView.stateText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.state_text, "field 'stateText'");
    }

    public static void reset(ChangeSecondaryCodesKeySafeView changeSecondaryCodesKeySafeView) {
        changeSecondaryCodesKeySafeView.recyclerView = null;
        changeSecondaryCodesKeySafeView.btnAddSecondaryCode = null;
        changeSecondaryCodesKeySafeView.stateText = null;
    }
}
