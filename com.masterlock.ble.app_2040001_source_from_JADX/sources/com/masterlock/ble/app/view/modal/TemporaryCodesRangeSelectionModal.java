package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.common.base.Strings;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.core.Lock;
import com.masterlock.core.TemporaryCodeRange;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TemporaryCodesRangeSelectionModal extends CardView {
    /* access modifiers changed from: private */
    public final String TAG;
    @InjectView(2131296631)
    Button bTNegative;
    @InjectView(2131296655)
    Button bTPositive;
    private Calendar calendar;
    private TemporaryCodeRange currentSelectedRange;
    private View currentSelectedRangeItem;
    @InjectView(2131296439)
    DatePicker datePicker;
    private String localizedLockTimeZoneName;
    private String lockTimeZoneId;
    /* access modifiers changed from: private */
    public ModalEventListener mModalEventListener;
    private ModalMode mModalMode;
    private OnClickListener onClickListener;
    private OnDateChangedListener onDateChangedListener;
    @InjectView(2131296585)
    LinearLayout rangeItemContainer;
    /* access modifiers changed from: private */
    public Date selectedDate;
    private String selectedRangeDate;
    @InjectView(2131296831)
    TextView tVSelectedDate;
    @InjectView(2131296837)
    TextView tVTimeZoneRemainder;
    @InjectView(2131296838)
    TextView tVTitle;
    private List<TemporaryCodeRange> temporaryCodeRangeList;
    private String timeZoneOffset;

    public interface ModalEventListener {
        void onModalAction(String str, String str2);

        void onModalAction(Date date);

        void onNegativeButtonClick();

        void onPositiveButtonClick();
    }

    public enum ModalMode {
        RANGE,
        DATE
    }

    public enum RangeFormatType {
        SUMMARY,
        LIST,
        MESSAGE
    }

    public static /* synthetic */ void lambda$new$0(TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal, DatePicker datePicker2, int i, int i2, int i3) {
        temporaryCodesRangeSelectionModal.calendar.set(1, i);
        temporaryCodesRangeSelectionModal.calendar.set(2, i2);
        temporaryCodesRangeSelectionModal.calendar.set(5, i3);
        temporaryCodesRangeSelectionModal.selectedDate = temporaryCodesRangeSelectionModal.calendar.getTime();
        ModalEventListener modalEventListener = temporaryCodesRangeSelectionModal.mModalEventListener;
        if (modalEventListener != null) {
            modalEventListener.onModalAction(temporaryCodesRangeSelectionModal.selectedDate);
        }
    }

    public TemporaryCodesRangeSelectionModal(Context context) {
        this(context, null);
    }

    public TemporaryCodesRangeSelectionModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
        this.mModalMode = ModalMode.DATE;
        this.calendar = Calendar.getInstance();
        this.currentSelectedRangeItem = null;
        this.currentSelectedRange = null;
        this.selectedRangeDate = null;
        this.selectedDate = new Date();
        this.onDateChangedListener = new OnDateChangedListener() {
            public final void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                TemporaryCodesRangeSelectionModal.lambda$new$0(TemporaryCodesRangeSelectionModal.this, datePicker, i, i2, i3);
            }
        };
        this.onClickListener = new OnClickListener() {
            public void onClick(View view) {
                Log.d(TemporaryCodesRangeSelectionModal.this.TAG, "onClick: ");
                if (TemporaryCodesRangeSelectionModal.this.mModalEventListener == null) {
                    Log.d(TemporaryCodesRangeSelectionModal.this.TAG, "onClick: ModalEventListener is null");
                } else if (view.getId() == C1075R.C1077id.positive_button) {
                    TemporaryCodesRangeSelectionModal.this.mModalEventListener.onModalAction(TemporaryCodesRangeSelectionModal.this.selectedDate);
                    TemporaryCodesRangeSelectionModal.this.mModalEventListener.onPositiveButtonClick();
                } else {
                    TemporaryCodesRangeSelectionModal.this.mModalEventListener.onNegativeButtonClick();
                }
            }
        };
        setup();
    }

    public void setTimePickerEventListener(ModalEventListener modalEventListener) {
        this.mModalEventListener = modalEventListener;
    }

    private void setup() {
        inflate(getContext(), C1075R.layout.temporary_codes_selection_modal, this);
        ButterKnife.inject((View) this);
    }

    public void setLockTimeZoneData(Lock lock) {
        this.lockTimeZoneId = lock.getTimezone();
        this.localizedLockTimeZoneName = lock.getLocalizedTimeZone();
        this.timeZoneOffset = lock.getTimeZoneOffset();
    }

    public void initialize(ModalMode modalMode, List<TemporaryCodeRange> list) {
        this.mModalMode = modalMode;
        if (this.mModalMode == ModalMode.RANGE) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initialize: codeRangeList size = ");
            sb.append(list.size());
            Log.d(str, sb.toString());
            this.temporaryCodeRangeList = list;
            this.tVTitle.setText(C1075R.string.temporary_code_range_items_title);
            this.rangeItemContainer.setVisibility(0);
            this.datePicker.setVisibility(4);
            this.tVSelectedDate.setVisibility(0);
            this.tVSelectedDate.setText(formatSelectedDate());
            this.tVTimeZoneRemainder.setVisibility(0);
            setTimeZoneRemainderMessage();
            parseInitialAndFinalDates();
            rangeViewSetup();
        } else {
            if (VERSION.SDK_INT < 23) {
                this.datePicker.setCalendarViewShown(false);
            }
            this.calendar = Calendar.getInstance();
            this.tVTitle.setText(C1075R.string.temporary_code_select_date_title);
            this.rangeItemContainer.setVisibility(4);
            this.datePicker.setVisibility(0);
            this.tVSelectedDate.setVisibility(4);
            this.tVTimeZoneRemainder.setVisibility(4);
            this.datePicker.setMinDate(this.calendar.getTimeInMillis() - 1000);
            this.calendar.add(1, 1);
            this.datePicker.setMaxDate(this.calendar.getTimeInMillis());
            this.calendar.add(1, -1);
            this.datePicker.init(this.calendar.get(1), this.calendar.get(2), this.calendar.get(5), this.onDateChangedListener);
        }
        this.bTPositive.setOnClickListener(this.onClickListener);
        this.bTNegative.setOnClickListener(this.onClickListener);
    }

    private void inflateRangeItems() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        this.rangeItemContainer.removeAllViews();
        for (TemporaryCodeRange temporaryCodeRange : this.temporaryCodeRangeList) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("From hour: ");
            sb.append(temporaryCodeRange.getInitialDate().toString());
            sb.append(" To hour: ");
            sb.append(temporaryCodeRange.getFinalDate().toString());
            sb.append(" current hour: ");
            sb.append(DateTime.now().toString());
            sb.append("\n");
            Log.d(str, sb.toString());
            if (!temporaryCodeRange.getInitialDate().isBeforeNow()) {
                String string = getContext().getString(C1075R.string.temporary_code_from);
                String string2 = getContext().getString(C1075R.string.temporary_code_to);
                String[] formatRanges = formatRanges(temporaryCodeRange, RangeFormatType.LIST, new String[0]);
                String str2 = formatRanges[0];
                String str3 = formatRanges[1];
                View inflate = layoutInflater.inflate(C1075R.layout.temporary_codes_range_item, this.rangeItemContainer, false);
                TextView textView = (TextView) inflate.findViewById(C1075R.C1077id.tv_to);
                TextView textView2 = (TextView) inflate.findViewById(C1075R.C1077id.tv_from_text);
                TextView textView3 = (TextView) inflate.findViewById(C1075R.C1077id.tv_to_text);
                ((TextView) inflate.findViewById(C1075R.C1077id.tv_from)).setText(str2);
                textView.setText(str3);
                textView2.setText(string);
                textView3.setText(string2);
                inflate.setTag(temporaryCodeRange);
                inflate.setOnClickListener(new OnClickListener(temporaryCodeRange) {
                    private final /* synthetic */ TemporaryCodeRange f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void onClick(View view) {
                        TemporaryCodesRangeSelectionModal.lambda$inflateRangeItems$1(TemporaryCodesRangeSelectionModal.this, this.f$1, view);
                    }
                });
                this.rangeItemContainer.addView(inflate);
                if (this.rangeItemContainer.getChildCount() == 1) {
                    inflate.performClick();
                }
            }
        }
        if (this.rangeItemContainer.getChildCount() == 0) {
            Toast.makeText(getContext(), C1075R.string.temporary_codes_no_ranges_for_date_error, 0).show();
            ModalEventListener modalEventListener = this.mModalEventListener;
            if (modalEventListener != null) {
                modalEventListener.onNegativeButtonClick();
            }
        }
    }

    public static /* synthetic */ void lambda$inflateRangeItems$1(TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal, TemporaryCodeRange temporaryCodeRange, View view) {
        temporaryCodesRangeSelectionModal.selectRangeItem(temporaryCodesRangeSelectionModal.currentSelectedRangeItem, false);
        temporaryCodesRangeSelectionModal.selectRangeItem(view, true);
        temporaryCodesRangeSelectionModal.currentSelectedRangeItem = view;
        temporaryCodesRangeSelectionModal.currentSelectedRange = view.getTag() != null ? (TemporaryCodeRange) view.getTag() : null;
        temporaryCodesRangeSelectionModal.selectedRangeDate = MLDateUtils.formatDateToUTC(temporaryCodesRangeSelectionModal.currentSelectedRange.getInitialDate().toDate()).split("\\.")[0];
        if (temporaryCodesRangeSelectionModal.mModalEventListener != null) {
            temporaryCodesRangeSelectionModal.mModalEventListener.onModalAction(temporaryCodesRangeSelectionModal.selectedRangeDate, temporaryCodesRangeSelectionModal.formatRanges(temporaryCodeRange, RangeFormatType.SUMMARY, new String[0])[0]);
        }
    }

    private void rangeViewSetup() {
        inflateRangeItems();
    }

    private void selectRangeItem(View view, boolean z) {
        if (view != null) {
            ((RadioButton) view.findViewById(C1075R.C1077id.rb_selection)).setChecked(z);
        }
    }

    private String formatSelectedDate() {
        return new SimpleDateFormat(getContext().getString(C1075R.string.regional_date_format)).format(this.selectedDate);
    }

    /* access modifiers changed from: 0000 */
    public boolean isSelectedDateToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date()).equals(simpleDateFormat.format(this.selectedDate));
    }

    public ModalMode getModalMode() {
        return this.mModalMode;
    }

    public String[] formatRanges(TemporaryCodeRange temporaryCodeRange, RangeFormatType rangeFormatType, String... strArr) {
        int i;
        String str = "";
        String str2 = "";
        switch (rangeFormatType) {
            case LIST:
                i = C1075R.string.regional_time_format;
                break;
            case MESSAGE:
            case SUMMARY:
                i = C1075R.string.regional_full_date_format;
                break;
            default:
                i = 0;
                break;
        }
        if (strArr.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(strArr[0]);
            sb.append(" ");
            str = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(strArr[1]);
            sb2.append(" ");
            str2 = sb2.toString();
        }
        DateTimeFormatter forPattern = DateTimeFormat.forPattern(getResources().getString(i));
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(forPattern.print((ReadableInstant) temporaryCodeRange.getInitialDate()));
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(forPattern.print((ReadableInstant) temporaryCodeRange.getFinalDate()));
        return new String[]{sb3.toString(), sb4.toString()};
    }

    private void parseInitialAndFinalDates() {
        if (this.lockTimeZoneId != null) {
            for (TemporaryCodeRange temporaryCodeRange : this.temporaryCodeRangeList) {
                temporaryCodeRange.setInitialDate(new DateTime((Object) temporaryCodeRange.getStart(), DateTimeZone.forID(this.lockTimeZoneId)));
                temporaryCodeRange.setFinalDate(new DateTime((Object) temporaryCodeRange.getEnd(), DateTimeZone.forID(this.lockTimeZoneId)));
            }
            return;
        }
        throw new IllegalArgumentException("lockTimeZoneId cannot be null");
    }

    private void setTimeZoneRemainderMessage() {
        if (!Strings.isNullOrEmpty(this.localizedLockTimeZoneName)) {
            this.tVTimeZoneRemainder.setText(String.format(getResources().getString(C1075R.string.future_temporary_code_dialog_time_zone_reminder), new Object[]{this.localizedLockTimeZoneName, !Strings.isNullOrEmpty(this.timeZoneOffset) ? this.timeZoneOffset.replaceAll("[\\(,\\)]", "") : ""}));
        }
    }
}
