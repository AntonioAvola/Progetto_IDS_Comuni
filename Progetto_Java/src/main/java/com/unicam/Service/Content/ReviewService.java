package com.unicam.Service.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Review;
import com.unicam.Repository.Content.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class ReviewService {
    private InterestPointService interestPointService;
    private final ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public List<Review> GetReviewSinglePoint(long idPoint) {
        InterestPoint interestPoint = this.interestPointService.GetSinglePoint(idPoint);
        List <Review> review = this.reviewRepository.findAllByReference(interestPoint);
        return review;
    }

    //TODO dopo aver implementato la rispettiva entità
}
