package p008io.fabric.sdk.android;

/* renamed from: io.fabric.sdk.android.Logger */
public interface Logger {
    /* renamed from: d */
    void mo21793d(String str, String str2);

    /* renamed from: d */
    void mo21794d(String str, String str2, Throwable th);

    /* renamed from: e */
    void mo21795e(String str, String str2);

    /* renamed from: e */
    void mo21796e(String str, String str2, Throwable th);

    int getLogLevel();

    /* renamed from: i */
    void mo21798i(String str, String str2);

    /* renamed from: i */
    void mo21799i(String str, String str2, Throwable th);

    boolean isLoggable(String str, int i);

    void log(int i, String str, String str2);

    void log(int i, String str, String str2, boolean z);

    void setLogLevel(int i);

    /* renamed from: v */
    void mo21804v(String str, String str2);

    /* renamed from: v */
    void mo21805v(String str, String str2, Throwable th);

    /* renamed from: w */
    void mo21806w(String str, String str2);

    /* renamed from: w */
    void mo21807w(String str, String str2, Throwable th);
}
