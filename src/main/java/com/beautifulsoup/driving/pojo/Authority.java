package com.beautifulsoup.driving.pojo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_acl")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String aclName;

    private String aclUrl;

    private Integer type;

    private Integer status;

    private String remark;

    private Date updateTime;

    private String operator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAclName() {
        return aclName;
    }

    public void setAclName(String aclName) {
        this.aclName = aclName == null ? null : aclName.trim();
    }

    public String getAclUrl() {
        return aclUrl;
    }

    public void setAclUrl(String aclUrl) {
        this.aclUrl = aclUrl == null ? null : aclUrl.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }
}