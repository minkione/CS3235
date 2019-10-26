package com.masterlock.ble.app.view.settings.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.entity.Timezone;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.padlock.LockTimezonePadLockPresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import java.util.List;

public class LockTimezonePadLockView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296597)
    TextView mLockNameBanner;
    private LockTimezonePadLockPresenter mLockNamePresenter;
    private ArrayAdapter<Timezone> mTimeZoneAdapter;
    @InjectView(2131296604)
    Spinner mTimezoneSpinner;
    private List<Timezone> mTimezones;
    @InjectView(2131296422)
    TextView textDeviceId;

    public LockTimezonePadLockView(Context context) {
        this(context, null);
    }

    public LockTimezonePadLockView(Context context, AttributeSet attributeSet) {
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
            this.mLockNamePresenter = new LockTimezonePadLockPresenter(this);
            this.mLockNamePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mLockNamePresenter.finish();
    }

    public void setTimezones(List<Timezone> list) {
        this.mTimezones = list;
        this.mTimeZoneAdapter.clear();
        this.mTimeZoneAdapter.addAll(list);
        this.mTimeZoneAdapter.notifyDataSetChanged();
    }

    public void updateTimezone(String str) {
        for (Timezone timezone : this.mTimezones) {
            if (timezone.timezoneId.equals(str)) {
                this.mTimezoneSpinner.setSelection(this.mTimeZoneAdapter.getPosition(timezone));
                return;
            }
        }
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void displaySuccess() {
        Toast.makeText(getContext(), getResources().getString(C1075R.string.lock_timezone_successfully_changed), 1).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296351})
    public void onSaveClicked() {
        this.mLockNamePresenter.updateTimezone(((Timezone) this.mTimezoneSpinner.getSelectedItem()).timezoneId);
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.textDeviceId.setText(str);
    }
}
