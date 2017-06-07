package com.company.utils.updater;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public abstract class AbstractGettersAndSetters<T> {

    private List<Map.Entry<Method, Method>> gettersAndSetters;

    AbstractGettersAndSetters() {
        initGettersAndSetters(
                (Class) ((ParameterizedType) getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0]);

    }

    private void initGettersAndSetters(Class clazz) {
        gettersAndSetters = new ArrayList<>();
        for(Field f : clazz.getDeclaredFields()) {
            String capitalizedName = f.getName().substring(0, 1).toUpperCase() +
                    f.getName().substring(1);
            try {
                Method getter = clazz.getMethod("get" + capitalizedName);
                Method setter = clazz.getMethod("set" + capitalizedName, f.getType());
                gettersAndSetters.add(new AbstractMap.SimpleEntry<>(getter, setter));
            }catch(NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map.Entry<Method, Method>> getGettersAndSetters() {
        return gettersAndSetters;
    }
}
