package com.google.common.p005io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
/* renamed from: com.google.common.io.LineProcessor */
public interface LineProcessor<T> {
    T getResult();

    boolean processLine(String str) throws IOException;
}
