package com.masterlock.ble.app.presenter.nav.settings;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.api.entity.NotificationEventSettings;
import com.masterlock.api.entity.NotificationEventSettings.NotificationItemState;
import com.masterlock.api.entity.NotificationEventSettingsResponse;
import com.masterlock.ble.app.C1075R;
import java.util.LinkedList;

public class NotificationItemRvAdapter extends Adapter<NotificationEventViewHolder> {
    private int disabledColor;
    /* access modifiers changed from: private */
    public boolean hasPhoneNumber;
    /* access modifiers changed from: private */
    public boolean isPhoneVerified;
    private final Context mContext;
    /* access modifiers changed from: private */
    public LinkedList<NotificationEventSettings> notificationEventSettings;
    /* access modifiers changed from: private */
    public RecyclerViewItemInteractionListener recyclerViewItemInteractionListener;
    private int selectedColor;
    private int unSelectedColor;

    public class NotificationEventViewHolder extends ViewHolder {
        @InjectView(2131296519)
        ImageView iVEmailNotification;
        @InjectView(2131296520)
        ImageView iVSMSNotification;
        protected View itemView;
        private OnClickListener onItemClickListener = new OnClickListener() {
            public void onClick(View view) {
                NotificationItemState notificationItemState = view.getTag() == NotificationItemState.SELECTED ? NotificationItemState.UNSELECTED : NotificationItemState.SELECTED;
                boolean z = false;
                if (view.getId() != C1075R.C1077id.ib_sms_notification) {
                    NotificationEventSettings notificationEventSettings = (NotificationEventSettings) NotificationItemRvAdapter.this.notificationEventSettings.get(NotificationEventViewHolder.this.getAdapterPosition());
                    if (notificationItemState == NotificationItemState.SELECTED) {
                        z = true;
                    }
                    notificationEventSettings.setEmailNotificationState(Boolean.valueOf(z));
                    NotificationItemRvAdapter.this.updateItemState((ImageView) view, notificationItemState, true);
                } else if (NotificationItemRvAdapter.this.isPhoneVerified) {
                    NotificationEventSettings notificationEventSettings2 = (NotificationEventSettings) NotificationItemRvAdapter.this.notificationEventSettings.get(NotificationEventViewHolder.this.getAdapterPosition());
                    if (notificationItemState == NotificationItemState.SELECTED) {
                        z = true;
                    }
                    notificationEventSettings2.setSmsNotificationState(Boolean.valueOf(z));
                    NotificationItemRvAdapter.this.updateItemState((ImageView) view, notificationItemState, true);
                } else if (NotificationItemRvAdapter.this.recyclerViewItemInteractionListener != null) {
                    NotificationItemRvAdapter.this.recyclerViewItemInteractionListener.onErrorDetected(NotificationItemRvAdapter.this.hasPhoneNumber ? new int[]{C1075R.string.unverified_mobile_phone_number_title, C1075R.string.unverified_mobile_phone_number_body} : new int[]{C1075R.string.no_mobile_phone_number_title, C1075R.string.no_mobile_phone_number_body});
                }
            }
        };
        @InjectView(2131296822)
        TextView tVEventName;

        public NotificationEventViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
            this.itemView = view;
            this.iVSMSNotification.setOnClickListener(this.onItemClickListener);
            this.iVEmailNotification.setOnClickListener(this.onItemClickListener);
        }
    }

    public interface RecyclerViewItemInteractionListener {
        void onErrorDetected(int[] iArr);
    }

    public NotificationItemRvAdapter(Context context, NotificationEventSettingsResponse notificationEventSettingsResponse, RecyclerViewItemInteractionListener recyclerViewItemInteractionListener2) {
        this.mContext = context;
        this.notificationEventSettings = notificationEventSettingsResponse.getNotificationEventSettingsList();
        this.isPhoneVerified = notificationEventSettingsResponse.getPhoneVerified().booleanValue();
        this.hasPhoneNumber = notificationEventSettingsResponse.hasPhoneNumber();
        this.recyclerViewItemInteractionListener = recyclerViewItemInteractionListener2;
        initializeStateColors();
    }

    public NotificationEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NotificationEventViewHolder(LayoutInflater.from(this.mContext).inflate(C1075R.layout.notification_event_item, viewGroup, false));
    }

    public void onBindViewHolder(NotificationEventViewHolder notificationEventViewHolder, int i) {
        NotificationEventSettings notificationEventSettings2 = (NotificationEventSettings) this.notificationEventSettings.get(i);
        notificationEventViewHolder.tVEventName.setText(notificationEventSettings2.getEventName());
        updateItemState(notificationEventViewHolder.iVSMSNotification, notificationEventSettings2.computeSmsNotificationState(this.isPhoneVerified), false);
        updateItemState(notificationEventViewHolder.iVEmailNotification, notificationEventSettings2.computeEmailNotificationState(), false);
        notificationEventViewHolder.iVEmailNotification.setEnabled(notificationEventSettings2.isEnabled().booleanValue());
        if (!this.isPhoneVerified) {
            notificationEventViewHolder.iVSMSNotification.setVisibility(8);
            LayoutParams layoutParams = (LayoutParams) notificationEventViewHolder.iVEmailNotification.getLayoutParams();
            layoutParams.addRule(11);
            layoutParams.setMargins(0, 0, 0, 0);
            notificationEventViewHolder.iVEmailNotification.setLayoutParams(layoutParams);
        }
    }

    public int getItemCount() {
        return this.notificationEventSettings.size();
    }

    private void initializeStateColors() {
        this.selectedColor = ContextCompat.getColor(this.mContext, C1075R.color.spruce);
        this.unSelectedColor = ContextCompat.getColor(this.mContext, C1075R.color.tulip);
        this.disabledColor = ContextCompat.getColor(this.mContext, C1075R.color.walnut);
    }

    public void updateItemState(ImageView imageView, NotificationItemState notificationItemState, boolean z) {
        int i;
        switch (notificationItemState) {
            case SELECTED:
                i = this.selectedColor;
                break;
            case UNSELECTED:
                i = this.unSelectedColor;
                break;
            case DISABLED:
                i = this.disabledColor;
                break;
            default:
                i = -1;
                break;
        }
        if (z) {
            animateImageView(imageView, i);
        } else {
            imageView.getDrawable().mutate().setColorFilter(i, Mode.SRC_ATOP);
        }
        imageView.setTag(notificationItemState);
    }

    public void animateImageView(ImageView imageView, int i) {
        ValueAnimator ofFloat = ObjectAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.addUpdateListener(new AnimatorUpdateListener(i, imageView) {
            private final /* synthetic */ int f$1;
            private final /* synthetic */ ImageView f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationItemRvAdapter.lambda$animateImageView$0(NotificationItemRvAdapter.this, this.f$1, this.f$2, valueAnimator);
            }
        });
        ofFloat.setDuration(250);
        ofFloat.start();
    }

    public static /* synthetic */ void lambda$animateImageView$0(NotificationItemRvAdapter notificationItemRvAdapter, int i, ImageView imageView, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        imageView.setColorFilter(notificationItemRvAdapter.adjustAlpha(i, floatValue), Mode.SRC_ATOP);
        if (((double) floatValue) == 0.0d) {
            imageView.setColorFilter(null);
        }
    }

    public int adjustAlpha(int i, float f) {
        return Color.argb(Math.round(((float) Color.alpha(i)) * f), Color.red(i), Color.green(i), Color.blue(i));
    }
}
