package com.masterlock.ble.app.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.ShowMode;
import com.daimajia.swipe.SwipeLayout.Status;
import com.daimajia.swipe.SwipeLayout.SwipeListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.InvitationAdapter.InvitationViewHolder;
import com.masterlock.ble.app.adapters.listeners.ItemClickListener;
import com.masterlock.ble.app.adapters.listeners.ItemSwipeListener;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.core.AccessType;
import com.masterlock.core.Invitation;
import com.masterlock.core.InvitationStatus;
import com.masterlock.core.Lock;
import com.masterlock.core.ScheduleType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvitationAdapter extends ArrayAdapter<Invitation> {
    private List<Invitation> items;
    private Lock lock;
    /* access modifiers changed from: private */
    public ItemClickListener mClickListener;
    /* access modifiers changed from: private */
    public ItemSwipeListener mSwipeListener;
    private Resources resources;

    /* renamed from: com.masterlock.ble.app.adapters.InvitationAdapter$2 */
    static /* synthetic */ class C11032 {
        static final /* synthetic */ int[] $SwitchMap$com$masterlock$core$ScheduleType = new int[ScheduleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                com.masterlock.core.ScheduleType[] r0 = com.masterlock.core.ScheduleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$masterlock$core$ScheduleType = r0
                int[] r0 = $SwitchMap$com$masterlock$core$ScheduleType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.masterlock.core.ScheduleType r1 = com.masterlock.core.ScheduleType.TWENTY_FOUR_SEVEN     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$masterlock$core$ScheduleType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.masterlock.core.ScheduleType r1 = com.masterlock.core.ScheduleType.SEVEN_AM_TO_SEVEN_PM     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$masterlock$core$ScheduleType     // Catch:{ NoSuchFieldError -> 0x002a }
                com.masterlock.core.ScheduleType r1 = com.masterlock.core.ScheduleType.SEVEN_PM_TO_SEVEN_AM     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$masterlock$core$ScheduleType     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.masterlock.core.ScheduleType r1 = com.masterlock.core.ScheduleType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.adapters.InvitationAdapter.C11032.<clinit>():void");
        }
    }

    public class InvitationViewHolder {
        @InjectView(2131296843)
        public TextView access;
        @InjectView(2131296858)
        public TextView activity;
        @InjectView(2131296507)
        public View container;
        @InjectView(2131296854)
        public TextView coowner;
        @InjectView(2131296530)
        public ImageView delete;
        public Invitation invitation;
        public SwipeListener listener;
        @InjectView(2131296860)
        public TextView name;
        @InjectView(2131296783)
        public SwipeLayout swipe;

        public InvitationViewHolder(View view, Invitation invitation2) {
            ButterKnife.inject((Object) this, view);
            this.invitation = invitation2;
            if (VERSION.SDK_INT >= 21) {
                this.container.setOnTouchListener(new OnTouchListener() {
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        return InvitationViewHolder.lambda$new$0(InvitationViewHolder.this, view, motionEvent);
                    }
                });
            }
        }

        public static /* synthetic */ boolean lambda$new$0(InvitationViewHolder invitationViewHolder, View view, MotionEvent motionEvent) {
            Drawable background = invitationViewHolder.container.getBackground();
            if (background != null && (background instanceof RippleDrawable)) {
                ((RippleDrawable) background).setHotspot(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }

        @OnClick({2131296496})
        public void click(View view) {
            if (this.swipe.getOpenStatus() != Status.Close) {
                this.swipe.requestDisallowInterceptTouchEvent(true);
            }
            if (InvitationAdapter.this.mClickListener != null) {
                InvitationAdapter.this.mClickListener.onItemClick(view, InvitationAdapter.this.getPosition(this.invitation));
            }
        }
    }

    public InvitationAdapter(Context context) {
        this(context, new ArrayList());
    }

    public InvitationAdapter(Context context, List<Invitation> list) {
        super(context, 0, 0, list);
        this.items = list;
        this.resources = context.getResources();
    }

    public ItemClickListener getItemClickListener() {
        return this.mClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public ItemSwipeListener getItemSwipeListener() {
        return this.mSwipeListener;
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {
        this.mSwipeListener = itemSwipeListener;
    }

    public Lock getLock() {
        return this.lock;
    }

    public void setLock(Lock lock2) {
        this.lock = lock2;
        this.items = lock2.getInvitations();
    }

    public Invitation getItem(int i) {
        return (Invitation) this.items.get(i);
    }

    public int getPosition(Invitation invitation) {
        return this.items.indexOf(invitation);
    }

    public int getCount() {
        return this.items.size();
    }

    private View newView(Context context, ViewGroup viewGroup, Invitation invitation) {
        View inflate = LayoutInflater.from(context).inflate(C1075R.layout.invitation_list_item, viewGroup, false);
        inflate.setTag(new InvitationViewHolder(inflate, invitation));
        return inflate;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        CharSequence charSequence;
        Invitation invitation = (Invitation) this.items.get(i);
        if (view == null) {
            view = newView(viewGroup.getContext(), viewGroup, invitation);
        }
        InvitationViewHolder invitationViewHolder = (InvitationViewHolder) view.getTag();
        invitationViewHolder.swipe.setSwipeEnabled(false);
        if (invitationViewHolder.listener != null) {
            invitationViewHolder.swipe.removeSwipeListener(invitationViewHolder.listener);
        }
        invitationViewHolder.invitation = invitation;
        int i2 = 8;
        if (invitation.getGuest() != null) {
            String displayNameForGuest = TextUtils.getDisplayNameForGuest(getContext(), invitation.getGuest());
            invitationViewHolder.name.setText(displayNameForGuest);
            invitationViewHolder.name.setVisibility(android.text.TextUtils.isEmpty(displayNameForGuest) ? 8 : 0);
        }
        TextView textView = invitationViewHolder.coowner;
        if (invitation.getAccessType() == AccessType.CO_OWNER) {
            i2 = 0;
        }
        textView.setVisibility(i2);
        int i3 = C11032.$SwitchMap$com$masterlock$core$ScheduleType[invitation.getScheduleType().ordinal()];
        int i4 = C1075R.C1076drawable.ic_calender_daytime;
        switch (i3) {
            case 1:
                charSequence = this.resources.getString(C1075R.string.unlimited);
                break;
            case 2:
                charSequence = this.resources.getString(C1075R.string.day);
                break;
            case 3:
                charSequence = this.resources.getString(C1075R.string.night);
                i4 = C1075R.C1076drawable.ic_calender_nighttime;
                break;
            default:
                charSequence = null;
                i4 = 0;
                break;
        }
        invitationViewHolder.access.setText(charSequence);
        invitationViewHolder.access.setCompoundDrawablesWithIntrinsicBounds(i4, 0, 0, 0);
        if (invitation.getStatus() == InvitationStatus.PENDING) {
            try {
                Date parseServerDate = MLDateUtils.parseServerDate(invitation.getCreatedOn());
                if (invitation.isExpired()) {
                    invitationViewHolder.activity.setText(C1075R.string.invitation_expired);
                } else {
                    invitationViewHolder.activity.setText(this.resources.getString(C1075R.string.invitation_date, new Object[]{DateUtils.formatDateTime(getContext(), parseServerDate.getTime(), 524305)}));
                }
            } catch (ParseException unused) {
                invitationViewHolder.activity.setText(null);
            }
        }
        showSwipe(invitationViewHolder);
        return view;
    }

    private void showSwipe(final InvitationViewHolder invitationViewHolder) {
        final ImageView imageView = invitationViewHolder.delete;
        invitationViewHolder.swipe.setSwipeEnabled(true);
        invitationViewHolder.swipe.setShowMode(ShowMode.LayDown);
        invitationViewHolder.listener = new SwipeListener() {
            public void onHandRelease(SwipeLayout swipeLayout, float f, float f2) {
            }

            public void onStartOpen(SwipeLayout swipeLayout) {
            }

            public void onOpen(SwipeLayout swipeLayout) {
                swipeLayout.requestDisallowInterceptTouchEvent(true);
                swipeLayout.close(true);
                if (InvitationAdapter.this.mSwipeListener != null) {
                    InvitationAdapter.this.mSwipeListener.onItemSwipe(invitationViewHolder.container, InvitationAdapter.this.getPosition(invitationViewHolder.invitation));
                }
            }

            public void onStartClose(SwipeLayout swipeLayout) {
                swipeLayout.setSwipeEnabled(false);
            }

            public void onClose(SwipeLayout swipeLayout) {
                swipeLayout.requestDisallowInterceptTouchEvent(true);
                swipeLayout.setSwipeEnabled(true);
            }

            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {
                if (swipeLayout.isSwipeEnabled()) {
                    float min = Math.min(1.0f, Math.abs(((float) i) / ((float) swipeLayout.getDragDistance())) * 1.5f);
                    imageView.setAlpha(min);
                    ImageView imageView = imageView;
                    imageView.setY((1.0f - min) * ((float) imageView.getHeight()));
                }
            }
        };
        invitationViewHolder.swipe.addSwipeListener(invitationViewHolder.listener);
    }
}
