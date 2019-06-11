package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_user")
public class SysUser {
    @Column(name = "user_id")
    private String userId;

    /**
     * 昵称
     */
    private String username;

    private String email;

    private String password;

    /**
     * 学号
eg. 16512345
     */
    @Column(name = "student_id")
    private String studentId;

    /**
     * 大一：1
大二：2
大三：3
大四：4
研/博：5
     */
    @Column(name = "admission_year")
    private Integer admissionYear;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取昵称
     *
     * @return username - 昵称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置昵称
     *
     * @param username 昵称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取学号
eg. 16512345
     *
     * @return student_id - 学号
eg. 16512345
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 设置学号
eg. 16512345
     *
     * @param studentId 学号
eg. 16512345
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * 获取大一：1
大二：2
大三：3
大四：4
研/博：5
     *
     * @return admission_year - 大一：1
大二：2
大三：3
大四：4
研/博：5
     */
    public Integer getAdmissionYear() {
        return admissionYear;
    }

    /**
     * 设置大一：1
大二：2
大三：3
大四：4
研/博：5
     *
     * @param admissionYear 大一：1
大二：2
大三：3
大四：4
研/博：5
     */
    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}