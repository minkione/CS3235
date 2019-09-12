package com.masterlock.ble.app.view.lock.dialspeed;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.dialspeed.EditDialSpeedCodesPresenter;
import com.masterlock.ble.app.screens.LockScreens.EditDialSpeedCodes;
import com.masterlock.ble.app.util.EmojiBlockerForEditText;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.masterlock.core.ProductCode;
import com.square.flow.appflow.AppFlow;
import java.util.ArrayList;
import java.util.List;

public class EditDialSpeedCodesView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296479)
    FloatingLabelEditText mCode1ET;
    @InjectView(2131296483)
    FloatingLabelEditText mCode2ET;
    @InjectView(2131296485)
    FloatingLabelEditText mCode3ET;
    @InjectView(2131296487)
    FloatingLabelEditText mCode4ET;
    @InjectView(2131296481)
    FloatingLabelEditText mCodeName1ET;
    @InjectView(2131296484)
    FloatingLabelEditText mCodeName2ET;
    @InjectView(2131296486)
    FloatingLabelEditText mCodeName3ET;
    @InjectView(2131296488)
    FloatingLabelEditText mCodeName4ET;
    Lock mLock;
    EditDialSpeedCodesPresenter mPresenter;

    public EditDialSpeedCodesView(Context context) {
        super(context);
    }

    public EditDialSpeedCodesView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EditDialSpeedCodesView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject((View) this);
        Lock lock = ((EditDialSpeedCodes) AppFlow.getScreen(getContext())).getLock();
        this.mLock = lock;
        this.mPresenter = new EditDialSpeedCodesPresenter(lock, this);
        this.mPresenter.start();
        setUpViewWithLock(lock);
    }

    private void disableEmojis() {
        new EmojiBlockerForEditText(new EditText[]{this.mCode1ET.getEditText(), this.mCodeName1ET.getEditText(), this.mCode2ET.getEditText(), this.mCodeName2ET.getEditText(), this.mCode3ET.getEditText(), this.mCodeName3ET.getEditText(), this.mCode4ET.getEditText(), this.mCodeName4ET.getEditText()});
    }

    public void setUpViewWithLock(Lock lock) {
        List<ProductCode> productCodes = lock.getProductCodes();
        if (lock.isBiometricPadLock()) {
            productCodes = new ArrayList<>();
            productCodes.add(lock.generateProductCode(1, getResources().getString(C1075R.string.title_primary_code), lock.getPrimaryCode()));
            findViewById(C1075R.C1077id.floating_code_1_edit).setVisibility(8);
            findViewById(C1075R.C1077id.floating_code_1_text).setVisibility(0);
        } else {
            findViewById(C1075R.C1077id.floating_code_1_edit).setVisibility(0);
            findViewById(C1075R.C1077id.floating_code_1_text).setVisibility(8);
        }
        for (ProductCode productCode : productCodes) {
            switch (productCode.getDisplayOrder()) {
                case 1:
                    this.mCodeName1ET.setText(productCode.getName());
                    this.mCode1ET.setText(productCode.getValue());
                    break;
                case 2:
                    this.mCodeName2ET.setText(productCode.getName());
                    this.mCode2ET.setText(productCode.getValue());
                    break;
                case 3:
                    this.mCodeName3ET.setText(productCode.getName());
                    this.mCode3ET.setText(productCode.getValue());
                    break;
                case 4:
                    this.mCodeName4ET.setText(productCode.getName());
                    this.mCode4ET.setText(productCode.getValue());
                    break;
            }
        }
        disableEmojis();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296349})
    public void saveCodes() {
        ViewUtil.hideKeyboard(getContext(), this.mCode1ET.getWindowToken());
        this.mPresenter.saveCodes(generateCodesList());
    }

    private List<ProductCode> generateCodesList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i < 5; i++) {
            ProductCode generateCode = generateCode(i);
            if (!TextUtils.isEmpty(generateCode.getName())) {
                arrayList.add(generateCode);
            }
        }
        return arrayList;
    }

    private ProductCode generateCode(int i) {
        String str = "";
        String str2 = "";
        switch (i) {
            case 1:
                str2 = this.mCodeName1ET.getText();
                str = this.mCode1ET.getText();
                break;
            case 2:
                str2 = this.mCodeName2ET.getText();
                str = this.mCode2ET.getText();
                break;
            case 3:
                str2 = this.mCodeName3ET.getText();
                str = this.mCode3ET.getText();
                break;
            case 4:
                str2 = this.mCodeName4ET.getText();
                str = this.mCode4ET.getText();
                break;
        }
        return this.mLock.generateProductCode(i, str2, str);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
