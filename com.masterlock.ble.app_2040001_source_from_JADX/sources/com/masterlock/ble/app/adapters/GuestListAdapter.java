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
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.core.Guest;
import java.util.List;

public class GuestListAdapter extends Adapter<GuestViewHolder> {
    private List<Guest> mGuestList;
    /* access modifiers changed from: private */
    public OnItemClickListener mItemClickListener;

    public class GuestViewHolder extends ViewHolder implements OnClickListener {
        @InjectView(2131296860)
        TextView mGuestName;

        public GuestViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }

        @OnClick({2131296860})
        public void onClick(View view) {
            if (GuestListAdapter.this.mItemClickListener != null) {
                GuestListAdapter.this.mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public GuestListAdapter(List<Guest> list) {
        this.mGuestList = list;
    }

    public GuestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GuestViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1075R.layout.guest_list_item, viewGroup, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void onBindViewHolder(GuestViewHolder guestViewHolder, int i) {
        guestViewHolder.mGuestName.setText(TextUtils.getDisplayNameForGuest(guestViewHolder.mGuestName.getContext(), (Guest) this.mGuestList.get(i)));
    }

    public int getItemCount() {
        return this.mGuestList.size();
    }
}
