package com.square.flow;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import flow.Parcer;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class GsonParcer<T> implements Parcer<T> {
    private final Gson gson;

    private static class Wrapper implements Parcelable {
        public static final Creator<Wrapper> CREATOR = new Creator<Wrapper>() {
            public Wrapper createFromParcel(Parcel parcel) {
                return new Wrapper(parcel.readString());
            }

            public Wrapper[] newArray(int i) {
                return new Wrapper[i];
            }
        };
        final String json;

        public int describeContents() {
            return 0;
        }

        Wrapper(String str) {
            this.json = str;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.json);
        }
    }

    public GsonParcer(Gson gson2) {
        this.gson = gson2;
    }

    public Parcelable wrap(T t) {
        try {
            return new Wrapper(encode(t));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T unwrap(Parcelable parcelable) {
        try {
            return decode(((Wrapper) parcelable).json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(T t) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            Class cls = t.getClass();
            jsonWriter.beginObject();
            jsonWriter.name(cls.getName());
            this.gson.toJson((Object) t, (Type) cls, jsonWriter);
            jsonWriter.endObject();
            return stringWriter.toString();
        } finally {
            jsonWriter.close();
        }
    }

    private T decode(String str) throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader(str));
        try {
            jsonReader.beginObject();
            T fromJson = this.gson.fromJson(jsonReader, (Type) Class.forName(jsonReader.nextName()));
            jsonReader.close();
            return fromJson;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            jsonReader.close();
            throw th;
        }
    }
}
