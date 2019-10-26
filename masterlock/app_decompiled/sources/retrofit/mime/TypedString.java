package retrofit.mime;

import java.io.UnsupportedEncodingException;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public class TypedString extends TypedByteArray {
    public TypedString(String str) {
        super("text/plain; charset=UTF-8", convertToBytes(str));
    }

    private static byte[] convertToBytes(String str) {
        try {
            return str.getBytes(HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("TypedString[");
            sb.append(new String(getBytes(), HttpRequest.CHARSET_UTF8));
            sb.append("]");
            return sb.toString();
        } catch (UnsupportedEncodingException unused) {
            throw new AssertionError("Must be able to decode UTF-8");
        }
    }
}
