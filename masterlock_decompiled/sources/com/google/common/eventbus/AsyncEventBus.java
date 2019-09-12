package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus extends EventBus {
    private final ConcurrentLinkedQueue<EventWithHandler> eventsToDispatch = new ConcurrentLinkedQueue<>();
    private final Executor executor;

    public AsyncEventBus(String str, Executor executor2) {
        super(str);
        this.executor = (Executor) Preconditions.checkNotNull(executor2);
    }

    public AsyncEventBus(Executor executor2) {
        this.executor = (Executor) Preconditions.checkNotNull(executor2);
    }

    /* access modifiers changed from: 0000 */
    public void enqueueEvent(Object obj, EventHandler eventHandler) {
        this.eventsToDispatch.offer(new EventWithHandler(obj, eventHandler));
    }

    /* access modifiers changed from: protected */
    public void dispatchQueuedEvents() {
        while (true) {
            EventWithHandler eventWithHandler = (EventWithHandler) this.eventsToDispatch.poll();
            if (eventWithHandler != null) {
                dispatch(eventWithHandler.event, eventWithHandler.handler);
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatch(final Object obj, final EventHandler eventHandler) {
        Preconditions.checkNotNull(obj);
        Preconditions.checkNotNull(eventHandler);
        this.executor.execute(new Runnable() {
            public void run() {
                AsyncEventBus.super.dispatch(obj, eventHandler);
            }
        });
    }
}
