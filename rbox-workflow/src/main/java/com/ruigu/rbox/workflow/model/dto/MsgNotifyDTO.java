package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cg1
 * @date 2020-02-03 17:53
 */
@Data
public class MsgNotifyDTO {
    private Long msgId;
    private Long groupId;
    private String fromConnName;
    private String content;
    private Integer contentType;
    private List<String> offUserList;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;
}
