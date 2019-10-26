package com.google.common.p005io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

@Beta
/* renamed from: com.google.common.io.ByteStreams */
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }

        public void write(int i) {
        }

        public void write(byte[] bArr) {
            Preconditions.checkNotNull(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            Preconditions.checkNotNull(bArr);
        }
    };

    /* renamed from: com.google.common.io.ByteStreams$ByteArrayDataInputStream */
    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(byte[] bArr) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr));
        }

        ByteArrayDataInputStream(byte[] bArr, int i) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr, i, bArr.length - i));
        }

        public void readFully(byte[] bArr) {
            try {
                this.input.readFully(bArr);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public void readFully(byte[] bArr, int i, int i2) {
            try {
                this.input.readFully(bArr, i, i2);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int skipBytes(int i) {
            try {
                return this.input.skipBytes(i);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (EOFException e) {
                throw new IllegalStateException(e);
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        }

        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public short readShort() {
            try {
                return this.input.readShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public char readChar() {
            try {
                return this.input.readChar();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int readInt() {
            try {
                return this.input.readInt();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public long readLong() {
            try {
                return this.input.readLong();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public String readLine() {
            try {
                return this.input.readLine();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /* renamed from: com.google.common.io.ByteStreams$ByteArrayDataOutputStream */
    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream() {
            this(new ByteArrayOutputStream());
        }

        ByteArrayDataOutputStream(int i) {
            this(new ByteArrayOutputStream(i));
        }

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputSteam = byteArrayOutputStream;
            this.output = new DataOutputStream(byteArrayOutputStream);
        }

        public void write(int i) {
            try {
                this.output.write(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void write(byte[] bArr) {
            try {
                this.output.write(bArr);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void write(byte[] bArr, int i, int i2) {
            try {
                this.output.write(bArr, i, i2);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeBoolean(boolean z) {
            try {
                this.output.writeBoolean(z);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeByte(int i) {
            try {
                this.output.writeByte(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeBytes(String str) {
            try {
                this.output.writeBytes(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeChar(int i) {
            try {
                this.output.writeChar(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeChars(String str) {
            try {
                this.output.writeChars(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeDouble(double d) {
            try {
                this.output.writeDouble(d);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeFloat(float f) {
            try {
                this.output.writeFloat(f);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeInt(int i) {
            try {
                this.output.writeInt(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeLong(long j) {
            try {
                this.output.writeLong(j);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeShort(int i) {
            try {
                this.output.writeShort(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeUTF(String str) {
            try {
                this.output.writeUTF(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    /* renamed from: com.google.common.io.ByteStreams$LimitedInputStream */
    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark = -1;

        LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            Preconditions.checkNotNull(inputStream);
            Preconditions.checkArgument(j >= 0, "limit must be non-negative");
            this.left = j;
        }

        public int available() throws IOException {
            return (int) Math.min((long) this.in.available(), this.left);
        }

        public synchronized void mark(int i) {
            this.in.mark(i);
            this.mark = this.left;
        }

        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int read = this.in.read();
            if (read != -1) {
                this.left--;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            long j = this.left;
            if (j == 0) {
                return -1;
            }
            int read = this.in.read(bArr, i, (int) Math.min((long) i2, j));
            if (read != -1) {
                this.left -= (long) read;
            }
            return read;
        }

        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            } else if (this.mark != -1) {
                this.in.reset();
                this.left = this.mark;
            } else {
                throw new IOException("Mark not set");
            }
        }

        public long skip(long j) throws IOException {
            long skip = this.in.skip(Math.min(j, this.left));
            this.left -= skip;
            return skip;
        }
    }

    private ByteStreams() {
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] bArr) {
        return asInputSupplier(ByteSource.wrap(bArr));
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] bArr, int i, int i2) {
        return asInputSupplier(ByteSource.wrap(bArr).slice((long) i, (long) i2));
    }

    @Deprecated
    public static ByteSource asByteSource(byte[] bArr) {
        return ByteSource.wrap(bArr);
    }

    public static void write(byte[] bArr, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        asByteSink(outputSupplier).write(bArr);
    }

    public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        return asByteSource(inputSupplier).copyTo(asByteSink(outputSupplier));
    }

    public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputStream outputStream) throws IOException {
        return asByteSource(inputSupplier).copyTo(outputStream);
    }

    public static long copy(InputStream inputStream, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        return asByteSink(outputSupplier).writeFrom(inputStream);
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static long copy(ReadableByteChannel readableByteChannel, WritableByteChannel writableByteChannel) throws IOException {
        Preconditions.checkNotNull(readableByteChannel);
        Preconditions.checkNotNull(writableByteChannel);
        ByteBuffer allocate = ByteBuffer.allocate(4096);
        long j = 0;
        while (readableByteChannel.read(allocate) != -1) {
            allocate.flip();
            while (allocate.hasRemaining()) {
                j += (long) writableByteChannel.write(allocate);
            }
            allocate.clear();
        }
        return j;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, (OutputStream) byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] toByteArray(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
        return asByteSource(inputSupplier).read();
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr) {
        return new ByteArrayDataInputStream(bArr);
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr, int i) {
        Preconditions.checkPositionIndex(i, bArr.length);
        return new ByteArrayDataInputStream(bArr, i);
    }

    public static ByteArrayDataOutput newDataOutput() {
        return new ByteArrayDataOutputStream();
    }

    public static ByteArrayDataOutput newDataOutput(int i) {
        Preconditions.checkArgument(i >= 0, "Invalid size: %s", Integer.valueOf(i));
        return new ByteArrayDataOutputStream(i);
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static InputStream limit(InputStream inputStream, long j) {
        return new LimitedInputStream(inputStream, j);
    }

    public static long length(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
        return asByteSource(inputSupplier).size();
    }

    public static boolean equal(InputSupplier<? extends InputStream> inputSupplier, InputSupplier<? extends InputStream> inputSupplier2) throws IOException {
        return asByteSource(inputSupplier).contentEquals(asByteSource(inputSupplier2));
    }

    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        int read = read(inputStream, bArr, i, i2);
        if (read != i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("reached end of stream after reading ");
            sb.append(read);
            sb.append(" bytes; ");
            sb.append(i2);
            sb.append(" bytes expected");
            throw new EOFException(sb.toString());
        }
    }

    public static void skipFully(InputStream inputStream, long j) throws IOException {
        long j2 = j;
        while (j2 > 0) {
            long skip = inputStream.skip(j2);
            if (skip != 0) {
                j2 -= skip;
            } else if (inputStream.read() != -1) {
                j2--;
            } else {
                long j3 = j - j2;
                StringBuilder sb = new StringBuilder();
                sb.append("reached end of stream after skipping ");
                sb.append(j3);
                sb.append(" bytes; ");
                sb.append(j);
                sb.append(" bytes expected");
                throw new EOFException(sb.toString());
            }
        }
    }

    public static <T> T readBytes(InputSupplier<? extends InputStream> inputSupplier, ByteProcessor<T> byteProcessor) throws IOException {
        Preconditions.checkNotNull(inputSupplier);
        Preconditions.checkNotNull(byteProcessor);
        Closer create = Closer.create();
        try {
            T readBytes = readBytes((InputStream) create.register((Closeable) inputSupplier.getInput()), byteProcessor);
            create.close();
            return readBytes;
        } catch (Throwable th) {
            create.close();
            throw th;
        }
    }

    public static <T> T readBytes(InputStream inputStream, ByteProcessor<T> byteProcessor) throws IOException {
        int read;
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(byteProcessor);
        byte[] bArr = new byte[4096];
        do {
            read = inputStream.read(bArr);
            if (read == -1) {
                break;
            }
        } while (byteProcessor.processBytes(bArr, 0, read));
        return byteProcessor.getResult();
    }

    public static HashCode hash(InputSupplier<? extends InputStream> inputSupplier, HashFunction hashFunction) throws IOException {
        return asByteSource(inputSupplier).hash(hashFunction);
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(bArr);
        if (i2 >= 0) {
            int i3 = 0;
            while (i3 < i2) {
                int read = inputStream.read(bArr, i + i3, i2 - i3);
                if (read == -1) {
                    break;
                }
                i3 += read;
            }
            return i3;
        }
        throw new IndexOutOfBoundsException("len is negative");
    }

    public static InputSupplier<InputStream> slice(InputSupplier<? extends InputStream> inputSupplier, long j, long j2) {
        return asInputSupplier(asByteSource(inputSupplier).slice(j, j2));
    }

    public static InputSupplier<InputStream> join(Iterable<? extends InputSupplier<? extends InputStream>> iterable) {
        Preconditions.checkNotNull(iterable);
        return asInputSupplier(ByteSource.concat(Iterables.transform(iterable, new Function<InputSupplier<? extends InputStream>, ByteSource>() {
            public ByteSource apply(InputSupplier<? extends InputStream> inputSupplier) {
                return ByteStreams.asByteSource(inputSupplier);
            }
        })));
    }

    public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>... inputSupplierArr) {
        return join((Iterable<? extends InputSupplier<? extends InputStream>>) Arrays.asList(inputSupplierArr));
    }

    public static ByteSource asByteSource(final InputSupplier<? extends InputStream> inputSupplier) {
        Preconditions.checkNotNull(inputSupplier);
        return new ByteSource() {
            public InputStream openStream() throws IOException {
                return (InputStream) inputSupplier.getInput();
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ByteStreams.asByteSource(");
                sb.append(inputSupplier);
                sb.append(")");
                return sb.toString();
            }
        };
    }

    public static ByteSink asByteSink(final OutputSupplier<? extends OutputStream> outputSupplier) {
        Preconditions.checkNotNull(outputSupplier);
        return new ByteSink() {
            public OutputStream openStream() throws IOException {
                return (OutputStream) outputSupplier.getOutput();
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ByteStreams.asByteSink(");
                sb.append(outputSupplier);
                sb.append(")");
                return sb.toString();
            }
        };
    }

    static <S extends InputStream> InputSupplier<S> asInputSupplier(ByteSource byteSource) {
        return (InputSupplier) Preconditions.checkNotNull(byteSource);
    }

    static <S extends OutputStream> OutputSupplier<S> asOutputSupplier(ByteSink byteSink) {
        return (OutputSupplier) Preconditions.checkNotNull(byteSink);
    }
}
