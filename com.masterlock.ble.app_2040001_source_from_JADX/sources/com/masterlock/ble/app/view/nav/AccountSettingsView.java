package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.AccountSettingsAdapter;
import com.masterlock.ble.app.adapters.OnItemClickListener;
import com.masterlock.ble.app.presenter.nav.AccountSettingsPresenter;

public class AccountSettingsView extends LinearLayout implements OnItemClickListener {
    @InjectView(2131296272)
    RecyclerView mAccountSettingsList;
    private AccountSettingsPresenter mAccountSettingsPresenter;

    public AccountSettingsView(Context context) {
        super(context);
    }

    public AccountSettingsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mAccountSettingsPresenter = new AccountSettingsPresenter(this);
            this.mAccountSettingsList.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.mAccountSettingsList.setHasFixedSize(true);
            AccountSettingsAdapter accountSettingsAdapter = new AccountSettingsAdapter(getResources().getStringArray(C1075R.array.account_settings));
            accountSettingsAdapter.setOnItemClickListener(this);
            this.mAccountSettingsList.setAdapter(accountSettingsAdapter);
        }
    }

    public void onItemClick(View view, int i) {
        this.mAccountSettingsPresenter.verifyPassword(i);
    }

    public void goToSelectedItemScreen(int i) {
        AccountSettingsAdapter accountSettingsAdapter = (AccountSettingsAdapter) this.mAccountSettingsList.getAdapter();
        if (accountSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_setting_profile_title))) {
            this.mAccountSettingsPresenter.goToAccountProfile();
        } else if (accountSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_setting_language_selection_title))) {
            this.mAccountSettingsPresenter.goToLanguageSelection();
        } else if (accountSettingsAdapter.getTitleId(i).equals(getResources().getString(C1075R.string.account_setting_notifications_title))) {
            this.mAccountSettingsPresenter.goToNotifications();
        }
    }
}
