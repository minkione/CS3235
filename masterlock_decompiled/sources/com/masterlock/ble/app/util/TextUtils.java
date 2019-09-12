package com.masterlock.ble.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.core.Guest;

public class TextUtils {
    public static final String DEFAULT_COUNTRY_ISO = "US";

    private TextUtils() {
    }

    public static String convertPhoneToE164(String str) {
        TelephonyManager telephonyManager = (TelephonyManager) MasterLockApp.get().getSystemService("phone");
        String upperCase = "".equals(telephonyManager.getSimCountryIso()) ? DEFAULT_COUNTRY_ISO : telephonyManager.getSimCountryIso().toUpperCase();
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parse = instance.parse(str, upperCase);
            if (instance.isValidNumber(parse)) {
                return instance.format(parse, PhoneNumberFormat.E164);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertPhoneToE164(String str, String str2) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parse = instance.parse(str, str2);
            if (instance.isValidNumber(parse)) {
                return instance.format(parse, PhoneNumberFormat.E164);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNationalPhone(String str) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parseAndKeepRawInput = instance.parseAndKeepRawInput(str, "");
            AsYouTypeFormatter asYouTypeFormatter = instance.getAsYouTypeFormatter(instance.getRegionCodeForCountryCode(parseAndKeepRawInput.getCountryCode()));
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(parseAndKeepRawInput.getNationalNumber());
            for (char inputDigit : sb.toString().toCharArray()) {
                str = asYouTypeFormatter.inputDigit(inputDigit);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isValidPhoneNumber(String str, String str2) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parse = instance.parse(str, str2);
            StringBuilder sb = new StringBuilder();
            sb.append("isValid: ");
            sb.append(instance.isValidNumber(parse));
            sb.append(", countryCode: ");
            sb.append(str2);
            sb.append(", phone: ");
            sb.append(str);
            Log.d("TextUtils", sb.toString());
            return instance.isValidNumber(parse);
        } catch (NumberParseException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("isValidPhoneNumber: countryCode = ");
            sb2.append(str2);
            sb2.append(", Error ");
            sb2.append(e.getMessage());
            Log.d("TextUtils", sb2.toString());
            return false;
        }
    }

    public static boolean isValidEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public static String convertPhoneFromE164(String str, String str2) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parse = instance.parse(str, str2);
            if (instance.isValidNumber(parse)) {
                instance.format(parse, PhoneNumberFormat.NATIONAL);
                StringBuilder sb = new StringBuilder();
                sb.append("convertPhoneFromE164: ");
                sb.append(parse.getNationalNumber());
                Log.d("TextUtils", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(parse.getNationalNumber());
                return sb2.toString();
            }
        } catch (NumberParseException unused) {
        }
        return null;
    }

    public static String convertPhoneFromE164(String str) {
        TelephonyManager telephonyManager = (TelephonyManager) MasterLockApp.get().getSystemService("phone");
        String upperCase = "".equals(telephonyManager.getSimCountryIso()) ? DEFAULT_COUNTRY_ISO : telephonyManager.getSimCountryIso().toUpperCase();
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber parse = instance.parse(str, upperCase);
            if (instance.isValidNumber(parse)) {
                String format = instance.format(parse, PhoneNumberFormat.NATIONAL);
                StringBuilder sb = new StringBuilder();
                sb.append("convertPhoneFromE164: ");
                sb.append(format);
                Log.d("TextUtils", sb.toString());
                return format;
            }
        } catch (NumberParseException unused) {
        }
        return null;
    }

    public static String getNationalNumber(String str) {
        String str2 = "";
        try {
            PhoneNumber parse = PhoneNumberUtil.getInstance().parse(str, getUserCountryCode());
            parse.getCountryCode();
            long nationalNumber = parse.getNationalNumber();
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(nationalNumber);
            return sb.toString();
        } catch (NumberParseException e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static String getUserCountryCode() {
        TelephonyManager telephonyManager = (TelephonyManager) MasterLockApp.get().getSystemService("phone");
        return "".equals(telephonyManager.getSimCountryIso()) ? DEFAULT_COUNTRY_ISO : telephonyManager.getSimCountryIso().toUpperCase();
    }

    public static String getFullNameInternationalized(Context context, String str, String str2) {
        if (!android.text.TextUtils.isEmpty(str) && !android.text.TextUtils.isEmpty(str2)) {
            str = context.getResources().getString(C1075R.string.full_name, new Object[]{str, str2});
        } else if (android.text.TextUtils.isEmpty(str)) {
            str = str2;
        }
        return str != null ? str.trim() : str;
    }

    public static String getDisplayNameForGuest(Context context, Guest guest) {
        String fullNameInternationalized = getFullNameInternationalized(context, guest.getFirstName(), guest.getLastName());
        if (android.text.TextUtils.isEmpty(fullNameInternationalized)) {
            fullNameInternationalized = guest.getEmail();
        }
        if (android.text.TextUtils.isEmpty(fullNameInternationalized)) {
            fullNameInternationalized = convertPhoneFromE164(guest.getMobileNumberE164());
        }
        return android.text.TextUtils.isEmpty(fullNameInternationalized) ? guest.getMobileNumberE164() : fullNameInternationalized;
    }

    public static float convertDpToPixel(float f) {
        return (float) Math.round(f * (((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f));
    }
}
