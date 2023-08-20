package com.andersen.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    private final Object methodOwner;
    private final Method method;

    public RequestHandler(Object methodOwner, Method method) {
        this.methodOwner = methodOwner;
        this.method = method;
    }

    public Object invoke(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return method.invoke(methodOwner, args);
    }

    public Method getMethod() {
        return method;
    }
}