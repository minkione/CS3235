package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class NoShacklePermissionDialog$$ViewInjector {
    public static void inject(Finder finder, NoShacklePermissionDialog noShacklePermissionDialog, Object obj) {
        noShacklePermissionDialog.mPositiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'mPositiveButton'");
    }

    public static void reset(NoShacklePermissionDialog noShacklePermissionDialog) {
        noShacklePermissionDialog.mPositiveButton = null;
    }
}
