package com.beautifulsoup.driving.pojo;

import javax.persistence.*;

@Entity
@Table(name = "tb_agent")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String agentName;

    private String agentPassword;

    private String agentPhone;

    private String agentEmail;

    private String agentIdcard;

    private String agentIdcardImg;

    private String agentSchool;

    private Integer agentAchieve;

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
}