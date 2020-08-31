package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.feign.handler.RsClientHandler;
import com.ruigu.rbox.workflow.model.dto.RsAreaDTO;
import com.ruigu.rbox.workflow.model.dto.RsCustomInfoDTO;
import com.ruigu.rbox.workflow.model.vo.CityVO;
import com.ruigu.rbox.workflow.service.RsInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 城市业务
 *
 * @author chenzhenya
 * @date 2020/6/12 16:19
 */

@Service
public class RsInfoServiceImpl implements RsInfoService {

    @Resource
    private RsClientHandler rsClientHandler;

    @Override
    public List<CityVO> getCities() {
        List<RsAreaDTO> areas = rsClientHandler.getCities();
        if (CollectionUtils.isNotEmpty(areas)) {
            return areas.stream().map(this::convertFromDTO).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public RsCustomInfoDTO getCustomInfo(String mobile) {
        return rsClientHandler.getCustomInfo(mobile);
    }

    private CityVO convertFromDTO(RsAreaDTO dto) {
        List<RsAreaDTO> children = dto.getChild();
        //结束条件
        if (children.isEmpty()) {
            CityVO areaVO = new CityVO();
            areaVO.setId(dto.getId());
            areaVO.setName(dto.getName());
            return areaVO;
        }
        CityVO vo = new CityVO();
        vo.setId(dto.getId());
        vo.setName(dto.getName());
        ArrayList<CityVO> areaNameList = new ArrayList<>();
        //递归
        for (RsAreaDTO areaDTO : children) {
            CityVO cityVO = convertFromDTO(areaDTO);
            areaNameList.add(cityVO);
        }
        vo.setChild(areaNameList);
        return vo;
    }
}
