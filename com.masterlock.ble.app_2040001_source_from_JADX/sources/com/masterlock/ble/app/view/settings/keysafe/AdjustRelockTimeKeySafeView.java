package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.keysafe.AdjustRelockTimeKeySafePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class AdjustRelockTimeKeySafeView extends LinearLayout implements IAuthenticatedView {
    private AdjustRelockTimeKeySafePresenter mAdjustRelockTimeKeySafePresenter;

    public AdjustRelockTimeKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mAdjustRelockTimeKeySafePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAdjustRelockTimeKeySafePresenter.finish();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mAdjustRelockTimeKeySafePresenter = new AdjustRelockTimeKeySafePresenter(this);
            ButterKnife.inject((View) this);
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296353})
    public void saveRelockTime() {
        try {
            this.mAdjustRelockTimeKeySafePresenter.updateRelockTime(Integer.parseInt(((TextView) ButterKnife.findById((View) this, (int) C1075R.C1077id.txt_relock_time)).getText().toString()));
        } catch (UnsupportedOperationException e) {
            Toast.makeText(getContext(), e.getMessage(), 0).show();
        }
    }
}
