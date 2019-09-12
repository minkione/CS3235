package com.masterlock.ble.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.AccountProfileInfo.Builder;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.comparator.LockComparator.SortId;
import java.util.ArrayList;
import java.util.List;

public class MasterLockSharedPreferences implements AuthenticationStore {
    public static final String ACCEPTED_TOS_VERSION = "acceptedTermsOfServiceVersion";
    public static final String AUTH_TOKEN = "authToken";
    public static final String CAN_MANAGE_LOCK = "canManageLock";
    public static final String DB_TOKEN = "dbToken";
    public static final String ENCRYPTED_AUTH_TOKEN = "encryptedAuthToken";
    public static final String GUEST_SIMPLE_SELECTION_MODE_FLAG = "guestSimpleSelectionModeFlag";
    public static final String HAS_SEEN_WALK_THROUGH = "hasSeenWalkthrough";
    public static final String INSIDE_LOCK_LANDING = "inside_lock_landing";
    public static final String INTENTS_EMAIL_RESEND = "intentsEmailResendCode";
    public static final String INTENTS_EMAIL_RESEND_PERIOD = "intentsEmailResendCodePeriod";
    private static final String INTENTS_SMS_RESEND_PERIOD = "intentsSMSResendCodePeriod";
    public static final String INVITATION_CODE = "invitationCode";
    public static final String LAST_CONNECTION_SUCCESS = "lastConnectionSucces";
    public static final String LAST_LOCATION_LATITUDE = "lastLocationLatitude";
    public static final String LAST_LOCATION_LONGITUDE = "lastLocationLongitude";
    public static final String LOCK_LIST_SORT_BY = "lockListSortBy";
    public static final String LOCK_LIST_VIEW = "lock_landing_view";
    public static final String LOCK_NOTIFICATION = "lockNotification:";
    public static final String RESEND_EMAIL_VERIFICATION_CODE_COUNT = "resendEmailVerificationCode";
    public static final String RESEND_EMAIL_VERIFICATION_CODE_TIME_WINDOW = "resendEmailVerificationCodeTimeWindow";
    public static final String SELECTED_LANGUAGE = "selectedLanguage";
    public static final String SELECTED_LANGUAGE_ARRAY_INDEX = "selectedLanguageArrayIndex";
    public static final String SIGN_UP_EMAIL = "signUpEmail";
    public static final String SIGN_UP_PHONE = "signUpPhone";
    public static final String SIGN_UP_PHONE_CODE = "signUpPhoneCode";
    public static final String SIGN_UP_SKIP_SMS = "signUpSkipSms";
    public static final String SIGN_UP_VERIFICATION_ID = "signUpVerificationId";
    public static final String USER_ALPHA_CC = "userAlphaCC";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_FIRST_NAME = "userFirstName";
    public static final String USER_LAST_NAME = "userLastName";
    public static final String USER_NAME = "userName";
    public static final String USER_PHONE_CC = "userPhoneCC";
    public static final String USER_PHONE_IS_VERIFIED = "userPhoneIsVerified";
    public static final String USER_PHONE_NUMBER = "userPhoneNumber";
    public static final String USER_TIME_ZONE = "userTimeZone";
    public static final String VALID_INVITATION_CODE = "validInvitationCode";
    private static MasterLockSharedPreferences sInstance;
    Context mContext;
    private SharedPreferences mSharedPreferences = null;

    private MasterLockSharedPreferences(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        this.mContext = context;
    }

    public static MasterLockSharedPreferences getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MasterLockSharedPreferences(context);
        }
        return sInstance;
    }

    public static MasterLockSharedPreferences getInstance() {
        if (sInstance == null) {
            sInstance = new MasterLockSharedPreferences(MasterLockApp.get());
        }
        return sInstance;
    }

    public void putGuestInterfaceSelectionModeForLockModel(String str, int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public int getGuestInterfaceSelectionModeForLockModel(String str) {
        return this.mSharedPreferences.getInt(str, GuestInterface.SIMPLE.getValue());
    }

    public void putAcceptedTermsOfServiceVersion(int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(ACCEPTED_TOS_VERSION, i);
        edit.apply();
    }

    public int getAcceptedTermsOfServiceVersion() {
        return this.mSharedPreferences.getInt(ACCEPTED_TOS_VERSION, -1);
    }

    public void putAuthToken(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(AUTH_TOKEN, str);
        edit.apply();
    }

    public String getAuthToken() {
        return this.mSharedPreferences.getString(AUTH_TOKEN, "");
    }

    public void putDbToken(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(DB_TOKEN, str);
        edit.apply();
    }

    public String getDbToken() {
        return this.mSharedPreferences.getString(DB_TOKEN, "");
    }

    public void putEncryptedAuthToken(String str, String str2) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(ENCRYPTED_AUTH_TOKEN, new PBKDF2Encryptor().encrypt(str, str2));
        edit.apply();
    }

    public String getEncryptedAuthToken() {
        return this.mSharedPreferences.getString(ENCRYPTED_AUTH_TOKEN, "");
    }

    public void putUsername(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_NAME, str);
        edit.apply();
    }

    public String getUsername() {
        return this.mSharedPreferences.getString(USER_NAME, "");
    }

    public void putUserFirstName(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_FIRST_NAME, str);
        edit.apply();
    }

    public String getUserFirstName() {
        return this.mSharedPreferences.getString(USER_FIRST_NAME, "");
    }

    public void putUserLastName(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_LAST_NAME, str);
        edit.apply();
    }

    public String getUserLastName() {
        return this.mSharedPreferences.getString(USER_LAST_NAME, "");
    }

    public void putUserEmail(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_EMAIL, str);
        edit.apply();
    }

    public String getUserEmail() {
        return this.mSharedPreferences.getString(USER_EMAIL, "");
    }

    public void putUserPhoneNumber(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_PHONE_NUMBER, str);
        edit.apply();
    }

    public String getUserPhoneNumber() {
        return this.mSharedPreferences.getString(USER_PHONE_NUMBER, "");
    }

    public void putUserAlphaCC(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_ALPHA_CC, str);
        edit.apply();
    }

    public String getUserAlphaCC() {
        return this.mSharedPreferences.getString(USER_ALPHA_CC, "");
    }

    public void putUserPhoneCC(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_PHONE_CC, str);
        edit.apply();
    }

    public String getUserPhoneCC() {
        return this.mSharedPreferences.getString(USER_PHONE_CC, "");
    }

    public void putUserPhoneIsVerified(boolean z) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(USER_PHONE_IS_VERIFIED, z);
        edit.apply();
    }

    public boolean getUserPhoneIsVerified() {
        return this.mSharedPreferences.getBoolean(USER_PHONE_IS_VERIFIED, false);
    }

    public void putUserTimeZone(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(USER_TIME_ZONE, str);
        edit.apply();
    }

    public String getUserTimeZone() {
        return this.mSharedPreferences.getString(USER_TIME_ZONE, "");
    }

    public void putAppHasShownWalkThrough(boolean z) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(HAS_SEEN_WALK_THROUGH, z);
        edit.apply();
    }

    public String getUserFullName() {
        return String.format("%s %s", new Object[]{getUserFirstName(), getUserLastName()});
    }

    public boolean getAppHasShownWalkThrough() {
        return this.mSharedPreferences.getBoolean(HAS_SEEN_WALK_THROUGH, false);
    }

    public boolean canManageLock() {
        return this.mSharedPreferences.getBoolean(CAN_MANAGE_LOCK, false);
    }

    public void putCanManageLock(boolean z) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(CAN_MANAGE_LOCK, z);
        edit.apply();
    }

    public void putNotificationFlagForLock(String str, String str2) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public String getNotificationFlagForLock(String str, String str2) {
        String string = this.mSharedPreferences.getString(str, null);
        if (string != null) {
            if (versionCompare(str2, str).intValue() <= 0) {
                return string;
            }
            deleteNotificationFlagForLock(str);
        }
        return null;
    }

    public void deleteNotificationFlagForLock(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.remove(str);
        edit.apply();
    }

    public void putSortBy(SortId sortId) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(LOCK_LIST_SORT_BY, sortId.getSortId());
        edit.apply();
    }

    public String getSortBy() {
        return this.mSharedPreferences.getString(LOCK_LIST_SORT_BY, SortId.NAME_SORT.getSortId());
    }

    public void putGuestSimpleSelectionModeFlag(boolean z) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(GUEST_SIMPLE_SELECTION_MODE_FLAG, z);
        edit.apply();
    }

    public boolean getGuestSimpleSelectionModeFlag() {
        return this.mSharedPreferences.getBoolean(GUEST_SIMPLE_SELECTION_MODE_FLAG, true);
    }

    public void putSignUpEmail(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(SIGN_UP_EMAIL, str);
        edit.apply();
    }

    public String getSignUpEmail() {
        return this.mSharedPreferences.getString(SIGN_UP_EMAIL, "");
    }

    public void putSignUpCountryCode(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(SIGN_UP_PHONE_CODE, str);
        edit.apply();
    }

    public String getSignUpCountryCode() {
        return this.mSharedPreferences.getString(SIGN_UP_PHONE_CODE, "+1");
    }

    public void putSignUpPhone(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(SIGN_UP_PHONE, str);
        edit.apply();
    }

    public String getSignUpPhone() {
        return this.mSharedPreferences.getString(SIGN_UP_PHONE, "");
    }

    public void putVerificationId(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(SIGN_UP_VERIFICATION_ID, str);
        edit.apply();
    }

    public String getVerificationId() {
        return this.mSharedPreferences.getString(SIGN_UP_VERIFICATION_ID, "");
    }

    public void putLatitude(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(LAST_LOCATION_LATITUDE, str);
        edit.apply();
    }

    public String getLatitude() {
        return this.mSharedPreferences.getString(LAST_LOCATION_LATITUDE, "");
    }

    public void putLongitude(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(LAST_LOCATION_LONGITUDE, str);
        edit.apply();
    }

    public String getLongitude() {
        return this.mSharedPreferences.getString(LAST_LOCATION_LONGITUDE, "");
    }

    public void putLastConnectionSuccess(long j) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putLong(LAST_CONNECTION_SUCCESS, j);
        edit.apply();
    }

    public void putLockLandingId(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(INSIDE_LOCK_LANDING, str);
        edit.apply();
    }

    public void putLockListVisible(boolean z) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(LOCK_LIST_VIEW, z);
        edit.apply();
    }

    public Long getLastConnectionSuccess() {
        return Long.valueOf(this.mSharedPreferences.getLong(LAST_CONNECTION_SUCCESS, 0));
    }

    public void putSkipSmsVerification(int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(SIGN_UP_SKIP_SMS, i);
        edit.apply();
    }

    public int getSkipSmsVerification() {
        return this.mSharedPreferences.getInt(SIGN_UP_SKIP_SMS, 0);
    }

    public void putInvitationCode(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(INVITATION_CODE, str);
        edit.apply();
    }

    public String getSelectedLanguage() {
        return this.mSharedPreferences.getString(SELECTED_LANGUAGE, "");
    }

    public void putSelectedLanguage(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(SELECTED_LANGUAGE, str);
        edit.apply();
    }

    public int getSelectedLanguageArrayIndex() {
        return this.mSharedPreferences.getInt(SELECTED_LANGUAGE_ARRAY_INDEX, -1);
    }

    public void putSelectedLanguageArrayIndex(int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(SELECTED_LANGUAGE_ARRAY_INDEX, i);
        edit.apply();
    }

    public String getInvitationCode() {
        return this.mSharedPreferences.getString(INVITATION_CODE, "");
    }

    public String getValidInvitationCode() {
        return this.mSharedPreferences.getString(VALID_INVITATION_CODE, "");
    }

    public void putFirstPermissionInteraction(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(str, true);
        edit.apply();
    }

    public boolean getFirstPermissionInteraction(String str) {
        return this.mSharedPreferences.getBoolean(str, false);
    }

    public void putValidInvitationCode(String str) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(VALID_INVITATION_CODE, str);
        edit.apply();
    }

    public void putIntentEmailResendPeriods(List<String> list) {
        String json = new Gson().toJson((Object) list);
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(INTENTS_EMAIL_RESEND_PERIOD, json);
        edit.apply();
    }

    public List<String> getIntentEmailResendPeriods() {
        List<String> list = (List) new Gson().fromJson(this.mSharedPreferences.getString(INTENTS_EMAIL_RESEND_PERIOD, null), new TypeToken<List<String>>() {
        }.getType());
        if (list != null) {
            return list;
        }
        return new ArrayList();
    }

    public void putIntentSMSResendPeriods(List<String> list) {
        String json = new Gson().toJson((Object) list);
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(INTENTS_SMS_RESEND_PERIOD, json);
        edit.apply();
    }

    public List<String> getIntentSMSResendPeriods() {
        List<String> list = (List) new Gson().fromJson(this.mSharedPreferences.getString(INTENTS_SMS_RESEND_PERIOD, null), new TypeToken<List<String>>() {
        }.getType());
        if (list != null) {
            return list;
        }
        return new ArrayList();
    }

    public void clearItentSMSResendPeriods() {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(INTENTS_SMS_RESEND_PERIOD, "");
        edit.apply();
    }

    public void putResendEmailVerificationCodeCount(int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putInt(RESEND_EMAIL_VERIFICATION_CODE_COUNT, i);
        edit.apply();
    }

    public int getResendEmailVerificationCodeCount() {
        return this.mSharedPreferences.getInt(RESEND_EMAIL_VERIFICATION_CODE_COUNT, 0);
    }

    public void putResendEmailVerificationCodeTimeWindow(long j) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putLong(RESEND_EMAIL_VERIFICATION_CODE_TIME_WINDOW, j);
        edit.apply();
    }

    public long getResnedEmailVerificationCodeTimeWindow() {
        return this.mSharedPreferences.getLong(RESEND_EMAIL_VERIFICATION_CODE_TIME_WINDOW, 0);
    }

    public AccountProfileInfo getAccountProfileInfo() {
        return new Builder().setFirstName(this.mSharedPreferences.getString(USER_FIRST_NAME, "")).setLastName(this.mSharedPreferences.getString(USER_LAST_NAME, "")).setEmail(this.mSharedPreferences.getString(USER_EMAIL, "")).setPhoneNumber(this.mSharedPreferences.getString(USER_PHONE_NUMBER, "")).setTimeZone(this.mSharedPreferences.getString(USER_TIME_ZONE, "")).setUserName(this.mSharedPreferences.getString(USER_NAME, "")).setCountryCode(this.mSharedPreferences.getString(USER_PHONE_CC, "")).setAlphaCountryCode(this.mSharedPreferences.getString(USER_ALPHA_CC, "")).setIsPhoneVerified(this.mSharedPreferences.getBoolean(USER_PHONE_IS_VERIFIED, false)).build();
    }

    public void clearSignUp() {
        putSignUpEmail(null);
        putSignUpCountryCode(null);
        putSignUpPhone(null);
        putVerificationId(null);
    }

    public Integer versionCompare(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int i = 0;
        while (i < split.length && i < split2.length && split[i].equals(split2[i])) {
            i++;
        }
        if (i >= split.length || i >= split2.length) {
            return Integer.valueOf(Integer.signum(split.length - split2.length));
        }
        return Integer.valueOf(Integer.signum(Integer.valueOf(split[i]).compareTo(Integer.valueOf(split2[i]))));
    }

    public void clear() {
        boolean appHasShownWalkThrough = getAppHasShownWalkThrough();
        int acceptedTermsOfServiceVersion = getAcceptedTermsOfServiceVersion();
        String signUpEmail = getSignUpEmail();
        String signUpCountryCode = getSignUpCountryCode();
        String signUpPhone = getSignUpPhone();
        String verificationId = getVerificationId();
        int skipSmsVerification = getSkipSmsVerification();
        this.mSharedPreferences.edit().clear().apply();
        putAppHasShownWalkThrough(appHasShownWalkThrough);
        putAcceptedTermsOfServiceVersion(acceptedTermsOfServiceVersion);
        putSignUpEmail(signUpEmail);
        putSignUpCountryCode(signUpCountryCode);
        putSignUpPhone(signUpPhone);
        putVerificationId(verificationId);
        putSkipSmsVerification(skipSmsVerification);
    }
}
