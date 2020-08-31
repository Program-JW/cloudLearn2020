package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/20 15:30
 */
@Data
public class SpecialAfterSaleNextNodeAndCcVO {

    private String nextNode;

    private List<PassportUserInfoDTO> ccList;
}
