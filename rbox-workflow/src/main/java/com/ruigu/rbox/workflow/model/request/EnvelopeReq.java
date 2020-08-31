package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author cg1
 * @date 2019-08-25 02:43
 */
@Data
public class EnvelopeReq implements Serializable {

    private String account;

    private Long msgId;

    private List<String> channel;

    private String templateCode;

    private Map<String, Object> property;

    private List<Map<String, Object>> content;

    private String source;

    @JsonAlias("user")
    private List<UserReq> users;

    private Map<String, Object> ext;

    private List<Integer> group;

    private Integer scope;

    private Integer msgType;

    private List<EnvelopeModeReq> mode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


}
