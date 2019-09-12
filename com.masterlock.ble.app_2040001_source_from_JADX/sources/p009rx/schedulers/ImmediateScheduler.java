package p009rx.schedulers;

import p009rx.Scheduler;
import p009rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.ImmediateScheduler */
public final class ImmediateScheduler extends Scheduler {
    public Worker createWorker() {
        return null;
    }

    private ImmediateScheduler() {
        throw new IllegalStateException("No instances!");
    }
}
