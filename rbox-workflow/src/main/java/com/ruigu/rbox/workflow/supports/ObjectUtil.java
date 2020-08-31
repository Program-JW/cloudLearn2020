package com.ruigu.rbox.workflow.supports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author alan.zhao
 */
public class ObjectUtil {
    static Logger logger = LoggerFactory.getLogger(ObjectUtil.class);
    public static <T> T extendObject(T t0, Object t1, boolean ignoreNull) {
        if (t1 != null) {
            Class tempCls = t0.getClass();
            while (tempCls != null) {
                Field[] fields = tempCls.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    if (Modifier.isStatic(fields[i].getModifiers()) || Modifier.isFinal(fields[i].getModifiers()) || !Reflections.hasField(t1, fields[i].getName())) {
                        continue;
                    }
                    Object value = Reflections.getFieldValue(t1, fields[i].getName());
                    if (ignoreNull) {
                        if (value != null) {
                            Reflections.setFieldValue(t0, fields[i].getName(), value);
                        }
                    } else {
                        Reflections.setFieldValue(t0, fields[i].getName(), value);
                    }
                }
                tempCls = tempCls.getSuperclass();
            }
        }
        return t0;
    }
    /**
     * 判断类中每个属性是否都为空
     *
     * @param o
     * @return
     */
    public static boolean allFieldIsNull(Object o){
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object object = field.get(o);
                if (object instanceof CharSequence) {
                    if (!org.springframework.util.ObjectUtils.isEmpty(object)) {
                        return false;
                    }
                } else {
                    if (null != object) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("判断对象属性为空异常", e);

        }
        return true;
    }

    /**
     * 从map中获取字符串值
     */
    public static String getString(Map<String, Object> map, String field) {
        try {
            return map != null && map.get(field) != null ? map.get(field).toString() : null;
        } catch (Exception e) {
            logger.error("判断对象属性为空异常", e);

        }
        return null;
    }

}
