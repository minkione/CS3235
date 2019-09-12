package okio;

import android.support.p000v4.media.session.PlaybackStateCompat;
import com.google.common.primitives.UnsignedBytes;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer;
    /* access modifiers changed from: private */
    public boolean closed;
    public final Source source;

    public RealBufferedSource(Source source2, Buffer buffer2) {
        if (source2 != null) {
            this.buffer = buffer2;
            this.source = source2;
            return;
        }
        throw new IllegalArgumentException("source == null");
    }

    public RealBufferedSource(Source source2) {
        this(source2, new Buffer());
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public long read(Buffer buffer2, long j) throws IOException {
        if (buffer2 == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (this.buffer.size == 0 && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
            return -1;
        } else {
            return this.buffer.read(buffer2, Math.min(j, this.buffer.size));
        }
    }

    public boolean exhausted() throws IOException {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1;
        }
        throw new IllegalStateException("closed");
    }

    public void require(long j) throws IOException {
        if (!request(j)) {
            throw new EOFException();
        }
    }

    public boolean request(long j) throws IOException {
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (!this.closed) {
            while (this.buffer.size < j) {
                if (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public byte readByte() throws IOException {
        require(1);
        return this.buffer.readByte();
    }

    public ByteString readByteString() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    public ByteString readByteString(long j) throws IOException {
        require(j);
        return this.buffer.readByteString(j);
    }

    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    public byte[] readByteArray(long j) throws IOException {
        require(j);
        return this.buffer.readByteArray(j);
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public void readFully(byte[] bArr) throws IOException {
        try {
            require((long) bArr.length);
            this.buffer.readFully(bArr);
        } catch (EOFException e) {
            int i = 0;
            while (this.buffer.size > 0) {
                Buffer buffer2 = this.buffer;
                int read = buffer2.read(bArr, i, ((int) buffer2.size) - i);
                if (read != -1) {
                    i += read;
                } else {
                    throw new AssertionError();
                }
            }
            throw e;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        long j = (long) i2;
        Util.checkOffsetAndCount((long) bArr.length, (long) i, j);
        if (this.buffer.size == 0 && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
            return -1;
        }
        return this.buffer.read(bArr, i, (int) Math.min(j, this.buffer.size));
    }

    public void readFully(Buffer buffer2, long j) throws IOException {
        try {
            require(j);
            this.buffer.readFully(buffer2, j);
        } catch (EOFException e) {
            buffer2.writeAll(this.buffer);
            throw e;
        }
    }

    public long readAll(Sink sink) throws IOException {
        if (sink != null) {
            long j = 0;
            while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) != -1) {
                long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
                if (completeSegmentByteCount > 0) {
                    j += completeSegmentByteCount;
                    sink.write(this.buffer, completeSegmentByteCount);
                }
            }
            if (this.buffer.size() <= 0) {
                return j;
            }
            long size = j + this.buffer.size();
            Buffer buffer2 = this.buffer;
            sink.write(buffer2, buffer2.size());
            return size;
        }
        throw new IllegalArgumentException("sink == null");
    }

    public String readUtf8() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    public String readUtf8(long j) throws IOException {
        require(j);
        return this.buffer.readUtf8(j);
    }

    public String readString(Charset charset) throws IOException {
        if (charset != null) {
            this.buffer.writeAll(this.source);
            return this.buffer.readString(charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readString(long j, Charset charset) throws IOException {
        require(j);
        if (charset != null) {
            return this.buffer.readString(j, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readUtf8Line() throws IOException {
        long indexOf = indexOf(10);
        if (indexOf != -1) {
            return this.buffer.readUtf8Line(indexOf);
        }
        return this.buffer.size != 0 ? readUtf8(this.buffer.size) : null;
    }

    public String readUtf8LineStrict() throws IOException {
        long indexOf = indexOf(10);
        if (indexOf != -1) {
            return this.buffer.readUtf8Line(indexOf);
        }
        Buffer buffer2 = new Buffer();
        Buffer buffer3 = this.buffer;
        buffer3.copyTo(buffer2, 0, Math.min(32, buffer3.size()));
        StringBuilder sb = new StringBuilder();
        sb.append("\\n not found: size=");
        sb.append(this.buffer.size());
        sb.append(" content=");
        sb.append(buffer2.readByteString().hex());
        sb.append("...");
        throw new EOFException(sb.toString());
    }

    public short readShort() throws IOException {
        require(2);
        return this.buffer.readShort();
    }

    public short readShortLe() throws IOException {
        require(2);
        return this.buffer.readShortLe();
    }

    public int readInt() throws IOException {
        require(4);
        return this.buffer.readInt();
    }

    public int readIntLe() throws IOException {
        require(4);
        return this.buffer.readIntLe();
    }

    public long readLong() throws IOException {
        require(8);
        return this.buffer.readLong();
    }

    public long readLongLe() throws IOException {
        require(8);
        return this.buffer.readLongLe();
    }

    public long readDecimalLong() throws IOException {
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (request((long) i2)) {
                byte b = this.buffer.getByte((long) i);
                if ((b < 48 || b > 57) && (i != 0 || b != 45)) {
                    break;
                }
                i = i2;
            } else {
                break;
            }
        }
        if (i != 0) {
            return this.buffer.readDecimalLong();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected leading [0-9] or '-' character but was 0x");
        sb.append(Integer.toHexString(this.buffer.getByte(0)));
        throw new NumberFormatException(sb.toString());
    }

    public long readHexadecimalUnsignedLong() throws IOException {
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (request((long) i2)) {
                byte b = this.buffer.getByte((long) i);
                if ((b < 48 || b > 57) && ((b < 97 || b > 102) && (b < 65 || b > 70))) {
                    break;
                }
                i = i2;
            } else {
                break;
            }
        }
        if (i != 0) {
            return this.buffer.readHexadecimalUnsignedLong();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected leading [0-9a-fA-F] character but was 0x");
        sb.append(Integer.toHexString(this.buffer.getByte(0)));
        throw new NumberFormatException(sb.toString());
    }

    public void skip(long j) throws IOException {
        if (!this.closed) {
            while (j > 0) {
                if (this.buffer.size == 0 && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    throw new EOFException();
                }
                long min = Math.min(j, this.buffer.size());
                this.buffer.skip(min);
                j -= min;
            }
            return;
        }
        throw new IllegalStateException("closed");
    }

    public long indexOf(byte b) throws IOException {
        return indexOf(b, 0);
    }

    public long indexOf(byte b, long j) throws IOException {
        if (!this.closed) {
            while (j >= this.buffer.size) {
                if (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    return -1;
                }
            }
            do {
                long indexOf = this.buffer.indexOf(b, j);
                if (indexOf != -1) {
                    return indexOf;
                }
                j = this.buffer.size;
            } while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) != -1);
            return -1;
        }
        throw new IllegalStateException("closed");
    }

    public long indexOfElement(ByteString byteString) throws IOException {
        return indexOfElement(byteString, 0);
    }

    public long indexOfElement(ByteString byteString, long j) throws IOException {
        if (!this.closed) {
            while (j >= this.buffer.size) {
                if (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    return -1;
                }
            }
            do {
                long indexOfElement = this.buffer.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                j = this.buffer.size;
            } while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) != -1);
            return -1;
        }
        throw new IllegalStateException("closed");
    }

    public InputStream inputStream() {
        return new InputStream() {
            public int read() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                } else if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    return -1;
                } else {
                    return RealBufferedSource.this.buffer.readByte() & UnsignedBytes.MAX_VALUE;
                }
            }

            public int read(byte[] bArr, int i, int i2) throws IOException {
                if (!RealBufferedSource.this.closed) {
                    Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
                    if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                        return -1;
                    }
                    return RealBufferedSource.this.buffer.read(bArr, i, i2);
                }
                throw new IOException("closed");
            }

            public int available() throws IOException {
                if (!RealBufferedSource.this.closed) {
                    return (int) Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
                }
                throw new IOException("closed");
            }

            public void close() throws IOException {
                RealBufferedSource.this.close();
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(RealBufferedSource.this);
                sb.append(".inputStream()");
                return sb.toString();
            }
        };
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("buffer(");
        sb.append(this.source);
        sb.append(")");
        return sb.toString();
    }
}
