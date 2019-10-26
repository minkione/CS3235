package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.keysafe.ShowRelockTimeKeySafePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class ShowRelockTimeKeySafeView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296361)
    LinearLayout buttonBar;
    @InjectView(2131296880)
    TextView mRelockTime;
    private ShowRelockTimeKeySafePresenter mShoweRelockTimeKeySafePresenter;

    public ShowRelockTimeKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mShoweRelockTimeKeySafePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mShoweRelockTimeKeySafePresenter.finish();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mShoweRelockTimeKeySafePresenter = new ShowRelockTimeKeySafePresenter(this);
            ButterKnife.inject((View) this);
        }
    }

    public void updateView(int i) {
        this.mRelockTime.setText(String.valueOf(i));
    }

    public void displayButtonBar(boolean z) {
        this.buttonBar.setVisibility(z ? 0 : 8);
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296335})
    public void changeRelockTime() {
        this.mShoweRelockTimeKeySafePresenter.goToAdjustRelockTime();
    }
}
