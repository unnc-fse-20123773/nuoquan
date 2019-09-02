package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_like_comment")
public class UserLikeComment {
    @Id
    private String id;

    /**
     * 点赞人
     */
    @Column(name = "user_id")
    private String userId;

    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 点赞消息是否被签收\n 0: 未签收 1：签收
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
     * 获取点赞人
     *
     * @return user_id - 点赞人
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置点赞人
     *
     * @param userId 点赞人
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return comment_id
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * @param commentId
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取点赞消息是否被签收\n 0: 未签收 1：签收
     *
     * @return sign_flag - 点赞消息是否被签收\n 0: 未签收 1：签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置点赞消息是否被签收\n 0: 未签收 1：签收
     *
     * @param signFlag 点赞消息是否被签收\n 0: 未签收 1：签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }
}