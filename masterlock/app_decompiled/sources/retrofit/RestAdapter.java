package retrofit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import retrofit.Profiler.RequestInformation;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class RestAdapter {
    static final String IDLE_THREAD_NAME = "Retrofit-Idle";
    static final String THREAD_PREFIX = "Retrofit-";
    final Executor callbackExecutor;
    /* access modifiers changed from: private */
    public final Provider clientProvider;
    final Converter converter;
    final ErrorHandler errorHandler;
    final Executor httpExecutor;
    final Log log;
    volatile LogLevel logLevel;
    /* access modifiers changed from: private */
    public final Profiler profiler;
    final RequestInterceptor requestInterceptor;
    /* access modifiers changed from: private */
    public RxSupport rxSupport;
    final Endpoint server;
    private final Map<Class<?>, Map<Method, RestMethodInfo>> serviceMethodInfoCache;

    public static class Builder {
        private Executor callbackExecutor;
        private Provider clientProvider;
        private Converter converter;
        private Endpoint endpoint;
        private ErrorHandler errorHandler;
        private Executor httpExecutor;
        private Log log;
        private LogLevel logLevel = LogLevel.NONE;
        private Profiler profiler;
        private RequestInterceptor requestInterceptor;

        public Builder setEndpoint(String str) {
            if (str == null || str.trim().length() == 0) {
                throw new NullPointerException("Endpoint may not be blank.");
            }
            this.endpoint = Endpoints.newFixedEndpoint(str);
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint2) {
            if (endpoint2 != null) {
                this.endpoint = endpoint2;
                return this;
            }
            throw new NullPointerException("Endpoint may not be null.");
        }

        public Builder setClient(final Client client) {
            if (client != null) {
                return setClient((Provider) new Provider() {
                    public Client get() {
                        return client;
                    }
                });
            }
            throw new NullPointerException("Client may not be null.");
        }

        public Builder setClient(Provider provider) {
            if (provider != null) {
                this.clientProvider = provider;
                return this;
            }
            throw new NullPointerException("Client provider may not be null.");
        }

        public Builder setExecutors(Executor executor, Executor executor2) {
            if (executor != null) {
                if (executor2 == null) {
                    executor2 = new SynchronousExecutor();
                }
                this.httpExecutor = executor;
                this.callbackExecutor = executor2;
                return this;
            }
            throw new NullPointerException("HTTP executor may not be null.");
        }

        public Builder setRequestInterceptor(RequestInterceptor requestInterceptor2) {
            if (requestInterceptor2 != null) {
                this.requestInterceptor = requestInterceptor2;
                return this;
            }
            throw new NullPointerException("Request interceptor may not be null.");
        }

        public Builder setConverter(Converter converter2) {
            if (converter2 != null) {
                this.converter = converter2;
                return this;
            }
            throw new NullPointerException("Converter may not be null.");
        }

        public Builder setProfiler(Profiler profiler2) {
            if (profiler2 != null) {
                this.profiler = profiler2;
                return this;
            }
            throw new NullPointerException("Profiler may not be null.");
        }

        public Builder setErrorHandler(ErrorHandler errorHandler2) {
            if (errorHandler2 != null) {
                this.errorHandler = errorHandler2;
                return this;
            }
            throw new NullPointerException("Error handler may not be null.");
        }

        public Builder setLog(Log log2) {
            if (log2 != null) {
                this.log = log2;
                return this;
            }
            throw new NullPointerException("Log may not be null.");
        }

        public Builder setLogLevel(LogLevel logLevel2) {
            if (logLevel2 != null) {
                this.logLevel = logLevel2;
                return this;
            }
            throw new NullPointerException("Log level may not be null.");
        }

        public RestAdapter build() {
            if (this.endpoint != null) {
                ensureSaneDefaults();
                RestAdapter restAdapter = new RestAdapter(this.endpoint, this.clientProvider, this.httpExecutor, this.callbackExecutor, this.requestInterceptor, this.converter, this.profiler, this.errorHandler, this.log, this.logLevel);
                return restAdapter;
            }
            throw new IllegalArgumentException("Endpoint may not be null.");
        }

        private void ensureSaneDefaults() {
            if (this.converter == null) {
                this.converter = Platform.get().defaultConverter();
            }
            if (this.clientProvider == null) {
                this.clientProvider = Platform.get().defaultClient();
            }
            if (this.httpExecutor == null) {
                this.httpExecutor = Platform.get().defaultHttpExecutor();
            }
            if (this.callbackExecutor == null) {
                this.callbackExecutor = Platform.get().defaultCallbackExecutor();
            }
            if (this.errorHandler == null) {
                this.errorHandler = ErrorHandler.DEFAULT;
            }
            if (this.log == null) {
                this.log = Platform.get().defaultLog();
            }
            if (this.requestInterceptor == null) {
                this.requestInterceptor = RequestInterceptor.NONE;
            }
        }
    }

    public interface Log {
        public static final Log NONE = new Log() {
            public void log(String str) {
            }
        };

        void log(String str);
    }

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        HEADERS_AND_ARGS,
        FULL;

        public boolean log() {
            return this != NONE;
        }
    }

    private class RestHandler implements InvocationHandler {
        private final Map<Method, RestMethodInfo> methodDetailsCache;

        RestHandler(Map<Method, RestMethodInfo> map) {
            this.methodDetailsCache = map;
        }

        public Object invoke(Object obj, Method method, final Object[] objArr) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, objArr);
            }
            final RestMethodInfo methodInfo = RestAdapter.getMethodInfo(this.methodDetailsCache, method);
            if (methodInfo.isSynchronous) {
                try {
                    return invokeRequest(RestAdapter.this.requestInterceptor, methodInfo, objArr);
                } catch (RetrofitError e) {
                    Throwable handleError = RestAdapter.this.errorHandler.handleError(e);
                    if (handleError == null) {
                        throw new IllegalStateException("Error handler returned null for wrapped exception.", e);
                    }
                    throw handleError;
                }
            } else if (RestAdapter.this.httpExecutor == null || RestAdapter.this.callbackExecutor == null) {
                throw new IllegalStateException("Asynchronous invocation requires calling setExecutors.");
            } else if (methodInfo.isObservable) {
                if (RestAdapter.this.rxSupport == null) {
                    if (Platform.HAS_RX_JAVA) {
                        RestAdapter restAdapter = RestAdapter.this;
                        restAdapter.rxSupport = new RxSupport(restAdapter.httpExecutor, RestAdapter.this.errorHandler, RestAdapter.this.requestInterceptor);
                    } else {
                        throw new IllegalStateException("Observable method found but no RxJava on classpath.");
                    }
                }
                return RestAdapter.this.rxSupport.createRequestObservable(new Invoker() {
                    public ResponseWrapper invoke(RequestInterceptor requestInterceptor) {
                        return (ResponseWrapper) RestHandler.this.invokeRequest(requestInterceptor, methodInfo, objArr);
                    }
                });
            } else {
                final RequestInterceptorTape requestInterceptorTape = new RequestInterceptorTape();
                RestAdapter.this.requestInterceptor.intercept(requestInterceptorTape);
                Callback callback = (Callback) objArr[objArr.length - 1];
                Executor executor = RestAdapter.this.httpExecutor;
                final Object[] objArr2 = objArr;
                C18912 r0 = new CallbackRunnable(callback, RestAdapter.this.callbackExecutor, RestAdapter.this.errorHandler) {
                    public ResponseWrapper obtainResponse() {
                        return (ResponseWrapper) RestHandler.this.invokeRequest(requestInterceptorTape, methodInfo, objArr2);
                    }
                };
                executor.execute(r0);
                return null;
            }
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:102:0x01c0, code lost:
            r13 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:103:0x01c1, code lost:
            throw r13;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x0168, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x016d, code lost:
            if (r3.threwException() != false) goto L_0x016f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x0173, code lost:
            throw r3.getThrownException();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0180, code lost:
            throw retrofit.RetrofitError.conversionError(r2, retrofit.Utils.replaceResponseBody(r15, null), r12.this$0.converter, r13, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x018e, code lost:
            r13 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x0190, code lost:
            r13 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x01a0, code lost:
            r12.this$0.logException(r13, r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x01b6, code lost:
            r12.this$0.logException(r13, r2);
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:102:0x01c0 A[Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194, all -> 0x0192 }, ExcHandler: RetrofitError (r13v1 'e' retrofit.RetrofitError A[CUSTOM_DECLARE, Catch:{  }]), Splitter:B:1:0x0001] */
        /* JADX WARNING: Removed duplicated region for block: B:92:0x01a0 A[Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194, all -> 0x0192 }] */
        /* JADX WARNING: Removed duplicated region for block: B:99:0x01b6 A[Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194, all -> 0x0192 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object invokeRequest(retrofit.RequestInterceptor r13, retrofit.RestMethodInfo r14, java.lang.Object[] r15) {
            /*
                r12 = this;
                r0 = 0
                r14.init()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.RestAdapter r1 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.Endpoint r1 = r1.server     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                java.lang.String r1 = r1.getUrl()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.RequestBuilder r2 = new retrofit.RequestBuilder     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.RestAdapter r3 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.converter.Converter r3 = r3.converter     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                r2.<init>(r1, r14, r3)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                r2.setArguments(r15)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                r13.intercept(r2)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                retrofit.client.Request r13 = r2.build()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                java.lang.String r2 = r13.getUrl()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x01aa, Throwable -> 0x0194 }
                boolean r3 = r14.isSynchronous     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r3 != 0) goto L_0x0058
                java.lang.String r3 = "?"
                int r4 = r1.length()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                int r3 = r2.indexOf(r3, r4)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r4 = -1
                if (r3 != r4) goto L_0x0038
                int r3 = r2.length()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x0038:
                java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r5.<init>()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.String r6 = "Retrofit-"
                r5.append(r6)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                int r6 = r1.length()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.String r3 = r2.substring(r6, r3)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r5.append(r3)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.String r3 = r5.toString()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r4.setName(r3)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x0058:
                retrofit.RestAdapter r3 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter$LogLevel r3 = r3.logLevel     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                boolean r3 = r3.log()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r3 == 0) goto L_0x006a
                retrofit.RestAdapter r3 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.String r4 = "HTTP"
                retrofit.client.Request r13 = r3.logAndReplaceRequest(r4, r13, r15)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x006a:
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.Profiler r15 = r15.profiler     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r15 == 0) goto L_0x007e
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.Profiler r15 = r15.profiler     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.lang.Object r15 = r15.beforeCall()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r8 = r15
                goto L_0x007f
            L_0x007e:
                r8 = r0
            L_0x007f:
                long r3 = java.lang.System.nanoTime()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.client.Client$Provider r15 = r15.clientProvider     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.client.Client r15 = r15.get()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.client.Response r15 = r15.execute(r13)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                long r6 = java.lang.System.nanoTime()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                long r6 = r6 - r3
                long r9 = r5.toMillis(r6)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                int r11 = r15.getStatus()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r3 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.Profiler r3 = r3.profiler     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r3 == 0) goto L_0x00b7
                retrofit.Profiler$RequestInformation r4 = retrofit.RestAdapter.getRequestInfo(r1, r14, r13)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r13 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.Profiler r3 = r13.profiler     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r5 = r9
                r7 = r11
                r3.afterCall(r4, r5, r7, r8)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x00b7:
                retrofit.RestAdapter r13 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter$LogLevel r13 = r13.logLevel     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                boolean r13 = r13.log()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r13 == 0) goto L_0x00c7
                retrofit.RestAdapter r13 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.client.Response r15 = r13.logAndReplaceResponse(r2, r15, r9)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x00c7:
                java.lang.reflect.Type r13 = r14.responseObjectType     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r1 = 200(0xc8, float:2.8E-43)
                if (r11 < r1) goto L_0x0181
                r1 = 300(0x12c, float:4.2E-43)
                if (r11 >= r1) goto L_0x0181
                java.lang.Class<retrofit.client.Response> r1 = retrofit.client.Response.class
                boolean r1 = r13.equals(r1)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r1 == 0) goto L_0x0106
                boolean r13 = r14.isStreaming     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r13 != 0) goto L_0x00e1
                retrofit.client.Response r15 = retrofit.Utils.readBodyToBytesIfNecessary(r15)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x00e1:
                boolean r13 = r14.isSynchronous     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r13 == 0) goto L_0x00f3
                boolean r13 = r14.isSynchronous
                if (r13 != 0) goto L_0x00f2
                java.lang.Thread r13 = java.lang.Thread.currentThread()
                java.lang.String r14 = "Retrofit-Idle"
                r13.setName(r14)
            L_0x00f2:
                return r15
            L_0x00f3:
                retrofit.ResponseWrapper r13 = new retrofit.ResponseWrapper     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r13.<init>(r15, r15)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                boolean r14 = r14.isSynchronous
                if (r14 != 0) goto L_0x0105
                java.lang.Thread r14 = java.lang.Thread.currentThread()
                java.lang.String r15 = "Retrofit-Idle"
                r14.setName(r15)
            L_0x0105:
                return r13
            L_0x0106:
                retrofit.mime.TypedInput r1 = r15.getBody()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r1 != 0) goto L_0x0131
                boolean r13 = r14.isSynchronous     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r13 == 0) goto L_0x011e
                boolean r13 = r14.isSynchronous
                if (r13 != 0) goto L_0x011d
                java.lang.Thread r13 = java.lang.Thread.currentThread()
                java.lang.String r14 = "Retrofit-Idle"
                r13.setName(r14)
            L_0x011d:
                return r0
            L_0x011e:
                retrofit.ResponseWrapper r13 = new retrofit.ResponseWrapper     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r13.<init>(r15, r0)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                boolean r14 = r14.isSynchronous
                if (r14 != 0) goto L_0x0130
                java.lang.Thread r14 = java.lang.Thread.currentThread()
                java.lang.String r15 = "Retrofit-Idle"
                r14.setName(r15)
            L_0x0130:
                return r13
            L_0x0131:
                retrofit.ExceptionCatchingTypedInput r3 = new retrofit.ExceptionCatchingTypedInput     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                r3.<init>(r1)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r4 = retrofit.RestAdapter.this     // Catch:{ ConversionException -> 0x0168 }
                retrofit.converter.Converter r4 = r4.converter     // Catch:{ ConversionException -> 0x0168 }
                java.lang.Object r4 = r4.fromBody(r3, r13)     // Catch:{ ConversionException -> 0x0168 }
                retrofit.RestAdapter r5 = retrofit.RestAdapter.this     // Catch:{ ConversionException -> 0x0168 }
                r5.logResponseBody(r1, r4)     // Catch:{ ConversionException -> 0x0168 }
                boolean r1 = r14.isSynchronous     // Catch:{ ConversionException -> 0x0168 }
                if (r1 == 0) goto L_0x0155
                boolean r13 = r14.isSynchronous
                if (r13 != 0) goto L_0x0154
                java.lang.Thread r13 = java.lang.Thread.currentThread()
                java.lang.String r14 = "Retrofit-Idle"
                r13.setName(r14)
            L_0x0154:
                return r4
            L_0x0155:
                retrofit.ResponseWrapper r1 = new retrofit.ResponseWrapper     // Catch:{ ConversionException -> 0x0168 }
                r1.<init>(r15, r4)     // Catch:{ ConversionException -> 0x0168 }
                boolean r13 = r14.isSynchronous
                if (r13 != 0) goto L_0x0167
                java.lang.Thread r13 = java.lang.Thread.currentThread()
                java.lang.String r14 = "Retrofit-Idle"
                r13.setName(r14)
            L_0x0167:
                return r1
            L_0x0168:
                r1 = move-exception
                boolean r4 = r3.threwException()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                if (r4 == 0) goto L_0x0174
                java.io.IOException r13 = r3.getThrownException()     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                throw r13     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x0174:
                retrofit.client.Response r15 = retrofit.Utils.replaceResponseBody(r15, r0)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r0 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.converter.Converter r0 = r0.converter     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RetrofitError r13 = retrofit.RetrofitError.conversionError(r2, r15, r0, r13, r1)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                throw r13     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x0181:
                retrofit.client.Response r15 = retrofit.Utils.readBodyToBytesIfNecessary(r15)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RestAdapter r0 = retrofit.RestAdapter.this     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.converter.Converter r0 = r0.converter     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                retrofit.RetrofitError r13 = retrofit.RetrofitError.httpError(r2, r15, r0, r13)     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
                throw r13     // Catch:{ RetrofitError -> 0x01c0, IOException -> 0x0190, Throwable -> 0x018e }
            L_0x018e:
                r13 = move-exception
                goto L_0x0196
            L_0x0190:
                r13 = move-exception
                goto L_0x01ac
            L_0x0192:
                r13 = move-exception
                goto L_0x01c2
            L_0x0194:
                r13 = move-exception
                r2 = r0
            L_0x0196:
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ all -> 0x0192 }
                retrofit.RestAdapter$LogLevel r15 = r15.logLevel     // Catch:{ all -> 0x0192 }
                boolean r15 = r15.log()     // Catch:{ all -> 0x0192 }
                if (r15 == 0) goto L_0x01a5
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ all -> 0x0192 }
                r15.logException(r13, r2)     // Catch:{ all -> 0x0192 }
            L_0x01a5:
                retrofit.RetrofitError r13 = retrofit.RetrofitError.unexpectedError(r2, r13)     // Catch:{ all -> 0x0192 }
                throw r13     // Catch:{ all -> 0x0192 }
            L_0x01aa:
                r13 = move-exception
                r2 = r0
            L_0x01ac:
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ all -> 0x0192 }
                retrofit.RestAdapter$LogLevel r15 = r15.logLevel     // Catch:{ all -> 0x0192 }
                boolean r15 = r15.log()     // Catch:{ all -> 0x0192 }
                if (r15 == 0) goto L_0x01bb
                retrofit.RestAdapter r15 = retrofit.RestAdapter.this     // Catch:{ all -> 0x0192 }
                r15.logException(r13, r2)     // Catch:{ all -> 0x0192 }
            L_0x01bb:
                retrofit.RetrofitError r13 = retrofit.RetrofitError.networkError(r2, r13)     // Catch:{ all -> 0x0192 }
                throw r13     // Catch:{ all -> 0x0192 }
            L_0x01c0:
                r13 = move-exception
                throw r13     // Catch:{ all -> 0x0192 }
            L_0x01c2:
                boolean r14 = r14.isSynchronous
                if (r14 != 0) goto L_0x01cf
                java.lang.Thread r14 = java.lang.Thread.currentThread()
                java.lang.String r15 = "Retrofit-Idle"
                r14.setName(r15)
            L_0x01cf:
                throw r13
            */
            throw new UnsupportedOperationException("Method not decompiled: retrofit.RestAdapter.RestHandler.invokeRequest(retrofit.RequestInterceptor, retrofit.RestMethodInfo, java.lang.Object[]):java.lang.Object");
        }
    }

    private RestAdapter(Endpoint endpoint, Provider provider, Executor executor, Executor executor2, RequestInterceptor requestInterceptor2, Converter converter2, Profiler profiler2, ErrorHandler errorHandler2, Log log2, LogLevel logLevel2) {
        this.serviceMethodInfoCache = new LinkedHashMap();
        this.server = endpoint;
        this.clientProvider = provider;
        this.httpExecutor = executor;
        this.callbackExecutor = executor2;
        this.requestInterceptor = requestInterceptor2;
        this.converter = converter2;
        this.profiler = profiler2;
        this.errorHandler = errorHandler2;
        this.log = log2;
        this.logLevel = logLevel2;
    }

    public void setLogLevel(LogLevel logLevel2) {
        if (this.logLevel != null) {
            this.logLevel = logLevel2;
            return;
        }
        throw new NullPointerException("Log level may not be null.");
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public <T> T create(Class<T> cls) {
        Utils.validateServiceClass(cls);
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new RestHandler(getMethodInfoCache(cls)));
    }

    /* access modifiers changed from: 0000 */
    public Map<Method, RestMethodInfo> getMethodInfoCache(Class<?> cls) {
        Map<Method, RestMethodInfo> map;
        synchronized (this.serviceMethodInfoCache) {
            map = (Map) this.serviceMethodInfoCache.get(cls);
            if (map == null) {
                map = new LinkedHashMap<>();
                this.serviceMethodInfoCache.put(cls, map);
            }
        }
        return map;
    }

    static RestMethodInfo getMethodInfo(Map<Method, RestMethodInfo> map, Method method) {
        RestMethodInfo restMethodInfo;
        synchronized (map) {
            restMethodInfo = (RestMethodInfo) map.get(method);
            if (restMethodInfo == null) {
                restMethodInfo = new RestMethodInfo(method);
                map.put(method, restMethodInfo);
            }
        }
        return restMethodInfo;
    }

    /* access modifiers changed from: 0000 */
    public Request logAndReplaceRequest(String str, Request request, Object[] objArr) throws IOException {
        this.log.log(String.format("---> %s %s %s", new Object[]{str, request.getMethod(), request.getUrl()}));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : request.getHeaders()) {
                this.log.log(header.toString());
            }
            String str2 = "no";
            TypedOutput body = request.getBody();
            if (body != null) {
                String mimeType = body.mimeType();
                if (mimeType != null) {
                    Log log2 = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Content-Type: ");
                    sb.append(mimeType);
                    log2.log(sb.toString());
                }
                long length = body.length();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(length);
                sb2.append("-byte");
                str2 = sb2.toString();
                if (length != -1) {
                    Log log3 = this.log;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Content-Length: ");
                    sb3.append(length);
                    log3.log(sb3.toString());
                }
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!request.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        request = Utils.readBodyToBytesIfNecessary(request);
                        body = request.getBody();
                    }
                    this.log.log(new String(((TypedByteArray) body).getBytes(), MimeUtil.parseCharset(body.mimeType(), HttpRequest.CHARSET_UTF8)));
                } else if (this.logLevel.ordinal() >= LogLevel.HEADERS_AND_ARGS.ordinal()) {
                    if (!request.getHeaders().isEmpty()) {
                        this.log.log("---> REQUEST:");
                    }
                    for (int i = 0; i < objArr.length; i++) {
                        Log log4 = this.log;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("#");
                        sb4.append(i);
                        sb4.append(": ");
                        sb4.append(objArr[i]);
                        log4.log(sb4.toString());
                    }
                }
            }
            this.log.log(String.format("---> END %s (%s body)", new Object[]{str, str2}));
        }
        return request;
    }

    /* access modifiers changed from: private */
    public Response logAndReplaceResponse(String str, Response response, long j) throws IOException {
        this.log.log(String.format("<--- HTTP %s %s (%sms)", new Object[]{Integer.valueOf(response.getStatus()), str, Long.valueOf(j)}));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : response.getHeaders()) {
                this.log.log(header.toString());
            }
            long j2 = 0;
            TypedInput body = response.getBody();
            if (body != null) {
                j2 = body.length();
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!response.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        response = Utils.readBodyToBytesIfNecessary(response);
                        body = response.getBody();
                    }
                    byte[] bytes = ((TypedByteArray) body).getBytes();
                    long length = (long) bytes.length;
                    this.log.log(new String(bytes, MimeUtil.parseCharset(body.mimeType(), HttpRequest.CHARSET_UTF8)));
                    j2 = length;
                }
            }
            this.log.log(String.format("<--- END HTTP (%s-byte body)", new Object[]{Long.valueOf(j2)}));
        }
        return response;
    }

    /* access modifiers changed from: private */
    public void logResponseBody(TypedInput typedInput, Object obj) {
        if (this.logLevel.ordinal() == LogLevel.HEADERS_AND_ARGS.ordinal()) {
            this.log.log("<--- BODY:");
            this.log.log(obj.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void logException(Throwable th, String str) {
        Log log2 = this.log;
        String str2 = "---- ERROR %s";
        Object[] objArr = new Object[1];
        if (str == null) {
            str = "";
        }
        objArr[0] = str;
        log2.log(String.format(str2, objArr));
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        this.log.log(stringWriter.toString());
        this.log.log("---- END ERROR");
    }

    /* access modifiers changed from: private */
    public static RequestInformation getRequestInfo(String str, RestMethodInfo restMethodInfo, Request request) {
        String str2;
        long j;
        TypedOutput body = request.getBody();
        if (body != null) {
            long length = body.length();
            str2 = body.mimeType();
            j = length;
        } else {
            str2 = null;
            j = 0;
        }
        RequestInformation requestInformation = new RequestInformation(restMethodInfo.requestMethod, str, restMethodInfo.requestUrl, j, str2);
        return requestInformation;
    }
}
