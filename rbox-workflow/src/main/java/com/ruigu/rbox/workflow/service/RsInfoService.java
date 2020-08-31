package com.ruigu.rbox.workflow.service;


import com.ruigu.rbox.workflow.model.dto.RsCustomInfoDTO;
import com.ruigu.rbox.workflow.model.vo.CityVO;

import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/6/12 16:18
 */
public interface RsInfoService {
    /**
     * 获取城市组织架构
     *
     * @return {@link CityVO}
     */
    List<CityVO> getCities();

    RsCustomInfoDTO getCustomInfo(String mobile);
}
