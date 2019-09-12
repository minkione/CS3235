package retrofit;

import android.os.Build.VERSION;
import android.os.Process;
import com.google.gson.Gson;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import retrofit.RestAdapter.Log;
import retrofit.android.AndroidApacheClient;
import retrofit.android.AndroidLog;
import retrofit.android.MainThreadExecutor;
import retrofit.appengine.UrlFetchClient;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.OkClient;
import retrofit.client.UrlConnectionClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

abstract class Platform {
    static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();
    private static final Platform PLATFORM = findPlatform();

    private static class Android extends Platform {
        private Android() {
        }

        /* access modifiers changed from: 0000 */
        public Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        /* access modifiers changed from: 0000 */
        public Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else if (VERSION.SDK_INT < 9) {
                client = new AndroidApacheClient();
            } else {
                client = new UrlConnectionClient();
            }
            return new Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable runnable) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Process.setThreadPriority(10);
                            runnable.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        /* access modifiers changed from: 0000 */
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        /* access modifiers changed from: 0000 */
        public Log defaultLog() {
            return new AndroidLog("Retrofit");
        }
    }

    private static class AppEngine extends Base {
        private AppEngine() {
            super();
        }

        /* access modifiers changed from: 0000 */
        public Provider defaultClient() {
            final UrlFetchClient urlFetchClient = new UrlFetchClient();
            return new Provider() {
                public Client get() {
                    return urlFetchClient;
                }
            };
        }
    }

    private static class Base extends Platform {
        private Base() {
        }

        /* access modifiers changed from: 0000 */
        public Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        /* access modifiers changed from: 0000 */
        public Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else {
                client = new UrlConnectionClient();
            }
            return new Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable runnable) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Thread.currentThread().setPriority(1);
                            runnable.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        /* access modifiers changed from: 0000 */
        public Executor defaultCallbackExecutor() {
            return new SynchronousExecutor();
        }

        /* access modifiers changed from: 0000 */
        public Log defaultLog() {
            return new Log() {
                public void log(String str) {
                    System.out.println(str);
                }
            };
        }
    }

    private static class OkClientInstantiator {
        private OkClientInstantiator() {
        }

        static Client instantiate() {
            return new OkClient();
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract Executor defaultCallbackExecutor();

    /* access modifiers changed from: 0000 */
    public abstract Provider defaultClient();

    /* access modifiers changed from: 0000 */
    public abstract Converter defaultConverter();

    /* access modifiers changed from: 0000 */
    public abstract Executor defaultHttpExecutor();

    /* access modifiers changed from: 0000 */
    public abstract Log defaultLog();

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException unused) {
        }
        if (System.getProperty("com.google.appengine.runtime.version") != null) {
            return new AppEngine();
        }
        return new Base();
    }

    /* access modifiers changed from: private */
    public static boolean hasOkHttpOnClasspath() {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    private static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
