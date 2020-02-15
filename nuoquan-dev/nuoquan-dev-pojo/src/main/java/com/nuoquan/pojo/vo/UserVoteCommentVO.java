package com.nuoquan.pojo.vo;

import java.util.Date;

public class UserVoteCommentVO {

    private String id;
    private String fromUserId;
    private String toUserId;
    private String fatherCommentId; // 复式评论，父评论，子评论无 article_id
    private String voteId;
    private String comment;
    private Date createDate;
    private Integer likeNum;
    private Integer dislikeNum;
    private Integer commentNum;
    private String underCommentId; // 显示在该条评论下方
    private Integer signFlag;
    private Integer status;
    
    private String faceImg;
    private String nickname;
    private String timeAgo;
    private String toNickname;
    
    private Boolean isLike; // 该用户是否点赞该评论
    
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
     * @return article_id
     */
    public String getArticleId() {
        return voteId;
    }

    /**
     * @param articleId
     */
    public void setArticleId(String articleId) {
        this.voteId = articleId;
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

	public String getFaceImg() {
		return faceImg;
	}

	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTimeAgo() {
		return timeAgo;
	}

	public void setTimeAgo(String timeAgo) {
		this.timeAgo = timeAgo;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFatherCommentId() {
		return fatherCommentId;
	}

	public void setFatherCommentId(String fatherCommentId) {
		this.fatherCommentId = fatherCommentId;
	}
	
    public String getUnderCommentId() {
        return underCommentId;
    }
    
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

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public String getToNickname() {
		return toNickname;
	}

	public void setToNickname(String toNickname) {
		this.toNickname = toNickname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
