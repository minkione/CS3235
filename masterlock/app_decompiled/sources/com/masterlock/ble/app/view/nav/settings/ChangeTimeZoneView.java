package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.masterlock.api.entity.Timezone;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.ChangeTimeZonePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import java.util.List;

public class ChangeTimeZoneView extends LinearLayout implements IAuthenticatedView {
    private ChangeTimeZonePresenter mChangeTimeZonePresenter;
    private ArrayAdapter<Timezone> mTimeZoneAdapter;
    @InjectView(2131296604)
    Spinner mTimezoneSpinner;
    private List<Timezone> mTimezones;

    public ChangeTimeZoneView(Context context) {
        this(context, null);
    }

    public ChangeTimeZoneView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mTimeZoneAdapter = new ArrayAdapter<>(getContext(), 17367048);
            this.mTimeZoneAdapter.setDropDownViewResource(17367049);
            this.mTimezoneSpinner.setAdapter(this.mTimeZoneAdapter);
            this.mChangeTimeZonePresenter = new ChangeTimeZonePresenter(this);
            this.mChangeTimeZonePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mChangeTimeZonePresenter.finish();
    }

    public void setTimezones(List<Timezone> list) {
        this.mTimezones = list;
        this.mTimeZoneAdapter.clear();
        this.mTimeZoneAdapter.addAll(list);
        this.mTimeZoneAdapter.notifyDataSetChanged();
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), i, 0).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296329})
    public void onSaveClicked() {
        Timezone timezone = (Timezone) this.mTimezoneSpinner.getSelectedItem();
        if (timezone != null) {
            this.mChangeTimeZonePresenter.updateTimezone(timezone.timezoneId);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnCheckedChanged({2131296724})
    public void onCheckedChange(boolean z) {
        this.mChangeTimeZonePresenter.setUpdateAllLocksTimeZone(z);
    }

    public void setCurrentTimeZone(String str) {
        for (Timezone timezone : this.mTimezones) {
            if (timezone.timezoneId.equals(str)) {
                this.mTimezoneSpinner.setSelection(this.mTimeZoneAdapter.getPosition(timezone));
                return;
            }
        }
    }
}
