package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LockNamePadLockView$$ViewInjector {
    public static void inject(Finder finder, final LockNamePadLockView lockNamePadLockView, Object obj) {
        lockNamePadLockView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'deviceId'");
        lockNamePadLockView.mLockName = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.lock_name_floating_edit_text, "field 'mLockName'");
        lockNamePadLockView.mLockSuggestionListFirst = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_first, "field 'mLockSuggestionListFirst'");
        lockNamePadLockView.mLockSuggestionListSecond = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_second, "field 'mLockSuggestionListSecond'");
        lockNamePadLockView.mLockSuggestionListThird = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_suggestion_list_third, "field 'mLockSuggestionListThird'");
        lockNamePadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_save_name, "field 'mSaveNameButton' and method 'onSaveClicked'");
        lockNamePadLockView.mSaveNameButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockNamePadLockView.onSaveClicked();
            }
        });
    }

    public static void reset(LockNamePadLockView lockNamePadLockView) {
        lockNamePadLockView.deviceId = null;
        lockNamePadLockView.mLockName = null;
        lockNamePadLockView.mLockSuggestionListFirst = null;
        lockNamePadLockView.mLockSuggestionListSecond = null;
        lockNamePadLockView.mLockSuggestionListThird = null;
        lockNamePadLockView.mLockNameBanner = null;
        lockNamePadLockView.mSaveNameButton = null;
    }
}
