package com.masterlock.ble.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.bus.ValidateInvitationCodeEvent;
import com.masterlock.ble.app.screens.SignUpScreens.AccountDetails;
import com.masterlock.ble.app.screens.SignUpScreens.SignUp;
import com.masterlock.ble.app.util.PermissionUtil;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Subscribe;
import java.util.List;
import javax.inject.Inject;

public class SignUpActivity extends FlowActivity {
    private static final int REQUEST_CONTACTS_PERMISSION = 2;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static boolean shouldValidateInvitation = false;
    MasterLockSharedPreferences mPreferences;
    @Inject
    PermissionUtil permissionUtil;

    public int getContentView() {
        return C1075R.layout.no_action_bar_activity;
    }

    public Object defaultScreen() {
        return new SignUp();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPreferences = MasterLockSharedPreferences.getInstance();
        Intent intent = getIntent();
        String action = intent.getAction();
        this.mPreferences.putInvitationCode("");
        if ("android.intent.action.VIEW".equals(action)) {
            handleVerificationEmail(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mPreferences.putInvitationCode("");
        if ("android.intent.action.VIEW".equals(intent.getAction()) && !handleVerificationEmail(intent)) {
            this.mEventBus.post(new ValidateInvitationCodeEvent());
        }
    }

    private boolean handleVerificationEmail(Intent intent) {
        List pathSegments = intent.getData().getPathSegments();
        String dataString = intent.getDataString();
        if (pathSegments.size() == 3) {
            String str = (String) pathSegments.get(1);
            String str2 = (String) pathSegments.get(2);
            if (dataString.contains("invitation/product")) {
                this.mPreferences.putInvitationCode(str2);
                if (!MasterLockApp.get().isSignedIn() || !LockActivity.isActive) {
                    shouldValidateInvitation = true;
                } else {
                    Intent intent2 = new Intent(this, LockActivity.class);
                    intent2.putExtra("invitationId", str2);
                    startActivity(intent2);
                    return true;
                }
            } else {
                MasterLockApp.get().logOut(false);
                verifyVerificationCode(str2);
            }
        }
        return false;
    }

    public void verifyVerificationCode(String str) {
        if (str.equals(this.mPreferences.getVerificationId())) {
            AppFlow.get(this).replaceTo(new AccountDetails());
        } else {
            AppFlow.get(this).replaceTo(new SignUp());
        }
    }

    @Subscribe
    public void changeSK(ChangeSoftKeyboardBehaviorEvent changeSoftKeyboardBehaviorEvent) {
        getWindow().setSoftInputMode(changeSoftKeyboardBehaviorEvent.getSoftKeyboardBehavior());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mEventBus.register(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
    }

    public void displayError(Throwable th) {
        Toast.makeText(this, th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }
}
