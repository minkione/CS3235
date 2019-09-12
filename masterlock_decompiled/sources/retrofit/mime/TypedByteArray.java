package retrofit.mime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class TypedByteArray implements TypedInput, TypedOutput {
    private final byte[] bytes;
    private final String mimeType;

    public String fileName() {
        return null;
    }

    public TypedByteArray(String str, byte[] bArr) {
        if (str == null) {
            str = "application/unknown";
        }
        if (bArr != null) {
            this.mimeType = str;
            this.bytes = bArr;
            return;
        }
        throw new NullPointerException("bytes");
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public long length() {
        return (long) this.bytes.length;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.bytes);
    }

    /* renamed from: in */
    public InputStream mo16704in() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TypedByteArray typedByteArray = (TypedByteArray) obj;
        return Arrays.equals(this.bytes, typedByteArray.bytes) && this.mimeType.equals(typedByteArray.mimeType);
    }

    public int hashCode() {
        return (this.mimeType.hashCode() * 31) + Arrays.hashCode(this.bytes);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TypedByteArray[length=");
        sb.append(length());
        sb.append("]");
        return sb.toString();
    }
}
