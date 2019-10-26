package flow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public final class Layouts {
    public static View createView(Context context, Object obj) {
        return createView(context, obj.getClass());
    }

    public static View createView(Context context, Class<?> cls) {
        Layout layout = (Layout) cls.getAnnotation(Layout.class);
        if (layout != null) {
            return inflateLayout(context, layout.value());
        }
        throw new IllegalArgumentException(String.format("@%s annotation not found on class %s", new Object[]{Layout.class.getSimpleName(), cls.getName()}));
    }

    private static View inflateLayout(Context context, int i) {
        return LayoutInflater.from(context).inflate(i, null);
    }

    private Layouts() {
    }
}
