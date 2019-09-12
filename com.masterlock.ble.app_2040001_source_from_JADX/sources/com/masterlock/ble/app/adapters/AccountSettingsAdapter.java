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
import com.masterlock.ble.app.C1075R;

public class AccountSettingsAdapter extends Adapter<AccountSettingsViewHolder> {
    private String[] mAccountSettingsIds;
    /* access modifiers changed from: private */
    public OnItemClickListener mItemClickListener;

    public class AccountSettingsViewHolder extends ViewHolder implements OnClickListener {
        @InjectView(2131296273)
        TextView mTextView;

        public AccountSettingsViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }

        @OnClick({2131296271})
        public void onClick(View view) {
            if (AccountSettingsAdapter.this.mItemClickListener != null) {
                AccountSettingsAdapter.this.mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public AccountSettingsAdapter(String[] strArr) {
        this.mAccountSettingsIds = strArr;
    }

    public AccountSettingsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AccountSettingsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1075R.layout.account_settings_item, viewGroup, false));
    }

    public void onBindViewHolder(AccountSettingsViewHolder accountSettingsViewHolder, int i) {
        accountSettingsViewHolder.mTextView.setText(this.mAccountSettingsIds[i]);
    }

    public int getItemCount() {
        return this.mAccountSettingsIds.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public String getTitleId(int i) {
        return this.mAccountSettingsIds[i];
    }
}
