package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author ：alan
 * @date ：Created in {2019/9/10} {17:10}
 */
@Data
public class SkuDetailVo {
    private Map<String, Object> productInfo;

    public Integer getInt(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        return Integer.valueOf(productInfo.get(field).toString());
    }

    public Date getDate(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(productInfo.get(field).toString());
        } catch (Exception e) {

        }
        return null;
    }

    public Date getDateFromInt(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        long time = Long.parseLong(productInfo.get(field).toString());
        if (time == 0) {
            return null;
        }
        return new Date(time * 1000);
    }

    public Float getFloat(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        return Float.valueOf(productInfo.get(field).toString());
    }

    public String getString(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        return productInfo.get(field).toString();
    }

    public BigDecimal getDecimal(String field) {
        if (productInfo == null || !productInfo.containsKey(field) || productInfo.get(field) == null) {
            return null;
        }
        return new BigDecimal(productInfo.get(field).toString());
    }

}
