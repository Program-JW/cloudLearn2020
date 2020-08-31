package com.ruigu.rbox.workflow.service.stock;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.model.enums.StockChangeApplyParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author alan.zhao
 * @date 2019/11/02 23:42
 */
@Slf4j
@Service("createStockChangeApplyServiceTask")
public class CreateStockChangeApplyServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {

        // 流程变量
        Map<String, Object> variables = delegateExecution.getVariables();
        // 是否立即执行
        int immediately = Integer.parseInt(String.valueOf(variables.get(StockChangeApplyParam.IMMEDIATELY.getDesc())));
        if (YesOrNoEnum.NO.getCode() == immediately) {
            // 计划执行时间
            delegateExecution.setVariable("planImplementTime", variables.get(StockChangeApplyParam.PLAN_TIME.getDesc()));
        }
        // 商品详情
        List<Map<String, Object>> items = (List<Map<String, Object>>) variables.get(StockChangeApplyParam.ITEM.getDesc());
        // 只有一个
        Map<String, Object> detail = items.get(0);
        // 是否增加
        delegateExecution.setVariable(StockChangeApplyParam.SKU_CODE.getDesc(), detail.get(StockChangeApplyParam.SKU_CODE.getDesc()));
        int count = Integer.parseInt(String.valueOf(detail.get(StockChangeApplyParam.COUNT.getDesc())));
        int increased = Integer.parseInt(String.valueOf(variables.get(StockChangeApplyParam.INCREASED.getDesc())));
        if (YesOrNoEnum.NO.getCode() == increased) {
            count = -count;
        }
        delegateExecution.setVariable(StockChangeApplyParam.COUNT.getDesc(), count);
    }
}
