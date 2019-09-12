package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import java.util.Calendar;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;

public class DateTimeSelectionModal extends CardView {
    /* access modifiers changed from: private */
    public final String TAG;
    @InjectView(2131296439)
    DatePicker datePicker;
    private DateTimeSelectionMode dateTimeSelectionMode;
    private ModalEventListener mModalEventListener;
    private DateTime minDateTime;
    private OnDateChangedListener onDateChangedListener;
    private OnTimeChangedListener onTimeChangedListener;
    /* access modifiers changed from: private */
    public DateTime selectedDate;
    @InjectView(2131296802)
    TextView tVTitle;
    @InjectView(2131296818)
    TimePicker timePicker;
    private String timeZoneId;

    public enum DateTimeSelectionMode {
        START,
        END
    }

    public interface ModalEventListener {
        void onDateChange(DateTime dateTime);

        void onNegativeButtonClick();

        void onPositiveButtonClick();
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
    }

    public DateTimeSelectionModal(Context context) {
        this(context, null);
    }

    public DateTimeSelectionModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
        this.dateTimeSelectionMode = DateTimeSelectionMode.START;
        this.onDateChangedListener = new OnDateChangedListener() {
            public final void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                DateTimeSelectionModal.this.selectedDate = DateTimeSelectionModal.this.selectedDate.year().setCopy(i).monthOfYear().setCopy(i2 + 1).dayOfMonth().setCopy(i3);
            }
        };
        this.onTimeChangedListener = new OnTimeChangedListener() {
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                DateTimeSelectionModal dateTimeSelectionModal = DateTimeSelectionModal.this;
                dateTimeSelectionModal.selectedDate = dateTimeSelectionModal.truncateSecondsAndMillis(dateTimeSelectionModal.selectedDate.hourOfDay().setCopy(i).minuteOfHour().setCopy(i2));
                String access$200 = DateTimeSelectionModal.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onTimeChanged: ");
                sb.append(DateTimeSelectionModal.this.selectedDate.toString());
                Log.d(access$200, sb.toString());
            }
        };
        setup();
    }

    private void setup() {
        inflate(getContext(), C1075R.layout.date_time_selection_modal, this);
        ButterKnife.inject((View) this);
    }

    public void initialize(DateTimeSelectionMode dateTimeSelectionMode2, DateTime dateTime, DateTime dateTime2, String str, String str2, ModalEventListener modalEventListener) {
        DateTime dateTime3;
        this.mModalEventListener = modalEventListener;
        this.minDateTime = dateTime != null ? truncateSecondsAndMillis(dateTime) : dateTime;
        this.dateTimeSelectionMode = dateTimeSelectionMode2;
        this.selectedDate = truncateSecondsAndMillis(dateTime2);
        this.timeZoneId = str2;
        if (dateTimeSelectionMode2 == DateTimeSelectionMode.END) {
            if (dateTime == null) {
                dateTime = DateTime.now();
            }
            dateTime3 = dateTime.plusMinutes(1);
        } else {
            if (dateTime == null) {
                dateTime = DateTime.now();
            }
            dateTime3 = dateTime.minusSeconds(1);
        }
        if (dateTimeSelectionMode2 == DateTimeSelectionMode.END) {
            this.selectedDate = dateTime3;
        }
        if (VERSION.SDK_INT >= 23) {
            this.datePicker.setMinDate(dateTime3.getMillis());
            this.datePicker.setMaxDate(DateTime.now().plusYears(1).getMillis());
            this.datePicker.init(dateTime2.getYear(), dateTime2.getMonthOfYear() - 1, dateTime2.getDayOfMonth(), this.onDateChangedListener);
        } else {
            Calendar instance = Calendar.getInstance(TimeZone.getTimeZone(str2));
            instance.set(11, instance.getMinimum(11));
            instance.set(12, instance.getMinimum(12));
            instance.set(13, instance.getMinimum(13));
            instance.set(14, instance.getMinimum(14));
            this.datePicker.setMinDate(instance.getTimeInMillis());
            instance.add(1, 1);
            instance.set(11, instance.getMaximum(11));
            instance.set(12, instance.getMaximum(12));
            instance.set(13, instance.getMaximum(13));
            instance.set(14, instance.getMaximum(14));
            this.datePicker.setMaxDate(instance.getTimeInMillis());
            this.datePicker.setMinDate(dateTime3.getMillis());
            this.datePicker.init(dateTime2.getYear(), dateTime2.getMonthOfYear() - 1, dateTime2.getDayOfMonth(), this.onDateChangedListener);
        }
        if (VERSION.SDK_INT >= 23) {
            if (dateTimeSelectionMode2 == DateTimeSelectionMode.END) {
                this.timePicker.setHour(dateTime3.getHourOfDay());
                this.timePicker.setMinute(dateTime3.getMinuteOfHour());
            } else {
                this.timePicker.setHour(dateTime2.getHourOfDay());
                this.timePicker.setMinute(dateTime2.getMinuteOfHour());
            }
        } else if (dateTimeSelectionMode2 == DateTimeSelectionMode.END) {
            this.timePicker.setCurrentHour(Integer.valueOf(dateTime3.getHourOfDay()));
            this.timePicker.setCurrentMinute(Integer.valueOf(dateTime3.getMinuteOfHour()));
        } else {
            this.timePicker.setCurrentHour(Integer.valueOf(dateTime2.getHourOfDay()));
            this.timePicker.setCurrentMinute(Integer.valueOf(dateTime2.getMinuteOfHour()));
        }
        this.timePicker.setOnTimeChangedListener(this.onTimeChangedListener);
        String string = getResources().getString(dateTimeSelectionMode2 == DateTimeSelectionMode.START ? C1075R.string.guest_details_starts_at : C1075R.string.guest_details_expires_at);
        StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(" ");
        sb.append(str);
        this.tVTitle.setText(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public boolean isSelectedDateToday() {
        return this.selectedDate.toLocalDate().isEqual(new LocalDate());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296655})
    public void onClickPositiveButton() {
        if (this.mModalEventListener != null) {
            boolean isSelectedDateToday = isSelectedDateToday();
            int i = C1075R.string.invalid_expires_at_date;
            if (isSelectedDateToday) {
                DateTime dateTime = this.minDateTime;
                if (dateTime == null) {
                    dateTime = truncateSecondsAndMillis(DateTime.now());
                }
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Date comparison: ");
                sb.append(this.selectedDate);
                sb.append(" isBefore ");
                sb.append(dateTime);
                sb.append(" ");
                sb.append(this.selectedDate.isBefore((ReadableInstant) dateTime));
                Log.d(str, sb.toString());
                if (this.dateTimeSelectionMode != DateTimeSelectionMode.END ? this.selectedDate.isBefore((ReadableInstant) dateTime) : !this.selectedDate.isAfter((ReadableInstant) dateTime)) {
                    boolean z = true;
                    boolean z2 = this.dateTimeSelectionMode == DateTimeSelectionMode.END;
                    if (this.minDateTime != null) {
                        z = false;
                    }
                    if (z2 && z) {
                        i = C1075R.string.invalid_expires_at_date_current_datetime;
                    } else if (this.dateTimeSelectionMode != DateTimeSelectionMode.END) {
                        i = C1075R.string.invalid_start_at_date;
                    }
                    Toast.makeText(getContext(), i, 0).show();
                    return;
                }
            } else if (this.selectedDate.isBefore((ReadableInstant) this.minDateTime) || !this.selectedDate.isAfter((ReadableInstant) this.minDateTime)) {
                Toast.makeText(getContext(), C1075R.string.invalid_expires_at_date, 0).show();
                return;
            }
            this.mModalEventListener.onDateChange(this.selectedDate);
            this.mModalEventListener.onPositiveButtonClick();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296631})
    public void onClickNegativeButton() {
        ModalEventListener modalEventListener = this.mModalEventListener;
        if (modalEventListener != null) {
            modalEventListener.onNegativeButtonClick();
        }
    }

    /* access modifiers changed from: private */
    public DateTime truncateSecondsAndMillis(DateTime dateTime) {
        return dateTime.secondOfMinute().setCopy(0).millisOfSecond().setCopy(0);
    }
}
