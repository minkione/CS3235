package dagger.internal;

import dagger.internal.Linker.ErrorHandler;
import java.util.List;

public final class ThrowingErrorHandler implements ErrorHandler {
    public void handleErrors(List<String> list) {
        if (!list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Errors creating object graph:");
            for (String str : list) {
                sb.append("\n  ");
                sb.append(str);
            }
            throw new IllegalStateException(sb.toString());
        }
    }
}
