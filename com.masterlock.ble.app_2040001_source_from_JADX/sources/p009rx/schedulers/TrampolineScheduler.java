package p009rx.schedulers;

import p009rx.Scheduler;
import p009rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.TrampolineScheduler */
public final class TrampolineScheduler extends Scheduler {
    public Worker createWorker() {
        return null;
    }

    private TrampolineScheduler() {
        throw new IllegalStateException("No instances!");
    }
}
