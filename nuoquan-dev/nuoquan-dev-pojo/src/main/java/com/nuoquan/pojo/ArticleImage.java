package com.nuoquan.pojo;

import javax.persistence.*;

@Table(name = "article_image")
public class ArticleImage {
    @Id
    private String id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "image_path")
    private String imagePath;

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
     * @return image_path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}