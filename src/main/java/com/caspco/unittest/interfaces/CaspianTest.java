package com.caspco.unittest.interfaces;

import com.caspco.unittest.annotations.DisableMockStatic;
import com.caspco.unittest.annotations.MockAutowired;
import com.caspco.unittest.annotations.MockModel;
import com.caspco.unittest.annotations.MockStatic;
import com.caspco.unittest.file.JsonModel;
import com.caspco.unittest.model.CaspianStatement;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CaspianTest {
    CaspianStatement caspianMethodBlock(FrameworkMethod frameworkMethod);

    default Object setRequirements(Object testInstance) throws IOException, IllegalAccessException, ClassNotFoundException {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            setModel(testInstance, field);
            autowired(testInstance, field);
        }

        return testInstance;
    }

    default void takeRequirements(Object testInstance) throws IllegalAccessException {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            closeMockStatic(testInstance, field);
        }
    }

    default void setModel(Object testInstance, Field field) throws IOException, IllegalAccessException {
        if (field.isAnnotationPresent(MockModel.class)) {
            Object model;
            String path = String.format("%s.%s", testInstance.getClass().getName(), field.getName());
            if (field.getGenericType() instanceof ParameterizedType) {
                model = JsonModel.getModel(path, field.getType(), ((ParameterizedType) field.getGenericType()).getActualTypeArguments());
            } else {
                model = JsonModel.getModel(path, field.getType());
            }

            field.set(testInstance, model);
        }
    }

    default void autowired(Object testInstance, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(MockAutowired.class)) {
            Object object = mock(field.getType());

            field.set(testInstance, object);
        }
    }

    default boolean isMockStaticDisabled(List<Class<?>> mockStaticClasses, Class<?> aClass) {
        return mockStaticClasses.stream().anyMatch(mockStaticClass -> mockStaticClass.equals(aClass));
    }

    default void mockStatic(Object testInstance, List<Class<?>> mockStaticClasses) throws IllegalAccessException, ClassNotFoundException {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(MockStatic.class)) {
                Class<?> aClass = Class.forName(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName());

                if (!isMockStaticDisabled(mockStaticClasses, aClass)) {
                    Object object = Mockito.mockStatic(aClass);
                    field.set(testInstance, object);
                }
            }
        }
    }

    default void closeMockStatic(Object testInstance, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(MockStatic.class)) {
            if (field.get(testInstance) != null) {
                ((MockedStatic) field.get(testInstance)).close();
            }
        }
    }

    default void mockStatic(FrameworkMethod frameworkMethod, Object testInstance, EachTestNotifier eachNotifier) {
        List<Class<?>> mockStaticClasses = new ArrayList<>();
        Method method = frameworkMethod.getMethod();
        if (frameworkMethod.getMethod().isAnnotationPresent(DisableMockStatic.class)) {
            DisableMockStatic disableMockStatic = method.getAnnotation(DisableMockStatic.class);
            mockStaticClasses = Arrays.asList(disableMockStatic.value());
        }

        try {
            mockStatic(testInstance, mockStaticClasses);
        } catch (IllegalAccessException e) {
            eachNotifier.addFailure(e);
        } catch (ClassNotFoundException e) {
            eachNotifier.addFailure(e);
        }
    }

    default void takeRequirements(FrameworkMethod frameworkMethod, Object testInstance, EachTestNotifier eachNotifier) {
        try {
            takeRequirements(testInstance);
        } catch (IllegalAccessException e) {
            eachNotifier.addFailure(e);
        }
    }
}
