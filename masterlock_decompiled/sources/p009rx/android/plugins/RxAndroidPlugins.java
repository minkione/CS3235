package p009rx.android.plugins;

import java.util.concurrent.atomic.AtomicReference;
import p009rx.annotations.Experimental;

/* renamed from: rx.android.plugins.RxAndroidPlugins */
public final class RxAndroidPlugins {
    private static final RxAndroidPlugins INSTANCE = new RxAndroidPlugins();
    private final AtomicReference<RxAndroidSchedulersHook> schedulersHook = new AtomicReference<>();

    public static RxAndroidPlugins getInstance() {
        return INSTANCE;
    }

    RxAndroidPlugins() {
    }

    @Experimental
    public void reset() {
        this.schedulersHook.set(null);
    }

    public RxAndroidSchedulersHook getSchedulersHook() {
        if (this.schedulersHook.get() == null) {
            this.schedulersHook.compareAndSet(null, RxAndroidSchedulersHook.getDefaultInstance());
        }
        return (RxAndroidSchedulersHook) this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxAndroidSchedulersHook rxAndroidSchedulersHook) {
        if (!this.schedulersHook.compareAndSet(null, rxAndroidSchedulersHook)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.schedulersHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }
}
