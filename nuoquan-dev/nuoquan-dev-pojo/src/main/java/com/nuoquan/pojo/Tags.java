package com.nuoquan.pojo;

import javax.persistence.*;

public class Tags {
    @Id
    private Integer id;

    @Column(name = "tag_content")
    private String tagContent;

    @Column(name = "article_id")
    private String articleId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
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