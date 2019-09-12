package p008io.fabric.sdk.android.services.common;

/* renamed from: io.fabric.sdk.android.services.common.Crash */
public abstract class Crash {
    private final String sessionId;

    /* renamed from: io.fabric.sdk.android.services.common.Crash$FatalException */
    public static class FatalException extends Crash {
        public FatalException(String str) {
            super(str);
        }
    }

    /* renamed from: io.fabric.sdk.android.services.common.Crash$LoggedException */
    public static class LoggedException extends Crash {
        public LoggedException(String str) {
            super(str);
        }
    }

    public Crash(String str) {
        this.sessionId = str;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}
