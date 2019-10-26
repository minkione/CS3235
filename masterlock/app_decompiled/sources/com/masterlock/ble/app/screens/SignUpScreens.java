package com.masterlock.ble.app.screens;

import com.masterlock.ble.app.C1075R;
import flow.Layout;

public class SignUpScreens {

    @Layout(2131427356)
    public static class AccountDetails extends AppScreen implements HideToolbar {
    }

    @Layout(2131427493)
    public static class PrivacyPolicy extends AppScreen implements MultiParent {
        public int getTitleResourceId() {
            return C1075R.string.privacy_policy;
        }
    }

    @Layout(2131427496)
    public static class ResendEmail extends AppScreen implements HideToolbar {
    }

    @Layout(2131427497)
    public static class ResendSms extends AppScreen implements HideToolbar {
    }

    @Layout(2131427516)
    public static class SignUp extends AppScreen implements HideToolbar {

        /* renamed from: cc */
        public String f171cc = "";
        public String email = "";
        public String phone = "";

        public SignUp() {
        }

        public SignUp(String str, String str2, String str3) {
            this.email = str;
            this.f171cc = str2;
            this.phone = str3;
        }
    }

    @Layout(2131427464)
    public static class TermsOfService extends AppScreen implements MultiParent {
        public final boolean mShouldShowOKButton;

        public int getTitleResourceId() {
            return C1075R.string.terms_of_service;
        }

        public TermsOfService() {
            this.mShouldShowOKButton = true;
        }

        public TermsOfService(boolean z) {
            this.mShouldShowOKButton = z;
        }
    }
}
