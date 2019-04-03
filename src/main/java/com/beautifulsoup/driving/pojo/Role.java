package com.beautifulsoup.driving.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_role")
@NamedEntityGraph(name = "role.all",attributeNodes = {
        @NamedAttributeNode("agents"),
        @NamedAttributeNode("authorities")
})
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
    private static final long serialVersionUID = 6464265280184012778L;
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name",length = 100)
    private String roleName;

    private Integer type;//角色类型,0表示超级管理员,1表示1级代理,2表示2级代理

    private Integer status;//状态.1表示正常,0表示冻结
    @Column(name = "remark",length = 500)
    private String remark;

    @Column(name = "operator",length = 100)
    private String operator;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "tb_age_role",joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "none")),
            inverseJoinColumns = @JoinColumn(name = "age_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "none")))
    private List<Agent> agents=new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tb_role_acl",joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "none")),
            inverseJoinColumns = @JoinColumn(name = "acl_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "none")))
    private List<Authority> authorities=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}