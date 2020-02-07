package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_vote_comment")
public class UserVoteComment {
    @Id
    private String id;

    @Column(name = "from_user_id")
    private String fromUserId;

    @Column(name = "to_user_id")
    private String toUserId;

    /**
     * 复式评论，父评论，子评论无 article_id
     */
    @Column(name = "father_comment_id")
    private String fatherCommentId;

    @Column(name = "vote_id")
    private String voteId;

    private String comment;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "like_num")
    private Integer likeNum;

    @Column(name = "dislike_num")
    private Integer dislikeNum;

    @Column(name = "comment_num")
    private Integer commentNum;

    @Column(name = "under_comment_id")
    private String underCommentId;

    /**
     * 评论消息是否被签收 0: 未签收 1：签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    private Integer status;

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
     * @return from_user_id
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     * @param fromUserId
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * @return to_user_id
     */
    public String getToUserId() {
        return toUserId;
    }

    /**
     * @param toUserId
     */
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    /**
     * 获取复式评论，父评论，子评论无 article_id
     *
     * @return father_comment_id - 复式评论，父评论，子评论无 article_id
     */
    public String getFatherCommentId() {
        return fatherCommentId;
    }

    /**
     * 设置复式评论，父评论，子评论无 article_id
     *
     * @param fatherCommentId 复式评论，父评论，子评论无 article_id
     */
    public void setFatherCommentId(String fatherCommentId) {
        this.fatherCommentId = fatherCommentId;
    }

    /**
     * @return vote_id
     */
    public String getVoteId() {
        return voteId;
    }

    /**
     * @param voteId
     */
    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
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
     * @return under_comment_id
     */
    public String getUnderCommentId() {
        return underCommentId;
    }

    /**
     * @param underCommentId
     */
    public void setUnderCommentId(String underCommentId) {
        this.underCommentId = underCommentId;
    }

    /**
     * 获取评论消息是否被签收 0: 未签收 1：签收
     *
     * @return sign_flag - 评论消息是否被签收 0: 未签收 1：签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置评论消息是否被签收 0: 未签收 1：签收
     *
     * @param signFlag 评论消息是否被签收 0: 未签收 1：签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}