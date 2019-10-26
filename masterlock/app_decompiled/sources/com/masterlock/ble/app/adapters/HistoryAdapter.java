package com.masterlock.ble.app.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.LockStatusResourceManager;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockStatus;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<KmsLogEntry> {
    private static final String TAG = "HistoryAdapter";
    private List<KmsLogEntry> items;
    private Lock mLock;
    private Resources resources;

    public class HistoryViewHolder {
        @InjectView(2131296389)
        public View container;
        @InjectView(2131296531)
        public ImageView image;
        public KmsLogEntry log;
        @InjectView(2131296863)
        public TextView message;
        @InjectView(2131296864)
        public TextView name;

        public HistoryViewHolder(View view, KmsLogEntry kmsLogEntry) {
            ButterKnife.inject((Object) this, view);
            this.log = kmsLogEntry;
        }
    }

    public HistoryAdapter(Context context, Lock lock) {
        this(context, new ArrayList(), lock);
    }

    public HistoryAdapter(Context context, List<KmsLogEntry> list, Lock lock) {
        super(context, 0, 0, list);
        this.items = list;
        this.mLock = lock;
        this.resources = context.getResources();
    }

    public KmsLogEntry getItem(int i) {
        return (KmsLogEntry) this.items.get(i);
    }

    public int getPosition(KmsLogEntry kmsLogEntry) {
        return this.items.indexOf(kmsLogEntry);
    }

    public int getCount() {
        return this.items.size();
    }

    private View newView(Context context, ViewGroup viewGroup, KmsLogEntry kmsLogEntry) {
        View inflate = LayoutInflater.from(context).inflate(C1075R.layout.history_list_item, viewGroup, false);
        inflate.setTag(new HistoryViewHolder(inflate, kmsLogEntry));
        return inflate;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        KmsLogEntry kmsLogEntry = (KmsLogEntry) this.items.get(i);
        if (view == null) {
            view = newView(viewGroup.getContext(), viewGroup, kmsLogEntry);
        }
        HistoryViewHolder historyViewHolder = (HistoryViewHolder) view.getTag();
        historyViewHolder.log = kmsLogEntry;
        historyViewHolder.name.setText(kmsLogEntry.getAlias());
        int i2 = 8;
        historyViewHolder.name.setVisibility(TextUtils.isEmpty(kmsLogEntry.getAlias()) ? 8 : 0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(kmsLogEntry.getMessage());
        spannableStringBuilder.setSpan(new TextAppearanceSpan(getContext(), C1075R.style.TextAppearance_Body2), 0, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C1075R.color.medium_grey)), 0, spannableStringBuilder.length(), 33);
        spannableStringBuilder.append(" • ");
        try {
            spannableStringBuilder.append(MLDateUtils.parseServerDateFormat(kmsLogEntry.getCreatedOn(), this.resources));
        } catch (ParseException e) {
            Log.e(TAG, "getView: failed parsing date", e);
        }
        historyViewHolder.message.setText(spannableStringBuilder);
        TextView textView = historyViewHolder.message;
        if (!TextUtils.isEmpty(spannableStringBuilder.toString())) {
            i2 = 0;
        }
        textView.setVisibility(i2);
        setEventIcon(historyViewHolder);
        return view;
    }

    public void setEventIcon(HistoryViewHolder historyViewHolder) {
        int i;
        if (historyViewHolder.log.getEventCode() != null) {
            switch (historyViewHolder.log.getEventCode()) {
                case INVALID_PASSCODE_INDETERMINATE:
                case INVALID_PASSCODE_MASTER:
                case INVALID_WIRELESS_AUTHENTICATED_CMD:
                case INVALID_WIRELESS_NOT_PERMITTED:
                case INVALID_WIRELESS_NOT_SHEDULED:
                case INVALID_WIRELESS_REPLAY:
                case INVALID_WIRELESS_SESSION_TIME:
                case INVALID_WIRELESS_UNAUTHENTICATED_CMD:
                case INVALID_WIRELESS_UNAUTHENTICATED_USER:
                case INVALID_PASSCODE_TEMPORARY:
                case TAMPER_WARNING:
                    i = C1075R.C1076drawable.ic_unauthorized;
                    break;
                case UNLOCK_MANUAL:
                case UNLOCK_MASTERCODE:
                case UNLOCK_PRIMARYCODE:
                case UNLOCK_SERVICECODE:
                case UNLOCK_WIRELESS:
                case UNLOCK_WIRELESS_SHACKLE:
                case UNLOCK_SHACKLE_MASTERCODE:
                case UNLOCK_SHACKLE_PRIMARYCODE:
                case UNLOCK_WIRELESS_DOOR:
                case UNLOCK_DOOR_MASTERCODE:
                case UNLOCK_DOOR_PRIMARYCODE:
                case UNLOCK_DOOR_SERVICECODE:
                case UNLOCK_SECONDARY_PASSCODE:
                    i = LockStatusResourceManager.getResIdForLockStatus(this.mLock, LockStatus.UNLOCKED);
                    break;
                case RELOCK_MANUAL:
                case RELOCK_AUTOMATIC:
                case RELOCK_MANUAL_DOOR:
                case RELOCK_AUTOMATIC_DOOR:
                case RELOCK_MANUAL_SHACKLE:
                case RELOCK_AUTOMATIC_SHACKLE:
                    i = LockStatusResourceManager.getResIdForLockStatus(this.mLock, LockStatus.LOCKED);
                    break;
                case FIRMWARE_UPDATE_COMPLETED:
                case FIRMWARE_UPDATE_INITIATED:
                case UPDATE_FIRMWAREVERSION:
                    i = LockStatusResourceManager.getResIdForLockStatus(this.mLock, LockStatus.UPDATE_MODE);
                    break;
                case BATTERYLEVEL_LOW:
                    i = C1075R.C1076drawable.ic_lowbattery;
                    break;
                case LOCK_OPENED:
                case SHACKLE_OPENED:
                case DOOR_OPENED:
                    i = LockStatusResourceManager.getResIdForLockStatus(this.mLock, LockStatus.OPENED);
                    break;
                default:
                    i = C1075R.C1076drawable.ic_locklist_default_empty;
                    break;
            }
        } else {
            i = 0;
        }
        Picasso with = Picasso.with(getContext());
        if (i == 0) {
            i = C1075R.C1076drawable.ic_locklist_default_empty;
        }
        with.load(i).into(historyViewHolder.image);
    }
}
