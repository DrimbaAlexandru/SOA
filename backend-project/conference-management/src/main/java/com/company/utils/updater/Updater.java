package com.company.utils.updater;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public class Updater {

    /**
     * Updates an object using reflection. It gets the not null fields of the updateSource and sets them on the former object, using the getters and setters provided
     * @param former The object to be updated
     * @param updateSource The object to get the new fields from
     * @param gettersAndSetters A list of pairs of getters and setters for each field which can be updated. The getter comes first and the setter second
     */
    public <T> void update(T former,
                        T updateSource,
                        List<Map.Entry<Method, Method>> gettersAndSetters) {
        for(Map.Entry<Method, Method> methods : gettersAndSetters) {
            try {
                Object data = methods.getKey().invoke(updateSource);
                if(data != null) {
                    methods.getValue().invoke(former, data);
                }
            }catch(InvocationTargetException e) {
                e.printStackTrace();
            }catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
