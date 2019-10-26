package retrofit;

import retrofit.client.Response;

final class ResponseWrapper {
    final Response response;
    final Object responseBody;

    ResponseWrapper(Response response2, Object obj) {
        this.response = response2;
        this.responseBody = obj;
    }
}
