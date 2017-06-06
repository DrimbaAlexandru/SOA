package com.company.utils.container;

/**
 * Created by AlexandruD on 06-Jun-17.
 */
public class Container<T> {

    private T value;
    public Container(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
