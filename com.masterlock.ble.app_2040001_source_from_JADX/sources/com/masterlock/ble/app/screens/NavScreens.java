package com.masterlock.ble.app.screens;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.core.AccountProfileInfo;
import flow.HasParent;
import flow.Layout;

public class NavScreens {

    @Layout(2131427358)
    public static class AccountProfile extends AppScreen implements HasParent<AccountSettings> {
        public int getTitleResourceId() {
            return C1075R.string.account_setting_profile_title;
        }

        public AccountSettings getParent() {
            return new AccountSettings();
        }
    }

    @Layout(2131427360)
    public static class AccountSettings extends AppScreen implements HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.settings_label;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427387)
    public static class ChangeEmailAddress extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.change_email_address_label;
        }

        public ChangeEmailAddress(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427428)
    public static class ChangeLanguage extends AppScreen implements HasParent<AccountSettings> {
        public int getTitleResourceId() {
            return C1075R.string.account_setting_language_selection_title;
        }

        public AccountSettings getParent() {
            return new AccountSettings();
        }
    }

    @Layout(2131427388)
    public static class ChangeName extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.change_name_label;
        }

        public ChangeName(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427389)
    public static class ChangePassword extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.profile_setting_change_password_title;
        }

        public ChangePassword(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427390)
    public static class ChangePhoneNumber extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.profile_setting_change_phone_number_title;
        }

        public ChangePhoneNumber(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427392)
    public static class ChangeTimezone extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.profile_setting_change_timezone_title;
        }

        public ChangeTimezone(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427393)
    public static class ChangeUsername extends AppScreen implements HasParent<AccountProfile>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.change_username_label;
        }

        public ChangeUsername(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public AccountProfile getParent() {
            return new AccountProfile();
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427394)
    public static class ContactMasterlock extends AppScreen implements HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.contact_master_lock;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427418)
    public static class Help extends AppScreen implements HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.help;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427473)
    public static class NotificationsOptions extends AppScreen implements HasParent<AccountSettings> {
        public int getTitleResourceId() {
            return C1075R.string.account_setting_notifications_title;
        }

        public AccountSettings getParent() {
            return new AccountSettings();
        }
    }

    @Layout(2131427493)
    public static class PrivacyPolicy extends AppScreen implements HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.privacy_policy;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427495)
    public static class Profile extends AppScreen implements HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.profile;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427530)
    public static class VerifyNewEmailAddress extends AppScreen implements HasParent<ChangeEmailAddress>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.change_email_address_label;
        }

        public VerifyNewEmailAddress(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public ChangeEmailAddress getParent() {
            return new ChangeEmailAddress(this.mAccountProfileInfo);
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }

    @Layout(2131427531)
    public static class VerifyNewPhone extends AppScreen implements HasParent<ChangePhoneNumber>, IProfileInfoScreen {
        private AccountProfileInfo mAccountProfileInfo;

        public int getTitleResourceId() {
            return C1075R.string.profile_setting_change_phone_number_title;
        }

        public VerifyNewPhone(AccountProfileInfo accountProfileInfo) {
            this.mAccountProfileInfo = accountProfileInfo;
        }

        public ChangePhoneNumber getParent() {
            return new ChangePhoneNumber(this.mAccountProfileInfo);
        }

        public AccountProfileInfo getAccountProfileInfo() {
            return this.mAccountProfileInfo;
        }
    }
}
