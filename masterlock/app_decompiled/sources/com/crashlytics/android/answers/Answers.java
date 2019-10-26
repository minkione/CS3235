package com.crashlytics.android.answers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import java.io.File;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.Crash.FatalException;
import p008io.fabric.sdk.android.services.common.Crash.LoggedException;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.settings.Settings;
import p008io.fabric.sdk.android.services.settings.SettingsData;

public class Answers extends Kit<Boolean> {
    static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    public static final String TAG = "Answers";
    SessionAnalyticsManager analyticsManager;

    public String getIdentifier() {
        return "com.crashlytics.sdk.android:answers";
    }

    public String getVersion() {
        return "1.3.6.97";
    }

    public static Answers getInstance() {
        return (Answers) Fabric.getKit(Answers.class);
    }

    public void logCustom(CustomEvent customEvent) {
        if (customEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onCustom(customEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logPurchase(PurchaseEvent purchaseEvent) {
        if (purchaseEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(purchaseEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logLogin(LoginEvent loginEvent) {
        if (loginEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(loginEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logShare(ShareEvent shareEvent) {
        if (shareEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(shareEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logInvite(InviteEvent inviteEvent) {
        if (inviteEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(inviteEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logSignUp(SignUpEvent signUpEvent) {
        if (signUpEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(signUpEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logLevelStart(LevelStartEvent levelStartEvent) {
        if (levelStartEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(levelStartEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logLevelEnd(LevelEndEvent levelEndEvent) {
        if (levelEndEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(levelEndEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logAddToCart(AddToCartEvent addToCartEvent) {
        if (addToCartEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(addToCartEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logStartCheckout(StartCheckoutEvent startCheckoutEvent) {
        if (startCheckoutEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(startCheckoutEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logRating(RatingEvent ratingEvent) {
        if (ratingEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(ratingEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logContentView(ContentViewEvent contentViewEvent) {
        if (contentViewEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(contentViewEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void logSearch(SearchEvent searchEvent) {
        if (searchEvent != null) {
            SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
            if (sessionAnalyticsManager != null) {
                sessionAnalyticsManager.onPredefined(searchEvent);
                return;
            }
            return;
        }
        throw new NullPointerException("event must not be null");
    }

    public void onException(LoggedException loggedException) {
        SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
        if (sessionAnalyticsManager != null) {
            sessionAnalyticsManager.onError(loggedException.getSessionId());
        }
    }

    public void onException(FatalException fatalException) {
        SessionAnalyticsManager sessionAnalyticsManager = this.analyticsManager;
        if (sessionAnalyticsManager != null) {
            sessionAnalyticsManager.onCrash(fatalException.getSessionId());
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean onPreExecute() {
        long j;
        try {
            Context context = getContext();
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String num = Integer.toString(packageInfo.versionCode);
            String str = packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName;
            if (VERSION.SDK_INT >= 9) {
                j = packageInfo.firstInstallTime;
            } else {
                j = new File(packageManager.getApplicationInfo(packageName, 0).sourceDir).lastModified();
            }
            this.analyticsManager = SessionAnalyticsManager.build(this, context, getIdManager(), num, str, j);
            this.analyticsManager.enable();
            return true;
        } catch (Exception e) {
            Fabric.getLogger().mo21796e(TAG, "Error retrieving app properties", e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground() {
        try {
            SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
            if (awaitSettingsData == null) {
                Fabric.getLogger().mo21795e(TAG, "Failed to retrieve settings");
                return Boolean.valueOf(false);
            } else if (awaitSettingsData.featuresData.collectAnalytics) {
                Fabric.getLogger().mo21793d(TAG, "Analytics collection enabled");
                this.analyticsManager.setAnalyticsSettingsData(awaitSettingsData.analyticsSettingsData, getOverridenSpiEndpoint());
                return Boolean.valueOf(true);
            } else {
                Fabric.getLogger().mo21793d(TAG, "Analytics collection disabled");
                this.analyticsManager.disable();
                return Boolean.valueOf(false);
            }
        } catch (Exception e) {
            Fabric.getLogger().mo21796e(TAG, "Error dealing with settings", e);
            return Boolean.valueOf(false);
        }
    }

    /* access modifiers changed from: 0000 */
    public String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }
}
