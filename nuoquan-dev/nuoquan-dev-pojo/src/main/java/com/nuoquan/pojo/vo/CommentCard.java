package com.nuoquan.pojo.vo;

/**
 * 评论通知卡片内容
 * @author jerrio
 */
public class CommentCard {
	
	private UserArticleCommentVO commentVO;
	private Object target; // 文章或评论
	
	public CommentCard(UserArticleCommentVO comment, Object target) {
		this.commentVO = comment;
		this.target = target;
	}
	
	public UserArticleCommentVO getComment() {
		return commentVO;
	}
	
	public void setComment(UserArticleCommentVO comment) {
		this.commentVO = comment;
	}
	
	public Object getTarget() {
		return target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}
}
