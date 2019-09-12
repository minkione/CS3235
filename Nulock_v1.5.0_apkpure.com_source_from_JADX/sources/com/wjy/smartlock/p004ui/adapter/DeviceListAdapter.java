package com.wjy.smartlock.p004ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wjy.smartlock.C0073R;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLockManager;

/* renamed from: com.wjy.smartlock.ui.adapter.DeviceListAdapter */
public class DeviceListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private SmartLockManager mSmartLockManager = null;

    /* renamed from: com.wjy.smartlock.ui.adapter.DeviceListAdapter$ViewHolder */
    private class ViewHolder {
        /* access modifiers changed from: private */
        public TextView tvConnectState;
        /* access modifiers changed from: private */
        public TextView tvMac;
        /* access modifiers changed from: private */
        public TextView tvName;

        public ViewHolder(TextView tvName2, TextView tvMac2, TextView tvConnectState2) {
            this.tvName = tvName2;
            this.tvMac = tvMac2;
            this.tvConnectState = tvConnectState2;
        }
    }

    public DeviceListAdapter(Context context, SmartLockManager smLockManager) {
        this.mContext = context;
        this.mSmartLockManager = smLockManager;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.mSmartLockManager.getSmLockSize();
    }

    public Object getItem(int position) {
        return this.mSmartLockManager.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(C0073R.layout.device_item2, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(C0073R.C0075id.device_item_lockname), (TextView) convertView.findViewById(C0073R.C0075id.device_item_mac), (TextView) convertView.findViewById(C0073R.C0075id.device_item_status));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SmartLock smartLock = this.mSmartLockManager.get(position);
        viewHolder.tvName.setText(smartLock.getName());
        viewHolder.tvMac.setText(smartLock.getMac());
        if (smartLock.isConnection()) {
            viewHolder.tvConnectState.setText(this.mContext.getResources().getString(C0073R.string.connect));
            viewHolder.tvConnectState.setTextColor(this.mContext.getResources().getColor(C0073R.color.mygreen));
        } else {
            viewHolder.tvConnectState.setText(this.mContext.getResources().getString(C0073R.string.disconnect));
            viewHolder.tvConnectState.setTextColor(this.mContext.getResources().getColor(C0073R.color.myred));
        }
        return convertView;
    }
}
