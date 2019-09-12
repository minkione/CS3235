package com.google.common.p005io;

import java.io.IOException;

/* renamed from: com.google.common.io.OutputSupplier */
public interface OutputSupplier<T> {
    T getOutput() throws IOException;
}
