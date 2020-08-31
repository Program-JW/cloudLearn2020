package com.ruigu.rbox.workflow.model.entity;

import javax.persistence.*;

/**
 * @author liqingtian
 * @date 2019/10/10 16:12
 */
@Entity
@Table(name = "business_param")
public class BusinessParamEntity {
    private Integer id;
    private String definitionCode;
    private String instanceId;
    private String paramKey;
    private String paramValue;
    private Integer findWay;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "definition_code")
    public String getDefinitionCode() {
        return definitionCode;
    }

    public void setDefinitionCode(String definitionCode) {
        this.definitionCode = definitionCode;
    }

    @Basic
    @Column(name = "instance_id")
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Basic
    @Column(name = "param_key")
    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    @Basic
    @Column(name = "param_value")
    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Basic
    @Column(name = "find_way")
    public Integer getFindWay() {
        return findWay;
    }

    public void setFindWay(Integer findWay) {
        this.findWay = findWay;
    }
}
