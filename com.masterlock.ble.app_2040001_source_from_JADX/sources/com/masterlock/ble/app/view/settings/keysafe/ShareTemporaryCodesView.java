package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.ShareTemporaryCodesPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.ShareTemporaryCodes;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class ShareTemporaryCodesView extends LinearLayout implements IAuthenticatedView {
    private int currentSelectedRadioButtonId = C1075R.C1077id.rb_share_code;
    @InjectView(2131296708)
    View dateContainer;
    @InjectView(2131296544)
    ImageView iVCalendar;
    private Lock mLock;
    private ShareTemporaryCodesPresenter mShareTemporaryCodesPresenter;
    @InjectView(2131296680)
    @Optional
    RadioButton rBShareCode;
    @InjectView(2131296681)
    RadioButton rBShareFutureCode;
    @InjectView(2131296820)
    TextView tVTemporaryCodeDate;
    @InjectView(2131296836)
    TextView tVTimeZone;

    public ShareTemporaryCodesView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mShareTemporaryCodesPresenter = new ShareTemporaryCodesPresenter(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mLock = ((ShareTemporaryCodes) AppFlow.getScreen(getContext())).mLock;
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void updateSelectedFutureDateContainer(String str) {
        this.tVTemporaryCodeDate.setText(str);
        this.tVTimeZone.setText(this.mLock.getLocalizedTimeZone());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296680})
    public void onClickShareCodeRadioButton() {
        toggleRBCheckedState(true);
        enableDateContainer(false);
        enableShareButton(true);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296681})
    public void onClickShareFutureCodeRadioButton() {
        toggleRBCheckedState(false);
        this.mShareTemporaryCodesPresenter.showDatePickerModal();
        enableDateContainer(false);
        enableShareButton(false);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296752})
    public void onClickShareCodeButton() {
        if (this.currentSelectedRadioButtonId == C1075R.C1077id.rb_share_code) {
            this.mShareTemporaryCodesPresenter.shareTemporaryCode(null);
            return;
        }
        this.mShareTemporaryCodesPresenter.shareTemporaryCode(this.mShareTemporaryCodesPresenter.getFutureDate());
    }

    private void toggleRBCheckedState(boolean z) {
        this.rBShareCode.setChecked(z);
        this.rBShareFutureCode.setChecked(!z);
        int i = z ? 4 : 0;
        this.currentSelectedRadioButtonId = z ? C1075R.C1077id.rb_share_code : C1075R.C1077id.rb_share_future_code;
        this.dateContainer.setVisibility(i);
        this.iVCalendar.setVisibility(i);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296544})
    public void onClick() {
        this.mShareTemporaryCodesPresenter.showDatePickerModal();
    }

    public void enableDateContainer(boolean z) {
        this.dateContainer.setVisibility(z ? 0 : 8);
    }

    public void enableShareButton(boolean z) {
        Resources resources;
        int i;
        Button button = (Button) ButterKnife.findById((View) this, (int) C1075R.C1077id.share_temporary_code_button);
        button.setEnabled(z);
        if (z) {
            resources = getResources();
            i = C1075R.color.primary;
        } else {
            resources = getResources();
            i = C1075R.color.primary_dark_material_light;
        }
        button.setTextColor(resources.getColor(i));
    }
}
