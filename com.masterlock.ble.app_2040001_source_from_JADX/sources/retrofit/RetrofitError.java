package retrofit;

import java.io.IOException;
import java.lang.reflect.Type;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;

public class RetrofitError extends RuntimeException {
    private final Converter converter;
    private final Kind kind;
    private final Response response;
    private final Type successType;
    private final String url;

    public enum Kind {
        NETWORK,
        CONVERSION,
        HTTP,
        UNEXPECTED
    }

    public static RetrofitError networkError(String str, IOException iOException) {
        RetrofitError retrofitError = new RetrofitError(iOException.getMessage(), str, null, null, null, Kind.NETWORK, iOException);
        return retrofitError;
    }

    public static RetrofitError conversionError(String str, Response response2, Converter converter2, Type type, ConversionException conversionException) {
        RetrofitError retrofitError = new RetrofitError(conversionException.getMessage(), str, response2, converter2, type, Kind.CONVERSION, conversionException);
        return retrofitError;
    }

    public static RetrofitError httpError(String str, Response response2, Converter converter2, Type type) {
        StringBuilder sb = new StringBuilder();
        sb.append(response2.getStatus());
        sb.append(" ");
        sb.append(response2.getReason());
        RetrofitError retrofitError = new RetrofitError(sb.toString(), str, response2, converter2, type, Kind.HTTP, null);
        return retrofitError;
    }

    public static RetrofitError unexpectedError(String str, Throwable th) {
        RetrofitError retrofitError = new RetrofitError(th.getMessage(), str, null, null, null, Kind.UNEXPECTED, th);
        return retrofitError;
    }

    RetrofitError(String str, String str2, Response response2, Converter converter2, Type type, Kind kind2, Throwable th) {
        super(str, th);
        this.url = str2;
        this.response = response2;
        this.converter = converter2;
        this.successType = type;
        this.kind = kind2;
    }

    public String getUrl() {
        return this.url;
    }

    public Response getResponse() {
        return this.response;
    }

    @Deprecated
    public boolean isNetworkError() {
        return this.kind == Kind.NETWORK;
    }

    public Kind getKind() {
        return this.kind;
    }

    public Object getBody() {
        return getBodyAs(this.successType);
    }

    public Type getSuccessType() {
        return this.successType;
    }

    public Object getBodyAs(Type type) {
        Response response2 = this.response;
        if (response2 == null) {
            return null;
        }
        TypedInput body = response2.getBody();
        if (body == null) {
            return null;
        }
        try {
            return this.converter.fromBody(body, type);
        } catch (ConversionException e) {
            throw new RuntimeException(e);
        }
    }
}
