package com.masterlock.ble.app.tape;

import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue.Converter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class GsonConverter<T> implements Converter<T> {
    private final Gson gson;
    private final Class<T> type;

    public GsonConverter(Gson gson2, Class<T> cls) {
        this.gson = gson2;
        this.type = cls;
    }

    public T from(byte[] bArr) {
        return this.gson.fromJson((Reader) new InputStreamReader(new ByteArrayInputStream(bArr)), this.type);
    }

    public void toStream(T t, OutputStream outputStream) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        this.gson.toJson((Object) t, (Appendable) outputStreamWriter);
        outputStreamWriter.close();
    }
}
