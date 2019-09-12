package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class DeleteLastLocationDialog$$ViewInjector {
    public static void inject(Finder finder, DeleteLastLocationDialog deleteLastLocationDialog, Object obj) {
        deleteLastLocationDialog.mClearLastLocation = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'mClearLastLocation'");
        deleteLastLocationDialog.mCancelButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'mCancelButton'");
    }

    public static void reset(DeleteLastLocationDialog deleteLastLocationDialog) {
        deleteLastLocationDialog.mClearLastLocation = null;
        deleteLastLocationDialog.mCancelButton = null;
    }
}
