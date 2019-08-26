package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "article_tags")
public class ArticleTags {
    @Id
    private String id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "tag_content")
    private String tagContent;

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
     * @return tag_content
     */
    public String getTagContent() {
        return tagContent;
    }

    /**
     * @param tagContent
     */
    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}