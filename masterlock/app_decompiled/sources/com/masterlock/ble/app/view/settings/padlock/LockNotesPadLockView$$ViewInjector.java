package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class LockNotesPadLockView$$ViewInjector {
    public static void inject(Finder finder, final LockNotesPadLockView lockNotesPadLockView, Object obj) {
        lockNotesPadLockView.mLockNotes = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.lock_notes_floating_edit_text, "field 'mLockNotes'");
        lockNotesPadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        lockNotesPadLockView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_notes, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockNotesPadLockView.onSaveClicked();
            }
        });
    }

    public static void reset(LockNotesPadLockView lockNotesPadLockView) {
        lockNotesPadLockView.mLockNotes = null;
        lockNotesPadLockView.mLockNameBanner = null;
        lockNotesPadLockView.textDeviceId = null;
    }
}
