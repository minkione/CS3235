package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.squareup.otto.Bus;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class EventBus {
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Set<Class<?>>>() {
        public Set<Class<?>> load(Class<?> cls) {
            return TypeToken.m162of(cls).getTypes().rawTypes();
        }
    });
    private final ThreadLocal<Queue<EventWithHandler>> eventsToDispatch;
    private final HandlerFindingStrategy finder;
    private final SetMultimap<Class<?>, EventHandler> handlersByType;
    private final ReadWriteLock handlersByTypeLock;
    private final ThreadLocal<Boolean> isDispatching;
    private final Logger logger;

    static class EventWithHandler {
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object obj, EventHandler eventHandler) {
            this.event = Preconditions.checkNotNull(obj);
            this.handler = (EventHandler) Preconditions.checkNotNull(eventHandler);
        }
    }

    public EventBus() {
        this(Bus.DEFAULT_IDENTIFIER);
    }

    public EventBus(String str) {
        this.handlersByType = HashMultimap.create();
        this.handlersByTypeLock = new ReentrantReadWriteLock();
        this.finder = new AnnotatedHandlerFinder();
        this.eventsToDispatch = new ThreadLocal<Queue<EventWithHandler>>() {
            /* access modifiers changed from: protected */
            public Queue<EventWithHandler> initialValue() {
                return new LinkedList();
            }
        };
        this.isDispatching = new ThreadLocal<Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean initialValue() {
                return Boolean.valueOf(false);
            }
        };
        StringBuilder sb = new StringBuilder();
        sb.append(EventBus.class.getName());
        sb.append(".");
        sb.append((String) Preconditions.checkNotNull(str));
        this.logger = Logger.getLogger(sb.toString());
    }

    public void register(Object obj) {
        Multimap findAllHandlers = this.finder.findAllHandlers(obj);
        this.handlersByTypeLock.writeLock().lock();
        try {
            this.handlersByType.putAll(findAllHandlers);
        } finally {
            this.handlersByTypeLock.writeLock().unlock();
        }
    }

    public void unregister(Object obj) {
        for (Entry entry : this.finder.findAllHandlers(obj).asMap().entrySet()) {
            Class cls = (Class) entry.getKey();
            Collection collection = (Collection) entry.getValue();
            this.handlersByTypeLock.writeLock().lock();
            try {
                Set set = this.handlersByType.get(cls);
                if (set.containsAll(collection)) {
                    set.removeAll(collection);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("missing event handler for an annotated method. Is ");
                    sb.append(obj);
                    sb.append(" registered?");
                    throw new IllegalArgumentException(sb.toString());
                }
            } finally {
                this.handlersByTypeLock.writeLock().unlock();
            }
        }
    }

    public void post(Object obj) {
        boolean z = false;
        for (Class cls : flattenHierarchy(obj.getClass())) {
            this.handlersByTypeLock.readLock().lock();
            try {
                Set<EventHandler> set = this.handlersByType.get(cls);
                if (!set.isEmpty()) {
                    z = true;
                    for (EventHandler enqueueEvent : set) {
                        enqueueEvent(obj, enqueueEvent);
                    }
                }
            } finally {
                this.handlersByTypeLock.readLock().unlock();
            }
        }
        if (!z && !(obj instanceof DeadEvent)) {
            post(new DeadEvent(this, obj));
        }
        dispatchQueuedEvents();
    }

    /* access modifiers changed from: 0000 */
    public void enqueueEvent(Object obj, EventHandler eventHandler) {
        ((Queue) this.eventsToDispatch.get()).offer(new EventWithHandler(obj, eventHandler));
    }

    /* access modifiers changed from: 0000 */
    public void dispatchQueuedEvents() {
        if (!((Boolean) this.isDispatching.get()).booleanValue()) {
            this.isDispatching.set(Boolean.valueOf(true));
            try {
                Queue queue = (Queue) this.eventsToDispatch.get();
                while (true) {
                    EventWithHandler eventWithHandler = (EventWithHandler) queue.poll();
                    if (eventWithHandler != null) {
                        dispatch(eventWithHandler.event, eventWithHandler.handler);
                    } else {
                        return;
                    }
                }
            } finally {
                this.isDispatching.remove();
                this.eventsToDispatch.remove();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatch(Object obj, EventHandler eventHandler) {
        try {
            eventHandler.handleEvent(obj);
        } catch (InvocationTargetException e) {
            Logger logger2 = this.logger;
            Level level = Level.SEVERE;
            StringBuilder sb = new StringBuilder();
            sb.append("Could not dispatch event: ");
            sb.append(obj);
            sb.append(" to handler ");
            sb.append(eventHandler);
            logger2.log(level, sb.toString(), e);
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public Set<Class<?>> flattenHierarchy(Class<?> cls) {
        try {
            return (Set) flattenHierarchyCache.getUnchecked(cls);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
}
