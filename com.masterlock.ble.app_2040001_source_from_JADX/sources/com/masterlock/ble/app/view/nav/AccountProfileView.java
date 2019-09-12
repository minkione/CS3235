package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.AccountProfileAdapter;
import com.masterlock.ble.app.adapters.OnItemClickListener;
import com.masterlock.ble.app.presenter.nav.AccountProfilePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccountProfileInfo;

public class AccountProfileView extends LinearLayout implements OnItemClickListener, IAuthenticatedView {
    @InjectView(2131296268)
    RecyclerView mAccountProfileSettingsList;
    private AccountProfileAdapter mAdapter;
    private AccountProfilePresenter mPresenter;

    public AccountProfileView(Context context) {
        super(context);
    }

    public AccountProfileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mPresenter = new AccountProfilePresenter(this);
            this.mAccountProfileSettingsList.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.mAccountProfileSettingsList.setHasFixedSize(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPresenter.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
    }

    public void updateView(AccountProfileInfo accountProfileInfo) {
        this.mAdapter = new AccountProfileAdapter(getResources().getStringArray(C1075R.array.account_profile), accountProfileInfo);
        this.mAdapter.setItemClickListener(this);
        this.mAccountProfileSettingsList.setAdapter(this.mAdapter);
    }

    public void onItemClick(View view, int i) {
        if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_email))) {
            this.mPresenter.goToChangeEmailAddress();
        } else if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_phone_number))) {
            this.mPresenter.goToChangePhoneNumber();
        } else if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_name))) {
            this.mPresenter.goToChangeName();
        } else if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_time_zone))) {
            this.mPresenter.goToChangeTimeZone();
        } else if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_username))) {
            this.mPresenter.goToChangeUsername();
        } else if (this.mAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_profile_password))) {
            this.mPresenter.goToChangePassword();
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
