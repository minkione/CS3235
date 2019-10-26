package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangeNameView$$ViewInjector {
    public static void inject(Finder finder, final ChangeNameView changeNameView, Object obj) {
        changeNameView.mFirstNameInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.first_name_edit_text, "field 'mFirstNameInput'");
        changeNameView.mLastNameInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.last_name_edit_text, "field 'mLastNameInput'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.change_name_continue_btn, "field 'mContinueBtn' and method 'updateProfileName'");
        changeNameView.mContinueBtn = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeNameView.updateProfileName();
            }
        });
    }

    public static void reset(ChangeNameView changeNameView) {
        changeNameView.mFirstNameInput = null;
        changeNameView.mLastNameInput = null;
        changeNameView.mContinueBtn = null;
    }
}
