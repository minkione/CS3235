package p009rx.schedulers;

import p009rx.Scheduler;
import p009rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.NewThreadScheduler */
public final class NewThreadScheduler extends Scheduler {
    public Worker createWorker() {
        return null;
    }

    private NewThreadScheduler() {
        throw new IllegalStateException("No instances!");
    }
}
