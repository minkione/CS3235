package p009rx.android.schedulers;

import android.os.Handler;
import p009rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.android.schedulers.HandlerScheduler */
public final class HandlerScheduler extends LooperScheduler {
    public /* bridge */ /* synthetic */ Worker createWorker() {
        return super.createWorker();
    }

    @Deprecated
    public static HandlerScheduler from(Handler handler) {
        if (handler != null) {
            return new HandlerScheduler(handler);
        }
        throw new NullPointerException("handler == null");
    }

    private HandlerScheduler(Handler handler) {
        super(handler);
    }
}
