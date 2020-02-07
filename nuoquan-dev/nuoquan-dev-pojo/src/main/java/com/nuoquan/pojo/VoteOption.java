package com.nuoquan.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "vote_option")
public class VoteOption {
    @Id
    private String id;

    @Column(name = "vote_id")
    private String voteId;

    @Column(name = "option_index")
    private Integer optionIndex;

    @Column(name = "create_date")
    private Date createDate;

    private Integer count;

    private Double percent;
    
    private Boolean hasVoted;
    
    public Boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Column(name = "option_content")
    private String optionContent;

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
     * @return option_index
     */
    public Integer getOptionIndex() {
        return optionIndex;
    }

    /**
     * @param optionIndex
     */
    public void setOptionIndex(Integer optionIndex) {
        this.optionIndex = optionIndex;
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
     * @return count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return percent
     */
    public Double getPercent() {
        return percent;
    }

    /**
     * @param percent
     */
    public void setPercent(Double percent) {
        this.percent = percent;
    }

    /**
     * @return option_content
     */
    public String getOptionContent() {
        return optionContent;
    }

    /**
     * @param optionContent
     */
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }
}