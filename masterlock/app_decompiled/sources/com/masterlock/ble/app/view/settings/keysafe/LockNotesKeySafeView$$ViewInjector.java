package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LockNotesKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final LockNotesKeySafeView lockNotesKeySafeView, Object obj) {
        lockNotesKeySafeView.mLockNotes = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.lock_notes_floating_edit_text, "field 'mLockNotes'");
        lockNotesKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        lockNotesKeySafeView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_notes, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockNotesKeySafeView.onSaveClicked();
            }
        });
    }

    public static void reset(LockNotesKeySafeView lockNotesKeySafeView) {
        lockNotesKeySafeView.mLockNotes = null;
        lockNotesKeySafeView.mLockNameBanner = null;
        lockNotesKeySafeView.textDeviceId = null;
    }
}
