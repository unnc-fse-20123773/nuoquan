package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

public class User {
    @Id
    private String id;

    private String email;

    private String password;

    private String nickname;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "face_img")
    private String faceImg;

    /**
     * 小头像
     */
    @Column(name = "face_img_thumb")
    private String faceImgThumb;

    @Column(name = "follow_num")
    private Integer followNum;

    /**
     * 0 = female, 1 = male, 2 = others
     */
    private Byte gender;

    private String major;

    @Column(name = "graduation_year")
    private Date graduationYear;

    /**
     * 0 = high school, 1 = undergraduate, 2 = graduate
     */
    private Byte degree;

    @Column(name = "receive_like_counts")
    private Integer receiveLikeCounts;

    /**
     * Client-id 设备id，用于消息推送
     */
    private String cid;

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
     * 获取小头像
     *
     * @return face_img_thumb - 小头像
     */
    public String getFaceImgThumb() {
        return faceImgThumb;
    }

    /**
     * 设置小头像
     *
     * @param faceImgThumb 小头像
     */
    public void setFaceImgThumb(String faceImgThumb) {
        this.faceImgThumb = faceImgThumb;
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

    /**
     * @return receive_like_counts
     */
    public Integer getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    /**
     * @param receiveLikeCounts
     */
    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }

    /**
     * 获取Client-id 设备id，用于消息推送
     *
     * @return cid - Client-id 设备id，用于消息推送
     */
    public String getCid() {
        return cid;
    }

    /**
     * 设置Client-id 设备id，用于消息推送
     *
     * @param cid Client-id 设备id，用于消息推送
     */
    public void setCid(String cid) {
        this.cid = cid;
    }
}