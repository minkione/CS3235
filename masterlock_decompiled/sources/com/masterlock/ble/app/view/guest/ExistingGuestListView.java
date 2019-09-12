package com.masterlock.ble.app.view.guest;

import android.content.Context;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.DrawableDecoration;
import com.masterlock.ble.app.adapters.DrawableDecoration.Position;
import com.masterlock.ble.app.adapters.GuestListAdapter;
import com.masterlock.ble.app.presenter.guest.ExistingGuestListPresenter;
import com.masterlock.ble.app.view.FlowTransitionCallback;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Guest;
import flow.Flow.Direction;
import java.util.EnumSet;
import java.util.List;

public class ExistingGuestListView extends LinearLayout implements IAuthenticatedView, FlowTransitionCallback {
    private ExistingGuestListPresenter mExistingGuestListPresenter;
    @InjectView(2131296870)
    public TextView mLockId;
    @InjectView(2131296871)
    public TextView mLockName;
    @InjectView(2131296684)
    RecyclerView mRecycler;

    public void transitionComplete(Direction direction) {
    }

    public ExistingGuestListView(Context context) {
        this(context, null);
    }

    public ExistingGuestListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mExistingGuestListPresenter = new ExistingGuestListPresenter(this);
            this.mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            this.mRecycler.addItemDecoration(new DrawableDecoration(getResources().getDrawable(C1075R.C1076drawable.line_divider), EnumSet.of(Position.MIDDLE)));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mExistingGuestListPresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void transitionStarted(Direction direction) {
        if (direction == Direction.FORWARD) {
            this.mExistingGuestListPresenter.start();
        }
    }

    public void updateView(List<Guest> list, String str, String str2) {
        this.mLockName.setText(str);
        this.mLockId.setText(str2);
        GuestListAdapter guestListAdapter = new GuestListAdapter(list);
        guestListAdapter.setOnItemClickListener(this.mExistingGuestListPresenter.getClickListener());
        this.mRecycler.setAdapter(guestListAdapter);
    }
}
