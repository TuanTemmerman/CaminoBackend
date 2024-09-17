package com.example.gip_project_goudvissen.DTO;

import java.util.Date;

public class ReviewDTO {

    private String title;

    private String summary;

    private int score;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
}
