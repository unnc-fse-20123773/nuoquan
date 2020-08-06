package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_like_longarticle")
public class UserLikeLongarticle {
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * 点赞消息是否被签收\\n 0: 未签收 1：签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

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
     * @return article_id
     */
    public String getArticleId() {
        return articleId;
    }

    /**
     * @param articleId
     */
    public void setArticleId(String articleId) {
        this.articleId = articleId;
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
     * 获取点赞消息是否被签收\\n 0: 未签收 1：签收
     *
     * @return sign_flag - 点赞消息是否被签收\\n 0: 未签收 1：签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置点赞消息是否被签收\\n 0: 未签收 1：签收
     *
     * @param signFlag 点赞消息是否被签收\\n 0: 未签收 1：签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }
}