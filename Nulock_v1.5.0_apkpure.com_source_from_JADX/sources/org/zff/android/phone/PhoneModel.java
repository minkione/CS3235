package org.zff.android.phone;

import android.os.Build;
import android.support.p000v4.p002os.EnvironmentCompat;

public class PhoneModel {
    public static final String[] LOW_PERFORMANCE_PHONE_BRAND = {"coolpad", "lenovo", "meizu", "zte"};
    public static final String[] LOW_PERFORMANCE_PHONE_MODEL = {"ZTE-T U960s"};
    public static final String[] PHONE_BRAND = {"Coolpad", "HUAWEI", "Lenovo", "LG", "meizu", "ZTE"};
    private String mPhoneBrand;
    private String mPhoneModel;

    public PhoneModel() {
        this.mPhoneModel = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mPhoneBrand = EnvironmentCompat.MEDIA_UNKNOWN;
        this.mPhoneModel = Build.MODEL;
        this.mPhoneBrand = Build.BRAND;
    }

    public boolean isLowPerformanceBrand() {
        for (String equals : LOW_PERFORMANCE_PHONE_BRAND) {
            if (equals.equals(this.mPhoneBrand.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLowPerformanceModel() {
        for (String equals : LOW_PERFORMANCE_PHONE_MODEL) {
            if (equals.equals(this.mPhoneModel.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
