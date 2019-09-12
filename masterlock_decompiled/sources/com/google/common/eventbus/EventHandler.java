package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

class EventHandler {
    private final Method method;
    private final Object target;

    EventHandler(Object obj, Method method2) {
        Preconditions.checkNotNull(obj, "EventHandler target cannot be null.");
        Preconditions.checkNotNull(method2, "EventHandler method cannot be null.");
        this.target = obj;
        this.method = method2;
        method2.setAccessible(true);
    }

    public void handleEvent(Object obj) throws InvocationTargetException {
        Preconditions.checkNotNull(obj);
        try {
            this.method.invoke(this.target, new Object[]{obj});
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Method rejected target/argument: ");
            sb.append(obj);
            throw new Error(sb.toString(), e);
        } catch (IllegalAccessException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Method became inaccessible: ");
            sb2.append(obj);
            throw new Error(sb2.toString(), e2);
        } catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw ((Error) e3.getCause());
            }
            throw e3;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[wrapper ");
        sb.append(this.method);
        sb.append("]");
        return sb.toString();
    }

    public int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + System.identityHashCode(this.target);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof EventHandler)) {
            return false;
        }
        EventHandler eventHandler = (EventHandler) obj;
        if (this.target == eventHandler.target && this.method.equals(eventHandler.method)) {
            z = true;
        }
        return z;
    }
}
