package retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p009rx.Observable;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.RestMethod;
import retrofit.http.Streaming;

final class RestMethodInfo {
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    String contentTypeHeader;
    List<Header> headers;
    final boolean isObservable;
    boolean isStreaming;
    final boolean isSynchronous;
    boolean loaded = false;
    final Method method;
    boolean requestHasBody;
    String requestMethod;
    Annotation[] requestParamAnnotations;
    String requestQuery;
    RequestType requestType = RequestType.SIMPLE;
    String requestUrl;
    Set<String> requestUrlParamNames;
    Type responseObjectType;
    final ResponseType responseType;

    enum RequestType {
        SIMPLE,
        MULTIPART,
        FORM_URL_ENCODED
    }

    private enum ResponseType {
        VOID,
        OBSERVABLE,
        OBJECT
    }

    private static final class RxSupport {
        private RxSupport() {
        }

        public static boolean isObservable(Class cls) {
            return cls == Observable.class;
        }

        public static Type getObservableType(Type type, Class cls) {
            return Types.getSupertype(type, cls, Observable.class);
        }
    }

    RestMethodInfo(Method method2) {
        boolean z = false;
        this.method = method2;
        this.responseType = parseResponseType();
        this.isSynchronous = this.responseType == ResponseType.OBJECT;
        if (this.responseType == ResponseType.OBSERVABLE) {
            z = true;
        }
        this.isObservable = z;
    }

    private RuntimeException methodError(String str, Object... objArr) {
        if (objArr.length > 0) {
            str = String.format(str, objArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.method.getDeclaringClass().getSimpleName());
        sb.append(".");
        sb.append(this.method.getName());
        sb.append(": ");
        sb.append(str);
        return new IllegalArgumentException(sb.toString());
    }

    private RuntimeException parameterError(int i, String str, Object... objArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" (parameter #");
        sb.append(i + 1);
        sb.append(")");
        return methodError(sb.toString(), objArr);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void init() {
        if (!this.loaded) {
            parseMethodAnnotations();
            parseParameters();
            this.loaded = true;
        }
    }

    private void parseMethodAnnotations() {
        Annotation[] annotations;
        for (Annotation annotation : this.method.getAnnotations()) {
            Class<Streaming> annotationType = annotation.annotationType();
            RestMethod restMethod = null;
            Annotation[] annotations2 = annotationType.getAnnotations();
            int length = annotations2.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Annotation annotation2 = annotations2[i];
                if (RestMethod.class == annotation2.annotationType()) {
                    restMethod = (RestMethod) annotation2;
                    break;
                }
                i++;
            }
            if (restMethod != null) {
                String str = this.requestMethod;
                if (str == null) {
                    try {
                        parsePath((String) annotationType.getMethod("value", new Class[0]).invoke(annotation, new Object[0]));
                        this.requestMethod = restMethod.value();
                        this.requestHasBody = restMethod.hasBody();
                    } catch (Exception unused) {
                        throw methodError("Failed to extract String 'value' from @%s annotation.", annotationType.getSimpleName());
                    }
                } else {
                    throw methodError("Only one HTTP method is allowed. Found: %s and %s.", str, restMethod.value());
                }
            } else if (annotationType == Headers.class) {
                String[] value = ((Headers) annotation).value();
                if (value.length != 0) {
                    this.headers = parseHeaders(value);
                } else {
                    throw methodError("@Headers annotation is empty.", new Object[0]);
                }
            } else if (annotationType == Multipart.class) {
                if (this.requestType == RequestType.SIMPLE) {
                    this.requestType = RequestType.MULTIPART;
                } else {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
            } else if (annotationType == FormUrlEncoded.class) {
                if (this.requestType == RequestType.SIMPLE) {
                    this.requestType = RequestType.FORM_URL_ENCODED;
                } else {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
            } else if (annotationType != Streaming.class) {
                continue;
            } else if (this.responseObjectType == Response.class) {
                this.isStreaming = true;
            } else {
                throw methodError("Only methods having %s as data type are allowed to have @%s annotation.", Response.class.getSimpleName(), Streaming.class.getSimpleName());
            }
        }
        if (this.requestMethod == null) {
            throw methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
        } else if (this.requestHasBody) {
        } else {
            if (this.requestType == RequestType.MULTIPART) {
                throw methodError("Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
            } else if (this.requestType == RequestType.FORM_URL_ENCODED) {
                throw methodError("FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
            }
        }
    }

    private void parsePath(String str) {
        String str2;
        String str3;
        if (str == null || str.length() == 0 || str.charAt(0) != '/') {
            throw methodError("URL path \"%s\" must start with '/'.", str);
        }
        int indexOf = str.indexOf(63);
        if (indexOf == -1 || indexOf >= str.length() - 1) {
            str2 = null;
            str3 = str;
        } else {
            str3 = str.substring(0, indexOf);
            str2 = str.substring(indexOf + 1);
            if (PARAM_URL_REGEX.matcher(str2).find()) {
                throw methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", str2);
            }
        }
        Set<String> parsePathParameters = parsePathParameters(str);
        this.requestUrl = str3;
        this.requestUrlParamNames = parsePathParameters;
        this.requestQuery = str2;
    }

    /* access modifiers changed from: 0000 */
    public List<Header> parseHeaders(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            int indexOf = str.indexOf(58);
            if (indexOf == -1 || indexOf == 0 || indexOf == str.length() - 1) {
                throw methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", str);
            }
            String substring = str.substring(0, indexOf);
            String trim = str.substring(indexOf + 1).trim();
            if ("Content-Type".equalsIgnoreCase(substring)) {
                this.contentTypeHeader = trim;
            } else {
                arrayList.add(new Header(substring, trim));
            }
        }
        return arrayList;
    }

    private ResponseType parseResponseType() {
        Type type;
        Type genericReturnType = this.method.getGenericReturnType();
        Type[] genericParameterTypes = this.method.getGenericParameterTypes();
        Class cls = null;
        boolean z = true;
        if (genericParameterTypes.length > 0) {
            type = genericParameterTypes[genericParameterTypes.length - 1];
            Type rawType = type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type;
            if (rawType instanceof Class) {
                cls = (Class) rawType;
            }
        } else {
            type = null;
        }
        boolean z2 = genericReturnType != Void.TYPE;
        if (cls == null || !Callback.class.isAssignableFrom(cls)) {
            z = false;
        }
        if (z2 && z) {
            throw methodError("Must have return type or Callback as last argument, not both.", new Object[0]);
        } else if (!z2 && !z) {
            throw methodError("Must have either a return type or Callback as last argument.", new Object[0]);
        } else if (z2) {
            if (Platform.HAS_RX_JAVA) {
                Class rawType2 = Types.getRawType(genericReturnType);
                if (RxSupport.isObservable(rawType2)) {
                    this.responseObjectType = getParameterUpperBound((ParameterizedType) RxSupport.getObservableType(genericReturnType, rawType2));
                    return ResponseType.OBSERVABLE;
                }
            }
            this.responseObjectType = genericReturnType;
            return ResponseType.OBJECT;
        } else {
            Type supertype = Types.getSupertype(type, Types.getRawType(type), Callback.class);
            if (supertype instanceof ParameterizedType) {
                this.responseObjectType = getParameterUpperBound((ParameterizedType) supertype);
                return ResponseType.VOID;
            }
            throw methodError("Last parameter must be of type Callback<X> or Callback<? super X>.", new Object[0]);
        }
    }

    private static Type getParameterUpperBound(ParameterizedType parameterizedType) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type type = actualTypeArguments[i];
            if (type instanceof WildcardType) {
                actualTypeArguments[i] = ((WildcardType) type).getUpperBounds()[0];
            }
        }
        return actualTypeArguments[0];
    }

    /* JADX WARNING: Removed duplicated region for block: B:119:0x012d A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x012a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseParameters() {
        /*
            r18 = this;
            r0 = r18
            java.lang.reflect.Method r1 = r0.method
            java.lang.Class[] r1 = r1.getParameterTypes()
            java.lang.reflect.Method r2 = r0.method
            java.lang.annotation.Annotation[][] r2 = r2.getParameterAnnotations()
            int r3 = r2.length
            boolean r4 = r0.isSynchronous
            if (r4 != 0) goto L_0x0019
            boolean r4 = r0.isObservable
            if (r4 != 0) goto L_0x0019
            int r3 = r3 + -1
        L_0x0019:
            java.lang.annotation.Annotation[] r4 = new java.lang.annotation.Annotation[r3]
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
        L_0x001f:
            if (r6 >= r3) goto L_0x0180
            r10 = r1[r6]
            r11 = r2[r6]
            if (r11 == 0) goto L_0x016a
            int r12 = r11.length
            r13 = r7
            r7 = 0
        L_0x002a:
            if (r7 >= r12) goto L_0x0166
            r14 = r11[r7]
            java.lang.Class r15 = r14.annotationType()
            java.lang.Class<retrofit.http.Path> r5 = retrofit.http.Path.class
            r16 = 1
            if (r15 != r5) goto L_0x0043
            r5 = r14
            retrofit.http.Path r5 = (retrofit.http.Path) r5
            java.lang.String r5 = r5.value()
            r0.validatePathName(r6, r5)
            goto L_0x008e
        L_0x0043:
            java.lang.Class<retrofit.http.EncodedPath> r5 = retrofit.http.EncodedPath.class
            if (r15 != r5) goto L_0x0052
            r5 = r14
            retrofit.http.EncodedPath r5 = (retrofit.http.EncodedPath) r5
            java.lang.String r5 = r5.value()
            r0.validatePathName(r6, r5)
            goto L_0x008e
        L_0x0052:
            java.lang.Class<retrofit.http.Query> r5 = retrofit.http.Query.class
            if (r15 != r5) goto L_0x0057
            goto L_0x008e
        L_0x0057:
            java.lang.Class<retrofit.http.EncodedQuery> r5 = retrofit.http.EncodedQuery.class
            if (r15 != r5) goto L_0x005c
            goto L_0x008e
        L_0x005c:
            java.lang.Class<retrofit.http.QueryMap> r5 = retrofit.http.QueryMap.class
            if (r15 != r5) goto L_0x0073
            java.lang.Class<java.util.Map> r5 = java.util.Map.class
            boolean r5 = r5.isAssignableFrom(r10)
            if (r5 == 0) goto L_0x0069
            goto L_0x008e
        L_0x0069:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@QueryMap parameter type must be Map."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x0073:
            java.lang.Class<retrofit.http.EncodedQueryMap> r5 = retrofit.http.EncodedQueryMap.class
            if (r15 != r5) goto L_0x008a
            java.lang.Class<java.util.Map> r5 = java.util.Map.class
            boolean r5 = r5.isAssignableFrom(r10)
            if (r5 == 0) goto L_0x0080
            goto L_0x008e
        L_0x0080:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@EncodedQueryMap parameter type must be Map."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x008a:
            java.lang.Class<retrofit.http.Header> r5 = retrofit.http.Header.class
            if (r15 != r5) goto L_0x0092
        L_0x008e:
            r17 = r1
            goto L_0x0126
        L_0x0092:
            java.lang.Class<retrofit.http.Field> r5 = retrofit.http.Field.class
            if (r15 != r5) goto L_0x00ab
            retrofit.RestMethodInfo$RequestType r5 = r0.requestType
            retrofit.RestMethodInfo$RequestType r8 = retrofit.RestMethodInfo.RequestType.FORM_URL_ENCODED
            if (r5 != r8) goto L_0x00a1
            r17 = r1
            r8 = 1
            goto L_0x0126
        L_0x00a1:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@Field parameters can only be used with form encoding."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x00ab:
            java.lang.Class<retrofit.http.FieldMap> r5 = retrofit.http.FieldMap.class
            if (r15 != r5) goto L_0x00d5
            retrofit.RestMethodInfo$RequestType r5 = r0.requestType
            retrofit.RestMethodInfo$RequestType r8 = retrofit.RestMethodInfo.RequestType.FORM_URL_ENCODED
            if (r5 != r8) goto L_0x00cb
            java.lang.Class<java.util.Map> r5 = java.util.Map.class
            boolean r5 = r5.isAssignableFrom(r10)
            if (r5 == 0) goto L_0x00c1
            r17 = r1
            r8 = 1
            goto L_0x0126
        L_0x00c1:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@FieldMap parameter type must be Map."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x00cb:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@FieldMap parameters can only be used with form encoding."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x00d5:
            java.lang.Class<retrofit.http.Part> r5 = retrofit.http.Part.class
            if (r15 != r5) goto L_0x00ed
            retrofit.RestMethodInfo$RequestType r5 = r0.requestType
            retrofit.RestMethodInfo$RequestType r9 = retrofit.RestMethodInfo.RequestType.MULTIPART
            if (r5 != r9) goto L_0x00e3
            r17 = r1
            r9 = 1
            goto L_0x0126
        L_0x00e3:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@Part parameters can only be used with multipart encoding."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x00ed:
            java.lang.Class<retrofit.http.PartMap> r5 = retrofit.http.PartMap.class
            if (r15 != r5) goto L_0x0117
            retrofit.RestMethodInfo$RequestType r5 = r0.requestType
            retrofit.RestMethodInfo$RequestType r9 = retrofit.RestMethodInfo.RequestType.MULTIPART
            if (r5 != r9) goto L_0x010d
            java.lang.Class<java.util.Map> r5 = java.util.Map.class
            boolean r5 = r5.isAssignableFrom(r10)
            if (r5 == 0) goto L_0x0103
            r17 = r1
            r9 = 1
            goto L_0x0126
        L_0x0103:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@PartMap parameter type must be Map."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x010d:
            r5 = 0
            java.lang.Object[] r1 = new java.lang.Object[r5]
            java.lang.String r2 = "@PartMap parameters can only be used with multipart encoding."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x0117:
            java.lang.Class<retrofit.http.Body> r5 = retrofit.http.Body.class
            if (r15 != r5) goto L_0x015e
            retrofit.RestMethodInfo$RequestType r5 = r0.requestType
            r17 = r1
            retrofit.RestMethodInfo$RequestType r1 = retrofit.RestMethodInfo.RequestType.SIMPLE
            if (r5 != r1) goto L_0x0154
            if (r13 != 0) goto L_0x014a
            r13 = 1
        L_0x0126:
            r1 = r4[r6]
            if (r1 != 0) goto L_0x012d
            r4[r6] = r14
            goto L_0x0160
        L_0x012d:
            r1 = 2
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = r4[r6]
            java.lang.Class r2 = r2.annotationType()
            java.lang.String r2 = r2.getSimpleName()
            r3 = 0
            r1[r3] = r2
            java.lang.String r2 = r15.getSimpleName()
            r1[r16] = r2
            java.lang.String r2 = "Multiple Retrofit annotations found, only one allowed: @%s, @%s."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x014a:
            r3 = 0
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.String r2 = "Multiple @Body method annotations found."
            java.lang.RuntimeException r1 = r0.methodError(r2, r1)
            throw r1
        L_0x0154:
            r3 = 0
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.String r2 = "@Body parameters cannot be used with form or multi-part encoding."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x015e:
            r17 = r1
        L_0x0160:
            int r7 = r7 + 1
            r1 = r17
            goto L_0x002a
        L_0x0166:
            r17 = r1
            r7 = r13
            goto L_0x016c
        L_0x016a:
            r17 = r1
        L_0x016c:
            r1 = r4[r6]
            if (r1 == 0) goto L_0x0176
            int r6 = r6 + 1
            r1 = r17
            goto L_0x001f
        L_0x0176:
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "No Retrofit annotation found."
            java.lang.RuntimeException r1 = r0.parameterError(r6, r2, r1)
            throw r1
        L_0x0180:
            r1 = 0
            retrofit.RestMethodInfo$RequestType r2 = r0.requestType
            retrofit.RestMethodInfo$RequestType r3 = retrofit.RestMethodInfo.RequestType.SIMPLE
            if (r2 != r3) goto L_0x0197
            boolean r2 = r0.requestHasBody
            if (r2 != 0) goto L_0x0197
            if (r7 != 0) goto L_0x018e
            goto L_0x0197
        L_0x018e:
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "Non-body HTTP method cannot contain @Body or @TypedOutput."
            java.lang.RuntimeException r1 = r0.methodError(r2, r1)
            throw r1
        L_0x0197:
            retrofit.RestMethodInfo$RequestType r2 = r0.requestType
            retrofit.RestMethodInfo$RequestType r3 = retrofit.RestMethodInfo.RequestType.FORM_URL_ENCODED
            if (r2 != r3) goto L_0x01a9
            if (r8 == 0) goto L_0x01a0
            goto L_0x01a9
        L_0x01a0:
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "Form-encoded method must contain at least one @Field."
            java.lang.RuntimeException r1 = r0.methodError(r2, r1)
            throw r1
        L_0x01a9:
            retrofit.RestMethodInfo$RequestType r2 = r0.requestType
            retrofit.RestMethodInfo$RequestType r3 = retrofit.RestMethodInfo.RequestType.MULTIPART
            if (r2 != r3) goto L_0x01bb
            if (r9 == 0) goto L_0x01b2
            goto L_0x01bb
        L_0x01b2:
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "Multipart method must contain at least one @Part."
            java.lang.RuntimeException r1 = r0.methodError(r2, r1)
            throw r1
        L_0x01bb:
            r0.requestParamAnnotations = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: retrofit.RestMethodInfo.parseParameters():void");
    }

    private void validatePathName(int i, String str) {
        if (!PARAM_NAME_REGEX.matcher(str).matches()) {
            throw parameterError(i, "@Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), str);
        } else if (!this.requestUrlParamNames.contains(str)) {
            throw parameterError(i, "URL \"%s\" does not contain \"{%s}\".", this.requestUrl, str);
        }
    }

    static Set<String> parsePathParameters(String str) {
        Matcher matcher = PARAM_URL_REGEX.matcher(str);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (matcher.find()) {
            linkedHashSet.add(matcher.group(1));
        }
        return linkedHashSet;
    }
}
