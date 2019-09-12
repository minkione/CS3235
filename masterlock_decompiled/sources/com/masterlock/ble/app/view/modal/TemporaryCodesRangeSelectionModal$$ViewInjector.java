package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class TemporaryCodesRangeSelectionModal$$ViewInjector {
    public static void inject(Finder finder, TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal, Object obj) {
        temporaryCodesRangeSelectionModal.datePicker = (DatePicker) finder.findRequiredView(obj, C1075R.C1077id.dp_date, "field 'datePicker'");
        temporaryCodesRangeSelectionModal.bTPositive = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'bTPositive'");
        temporaryCodesRangeSelectionModal.bTNegative = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'bTNegative'");
        temporaryCodesRangeSelectionModal.tVTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_title, "field 'tVTitle'");
        temporaryCodesRangeSelectionModal.tVSelectedDate = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_selected_date, "field 'tVSelectedDate'");
        temporaryCodesRangeSelectionModal.rangeItemContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.ll_range_item_container, "field 'rangeItemContainer'");
        temporaryCodesRangeSelectionModal.tVTimeZoneRemainder = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_time_zone_remainder, "field 'tVTimeZoneRemainder'");
    }

    public static void reset(TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal) {
        temporaryCodesRangeSelectionModal.datePicker = null;
        temporaryCodesRangeSelectionModal.bTPositive = null;
        temporaryCodesRangeSelectionModal.bTNegative = null;
        temporaryCodesRangeSelectionModal.tVTitle = null;
        temporaryCodesRangeSelectionModal.tVSelectedDate = null;
        temporaryCodesRangeSelectionModal.rangeItemContainer = null;
        temporaryCodesRangeSelectionModal.tVTimeZoneRemainder = null;
    }
}
