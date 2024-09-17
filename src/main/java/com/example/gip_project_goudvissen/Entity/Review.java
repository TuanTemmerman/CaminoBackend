package com.example.gip_project_goudvissen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Title cannot be null")
    private String title;
    private Date date;
    @NotNull(message = "Summary cannot be null")
    @Column(length = 1000)
    private String summary;
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private int score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-review")
    private User createdByUser;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    @JsonBackReference
    private POI reviewForPOI;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public POI getReviewForPOI() {
        return reviewForPOI;
    }

    public void setReviewForPOI(POI reviewForPOI) {
        this.reviewForPOI = reviewForPOI;
    }
}
