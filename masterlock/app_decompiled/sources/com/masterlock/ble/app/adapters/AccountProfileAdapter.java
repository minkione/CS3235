package com.masterlock.ble.app.adapters;

import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.common.base.Strings;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.core.AccountProfileInfo;
import java.util.Locale;

public class AccountProfileAdapter extends Adapter<AccountProfileViewHolder> {
    private AccountProfileInfo mAccountProfileInfo;
    private String[] mAccountProfileSettingsIds;
    /* access modifiers changed from: private */
    public OnItemClickListener mItemClickListener;

    public class AccountProfileViewHolder extends ViewHolder implements OnClickListener {
        @InjectView(2131296267)
        TextView mPhoneCountryInfo;
        @InjectView(2131296269)
        TextView mTitleText;
        @InjectView(2131296270)
        TextView mValueText;

        public AccountProfileViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }

        @OnClick({2131296266})
        public void onClick(View view) {
            if (AccountProfileAdapter.this.mItemClickListener != null) {
                AccountProfileAdapter.this.mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public AccountProfileAdapter(String[] strArr, AccountProfileInfo accountProfileInfo) {
        this.mAccountProfileSettingsIds = strArr;
        this.mAccountProfileInfo = accountProfileInfo;
    }

    public AccountProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AccountProfileViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1075R.layout.account_profile_item, viewGroup, false));
    }

    public void onBindViewHolder(AccountProfileViewHolder accountProfileViewHolder, int i) {
        String str;
        accountProfileViewHolder.mTitleText.setText(this.mAccountProfileSettingsIds[i]);
        switch (i) {
            case 0:
                str = this.mAccountProfileInfo.getEmail();
                break;
            case 1:
                str = formatMobilePhoneInformation();
                String[] split = str.split("\n");
                if (split.length != 2) {
                    accountProfileViewHolder.mPhoneCountryInfo.setVisibility(8);
                    break;
                } else {
                    accountProfileViewHolder.mPhoneCountryInfo.setVisibility(0);
                    accountProfileViewHolder.mPhoneCountryInfo.setText(split[1]);
                    str = split[0];
                    break;
                }
            case 2:
                StringBuilder sb = new StringBuilder();
                sb.append(this.mAccountProfileInfo.getFirstName());
                sb.append(" ");
                sb.append(this.mAccountProfileInfo.getLastName());
                str = sb.toString();
                break;
            case 3:
                str = this.mAccountProfileInfo.getTimezoneDisplay();
                break;
            case 4:
                str = this.mAccountProfileInfo.getUserName();
                break;
            default:
                str = "";
                break;
        }
        if (i != 1) {
            accountProfileViewHolder.mPhoneCountryInfo.setVisibility(8);
        }
        accountProfileViewHolder.mValueText.setText(str);
    }

    private String formatMobilePhoneInformation() {
        String str;
        if (Strings.isNullOrEmpty(this.mAccountProfileInfo.getPhoneNumber())) {
            return "";
        }
        String nationalPhone = TextUtils.getNationalPhone(this.mAccountProfileInfo.getPhoneNumber());
        String displayCountry = new Locale("", this.mAccountProfileInfo.getAlphaCountryCode()).getDisplayCountry();
        if (Strings.isNullOrEmpty(displayCountry)) {
            str = "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mAccountProfileInfo.getCountryCode());
            sb.append(" ");
            sb.append(displayCountry);
            str = sb.toString();
        }
        if (!str.isEmpty() && !str.contains("+")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("+");
            sb2.append(str);
            str = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(nationalPhone);
        sb3.append("\n");
        sb3.append(str);
        return sb3.toString();
    }

    public int getItemCount() {
        return this.mAccountProfileSettingsIds.length;
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public String getTitleId(int i) {
        return this.mAccountProfileSettingsIds[i];
    }
}
