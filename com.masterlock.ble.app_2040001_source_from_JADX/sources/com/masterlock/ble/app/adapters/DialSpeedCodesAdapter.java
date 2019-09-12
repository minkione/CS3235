package com.masterlock.ble.app.adapters;

import android.content.Context;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.ArrowsCodeUtil;
import com.masterlock.core.ProductCode;
import java.util.ArrayList;
import java.util.List;

public class DialSpeedCodesAdapter extends Adapter<DialCodeViewHolder> {
    List<ProductCode> mCodesList = new ArrayList();
    Context mContext;

    public class DialCodeViewHolder extends ViewHolder {
        @InjectView(2131296310)
        LinearLayout codeContainer;
        @InjectView(2131296386)
        TextView codeNameTV;

        public DialCodeViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }
    }

    public DialSpeedCodesAdapter(List<ProductCode> list, Context context) {
        if (list != null) {
            this.mCodesList = list;
        }
        this.mContext = context;
    }

    public DialCodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DialCodeViewHolder(LayoutInflater.from(this.mContext).inflate(C1075R.layout.dial_code_list_item, viewGroup, false));
    }

    public void onBindViewHolder(DialCodeViewHolder dialCodeViewHolder, int i) {
        dialCodeViewHolder.codeNameTV.setText(((ProductCode) this.mCodesList.get(i)).getName());
        new ArrowsCodeUtil().showMasterCode(((ProductCode) this.mCodesList.get(i)).getValue(), dialCodeViewHolder.codeContainer, this.mContext);
    }

    public int getItemCount() {
        return this.mCodesList.size();
    }
}
