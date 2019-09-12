package com.masterlock.ble.app.view.modal;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class DateTimeSelectionModal$$ViewInjector {
    public static void inject(Finder finder, final DateTimeSelectionModal dateTimeSelectionModal, Object obj) {
        dateTimeSelectionModal.datePicker = (DatePicker) finder.findRequiredView(obj, C1075R.C1077id.dp_date, "field 'datePicker'");
        dateTimeSelectionModal.timePicker = (TimePicker) finder.findRequiredView(obj, C1075R.C1077id.tp_time, "field 'timePicker'");
        dateTimeSelectionModal.tVTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'tVTitle'");
        finder.findRequiredView(obj, C1075R.C1077id.positive_button, "method 'onClickPositiveButton'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dateTimeSelectionModal.onClickPositiveButton();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.negative_button, "method 'onClickNegativeButton'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dateTimeSelectionModal.onClickNegativeButton();
            }
        });
    }

    public static void reset(DateTimeSelectionModal dateTimeSelectionModal) {
        dateTimeSelectionModal.datePicker = null;
        dateTimeSelectionModal.timePicker = null;
        dateTimeSelectionModal.tVTitle = null;
    }
}
