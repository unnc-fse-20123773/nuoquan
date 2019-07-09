package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "user_like_article")
public class UserLikeArticle {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "article_id")
    private String articleId;

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
}