package retrofit.mime;

import java.io.IOException;
import java.io.InputStream;

public interface TypedInput {
    /* renamed from: in */
    InputStream mo16704in() throws IOException;

    long length();

    String mimeType();
}
