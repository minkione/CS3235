package com.masterlock.ble.app.screens;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.core.Country;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Lock;
import flow.HasParent;
import flow.Layout;

public class GuestScreens {

    @Layout(2131427416)
    public static class EditGuestDetailsFromInvitationListKeySafe extends AppScreen implements HasParent<InvitationListKeySafe>, IGuestWithLockScreen {
        public Country country;
        public final Guest mGuest;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_share_with_guest;
        }

        public EditGuestDetailsFromInvitationListKeySafe(Lock lock, Guest guest) {
            this.mLock = lock;
            this.mGuest = guest;
        }

        public InvitationListKeySafe getParent() {
            return new InvitationListKeySafe(this.mLock);
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Country getCountry() {
            return this.country;
        }

        public void setCountry(Country country2) {
            this.country = country2;
        }
    }

    @Layout(2131427416)
    public static class EditGuestDetailsFromLockDetailsKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        public Country country;
        public final Guest mGuest;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_share_with_guest;
        }

        public EditGuestDetailsFromLockDetailsKeySafe(Lock lock, Guest guest) {
            this.mLock = lock;
            this.mGuest = guest;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Country getCountry() {
            return this.country;
        }

        public void setCountry(Country country2) {
            this.country = country2;
        }
    }

    @Layout(2131427416)
    public static class EditGuestDetailsFromSettingsKeySafe extends AppScreen implements HasParent<LockSettings>, IGuestWithLockScreen {
        private Country country;
        public final Guest mGuest;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.send_temp_code;
        }

        public EditGuestDetailsFromSettingsKeySafe(Lock lock, Guest guest) {
            this.mLock = lock;
            this.mGuest = guest;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Country getCountry() {
            return this.country;
        }

        public void setCountry(Country country2) {
            this.country = country2;
        }
    }

    @Layout(2131427407)
    public static class ExistingGuestListFromInvitationListKeySafe extends AppScreen implements MultiParent, IExistingGuestScreen {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.add_a_guest;
        }

        public ExistingGuestListFromInvitationListKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public Lock getLock() {
            return this.mLock;
        }
    }

    @Layout(2131427407)
    public static class ExistingGuestListFromSettingsKeySafe extends AppScreen implements HasParent<LockSettings>, IExistingGuestScreen {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.send_temp_code;
        }

        public ExistingGuestListFromSettingsKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }

        public Lock getLock() {
            return this.mLock;
        }
    }

    @Layout(2131427407)
    public static class ExistingGuestListKeySafe extends AppScreen implements MultiParent, IExistingGuestScreen {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.add_a_guest;
        }

        public ExistingGuestListKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public Lock getLock() {
            return this.mLock;
        }
    }

    @Layout(2131427414)
    public static class GrantAccessKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        public final Guest mGuest;
        public final Invitation mInvitation;
        public final Lock mLock;

        public Country getCountry() {
            return null;
        }

        public int getTitleResourceId() {
            return C1075R.string.add_a_guest;
        }

        public void setCountry(Country country) {
        }

        public GrantAccessKeySafe(Lock lock, Guest guest, Invitation invitation) {
            this.mLock = lock;
            this.mGuest = guest;
            this.mInvitation = invitation;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Invitation getInvitation() {
            return this.mInvitation;
        }
    }

    @Layout(2131427414)
    public static class InvitationDetailFromInvitationListKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        public final Guest mGuest;
        public final Invitation mInvitation;
        public final Lock mLock;

        public Country getCountry() {
            return null;
        }

        public int getMenuResourceId() {
            return C1075R.C1078menu.edit_guest_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.guest_detail_title;
        }

        public void setCountry(Country country) {
        }

        public InvitationDetailFromInvitationListKeySafe(Lock lock, Invitation invitation) {
            this.mLock = lock;
            this.mGuest = invitation.getGuest();
            this.mInvitation = invitation;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Invitation getInvitation() {
            return this.mInvitation;
        }
    }

    @Layout(2131427414)
    public static class InvitationDetailFromLockDetailsKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        public final Guest mGuest;
        public final Invitation mInvitation;
        public final Lock mLock;

        public Country getCountry() {
            return null;
        }

        public int getMenuResourceId() {
            return C1075R.C1078menu.edit_guest_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.guest_detail_title;
        }

        public void setCountry(Country country) {
        }

        public InvitationDetailFromLockDetailsKeySafe(Lock lock, Invitation invitation) {
            this.mLock = lock;
            this.mGuest = invitation.getGuest();
            this.mInvitation = invitation;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Invitation getInvitation() {
            return this.mInvitation;
        }
    }

    @Layout(2131427427)
    public static class InvitationListKeySafe extends AppScreen implements MultiParent {
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.invitations_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.guests_label;
        }

        public InvitationListKeySafe(Lock lock) {
            this.mLock = lock;
        }
    }

    @Layout(2131427414)
    public static class TemporaryAccessKeySafe extends AppScreen implements HasParent<EditGuestDetailsFromSettingsKeySafe>, IGuestWithLockScreen {
        public final Guest mGuest;
        public final Lock mLock;

        public Country getCountry() {
            return null;
        }

        public int getTitleResourceId() {
            return C1075R.string.send_temp_code;
        }

        public void setCountry(Country country) {
        }

        public TemporaryAccessKeySafe(Lock lock, Guest guest) {
            this.mLock = lock;
            this.mGuest = guest;
        }

        public EditGuestDetailsFromSettingsKeySafe getParent() {
            return new EditGuestDetailsFromSettingsKeySafe(this.mLock, this.mGuest);
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }
    }

    @Layout(2131427416)
    public static class UpdateGuestFromEditGuestDetailsKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        private Country country;
        public final boolean isFromInvitationCreationFlow;
        public final Guest mGuest;
        public final Invitation mInvitation;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.edit_access;
        }

        public UpdateGuestFromEditGuestDetailsKeySafe(Lock lock, Guest guest, Invitation invitation, boolean z) {
            this.mLock = lock;
            this.mGuest = guest;
            this.mInvitation = invitation;
            this.isFromInvitationCreationFlow = z;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Invitation getInvitation() {
            return this.mInvitation;
        }

        public Country getCountry() {
            return this.country;
        }

        public void setCountry(Country country2) {
            this.country = country2;
        }
    }

    @Layout(2131427416)
    public static class UpdateGuestFromExistingGuestListKeySafe extends AppScreen implements MultiParent, IGuestWithLockScreen {
        private Country country;
        public final Guest mGuest;
        public final Invitation mInvitation;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.edit_access;
        }

        public UpdateGuestFromExistingGuestListKeySafe(Lock lock, Guest guest, Invitation invitation) {
            this.mLock = lock;
            this.mGuest = guest;
            this.mInvitation = invitation;
        }

        public Guest getGuest() {
            return this.mGuest;
        }

        public Lock getLock() {
            return this.mLock;
        }

        public Invitation getInvitation() {
            return this.mInvitation;
        }

        public Country getCountry() {
            return this.country;
        }

        public void setCountry(Country country2) {
            this.country = country2;
        }
    }
}
