package com.masterlock.ble.app.view.guest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.adapters.InvitationAdapter;
import com.masterlock.ble.app.adapters.listeners.ItemClickListener;
import com.masterlock.ble.app.adapters.listeners.ItemSwipeListener;
import com.masterlock.ble.app.bus.AddGuestEvent;
import com.masterlock.ble.app.presenter.guest.InvitationListPresenter;
import com.masterlock.ble.app.screens.GuestScreens.InvitationListKeySafe;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class InvitationListView extends LinearLayout implements ItemClickListener, ItemSwipeListener, IAuthenticatedView {
    @InjectView(2131296360)
    public View mAddGuest;
    @InjectView(2131296422)
    public TextView mDeviceId;
    @InjectView(2131296453)
    public View mEmptyView;
    private InvitationAdapter mInvitationAdapter;
    private InvitationListPresenter mInvitationListPresenter;
    @InjectView(2131296572)
    ListView mListView;
    @InjectView(2131296597)
    public TextView mLockName;

    public InvitationListView(Context context) {
        super(context);
    }

    public InvitationListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public InvitationListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void updateView(Lock lock) {
        this.mInvitationAdapter.setNotifyOnChange(false);
        this.mInvitationAdapter.clear();
        if (lock != null) {
            this.mLockName.setText(lock.getName());
            this.mDeviceId.setText(lock.getKmsDeviceKey().getDeviceId());
            this.mInvitationAdapter.setLock(lock);
        } else {
            this.mLockName.setText(null);
            this.mDeviceId.setText(null);
        }
        this.mInvitationAdapter.notifyDataSetChanged();
        this.mInvitationAdapter.setNotifyOnChange(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mInvitationAdapter = new InvitationAdapter(getContext());
            this.mInvitationAdapter.setItemClickListener(this);
            this.mInvitationAdapter.setItemSwipeListener(this);
            this.mListView.setAdapter(this.mInvitationAdapter);
            this.mListView.setEmptyView(this.mEmptyView);
            this.mInvitationListPresenter = new InvitationListPresenter(((InvitationListKeySafe) AppFlow.getScreen(getContext())).mLock, this);
            MasterLockApp.get().inject(this.mInvitationListPresenter);
        }
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mInvitationListPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mInvitationListPresenter.finish();
    }

    public void onItemClick(View view, int i) {
        this.mInvitationListPresenter.goToInvitation(this.mInvitationAdapter.getItem(i));
    }

    public void onItemSwipe(View view, int i) {
        this.mInvitationListPresenter.deleteInvitation(this.mInvitationAdapter.getItem(i));
    }

    @OnClick({2131296360})
    public void click(View view) {
        this.mInvitationListPresenter.goToAddGuest(new AddGuestEvent());
    }

    public void showAccessRevoked() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_access_revoked_explanation), 1).show();
    }

    public void showGuestDeleted() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_deleted_explanation), 1).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
