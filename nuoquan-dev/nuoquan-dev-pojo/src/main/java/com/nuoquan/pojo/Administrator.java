package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

public class Administrator {
    @Id
    private String id;

    @Column(name = "ad_username")
    private String adUsername;

    @Column(name = "ad_possward")
    private String adPossward;

    @Column(name = "create_date")
    private Date createDate;

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
     * @return ad_username
     */
    public String getAdUsername() {
        return adUsername;
    }

    /**
     * @param adUsername
     */
    public void setAdUsername(String adUsername) {
        this.adUsername = adUsername;
    }

    /**
     * @return ad_possward
     */
    public String getAdPossward() {
        return adPossward;
    }

    /**
     * @param adPossward
     */
    public void setAdPossward(String adPossward) {
        this.adPossward = adPossward;
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
}