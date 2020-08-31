/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ruigu.rbox.workflow.supports;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.*;

/**
 * 反射工具类.
 * <p/>
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author calvin
 */
public class Reflections {
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory
            .getLogger(Reflections.class);


    /**
     * 实例化
     */
    public static <T> T newInstance(Class<T> t) {
        T instance = null;
        try {
            instance = t.newInstance();
        } catch (Throwable e) {
            logger.error("", e);
        }
        return instance;
    }

    /**
     * 调用Getter方法.
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = GETTER_PREFIX
                + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{},
                new Object[]{});
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     */
    public static void invokeSetter(Object obj, String propertyName,
                                    Object value) {
        String setterMethodName = SETTER_PREFIX
                + StringUtils.capitalize(propertyName);
        invokeMethodByName(obj, setterMethodName,
                new Object[]{value});
    }

    public static boolean hasField(final Object obj,
                                   final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        return field != null;
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj,
                                       final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException(
                    "Could not find field [" + fieldName
                            + "] on target [" + obj
                            + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj,
                                     final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException(
                    "Could not find field [" + fieldName
                            + "] on target [" + obj
                            + "]");
        }

        try {
            if (value == null) {
                field.set(obj, null);
            } else if (field.getType().equals(Integer.class)) {
                field.set(obj, Integer.valueOf(value.toString()));
            } else if (field.getType().equals(Short.class)) {
                field.set(obj, Short.valueOf(value.toString()));
            } else if (field.getType().equals(Long.class)) {
                field.set(obj, Long.valueOf(value.toString()));
            } else if (field.getType().equals(Double.class)) {
                field.set(obj, Double.valueOf(value.toString()));
            } else if (field.getType().equals(Float.class)) {
                field.set(obj, Float.valueOf(value.toString()));
            } else if (field.getType().equals(Boolean.class)) {
                field.set(obj, Boolean.valueOf(value.toString()));
            } else if (field.getType().equals(Byte.class)) {
                field.set(obj, Byte.valueOf(value.toString()));
            } else if (field.getType().equals(Character.class)) {
                field.set(obj, Character.valueOf(value.toString().charAt(0)));
            } else {
                field.set(obj, value);
            }
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj,
                                      final String methodName,
                                      final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName,
                parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find method [" + methodName
                            + "] on target [" + obj
                            + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static <T> Object invokeStaticMethod(Class<T> cls, String methodName, Object[] args) {
        try {
            Class[] argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
            Method method = cls.getMethod(methodName, argsClass);
            return method.invoke(null, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj,
                                            final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find method [" + methodName
                            + "] on target [" + obj
                            + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj,
                                           final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                Field field = superClass
                        .getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                boolean found = (name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()));
                if (found) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p/>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj,
     * Object... args)
     */
    public static Method getAccessibleMethod(final Object obj,
                                             final String methodName,
                                             final Class<?>... parameterTypes) {
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(
                        methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * <p/>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj,
     * Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj,
                                                   final String methodName) {
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        boolean can = (!Modifier.isPublic(method.getModifiers()) || !Modifier
                .isPublic(method.getDeclaringClass()
                        .getModifiers()))
                && !method.isAccessible();
        if (can) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        boolean can = (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass()
                .getModifiers()) || Modifier
                .isFinal(field.getModifiers()))
                && !field.isAccessible();
        if (can) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p/>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getClassGenricType(final Class clazz,
                                           final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName()
                    + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType)
                .getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            logger.warn("Index: " + index + ", Size of "
                    + clazz.getSimpleName()
                    + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName()
                    + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    @SuppressWarnings("rawtypes")
    public static Class<?> getUserClass(Object instance) {
        Class clazz = instance.getClass();
        if ((clazz != null)
                && clazz.getName().contains(
                CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null)
                    && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(
            Exception e) {
        if ((e instanceof IllegalAccessException)
                || (e instanceof IllegalArgumentException)
                || (e instanceof NoSuchMethodException)) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(
                    ((InvocationTargetException) e)
                            .getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    public static Field[] getAllFields(Class<?> initClass) {
        Field[] fields = initClass.getDeclaredFields();
        while (!initClass.getSuperclass().equals(Object.class)) {
            initClass = initClass.getSuperclass();
            Field[] tempfields = initClass.getDeclaredFields();
            fields = (Field[]) ArrayUtils.addAll(fields, tempfields);
        }
        return fields;
    }
}
