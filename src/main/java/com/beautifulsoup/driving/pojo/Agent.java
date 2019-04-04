package com.beautifulsoup.driving.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_agent")
@NamedEntityGraph(name = "agent.all",attributeNodes = {
        @NamedAttributeNode("role")
})
public class Agent implements Serializable {

    private static final long serialVersionUID = -6959540856586065830L;

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "agent_name",length = 100)
    private String agentName;
    @Column(name = "agent_password",length = 500)
    private String agentPassword;
    @Column(name = "agent_phone",length = 50)
    private String agentPhone;
    @Column(name = "agent_email",length = 60)
    private String agentEmail;
    @Column(name = "agent_idcard",length = 100)
    private String agentIdcard;
    @Column(name = "agent_idcard_img",length = 1024)
    private String agentIdcardImg;
    @Column(name = "agent_school",length = 100)
    private String agentSchool;

    private Integer agentAchieve;//总业绩

    private Integer parentId;//超级管理员父节点不存在,为-1.

    private Integer status;//状态。1表示正常，0表示冻结

    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role=new Role();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName == null ? null : agentName.trim();
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public void setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword == null ? null : agentPassword.trim();
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone == null ? null : agentPhone.trim();
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail == null ? null : agentEmail.trim();
    }

    public String getAgentIdcard() {
        return agentIdcard;
    }

    public void setAgentIdcard(String agentIdcard) {
        this.agentIdcard = agentIdcard == null ? null : agentIdcard.trim();
    }

    public String getAgentIdcardImg() {
        return agentIdcardImg;
    }

    public void setAgentIdcardImg(String agentIdcardImg) {
        this.agentIdcardImg = agentIdcardImg == null ? null : agentIdcardImg.trim();
    }

    public String getAgentSchool() {
        return agentSchool;
    }

    public void setAgentSchool(String agentSchool) {
        this.agentSchool = agentSchool == null ? null : agentSchool.trim();
    }

    public Integer getAgentAchieve() {
        return agentAchieve;
    }

    public void setAgentAchieve(Integer agentAchieve) {
        this.agentAchieve = agentAchieve;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}