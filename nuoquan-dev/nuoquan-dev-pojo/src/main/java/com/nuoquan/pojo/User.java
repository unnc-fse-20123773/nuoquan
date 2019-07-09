package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="用户对象", description="this is user object")
public class User {
	@ApiModelProperty(hidden=true)
    @Id
    private String id;

	@ApiModelProperty(hidden=true)
    private String email;
    
    @ApiModelProperty(value="密码", name="password", example="123456", required=true)
    private String password;

    @ApiModelProperty(value="昵称", name="nickname", example="JUMBOX123", required=true)
    private String nickname;

    @Column(name = "create_date")
    @ApiModelProperty(hidden=true)
    private Date createDate;

    @Column(name = "face_img")
    @ApiModelProperty(hidden=true)
    private String faceImg;

    @Column(name = "follow_num")
    @ApiModelProperty(hidden=true)
    private Integer followNum;

    /**
     * 0 = female, 1 = male, 2 = others
     */
    @ApiModelProperty(hidden=true)
    private Byte gender;

    @ApiModelProperty(hidden=true)
    private String major;

    @Column(name = "graduation_year")
    @ApiModelProperty(hidden=true)
    private Date graduationYear;

    /**
     * 0 = high school, 1 = undergraduate, 2 = graduate
     */
    @ApiModelProperty(hidden=true)
    private Byte degree;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
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
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    /**
     * @return face_img
     */
    public String getFaceImg() {
        return faceImg;
    }

    /**
     * @param faceImg
     */
    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    /**
     * @return follow_num
     */
    public Integer getFollowNum() {
        return followNum;
    }

    /**
     * @param followNum
     */
    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    /**
     * 获取0 = female, 1 = male, 2 = others
     *
     * @return gender - 0 = female, 1 = male, 2 = others
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * 设置0 = female, 1 = male, 2 = others
     *
     * @param gender 0 = female, 1 = male, 2 = others
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @param major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * @return graduation_year
     */
    public Date getGraduationYear() {
        return graduationYear;
    }

    /**
     * @param graduationYear
     */
    public void setGraduationYear(Date graduationYear) {
        this.graduationYear = graduationYear;
    }

    /**
     * 获取0 = high school, 1 = undergraduate, 2 = graduate
     *
     * @return degree - 0 = high school, 1 = undergraduate, 2 = graduate
     */
    public Byte getDegree() {
        return degree;
    }

    /**
     * 设置0 = high school, 1 = undergraduate, 2 = graduate
     *
     * @param degree 0 = high school, 1 = undergraduate, 2 = graduate
     */
    public void setDegree(Byte degree) {
        this.degree = degree;
    }
}