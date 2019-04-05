package com.beautifulsoup.driving.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_student")
@EntityListeners(AuditingEntityListener.class)
public class Student implements Serializable {

    private static final long serialVersionUID = -330151956654151223L;

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id",length = 100)
    private String studentId;

    @Column(name = "student_name",length = 100)
    private String studentName;
    @Column(name = "student_phone",length = 50)
    private String studentPhone;
    @Column(name = "student_img",length = 1024)
    private String studentImg;
    @Column(name = "student_school",length = 100)
    private String studentSchool;

    private BigDecimal studentPrice;
    @Column(name = "operator",length = 100)
    private String operator;

    private Integer status;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId == null ? null : studentId.trim();
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName == null ? null : studentName.trim();
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone == null ? null : studentPhone.trim();
    }

    public String getStudentImg() {
        return studentImg;
    }

    public void setStudentImg(String studentImg) {
        this.studentImg = studentImg == null ? null : studentImg.trim();
    }

    public String getStudentSchool() {
        return studentSchool;
    }

    public void setStudentSchool(String studentSchool) {
        this.studentSchool = studentSchool == null ? null : studentSchool.trim();
    }

    public BigDecimal getStudentPrice() {
        return studentPrice;
    }

    public void setStudentPrice(BigDecimal studentPrice) {
        this.studentPrice = studentPrice;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}