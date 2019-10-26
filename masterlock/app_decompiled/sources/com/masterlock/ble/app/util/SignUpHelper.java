package com.masterlock.ble.app.util;

import android.util.Log;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.dao.SignUpDAO;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;

public class SignUpHelper {
    private final int MAX_PASSWORD_LENGTH = 100;
    private final int MIN_PASSWORD_LENGTH = 8;
    private final int STRONG_PASSWORD_LENGTH = 10;
    private final String TAG = "SignUpHelper";
    /* access modifiers changed from: private */
    public List<String> failingStringList = new ArrayList();
    /* access modifiers changed from: private */
    public Map<PasswordCriteria, Boolean> passwordCriteriaMap = new HashMap();

    public enum PasswordCriteria {
        UPPERCASE_CHAR(C1075R.string.password_tip_uppercase),
        LOWERCASE_CHAR(C1075R.string.password_tip_lowercase),
        NUMBER(C1075R.string.password_tip_number),
        SPECIAL_CHAR(C1075R.string.password_tip_special_char),
        DICTIONARY_WORD(C1075R.string.password_tip_dictionary_words),
        CONSECUTIVE_REPEATED_CHAR(C1075R.string.password_tip_repeated_or_consecutive_chars),
        MIN_PASSWORD_LENGTH(C1075R.string.password_tip_min_length);
        
        int mStringResourceId;

        private PasswordCriteria(int i) {
            this.mStringResourceId = i;
        }

        public int getStringResourceId() {
            return this.mStringResourceId;
        }
    }

    public enum PasswordStrength {
        WEAK(C1075R.color.password_weak, C1075R.string.password_strength_weak, 1),
        OK(C1075R.color.password_ok, C1075R.string.password_strength_ok, 3),
        STRONG(C1075R.color.password_strong, C1075R.string.password_strength_strong, 4);
        
        int mColorResId;
        int mMessageResId;
        int mPasswordCriteriaRange;
        private Map<PasswordCriteria, Boolean> passwordCriteriaMap;

        private PasswordStrength(int i, int i2, int i3) {
            this.mColorResId = i;
            this.mMessageResId = i2;
            this.mPasswordCriteriaRange = i3;
        }

        public int getColorResId() {
            return this.mColorResId;
        }

        public int getMessageResId() {
            return this.mMessageResId;
        }
    }

    public boolean isPasswordInMaxRange(int i) {
        return i <= 100;
    }

    public boolean isPasswordInMinRange(int i) {
        return i >= 8;
    }

    public List<String> getFailingStringList() {
        return this.failingStringList;
    }

    public Map<PasswordCriteria, Boolean> getPasswordCriteriaMap() {
        return this.passwordCriteriaMap;
    }

    public boolean isPasswordInRange(int i) {
        boolean isPasswordInMinRange = isPasswordInMinRange(i);
        String str = !isPasswordInMinRange ? "Password did not meet the minimum required length" : null;
        boolean isPasswordInMaxRange = isPasswordInMaxRange(i);
        if (!isPasswordInMaxRange) {
            str = "Password did not meet the maximum required length";
        }
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("isPasswordInRange: ");
            sb.append(str);
            Log.d("SignUpHelper", sb.toString());
        }
        return isPasswordInMaxRange & isPasswordInMinRange;
    }

    private boolean containsUppercase(String str) {
        boolean isLetter;
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (Character.isUpperCase(c) && Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowercase(String str) {
        boolean isLetter;
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if ((!Character.isUpperCase(c)) && Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNumber(String str) {
        for (char isDigit : str.toCharArray()) {
            if (Character.isDigit(isDigit)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String str) {
        boolean isDigit;
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if ((!Character.isLetter(c)) && (Character.isDigit(c) ^ true)) {
                return true;
            }
        }
        return false;
    }

    private int validatePasswordCriteria(String str) {
        this.passwordCriteriaMap = createPasswordCriteriaMap();
        boolean containsUppercase = containsUppercase(str);
        int i = (containsUppercase ? 1 : 0) + false;
        this.passwordCriteriaMap.put(PasswordCriteria.UPPERCASE_CHAR, Boolean.valueOf(containsUppercase));
        boolean containsLowercase = containsLowercase(str);
        int i2 = i + (containsLowercase ? 1 : 0);
        this.passwordCriteriaMap.put(PasswordCriteria.LOWERCASE_CHAR, Boolean.valueOf(containsLowercase));
        boolean containsNumber = containsNumber(str);
        int i3 = i2 + (containsNumber ? 1 : 0);
        this.passwordCriteriaMap.put(PasswordCriteria.NUMBER, Boolean.valueOf(containsNumber));
        boolean containsSpecialCharacter = containsSpecialCharacter(str);
        int i4 = i3 + (containsSpecialCharacter ? 1 : 0);
        this.passwordCriteriaMap.put(PasswordCriteria.SPECIAL_CHAR, Boolean.valueOf(containsSpecialCharacter));
        this.passwordCriteriaMap.put(PasswordCriteria.MIN_PASSWORD_LENGTH, Boolean.valueOf(str.length() >= 10));
        StringBuilder sb = new StringBuilder();
        sb.append("validatePasswordCriteria: ");
        sb.append(i4);
        Log.d("SignUpHelper", sb.toString());
        return i4;
    }

    /* access modifiers changed from: private */
    public String containsConsecutiveOrRepeatedCharacters(String str) {
        char[] charArray = str.toCharArray();
        int i = 0;
        while (i < charArray.length - 2) {
            char c = charArray[i];
            int i2 = i + 1;
            char c2 = charArray[i2];
            char c3 = charArray[i + 2];
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("Current : ");
            sb.append(c);
            sb.append(" Next: ");
            sb.append(c2);
            sb.append(" third: ");
            sb.append(c3);
            printStream.println(sb.toString());
            if (c == c2 && c == c3 && c3 == c2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(c);
                sb2.append(c2);
                sb2.append("");
                return sb2.toString();
            } else if (((Character.isLetter(c) && Character.isLetter(c2)) || (Character.isDigit(c) && Character.isDigit(c2))) && c + 1 == c2 && c + 2 == c3) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("");
                sb3.append(c);
                sb3.append(c2);
                sb3.append("");
                return sb3.toString();
            } else {
                i = i2;
            }
        }
        return "";
    }

    public Observable<PasswordStrength> validatePassword(final String str, SignUpDAO signUpDAO) {
        this.passwordCriteriaMap.clear();
        this.failingStringList.clear();
        final int validatePasswordCriteria = validatePasswordCriteria(str);
        StringBuilder sb = new StringBuilder();
        sb.append("validatePassword: ");
        sb.append(str);
        Log.d("SignUpHelper", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("passwordCriteriaMap: ");
        sb2.append(this.passwordCriteriaMap.toString());
        Log.d("SignUpHelper", sb2.toString());
        return isPasswordWeak(str, validatePasswordCriteria, signUpDAO).flatMap(new Func1<Boolean, Observable<PasswordStrength>>() {
            public Observable<PasswordStrength> call(Boolean bool) {
                if (bool.booleanValue()) {
                    return Observable.just(PasswordStrength.WEAK);
                }
                return SignUpHelper.this.isPasswordOkay(validatePasswordCriteria).flatMap(new Func1<Boolean, Observable<PasswordStrength>>() {
                    public Observable<PasswordStrength> call(Boolean bool) {
                        if (bool.booleanValue()) {
                            return Observable.just(PasswordStrength.OK);
                        }
                        return SignUpHelper.this.isPasswordStrong(str, validatePasswordCriteria).flatMap(new Func1<Boolean, Observable<PasswordStrength>>() {
                            public Observable<PasswordStrength> call(Boolean bool) {
                                if (bool.booleanValue()) {
                                    return Observable.just(PasswordStrength.STRONG);
                                }
                                return Observable.just(PasswordStrength.OK);
                            }
                        });
                    }
                });
            }
        });
    }

    private Observable<Boolean> isPasswordWeak(final String str, final int i, final SignUpDAO signUpDAO) {
        StringBuilder sb = new StringBuilder();
        sb.append("Weak validation containsConsecutiveOrRepeatedCharacters: ");
        sb.append(containsConsecutiveOrRepeatedCharacters(str));
        Log.d("SignUpHelper", sb.toString());
        String str2 = "SignUpHelper";
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Weak validation passedPasswordCriteriaCount: ");
        sb2.append(i <= PasswordStrength.WEAK.mPasswordCriteriaRange);
        Log.d(str2, sb2.toString());
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                boolean z = true;
                boolean z2 = !SignUpHelper.this.containsConsecutiveOrRepeatedCharacters(str).isEmpty();
                boolean z3 = i <= PasswordStrength.WEAK.mPasswordCriteriaRange;
                SignUpHelper.this.passwordCriteriaMap.put(PasswordCriteria.CONSECUTIVE_REPEATED_CHAR, Boolean.valueOf(!z2));
                SignUpHelper.this.failingStringList = signUpDAO.getMatchingDictionaryWords(str);
                boolean z4 = !SignUpHelper.this.failingStringList.isEmpty();
                SignUpHelper.this.passwordCriteriaMap.put(PasswordCriteria.DICTIONARY_WORD, Boolean.valueOf(!z4));
                if (!z2 && !z3 && !z4) {
                    z = false;
                }
                subscriber.onNext(Boolean.valueOf(z));
                subscriber.onCompleted();
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<Boolean> isPasswordOkay(final int i) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(Boolean.valueOf(i <= PasswordStrength.OK.mPasswordCriteriaRange));
                subscriber.onCompleted();
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<Boolean> isPasswordStrong(final String str, final int i) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                boolean z = true;
                boolean z2 = i >= PasswordStrength.STRONG.mPasswordCriteriaRange;
                if (str.length() < 10) {
                    z = false;
                }
                subscriber.onNext(Boolean.valueOf(z2 & z));
                subscriber.onCompleted();
            }
        });
    }

    private Map<PasswordCriteria, Boolean> createPasswordCriteriaMap() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (PasswordCriteria put : PasswordCriteria.values()) {
            linkedHashMap.put(put, null);
        }
        return linkedHashMap;
    }
}
