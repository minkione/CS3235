package com.masterlock.ble.app.view.modal;

import android.app.Dialog;
import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.CompletedContactPickerEvent;
import com.masterlock.ble.app.bus.StartContactPickerEvent;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.screens.GuestScreens.EditGuestDetailsFromInvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.EditGuestDetailsFromLockDetailsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.ExistingGuestListFromInvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.ExistingGuestListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationListKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Flow;
import flow.Screen;
import javax.inject.Inject;

public class AddGuestModal extends CardView {
    @InjectView(2131296331)
    Button addFromContactsButton;
    @InjectView(2131296339)
    Button enterManuallyButton;
    @InjectView(2131296340)
    Button existingGuestButton;
    @InjectView(2131296317)
    TextView mBody;
    @InjectView(2131296320)
    LinearLayout mBodyContainer;
    /* access modifiers changed from: private */
    public Dialog mDialog;
    @Inject
    Bus mEventBus;
    private boolean mIsTemporary;
    /* access modifiers changed from: private */
    public Lock mLock;
    private Object mReceivedPickedContactListener;
    /* access modifiers changed from: private */
    public Screen mScreen;

    public AddGuestModal(Context context, Dialog dialog, Lock lock) {
        this(context, dialog, lock, false);
    }

    public AddGuestModal(Context context, Dialog dialog, Lock lock, boolean z) {
        this(context, (AttributeSet) null, dialog, lock);
        this.mIsTemporary = z;
    }

    public AddGuestModal(Context context, AttributeSet attributeSet, Dialog dialog, Lock lock) {
        super(context, attributeSet);
        this.mReceivedPickedContactListener = new Object() {
            @Subscribe
            public void onReceivedContact(CompletedContactPickerEvent completedContactPickerEvent) {
                AddGuestModal.this.mDialog.dismiss();
                if ((AddGuestModal.this.mScreen instanceof LockDetailsKeySafe) || (AddGuestModal.this.mScreen instanceof LockDetailsPadLock)) {
                    AppFlow.get(AddGuestModal.this.getContext()).goTo(new EditGuestDetailsFromLockDetailsKeySafe(AddGuestModal.this.mLock, completedContactPickerEvent.guest));
                } else if (AddGuestModal.this.mScreen instanceof InvitationListKeySafe) {
                    AppFlow.get(AddGuestModal.this.getContext()).goTo(new EditGuestDetailsFromInvitationListKeySafe(AddGuestModal.this.mLock, completedContactPickerEvent.guest));
                }
                AddGuestModal.this.resetToolbar();
            }
        };
        this.mDialog = dialog;
        this.mLock = lock;
        this.mScreen = (Screen) AppFlow.getScreen(context);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.add_guest_modal, this);
        ButterKnife.inject((View) this);
        MasterLockApp.get().inject(this);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.requestWindowFeature(1);
        this.mDialog.setContentView(this);
        this.existingGuestButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AddGuestModal.lambda$init$0(AddGuestModal.this, view);
            }
        });
        this.addFromContactsButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AddGuestModal.this.pickFromContacts();
            }
        });
        this.enterManuallyButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AddGuestModal.lambda$init$2(AddGuestModal.this, view);
            }
        });
        this.mEventBus.register(this.mReceivedPickedContactListener);
    }

    public static /* synthetic */ void lambda$init$0(AddGuestModal addGuestModal, View view) {
        Flow flow2 = AppFlow.get(addGuestModal.getContext());
        Screen screen = addGuestModal.mScreen;
        if ((screen instanceof LockDetailsKeySafe) || (screen instanceof LockDetailsPadLock)) {
            flow2.goTo(new ExistingGuestListKeySafe(addGuestModal.mLock));
        } else if (screen instanceof InvitationListKeySafe) {
            flow2.goTo(new ExistingGuestListFromInvitationListKeySafe(addGuestModal.mLock));
        }
        addGuestModal.resetToolbar();
        addGuestModal.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$init$2(AddGuestModal addGuestModal, View view) {
        Flow flow2 = AppFlow.get(addGuestModal.getContext());
        Screen screen = addGuestModal.mScreen;
        if ((screen instanceof LockDetailsKeySafe) || (screen instanceof LockDetailsPadLock)) {
            flow2.goTo(new EditGuestDetailsFromLockDetailsKeySafe(addGuestModal.mLock, null));
        } else if (screen instanceof InvitationListKeySafe) {
            flow2.goTo(new EditGuestDetailsFromInvitationListKeySafe(addGuestModal.mLock, null));
        }
        addGuestModal.resetToolbar();
        addGuestModal.mDialog.dismiss();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEventBus.unregister(this.mReceivedPickedContactListener);
    }

    public void pickFromContacts() {
        this.mEventBus.post(new StartContactPickerEvent(this.mLock));
    }

    public TextView getBody() {
        return this.mBody;
    }

    public void setBody(TextView textView) {
        this.mBody = textView;
    }

    public LinearLayout getBodyContainer() {
        return this.mBodyContainer;
    }

    public void setBodyContainer(LinearLayout linearLayout) {
        this.mBodyContainer = linearLayout;
    }

    public Button getExistingGuestButton() {
        return this.existingGuestButton;
    }

    public void setExistingGuestButton(Button button) {
        this.existingGuestButton = button;
    }

    public Button getAddFromContactsButton() {
        return this.addFromContactsButton;
    }

    public void setAddFromContactsButton(Button button) {
        this.addFromContactsButton = button;
    }

    public Button getEnterManuallyButton() {
        return this.enterManuallyButton;
    }

    public void setEnterManuallyButton(Button button) {
        this.enterManuallyButton = button;
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    /* access modifiers changed from: private */
    public void resetToolbar() {
        Builder builder = new Builder(getResources());
        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
        this.mEventBus.post(builder.build());
    }
}
