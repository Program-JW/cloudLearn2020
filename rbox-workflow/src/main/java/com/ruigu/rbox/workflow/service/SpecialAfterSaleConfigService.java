package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleReviewPositionDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleNextNodeAndCcVO;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 16:36
 */
public interface SpecialAfterSaleConfigService {

    /**
     * 获取匹配的职位信息
     *
     * @param userId 用户id
     * @return 匹配到的信息
     */
    List<SpecialAfterSaleReviewPositionDTO> matchConfigs(Integer userId);

    /**
     * 获取匹配的职位信息
     *
     * @param userInfo 用户信息
     * @return 匹配到的信息
     */
    List<SpecialAfterSaleReviewPositionDTO> matchConfigs(UserGroupSimpleDTO userInfo);

    /**
     * 查询当前操作人所使用审批规则的下一个节点和抄送人信息
     *
     * @return  信息
     */
    SpecialAfterSaleNextNodeAndCcVO queryNextNodeAndCcInfo();
}
