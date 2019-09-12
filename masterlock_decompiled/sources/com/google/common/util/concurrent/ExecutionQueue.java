package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
final class ExecutionQueue {
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(ExecutionQueue.class.getName());
    /* access modifiers changed from: private */
    public final ReentrantLock lock = new ReentrantLock();
    private final ConcurrentLinkedQueue<RunnableExecutorPair> queuedListeners = Queues.newConcurrentLinkedQueue();

    private final class RunnableExecutorPair implements Runnable {
        private final Executor executor;
        @GuardedBy("lock")
        private boolean hasBeenExecuted = false;
        private final Runnable runnable;

        RunnableExecutorPair(Runnable runnable2, Executor executor2) {
            this.runnable = (Runnable) Preconditions.checkNotNull(runnable2);
            this.executor = (Executor) Preconditions.checkNotNull(executor2);
        }

        /* access modifiers changed from: private */
        public void submit() {
            ExecutionQueue.this.lock.lock();
            try {
                if (!this.hasBeenExecuted) {
                    this.executor.execute(this);
                }
            } catch (Exception e) {
                Logger access$200 = ExecutionQueue.logger;
                Level level = Level.SEVERE;
                StringBuilder sb = new StringBuilder();
                sb.append("Exception while executing listener ");
                sb.append(this.runnable);
                sb.append(" with executor ");
                sb.append(this.executor);
                access$200.log(level, sb.toString(), e);
            } catch (Throwable th) {
                if (ExecutionQueue.this.lock.isHeldByCurrentThread()) {
                    this.hasBeenExecuted = true;
                    ExecutionQueue.this.lock.unlock();
                }
                throw th;
            }
            if (ExecutionQueue.this.lock.isHeldByCurrentThread()) {
                this.hasBeenExecuted = true;
                ExecutionQueue.this.lock.unlock();
            }
        }

        public final void run() {
            if (ExecutionQueue.this.lock.isHeldByCurrentThread()) {
                this.hasBeenExecuted = true;
                ExecutionQueue.this.lock.unlock();
            }
            this.runnable.run();
        }
    }

    ExecutionQueue() {
    }

    /* access modifiers changed from: 0000 */
    public void add(Runnable runnable, Executor executor) {
        this.queuedListeners.add(new RunnableExecutorPair(runnable, executor));
    }

    /* access modifiers changed from: 0000 */
    public void execute() {
        Iterator it = this.queuedListeners.iterator();
        while (it.hasNext()) {
            ((RunnableExecutorPair) it.next()).submit();
            it.remove();
        }
    }
}
