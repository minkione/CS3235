package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.entity.NotificationEventSettingsResponse;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.NotificationEventsPresenter;
import com.masterlock.ble.app.presenter.nav.settings.NotificationItemRvAdapter;
import com.masterlock.ble.app.presenter.nav.settings.NotificationItemRvAdapter.RecyclerViewItemInteractionListener;

public class NotificationEventsView extends LinearLayout {
    private NotificationEventsPresenter mNotificationEventsPresenter;
    private NotificationItemRvAdapter notificationItemRvAdapter;
    @InjectView(2131296721)
    RecyclerView rVNotificationEvents;

    public NotificationEventsView(Context context) {
        this(context, null);
    }

    public NotificationEventsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mNotificationEventsPresenter = new NotificationEventsPresenter(this);
            this.mNotificationEventsPresenter.start();
        }
    }

    public void displaySuccess() {
        Toast.makeText(getContext(), C1075R.string.notifications_events_updated, 0).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void initializeView(NotificationEventSettingsResponse notificationEventSettingsResponse) {
        this.notificationItemRvAdapter = new NotificationItemRvAdapter(getContext(), notificationEventSettingsResponse, new RecyclerViewItemInteractionListener() {
            public final void onErrorDetected(int[] iArr) {
                NotificationEventsView.this.mNotificationEventsPresenter.showErrorModal(iArr);
            }
        });
        this.rVNotificationEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rVNotificationEvents.setHasFixedSize(true);
        this.rVNotificationEvents.setAdapter(this.notificationItemRvAdapter);
    }

    public void notifyAdapter() {
        NotificationItemRvAdapter notificationItemRvAdapter2 = this.notificationItemRvAdapter;
        if (notificationItemRvAdapter2 != null) {
            notificationItemRvAdapter2.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296349})
    public void updateNotificationSettings() {
        this.mNotificationEventsPresenter.updateNotificationEventSettings();
    }
}
