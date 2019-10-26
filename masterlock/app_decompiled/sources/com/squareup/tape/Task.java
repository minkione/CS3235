package com.squareup.tape;

import java.io.Serializable;

public interface Task<T> extends Serializable {
    void execute(T t);
}
