package com.masterlock.ble.app.presenter.settings.keysafe;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.SettingsScreens.AdjustRelockTimeKeySafe;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.ble.app.view.settings.keysafe.AdjustRelockTimeKeySafeView;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;

public class AdjustRelockTimeKeySafePresenter extends AuthenticatedPresenter<Lock, AdjustRelockTimeKeySafeView> {
    public static final int MINIMUM_RELOCK_TIME = 4;
    private SeekBar mRelockSeekBar;
    /* access modifiers changed from: private */
    public TextView mRelockTimeTextView;

    public AdjustRelockTimeKeySafePresenter(AdjustRelockTimeKeySafeView adjustRelockTimeKeySafeView) {
        super(adjustRelockTimeKeySafeView);
        this.model = ((AdjustRelockTimeKeySafe) AppFlow.getScreen(adjustRelockTimeKeySafeView.getContext())).mLock;
    }

    public void start() {
        super.start();
        this.mEventBus.post(new Builder(((AdjustRelockTimeKeySafeView) this.view).getResources()).build());
        setUpHeaderView();
    }

    public void updateRelockTime(int i) {
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
            return;
        }
        ((Lock) this.model).setRelockTimeInSeconds(i);
        AppFlow.get(((AdjustRelockTimeKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_adjust_relock_time, LockConfigAction.RELOCK_TIME));
    }

    public void setUpHeaderView() {
        this.mRelockTimeTextView = (TextView) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.txt_relock_time);
        this.mRelockSeekBar = (SeekBar) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.seekbar_relock_time);
        this.mRelockTimeTextView.setText(String.valueOf(((Lock) this.model).getRelockTimeInSeconds()));
        this.mRelockSeekBar.setProgress(getInitialProgress());
        this.mRelockSeekBar.setOnSeekBarChangeListener(getSeekBarListener());
    }

    private int getInitialProgress() {
        return Integer.parseInt(this.mRelockTimeTextView.getText().toString()) - 4;
    }

    private OnSeekBarChangeListener getSeekBarListener() {
        return new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AdjustRelockTimeKeySafePresenter.this.mRelockTimeTextView.setText(String.valueOf(i + 4));
            }
        };
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((AdjustRelockTimeKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((AdjustRelockTimeKeySafeView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(simpleDialog);
        simpleDialog.setMessage((int) C1075R.string.locker_mode_body);
        simpleDialog.setPositiveButton((int) C1075R.string.f165ok);
        simpleDialog.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.show();
    }
}
