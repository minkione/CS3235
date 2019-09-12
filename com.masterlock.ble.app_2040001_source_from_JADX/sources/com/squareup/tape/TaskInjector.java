package com.squareup.tape;

import com.squareup.tape.Task;

public interface TaskInjector<T extends Task> {
    void injectMembers(T t);
}
