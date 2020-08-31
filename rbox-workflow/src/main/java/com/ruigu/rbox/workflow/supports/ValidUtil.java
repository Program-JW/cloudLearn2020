package com.ruigu.rbox.workflow.supports;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证插件
 *
 * @author alan.zhao
 */
public class ValidUtil {
    /**
     * 验证不可缺少项
     */
    public static final int REQUIRED = 1;

    /**
     * 验证非负整数
     */
    public static final int NON_NEGATIVE_INTEGER = 2;

    /**
     * 验证逗号分隔的id串
     */
    public static final int COMMA_SPLIT_IDS = 3;

    /**
     * 验证日期
     */
    public static final int DATE = 4;

    /**
     * 验证时间
     */
    public static final int TIME = 5;

    /**
     * 验证时间范围,08:00~11:40
     */
    public static final int TIME_RANGE = 6;

    /**
     * 验证逗号分隔的经纬度，如:116.515802,40.000106
     */
    public static final int LNG_COMMA_LAT = 7;

    /**
     * 验证小数
     */
    public static final int DECIMAL = 8;

    /**
     * 验证时间08:00
     */
    public static final int HOUR = 9;

    /**
     * 验证日期范围
     */
    public static final int DATE_RANGE = 10;

    /**
     * 验证时长
     */
    public static final int HOURS = 11;

    /**
     * 必须为空
     */
    public static final int MUST_NULL = 12;

    /**
     * 不能为空字符串
     */
    public static final int NOT_BLANK = 13;


    public static boolean isDecimal(String value) {
        String exp = "[0-9]*(\\.?)[0-9]*";
        Pattern pattern = Pattern.compile(exp);
        return pattern.matcher(value).matches();
    }

    public static boolean isDate(String value) {
        String exp = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
        Pattern pattern = Pattern.compile(exp);
        return pattern.matcher(value).matches();
    }

    public static boolean isDates(String value) {
        String[] vs = value.split("~");
        boolean vl = false;
        int two = 2;
        if (vs.length == two) {
            vl = true;
            for (int i = 0; i < vs.length; i++) {
                vl = vl && isDate(vs[i]);
                if (!vl) {
                    break;
                }
            }
        } else {
            vl = false;
        }
        return vl;
    }

    /**
     * 验证时间范围,08:00~11:40
     */
    public static boolean isTimeRange(String value) {
        String exp = "^(([01][0-9])|(2[0-3])):[03]0~(([01][0-9])|(2[0-3])):[03]0$";
        Pattern pattern = Pattern.compile(exp);
        return pattern.matcher(value).matches();
    }

    /**
     * 验证时间,08:00
     */
    public static boolean isHourMinute(String value) {
        String exp = "^(([01][0-9])|(2[0-3])):[03]0$";
        Pattern pattern = Pattern.compile(exp);
        return pattern.matcher(value).matches();
    }

    public static boolean isNonNegativeInteger(String value) {
        String exp = "^\\d+$";
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(value);
        boolean valid = m.matches();
        return valid;
    }

    public static ValidResult requiredFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (value == null) {
            result.message = "缺少参数" + field;
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult notBlankFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (value != null && StringUtils.isBlank(value)) {
            result.message = "参数" + field + "不能为空";
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult mustNullFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (value != null) {
            result.message = "参数" + field + "不能有值";
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult nonNegativeIntegerFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (StringUtils.isNotEmpty(value) && !ValidUtil.isNonNegativeInteger(value)) {
            result.message = "参数" + field + "=" + value + "不是合法的值";
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult decimalFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (StringUtils.isNotEmpty(value) && !ValidUtil.isDecimal(value)) {
            result.message = "参数" + field + "=" + value + "不是合法的值";
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult dateFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        if (StringUtils.isNotEmpty(value) && !ValidUtil.isDate(value)) {
            result.message = "参数" + field + "=" + value + "不是合法,日期形式必须是yyyy-MM-dd";
            result.valid = false;
        } else {
            result.valid = true;
        }
        return result;
    }

    public static ValidResult commaSplitIdsFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        result.valid = true;
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        String[] idsArr = value.split(",");
        for (int i = 0; i < idsArr.length; i++) {
            if (!ValidUtil.isNonNegativeInteger(idsArr[i])) {
                result.message = field + "含有不合法ID";
                result.valid = false;
                break;
            }
        }
        return result;
    }

    public static ValidResult hoursFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        result.valid = true;
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        double d = Double.parseDouble(value);
        int two = 2, six = 6;
        if (d < two || d > six) {
            result.message = field + "值必须>=2,<=6";
            result.valid = false;
        } else {
            String[] ss = value.split("\\.");
            if (ss.length == two) {
                int i = Integer.valueOf(ss[1]);
                result.valid = (i == 0 || i == 5);
                if (!result.valid) {
                    result.message = field + ",时长必须是半个小时粒度";
                }
            } else {
                result.valid = true;
            }
        }
        return result;
    }

    public static ValidResult timeRangeFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        result.valid = true;
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        if (!ValidUtil.isTimeRange(value)) {
            result.message = field + "值不合法,格式:HH:mm~HH:mm";
            result.valid = false;
        } else {
            String[] ll = value.split("~");
            Date start = DateUtil.parseStrToDateTime("2015-01-01 " + ll[0] + ":00");
            Date end = DateUtil.parseStrToDateTime("2015-01-01 " + ll[1] + ":00");
            if (start.compareTo(end) >= 0) {
                result.message = field + "值不合法,开始时间必须小于结束时间";
                result.valid = false;
            }
        }
        return result;
    }

    public static ValidResult hourFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        result.valid = true;
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        if (!ValidUtil.isHourMinute(value)) {
            result.message = field + "值不合法,格式:HH:mm";
            result.valid = false;
        }
        return result;
    }

    public static ValidResult lngCommaLatFieldPlugin(String field, String value) {
        ValidResult result = new ValidResult();
        result.valid = true;
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        String[] lnglat = value.split(",");
        int two = 2;
        if (lnglat.length != two) {
            result.message = field + "=" + value + "不是有效的经纬度参数";
            result.valid = false;
        } else {
            for (int i = 0; i < lnglat.length; i++) {
                if (!ValidUtil.isDecimal(lnglat[i])) {
                    result.message = field + "=" + value + "不是有效的经纬度参数";
                    result.valid = false;
                    break;
                }
            }
        }

        return result;
    }

    public static String parseParam(Object param, String key) {
        if (param instanceof HashMap) {
            return ((HashMap<String, String>) param).get(key);
        } else {
            Object value = Reflections.getFieldValue(param, key);
            return value == null ? null : value.toString();
        }
    }

    public static ValidResult validField(Object param, String field, Object[] plugins) {
        ValidResult result = new ValidResult();
        result.valid = true;
        String value = parseParam(param, field);
        for (int i = 0; i < plugins.length; i++) {
            Object plugin = plugins[i];
            if (plugin instanceof ValidPlugin) {
                result = ((ValidPlugin) plugin).doValid(field, value);
            } else {
                if (plugin.equals(REQUIRED)) {
                    result = requiredFieldPlugin(field, value);
                } else if (plugin.equals(NON_NEGATIVE_INTEGER)) {
                    result = nonNegativeIntegerFieldPlugin(field, value);
                } else if (plugin.equals(COMMA_SPLIT_IDS)) {
                    result = commaSplitIdsFieldPlugin(field, value);
                } else if (plugin.equals(LNG_COMMA_LAT)) {
                    result = lngCommaLatFieldPlugin(field, value);
                } else if (plugin.equals(DECIMAL)) {
                    result = decimalFieldPlugin(field, value);
                } else if (plugin.equals(DATE)) {
                    result = dateFieldPlugin(field, value);
                } else if (plugin.equals(TIME_RANGE)) {
                    result = timeRangeFieldPlugin(field, value);
                } else if (plugin.equals(HOUR)) {
                    result = hourFieldPlugin(field, value);
                } else if (plugin.equals(HOURS)) {
                    result = hoursFieldPlugin(field, value);
                } else if (plugin.equals(MUST_NULL)) {
                    result = mustNullFieldPlugin(field, value);
                } else if (plugin.equals(NOT_BLANK)) {
                    result = notBlankFieldPlugin(field, value);
                }
            }
            if (!result.valid) {
                break;
            }
        }
        return result;
    }

    public static ValidResult validFields(Object param, String[] fields, Object[][] pluginsList) {
        ValidResult result = new ValidResult();
        result.valid = true;
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Object[] plugins = pluginsList[i];
            ValidResult temp = validField(param, fields[i], plugins);
            result.valid = result.valid && temp.valid;
            if (!temp.valid) {
                errors.add(temp.message);
            }
        }
        if (errors.size() > 0) {
            result.message = StringUtils.join(errors, ";");
        }
        return result;
    }

    public static class StringLength implements ValidPlugin {
        private Integer max;
        private Integer min = 0;

        public StringLength(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public ValidResult doValid(String field, String value) {
            ValidResult v = new ValidResult();
            boolean valid = StringUtils.isBlank(value) ||
                    (max == null || value.length() <= max) && (min == null || value.length() >= min);
            if (valid) {
                v.valid = true;
            } else{
                v.message = "参数" + field + "的值的长度必须在[" + (this.min != null ? this.min : "") + "," + (this.max != null ? this.max : "") + "]范围内";
                v.valid = false;
            }
            return v;
        }
    }
}