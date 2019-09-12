package p009rx.plugins;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/* renamed from: rx.plugins.RxJavaPlugins */
public class RxJavaPlugins {
    static final RxJavaErrorHandler DEFAULT_ERROR_HANDLER = new RxJavaErrorHandler() {
    };
    private static final RxJavaPlugins INSTANCE = new RxJavaPlugins();
    private final AtomicReference<RxJavaCompletableExecutionHook> completableExecutionHook = new AtomicReference<>();
    private final AtomicReference<RxJavaErrorHandler> errorHandler = new AtomicReference<>();
    private final AtomicReference<RxJavaObservableExecutionHook> observableExecutionHook = new AtomicReference<>();
    private final AtomicReference<RxJavaSchedulersHook> schedulersHook = new AtomicReference<>();
    private final AtomicReference<RxJavaSingleExecutionHook> singleExecutionHook = new AtomicReference<>();

    @Deprecated
    public static RxJavaPlugins getInstance() {
        return INSTANCE;
    }

    RxJavaPlugins() {
    }

    public void reset() {
        INSTANCE.errorHandler.set(null);
        INSTANCE.observableExecutionHook.set(null);
        INSTANCE.singleExecutionHook.set(null);
        INSTANCE.completableExecutionHook.set(null);
        INSTANCE.schedulersHook.set(null);
    }

    public RxJavaErrorHandler getErrorHandler() {
        if (this.errorHandler.get() == null) {
            Object pluginImplementationViaProperty = getPluginImplementationViaProperty(RxJavaErrorHandler.class, getSystemPropertiesSafe());
            if (pluginImplementationViaProperty == null) {
                this.errorHandler.compareAndSet(null, DEFAULT_ERROR_HANDLER);
            } else {
                this.errorHandler.compareAndSet(null, (RxJavaErrorHandler) pluginImplementationViaProperty);
            }
        }
        return (RxJavaErrorHandler) this.errorHandler.get();
    }

    public void registerErrorHandler(RxJavaErrorHandler rxJavaErrorHandler) {
        if (!this.errorHandler.compareAndSet(null, rxJavaErrorHandler)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.errorHandler.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    public RxJavaObservableExecutionHook getObservableExecutionHook() {
        if (this.observableExecutionHook.get() == null) {
            Object pluginImplementationViaProperty = getPluginImplementationViaProperty(RxJavaObservableExecutionHook.class, getSystemPropertiesSafe());
            if (pluginImplementationViaProperty == null) {
                this.observableExecutionHook.compareAndSet(null, RxJavaObservableExecutionHookDefault.getInstance());
            } else {
                this.observableExecutionHook.compareAndSet(null, (RxJavaObservableExecutionHook) pluginImplementationViaProperty);
            }
        }
        return (RxJavaObservableExecutionHook) this.observableExecutionHook.get();
    }

    public void registerObservableExecutionHook(RxJavaObservableExecutionHook rxJavaObservableExecutionHook) {
        if (!this.observableExecutionHook.compareAndSet(null, rxJavaObservableExecutionHook)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.observableExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    public RxJavaSingleExecutionHook getSingleExecutionHook() {
        if (this.singleExecutionHook.get() == null) {
            Object pluginImplementationViaProperty = getPluginImplementationViaProperty(RxJavaSingleExecutionHook.class, getSystemPropertiesSafe());
            if (pluginImplementationViaProperty == null) {
                this.singleExecutionHook.compareAndSet(null, RxJavaSingleExecutionHookDefault.getInstance());
            } else {
                this.singleExecutionHook.compareAndSet(null, (RxJavaSingleExecutionHook) pluginImplementationViaProperty);
            }
        }
        return (RxJavaSingleExecutionHook) this.singleExecutionHook.get();
    }

    public void registerSingleExecutionHook(RxJavaSingleExecutionHook rxJavaSingleExecutionHook) {
        if (!this.singleExecutionHook.compareAndSet(null, rxJavaSingleExecutionHook)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.singleExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    public RxJavaCompletableExecutionHook getCompletableExecutionHook() {
        if (this.completableExecutionHook.get() == null) {
            Object pluginImplementationViaProperty = getPluginImplementationViaProperty(RxJavaCompletableExecutionHook.class, getSystemPropertiesSafe());
            if (pluginImplementationViaProperty == null) {
                this.completableExecutionHook.compareAndSet(null, new RxJavaCompletableExecutionHook() {
                });
            } else {
                this.completableExecutionHook.compareAndSet(null, (RxJavaCompletableExecutionHook) pluginImplementationViaProperty);
            }
        }
        return (RxJavaCompletableExecutionHook) this.completableExecutionHook.get();
    }

    public void registerCompletableExecutionHook(RxJavaCompletableExecutionHook rxJavaCompletableExecutionHook) {
        if (!this.completableExecutionHook.compareAndSet(null, rxJavaCompletableExecutionHook)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.singleExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    static Properties getSystemPropertiesSafe() {
        try {
            return System.getProperties();
        } catch (SecurityException unused) {
            return new Properties();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0060, code lost:
        r3 = r7.substring(0, r7.length() - 6).substring(14);
        r5 = new java.lang.StringBuilder();
        r5.append(r1);
        r5.append(r3);
        r5.append(r4);
        r1 = r5.toString();
        r10 = r10.getProperty(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0087, code lost:
        if (r10 == null) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0089, code lost:
        r2 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r3 = new java.lang.StringBuilder();
        r3.append("Implementing class declaration for ");
        r3.append(r0);
        r3.append(" missing: ");
        r3.append(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00a9, code lost:
        throw new java.lang.IllegalStateException(r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00aa, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00ab, code lost:
        r2 = r10;
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00b3 A[SYNTHETIC, Splitter:B:24:0x00b3] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0134  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object getPluginImplementationViaProperty(java.lang.Class<?> r9, java.util.Properties r10) {
        /*
            java.lang.Object r10 = r10.clone()
            java.util.Properties r10 = (java.util.Properties) r10
            java.lang.String r0 = r9.getSimpleName()
            java.lang.String r1 = "rxjava.plugin."
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r0)
            java.lang.String r3 = ".implementation"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = r10.getProperty(r2)
            if (r2 != 0) goto L_0x00b1
            java.lang.String r3 = ".class"
            java.lang.String r4 = ".impl"
            java.util.Set r5 = r10.entrySet()     // Catch:{ SecurityException -> 0x00ad }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ SecurityException -> 0x00ad }
        L_0x0032:
            boolean r6 = r5.hasNext()     // Catch:{ SecurityException -> 0x00ad }
            if (r6 == 0) goto L_0x00b1
            java.lang.Object r6 = r5.next()     // Catch:{ SecurityException -> 0x00ad }
            java.util.Map$Entry r6 = (java.util.Map.Entry) r6     // Catch:{ SecurityException -> 0x00ad }
            java.lang.Object r7 = r6.getKey()     // Catch:{ SecurityException -> 0x00ad }
            java.lang.String r7 = r7.toString()     // Catch:{ SecurityException -> 0x00ad }
            boolean r8 = r7.startsWith(r1)     // Catch:{ SecurityException -> 0x00ad }
            if (r8 == 0) goto L_0x0032
            boolean r8 = r7.endsWith(r3)     // Catch:{ SecurityException -> 0x00ad }
            if (r8 == 0) goto L_0x0032
            java.lang.Object r6 = r6.getValue()     // Catch:{ SecurityException -> 0x00ad }
            java.lang.String r6 = r6.toString()     // Catch:{ SecurityException -> 0x00ad }
            boolean r6 = r0.equals(r6)     // Catch:{ SecurityException -> 0x00ad }
            if (r6 == 0) goto L_0x0032
            r3 = 0
            int r5 = r7.length()     // Catch:{ SecurityException -> 0x00ad }
            int r5 = r5 + -6
            java.lang.String r3 = r7.substring(r3, r5)     // Catch:{ SecurityException -> 0x00ad }
            r5 = 14
            java.lang.String r3 = r3.substring(r5)     // Catch:{ SecurityException -> 0x00ad }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00ad }
            r5.<init>()     // Catch:{ SecurityException -> 0x00ad }
            r5.append(r1)     // Catch:{ SecurityException -> 0x00ad }
            r5.append(r3)     // Catch:{ SecurityException -> 0x00ad }
            r5.append(r4)     // Catch:{ SecurityException -> 0x00ad }
            java.lang.String r1 = r5.toString()     // Catch:{ SecurityException -> 0x00ad }
            java.lang.String r10 = r10.getProperty(r1)     // Catch:{ SecurityException -> 0x00ad }
            if (r10 == 0) goto L_0x008b
            r2 = r10
            goto L_0x00b1
        L_0x008b:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ SecurityException -> 0x00aa }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00aa }
            r3.<init>()     // Catch:{ SecurityException -> 0x00aa }
            java.lang.String r4 = "Implementing class declaration for "
            r3.append(r4)     // Catch:{ SecurityException -> 0x00aa }
            r3.append(r0)     // Catch:{ SecurityException -> 0x00aa }
            java.lang.String r4 = " missing: "
            r3.append(r4)     // Catch:{ SecurityException -> 0x00aa }
            r3.append(r1)     // Catch:{ SecurityException -> 0x00aa }
            java.lang.String r1 = r3.toString()     // Catch:{ SecurityException -> 0x00aa }
            r2.<init>(r1)     // Catch:{ SecurityException -> 0x00aa }
            throw r2     // Catch:{ SecurityException -> 0x00aa }
        L_0x00aa:
            r1 = move-exception
            r2 = r10
            goto L_0x00ae
        L_0x00ad:
            r1 = move-exception
        L_0x00ae:
            r1.printStackTrace()
        L_0x00b1:
            if (r2 == 0) goto L_0x0134
            java.lang.Class r10 = java.lang.Class.forName(r2)     // Catch:{ ClassCastException -> 0x0111, ClassNotFoundException -> 0x00f6, InstantiationException -> 0x00db, IllegalAccessException -> 0x00c0 }
            java.lang.Class r9 = r10.asSubclass(r9)     // Catch:{ ClassCastException -> 0x0111, ClassNotFoundException -> 0x00f6, InstantiationException -> 0x00db, IllegalAccessException -> 0x00c0 }
            java.lang.Object r9 = r9.newInstance()     // Catch:{ ClassCastException -> 0x0111, ClassNotFoundException -> 0x00f6, InstantiationException -> 0x00db, IllegalAccessException -> 0x00c0 }
            return r9
        L_0x00c0:
            r9 = move-exception
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " implementation not able to be accessed: "
            r1.append(r0)
            r1.append(r2)
            java.lang.String r0 = r1.toString()
            r10.<init>(r0, r9)
            throw r10
        L_0x00db:
            r9 = move-exception
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " implementation not able to be instantiated: "
            r1.append(r0)
            r1.append(r2)
            java.lang.String r0 = r1.toString()
            r10.<init>(r0, r9)
            throw r10
        L_0x00f6:
            r9 = move-exception
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " implementation class not found: "
            r1.append(r0)
            r1.append(r2)
            java.lang.String r0 = r1.toString()
            r10.<init>(r0, r9)
            throw r10
        L_0x0111:
            r9 = move-exception
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = " implementation is not an instance of "
            r1.append(r3)
            r1.append(r0)
            java.lang.String r0 = ": "
            r1.append(r0)
            r1.append(r2)
            java.lang.String r0 = r1.toString()
            r10.<init>(r0, r9)
            throw r10
        L_0x0134:
            r9 = 0
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: p009rx.plugins.RxJavaPlugins.getPluginImplementationViaProperty(java.lang.Class, java.util.Properties):java.lang.Object");
    }

    public RxJavaSchedulersHook getSchedulersHook() {
        if (this.schedulersHook.get() == null) {
            Object pluginImplementationViaProperty = getPluginImplementationViaProperty(RxJavaSchedulersHook.class, getSystemPropertiesSafe());
            if (pluginImplementationViaProperty == null) {
                this.schedulersHook.compareAndSet(null, RxJavaSchedulersHook.getDefaultInstance());
            } else {
                this.schedulersHook.compareAndSet(null, (RxJavaSchedulersHook) pluginImplementationViaProperty);
            }
        }
        return (RxJavaSchedulersHook) this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxJavaSchedulersHook rxJavaSchedulersHook) {
        if (!this.schedulersHook.compareAndSet(null, rxJavaSchedulersHook)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.schedulersHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }
}
