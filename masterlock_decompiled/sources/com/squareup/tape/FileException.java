package com.squareup.tape;

import java.io.File;
import java.io.IOException;

public class FileException extends RuntimeException {
    private final File file;

    public FileException(String str, IOException iOException, File file2) {
        super(str, iOException);
        this.file = file2;
    }

    public File getFile() {
        return this.file;
    }
}
