package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class SimpleDialog$$ViewInjector {
    public static void inject(Finder finder, SimpleDialog simpleDialog, Object obj) {
        simpleDialog.message = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txt_message);
        simpleDialog.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
        simpleDialog.negativeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'negativeButton'");
    }

    public static void reset(SimpleDialog simpleDialog) {
        simpleDialog.message = null;
        simpleDialog.positiveButton = null;
        simpleDialog.negativeButton = null;
    }
}
