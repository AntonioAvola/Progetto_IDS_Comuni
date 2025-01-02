package com.unicam.DTO.Response;

import java.time.LocalTime;

public class InterestPointWithReviewNum extends InterestPointResponse{

    private int reviewCount;

    public InterestPointWithReviewNum(long id, String title, String description, String reference, LocalTime open, LocalTime close) {
        super(id, title, description, reference, open, close);
        this.reviewCount = 0;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
