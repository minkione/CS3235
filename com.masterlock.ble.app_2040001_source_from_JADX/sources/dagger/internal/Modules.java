package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Modules {
    private Modules() {
    }

    public static Map<ModuleAdapter<?>, Object> loadModules(Loader loader, Object[] objArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(objArr.length);
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] instanceof Class) {
                ModuleAdapter moduleAdapter = loader.getModuleAdapter(objArr[i]);
                linkedHashMap.put(moduleAdapter, moduleAdapter.newModule());
            } else {
                linkedHashMap.put(loader.getModuleAdapter(objArr[i].getClass()), objArr[i]);
            }
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(linkedHashMap);
        LinkedHashMap linkedHashMap3 = new LinkedHashMap();
        for (ModuleAdapter collectIncludedModulesRecursively : linkedHashMap.keySet()) {
            collectIncludedModulesRecursively(loader, collectIncludedModulesRecursively, linkedHashMap3);
        }
        for (ModuleAdapter moduleAdapter2 : linkedHashMap3.values()) {
            if (!linkedHashMap2.containsKey(moduleAdapter2)) {
                linkedHashMap2.put(moduleAdapter2, moduleAdapter2.newModule());
            }
        }
        return linkedHashMap2;
    }

    private static void collectIncludedModulesRecursively(Loader loader, ModuleAdapter<?> moduleAdapter, Map<Class<?>, ModuleAdapter<?>> map) {
        Class<?>[] clsArr;
        for (Class<?> cls : moduleAdapter.includes) {
            if (!map.containsKey(cls)) {
                ModuleAdapter moduleAdapter2 = loader.getModuleAdapter(cls);
                map.put(cls, moduleAdapter2);
                collectIncludedModulesRecursively(loader, moduleAdapter2, map);
            }
        }
    }
}
