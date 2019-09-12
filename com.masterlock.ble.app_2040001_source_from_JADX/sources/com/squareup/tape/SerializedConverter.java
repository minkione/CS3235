package com.squareup.tape;

import com.squareup.tape.FileObjectQueue.Converter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializedConverter<T extends Serializable> implements Converter<T> {
    private T deserialize(InputStream inputStream) throws IOException {
        try {
            return (Serializable) new ObjectInputStream(new BufferedInputStream(inputStream, 1024)).readUnshared();
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public T from(byte[] bArr) throws IOException {
        return deserialize(new ByteArrayInputStream(bArr));
    }

    public void toStream(T t, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeUnshared(t);
        objectOutputStream.close();
    }
}
