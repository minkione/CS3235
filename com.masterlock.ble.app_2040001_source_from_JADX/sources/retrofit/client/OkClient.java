package retrofit.client;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okio.BufferedSink;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class OkClient implements Client {
    private final OkHttpClient client;

    private static OkHttpClient generateDefaultOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(20000, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    public OkClient() {
        this(generateDefaultOkHttp());
    }

    public OkClient(OkHttpClient okHttpClient) {
        if (okHttpClient != null) {
            this.client = okHttpClient;
            return;
        }
        throw new NullPointerException("client == null");
    }

    public Response execute(Request request) throws IOException {
        return parseResponse(this.client.newCall(createRequest(request)).execute());
    }

    static Request createRequest(Request request) {
        Builder method = new Builder().url(request.getUrl()).method(request.getMethod(), createRequestBody(request.getBody()));
        List headers = request.getHeaders();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            Header header = (Header) headers.get(i);
            String value = header.getValue();
            if (value == null) {
                value = "";
            }
            method.addHeader(header.getName(), value);
        }
        return method.build();
    }

    static Response parseResponse(Response response) {
        Response response2 = new Response(response.request().urlString(), response.code(), response.message(), createHeaders(response.headers()), createResponseBody(response.body()));
        return response2;
    }

    private static RequestBody createRequestBody(final TypedOutput typedOutput) {
        if (typedOutput == null) {
            return null;
        }
        final MediaType parse = MediaType.parse(typedOutput.mimeType());
        return new RequestBody() {
            public MediaType contentType() {
                return parse;
            }

            public void writeTo(BufferedSink bufferedSink) throws IOException {
                typedOutput.writeTo(bufferedSink.outputStream());
            }

            public long contentLength() {
                return typedOutput.length();
            }
        };
    }

    private static TypedInput createResponseBody(final ResponseBody responseBody) {
        if (responseBody.contentLength() == 0) {
            return null;
        }
        return new TypedInput() {
            public String mimeType() {
                MediaType contentType = responseBody.contentType();
                if (contentType == null) {
                    return null;
                }
                return contentType.toString();
            }

            public long length() {
                return responseBody.contentLength();
            }

            /* renamed from: in */
            public InputStream mo16704in() throws IOException {
                return responseBody.byteStream();
            }
        };
    }

    private static List<Header> createHeaders(Headers headers) {
        int size = headers.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new Header(headers.name(i), headers.value(i)));
        }
        return arrayList;
    }
}
