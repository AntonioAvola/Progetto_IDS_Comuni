package com.unicam.Service.Content;

import com.unicam.DTO.Response.ReviewPOIResponse;
import com.unicam.DTO.Response.ReviewResponse;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Media;
import com.unicam.Entity.Content.Review;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public List<ReviewPOIResponse> GetReviewSinglePoint(InterestPoint point) {
        List<Review> reviews = this.reviewRepository.findAllByReference(point);
        return convertResponse(reviews, point.getId());
    }

    private List<ReviewPOIResponse> convertResponse(List<Review> reviews, long pointId) {
        List<ReviewPOIResponse> listResponse = new ArrayList<>();
        for(Review review : reviews){
            ReviewPOIResponse response = new ReviewPOIResponse(review.getTitle(), review.getDescription(), review.getAuthor().getUsername());
            List<Media> medias = review.getMedias();
            for(Media file: medias){
                response.setFileUrl("/api/poi/" + pointId + "/media/" + file.getId());
            }
            listResponse.add(response);
        }
        return listResponse;
    }

    public void add(Review review) {
        this.reviewRepository.save(review);
    }

    public int getReviewsNum(InterestPoint point) {
        List<Review> reviews = this.reviewRepository.findAllByReference(point);
        return reviews.size();
    }

    public List<ReviewResponse> getByUser(User user, String visitedMunicipality) {
        List<Review> reviews = this.reviewRepository.findAllByMunicipalityAndAuthor(visitedMunicipality, user);
        List<ReviewResponse> response = new ArrayList<>();
        for(Review review : reviews){
            ReviewResponse reviewResponse = new ReviewResponse(review.getTitle(), review.getDescription(),
                    review.getAuthor().getUsername(), review.getReference().getTitle(), review.getReference().getReference().getName());
            response.add(reviewResponse);
        }
        return response;
    }
}
