package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangeUserNameView$$ViewInjector {
    public static void inject(Finder finder, final ChangeUserNameView changeUserNameView, Object obj) {
        changeUserNameView.mUsernameInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.username_edit_text, "field 'mUsernameInput'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.change_username_save_btn, "field 'mContinueBtn' and method 'sendName'");
        changeUserNameView.mContinueBtn = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeUserNameView.sendName();
            }
        });
    }

    public static void reset(ChangeUserNameView changeUserNameView) {
        changeUserNameView.mUsernameInput = null;
        changeUserNameView.mContinueBtn = null;
    }
}
