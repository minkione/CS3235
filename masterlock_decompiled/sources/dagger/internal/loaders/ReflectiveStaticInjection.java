package dagger.internal.loaders;

import dagger.internal.Binding;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javax.inject.Inject;

public final class ReflectiveStaticInjection extends StaticInjection {
    private Binding<?>[] bindings;
    private final Field[] fields;
    private final ClassLoader loader;

    private ReflectiveStaticInjection(ClassLoader classLoader, Field[] fieldArr) {
        this.fields = fieldArr;
        this.loader = classLoader;
    }

    public void attach(Linker linker) {
        this.bindings = new Binding[this.fields.length];
        int i = 0;
        while (true) {
            Field[] fieldArr = this.fields;
            if (i < fieldArr.length) {
                Field field = fieldArr[i];
                this.bindings[i] = linker.requestBinding(Keys.get(field.getGenericType(), field.getAnnotations(), field), field, this.loader);
                i++;
            } else {
                return;
            }
        }
    }

    public void inject() {
        int i = 0;
        while (i < this.fields.length) {
            try {
                this.fields[i].set(null, this.bindings[i].get());
                i++;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    public static StaticInjection create(Class<?> cls) {
        Field[] declaredFields;
        ArrayList arrayList = new ArrayList();
        for (Field field : cls.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                arrayList.add(field);
            }
        }
        if (!arrayList.isEmpty()) {
            return new ReflectiveStaticInjection(cls.getClassLoader(), (Field[]) arrayList.toArray(new Field[arrayList.size()]));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No static injections: ");
        sb.append(cls.getName());
        throw new IllegalArgumentException(sb.toString());
    }
}
