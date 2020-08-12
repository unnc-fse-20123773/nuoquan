package com.nuoquan.pojo.vo;

import java.util.Date;
import javax.persistence.*;

public class LongarticleVO {
    private String id;
    private String title;
    private String subTitle;
    private String userId;
    /**
     * 是否原创，0 = 原创，1 = 非原创
     */
    private Integer original;
    /**
     * 保留字段
     */
    private String tags;
    private String content;
    private Integer likeNum;
    private Integer dislikeNum;
    private Integer commentNum;
    private Integer collectNum;
    private Integer popularity;
    /**
     * 0 = unreadable, 1 = readable, 2 = checking
     */
    private Integer status;
    private Date createDate;
    /**
     * 0 = not anonymous, 1 = anonymous
     */
    private Byte isAnonymous;
    /**
     * 浏览量
     */
    private Integer viewNum;
    /**
     * 内容来源
     */
    private Integer source;

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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return sub_title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

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
     * 获取是否原创，0=原创，1=非原创
     *
     * @return original - 是否原创，0=原创，1=非原创
     */
    public Integer getOriginal() {
        return original;
    }

    /**
     * 设置是否原创，0=原创，1=非原创
     *
     * @param original 是否原创，0=原创，1=非原创
     */
    public void setOriginal(Integer original) {
        this.original = original;
    }

    /**
     * 获取保留字段
     *
     * @return tags - 保留字段
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置保留字段
     *
     * @param tags 保留字段
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return like_num
     */
    public Integer getLikeNum() {
        return likeNum;
    }

    /**
     * @param likeNum
     */
    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    /**
     * @return dislike_num
     */
    public Integer getDislikeNum() {
        return dislikeNum;
    }

    /**
     * @param dislikeNum
     */
    public void setDislikeNum(Integer dislikeNum) {
        this.dislikeNum = dislikeNum;
    }

    /**
     * @return comment_num
     */
    public Integer getCommentNum() {
        return commentNum;
    }

    /**
     * @param commentNum
     */
    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    /**
     * @return collect_num
     */
    public Integer getCollectNum() {
        return collectNum;
    }

    /**
     * @param collectNum
     */
    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    /**
     * @return popularity
     */
    public Integer getPopularity() {
        return popularity;
    }

    /**
     * @param popularity
     */
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    /**
     * 获取0 = unreadable, 1 = readable, 2 = checking
     *
     * @return status - 0 = unreadable, 1 = readable, 2 = checking
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0 = unreadable, 1 = readable, 2 = checking
     *
     * @param status 0 = unreadable, 1 = readable, 2 = checking
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取0 = not anonymous, 1 = anonymous
     *
     * @return is_anonymous - 0 = not anonymous, 1 = anonymous
     */
    public Byte getIsAnonymous() {
        return isAnonymous;
    }

    /**
     * 设置0 = not anonymous, 1 = anonymous
     *
     * @param isAnonymous 0 = not anonymous, 1 = anonymous
     */
    public void setIsAnonymous(Byte isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    /**
     * 获取浏览量
     *
     * @return view_num - 浏览量
     */
    public Integer getViewNum() {
        return viewNum;
    }

    /**
     * 设置浏览量
     *
     * @param viewNum 浏览量
     */
    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    /**
     * 获取内容来源
     *
     * @return source - 内容来源
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置内容来源
     *
     * @param source 内容来源
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}