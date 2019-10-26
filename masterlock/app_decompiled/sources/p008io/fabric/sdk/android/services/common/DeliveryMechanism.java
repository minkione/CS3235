package p008io.fabric.sdk.android.services.common;

/* renamed from: io.fabric.sdk.android.services.common.DeliveryMechanism */
public enum DeliveryMechanism {
    DEVELOPER(1),
    USER_SIDELOAD(2),
    TEST_DISTRIBUTION(3),
    APP_STORE(4);
    
    public static final String BETA_APP_PACKAGE_NAME = "io.crash.air";

    /* renamed from: id */
    private final int f205id;

    private DeliveryMechanism(int i) {
        this.f205id = i;
    }

    public int getId() {
        return this.f205id;
    }

    public String toString() {
        return Integer.toString(this.f205id);
    }

    public static DeliveryMechanism determineFrom(String str) {
        if (BETA_APP_PACKAGE_NAME.equals(str)) {
            return TEST_DISTRIBUTION;
        }
        if (str != null) {
            return APP_STORE;
        }
        return DEVELOPER;
    }
}
