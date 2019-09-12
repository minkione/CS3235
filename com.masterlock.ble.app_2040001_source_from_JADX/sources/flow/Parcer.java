package flow;

import android.os.Parcelable;

public interface Parcer<T> {
    T unwrap(Parcelable parcelable);

    Parcelable wrap(T t);
}
