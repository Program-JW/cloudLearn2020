package com.ruigu.rbox.workflow.supports;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

/**
 * @author alan.zhao
 */
public class ElUtil {

    /**
     * 使用Spring EL 计算模板
     *
     * @param root     变量根上下文
     * @param template 例如 #{[message]}
     * @return 返回填充好的字符串
     */
    public static String fill(Map<String, Object> root, String template) {
        if (StringUtils.isBlank(template) || root == null || root.isEmpty()) {
            return template;
        }
        ExpressionParser parser = new SpelExpressionParser();
        String value = parser.parseExpression(template, new TemplateParserContext()).getValue(root, String.class);
        return value;
    }

}
