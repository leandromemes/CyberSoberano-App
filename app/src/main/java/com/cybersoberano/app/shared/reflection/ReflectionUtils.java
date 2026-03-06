package com.cybersoberano.app.shared.reflection;

import com.cybersoberano.app.shared.logger.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static class ReflectionResult {
        public Object value;
        public ReflectionResult(Object value) { this.value = value; }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            return null;
        }
    }

    public static ReflectionResult invokeMethod(Method method, Object obj, Object... args) {
        try {
            return new ReflectionResult(method.invoke(obj, args));
        } catch (Exception e) {
            return new ReflectionResult(null);
        }
    }

    public static ReflectionResult invokeField(Class<?> clazz, String fieldName, Object obj) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return new ReflectionResult(field.get(obj));
        } catch (Exception e) {
            return new ReflectionResult(null);
        }
    }
}
