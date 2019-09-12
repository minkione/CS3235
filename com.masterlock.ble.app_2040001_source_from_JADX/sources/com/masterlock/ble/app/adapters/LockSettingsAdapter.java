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

public class LockSettingsAdapter extends Adapter<SettingsViewHolder> {
    /* access modifiers changed from: private */
    public OnItemClickListener mItemClickListener;
    private String[] mSettingIds;

    public class SettingsViewHolder extends ViewHolder implements OnClickListener {
        @InjectView(2131296890)
        TextView mTextView;

        public SettingsViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }

        @OnClick({2131296890})
        public void onClick(View view) {
            if (LockSettingsAdapter.this.mItemClickListener != null) {
                LockSettingsAdapter.this.mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public LockSettingsAdapter(String[] strArr) {
        this.mSettingIds = strArr;
    }

    public SettingsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SettingsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1075R.layout.list_item, viewGroup, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void onBindViewHolder(SettingsViewHolder settingsViewHolder, int i) {
        settingsViewHolder.mTextView.setText(this.mSettingIds[i]);
    }

    public int getItemCount() {
        return this.mSettingIds.length;
    }

    public String getTitleId(int i) {
        return this.mSettingIds[i];
    }
}
