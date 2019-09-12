package com.square.flow.screenswitcher;

import android.content.Context;
import android.content.ContextWrapper;
import com.square.flow.appflow.ScreenContextFactory;
import com.square.flow.util.Preconditions;
import flow.Screen;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PathContext extends ContextWrapper {
    private static final Map<String, Context> EMPTY_CONTEXT_MAP = Collections.emptyMap();
    private static final List<Screen> EMPTY_PATH = Collections.emptyList();
    private static final String SERVICE_NAME = "PATH_CONTEXT";
    private final Map<String, Context> contexts;
    private final List<Screen> path;

    PathContext(Context context, List<Screen> list, Map<String, Context> map) {
        super(context);
        boolean z = true;
        Preconditions.checkArgument(context != null, "Leaf context may not be null.", new Object[0]);
        Preconditions.checkArgument(list.size() == map.size(), "Path and context map are not the same size", new Object[0]);
        if (!list.isEmpty()) {
            if (context != map.get(((Screen) list.get(list.size() - 1)).getName())) {
                z = false;
            }
            Preconditions.checkArgument(z, "Base context is not path's leaf context.", new Object[0]);
        }
        this.path = list;
        this.contexts = map;
    }

    public static PathContext empty(Context context) {
        return new PathContext(context, EMPTY_PATH, EMPTY_CONTEXT_MAP);
    }

    public static PathContext create(PathContext pathContext, Screen screen, ScreenContextFactory screenContextFactory) {
        List path2 = screen.getPath();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (!path2.isEmpty()) {
            Context baseContext = pathContext.getBaseContext();
            Iterator it = path2.iterator();
            Iterator it2 = pathContext.path.iterator();
            while (true) {
                if (!it.hasNext() || !it2.hasNext()) {
                    break;
                }
                Screen screen2 = (Screen) it.next();
                if (!((Screen) it2.next()).getName().equals(screen2.getName())) {
                    baseContext = screenContextFactory.createContext(screen2, baseContext);
                    linkedHashMap.put(screen2.getName(), baseContext);
                    break;
                }
                baseContext = (Context) pathContext.contexts.get(screen2.getName());
                linkedHashMap.put(screen2.getName(), baseContext);
            }
            while (it.hasNext()) {
                Screen screen3 = (Screen) it.next();
                baseContext = screenContextFactory.createContext(screen3, baseContext);
                linkedHashMap.put(screen3.getName(), baseContext);
            }
            return new PathContext(baseContext, path2, linkedHashMap);
        }
        throw new IllegalArgumentException("Screen has empty path");
    }

    public void destroyNotIn(PathContext pathContext, ScreenContextFactory screenContextFactory) {
        Iterator it = this.path.iterator();
        Iterator it2 = pathContext.path.iterator();
        while (it.hasNext() && it2.hasNext()) {
            String name = ((Screen) it.next()).getName();
            if (!name.equals(((Screen) it2.next()).getName())) {
                screenContextFactory.destroyContext((Context) this.contexts.get(name));
                return;
            }
        }
    }

    public static PathContext get(Context context) {
        return (PathContext) Preconditions.checkNotNull((PathContext) context.getSystemService(SERVICE_NAME), "Expected to find a PathContext but did not.", new Object[0]);
    }

    public Object getSystemService(String str) {
        if (SERVICE_NAME.equals(str)) {
            return this;
        }
        return super.getSystemService(str);
    }
}
