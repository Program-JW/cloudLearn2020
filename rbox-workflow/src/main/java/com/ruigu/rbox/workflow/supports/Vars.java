package com.ruigu.rbox.workflow.supports;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程变量工具
 *
 * @author alan.zhao
 */
public class Vars {

    /**
     * 从delegateExecution获取变量，并尽量转化为期望类型
     *
     * @param delegateExecution 流程delegateExecution
     * @param varName           变量名称
     * @param resultClass       期望的结果类型
     * @param <T>               期望的结果类型
     * @return 返回变量值
     */
    public static <T> T getVar(DelegateExecution delegateExecution, String varName, Class resultClass) {
        Object variable = delegateExecution.getVariable(varName);
        return (T) convert(variable, resultClass, null);
    }

    /**
     * 从delegateTask获取变量，并尽量转化为期望类型
     *
     * @param delegateTask 流程delegateExecution
     * @param varName           变量名称
     * @param resultClass       期望的结果类型
     * @param <T>               期望的结果类型
     * @return 返回变量值
     */
    public static <T> T getVar(DelegateTask delegateTask, String varName, Class resultClass) {
        Object variable = delegateTask.getVariable(varName);
        return (T) convert(variable, resultClass, null);
    }

    /**
     * 从delegateExecution获取变量，并尽量转化为期望类型
     *
     * @param delegateExecution 流程delegateExecution
     * @param varName           变量名称
     * @param resultClass       期望的结果类型
     * @param <T>               期望的结果类型
     * @return 返回变量值
     */
    public static <T> T getVar(DelegateExecution delegateExecution, String varName, Class resultClass, Class elClass) {
        Object variable = delegateExecution.getVariable(varName);
        return (T) convert(variable, resultClass, elClass);
    }

    /**
     * 将对象尽量转化为期望类型
     *
     * @param resultClass 期望的结果类型
     * @param <T>         期望的结果类型
     * @return 返回转化后的值
     */
    private static <T> T convert(Object valueObject, Class<T> resultClass, Class elClass) {
        if (valueObject == null) {
            return null;
        }
        String value = valueObject.toString();
        T val = null;
        if (Short.class == resultClass) {
            val = (T) Short.valueOf(value);
        } else if (Integer.class == resultClass) {
            val = (T) Integer.valueOf(value);
        } else if (Long.class == resultClass) {
            val = (T) Long.valueOf(value);
        } else if (Float.class == resultClass) {
            val = (T) Float.valueOf(value);
        } else if (Double.class == resultClass) {
            val = (T) Double.valueOf(value);
        } else if (BigDecimal.class == resultClass) {
            val = (T) new BigDecimal(value);
        } else if (String.class == resultClass) {
            val = (T) value;
        } else if (List.class == resultClass) {
            val = (T) JsonUtil.parseArray(value, elClass);
        } else {
            val = null;
        }
        return val;
    }

    public static void main(String[] args) {
        List<String> ss = new ArrayList<>();
        ss.add("1");
        List<Integer> list = Vars.convert(ss, List.class, Integer.class);
        System.out.println(list);
    }
}
