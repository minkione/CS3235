package com.masterlock.ble.app.view.settings;

import android.content.Context;
import android.content.res.Resources;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.DrawableDecoration;
import com.masterlock.ble.app.adapters.DrawableDecoration.Position;
import com.masterlock.ble.app.adapters.LockSettingsAdapter;
import com.masterlock.ble.app.adapters.OnItemClickListener;
import com.masterlock.ble.app.presenter.settings.LockSettingsPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import java.util.EnumSet;

public class LockSettingsView extends LinearLayout implements OnItemClickListener, IAuthenticatedView {
    @InjectView(2131296337)
    Button mDelete;
    @InjectView(2131296871)
    public TextView mLockName;
    private LockSettingsPresenter mLockSettingsPresenter;
    @InjectView(2131296684)
    RecyclerView mRecycler;
    @InjectView(2131296422)
    public TextView textDeviceId;

    public LockSettingsView(Context context) {
        super(context);
    }

    public LockSettingsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LockSettingsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mLockSettingsPresenter = new LockSettingsPresenter(((LockSettings) AppFlow.getScreen(getContext())).mLock, this);
            this.mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            this.mRecycler.addItemDecoration(new DrawableDecoration(getResources().getDrawable(C1075R.C1076drawable.line_divider), EnumSet.of(Position.MIDDLE)));
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mLockSettingsPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mLockSettingsPresenter.finish();
    }

    public void updateView(Lock lock) {
        LockSettingsAdapter lockSettingsAdapter;
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        TextView textView = this.textDeviceId;
        if (lock.isDialSpeedLock()) {
            StringBuilder sb = new StringBuilder();
            sb.append(lock.getModelName());
            sb.append(" ");
            sb.append(lock.getModelNumber());
            deviceId = sb.toString();
        }
        textView.setText(deviceId);
        this.mLockName.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{lock.getName()}));
        if (lock.isDialSpeedLock() || lock.isBiometricPadLock()) {
            lockSettingsAdapter = new LockSettingsAdapter(getResources().getStringArray(C1075R.array.settings_dial_speed));
        } else if (lock.getAccessType() == AccessType.OWNER || lock.getAccessType() == AccessType.CO_OWNER) {
            lockSettingsAdapter = new LockSettingsAdapter(getResources().getStringArray(C1075R.array.settings_owner));
        } else {
            lockSettingsAdapter = new LockSettingsAdapter(getResources().getStringArray(AccessWindowUtil.hasStarted(lock) ? C1075R.array.settings_guest : C1075R.array.settings_guest_no_access));
        }
        lockSettingsAdapter.setOnItemClickListener(this);
        this.mRecycler.setAdapter(lockSettingsAdapter);
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void onItemClick(View view, int i) {
        LockSettingsAdapter lockSettingsAdapter = (LockSettingsAdapter) this.mRecycler.getAdapter();
        if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_lock_name))) {
            this.mLockSettingsPresenter.renameLock();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_notes))) {
            this.mLockSettingsPresenter.editNotes();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_timezone_label))) {
            this.mLockSettingsPresenter.editTimeZone();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_unlock_mode))) {
            this.mLockSettingsPresenter.goToUnlockMode();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_calibrate))) {
            this.mLockSettingsPresenter.calibrate();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_backup))) {
            this.mLockSettingsPresenter.goToBackupMasterCombination();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_about))) {
            this.mLockSettingsPresenter.goToAboutLock();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.settings_reset))) {
            this.mLockSettingsPresenter.resetKeys();
        } else if (lockSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.send_temp_code))) {
            this.mLockSettingsPresenter.goToShareTemporaryCodes();
        } else {
            Context context = view.getContext();
            StringBuilder sb = new StringBuilder();
            sb.append(((TextView) view).getText());
            sb.append(" pressed!");
            Toast.makeText(context, sb.toString(), 0).show();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296337})
    public void deleteLock() {
        this.mLockSettingsPresenter.deleteLock();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void enableDelete(boolean z) {
        int i;
        Resources resources;
        Button button = this.mDelete;
        if (z) {
            resources = getResources();
            i = C1075R.color.primary;
        } else {
            resources = getResources();
            i = C1075R.color.dark_grey;
        }
        button.setTextColor(resources.getColor(i));
        this.mDelete.setEnabled(z);
    }
}
