package com.ruigu.rbox.workflow.supports;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author alan.zhao
 */
public class ClassUtils {

    /**
     * @description 将父类实例所有的参数赋值给子类实例
     * @param father
     * @param child
     * @param <T>
     * @throws Exception
     */

    public static <T>void fatherToChild(T father,T child) throws Exception {
        if (child.getClass().getSuperclass()!=father.getClass()){
            throw new Exception("child 不是 father 的子类");
        }
        Class<?> fatherClass = father.getClass();
        while (fatherClass != null) {
            Field[] declaredFields = fatherClass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                Method method = fatherClass.getDeclaredMethod("get" + upperHeadChar(field.getName()));
                Object obj = method.invoke(father);
                field.setAccessible(true);
                field.set(child, obj);
            }
            fatherClass = fatherClass.getSuperclass();
        }

    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }
}
