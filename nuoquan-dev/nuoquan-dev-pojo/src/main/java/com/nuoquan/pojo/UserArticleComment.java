package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_article_comment")
public class UserArticleComment {
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

    @Column(name = "article_id")
    private String articleId;

    private String comment;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "like_num")
    private Integer likeNum;

    @Column(name = "dislike_num")
    private Integer dislikeNum;

    @Column(name = "comment_num")
    private Integer commentNum;

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
     * @return create_time
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
}