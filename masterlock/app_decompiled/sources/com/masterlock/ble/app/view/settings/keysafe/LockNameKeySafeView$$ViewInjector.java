package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LockNameKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final LockNameKeySafeView lockNameKeySafeView, Object obj) {
        lockNameKeySafeView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'deviceId'");
        lockNameKeySafeView.mLockName = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.lock_name_floating_edit_text, "field 'mLockName'");
        lockNameKeySafeView.mLockSuggestionListFirst = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_first, "field 'mLockSuggestionListFirst'");
        lockNameKeySafeView.mLockSuggestionListSecond = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_second, "field 'mLockSuggestionListSecond'");
        lockNameKeySafeView.mLockSuggestionListThird = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_third, "field 'mLockSuggestionListThird'");
        lockNameKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_save_name, "field 'mSaveNameButton' and method 'onSaveClicked'");
        lockNameKeySafeView.mSaveNameButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockNameKeySafeView.onSaveClicked();
            }
        });
    }

    public static void reset(LockNameKeySafeView lockNameKeySafeView) {
        lockNameKeySafeView.deviceId = null;
        lockNameKeySafeView.mLockName = null;
        lockNameKeySafeView.mLockSuggestionListFirst = null;
        lockNameKeySafeView.mLockSuggestionListSecond = null;
        lockNameKeySafeView.mLockSuggestionListThird = null;
        lockNameKeySafeView.mLockNameBanner = null;
        lockNameKeySafeView.mSaveNameButton = null;
    }
}
