package com.unicam.Service.Content;

import com.unicam.DTO.Response.ReviewResponse;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Media;
import com.unicam.Entity.Content.Review;
import com.unicam.Repository.Content.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private InterestPointService interestPointService;
    private final ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public List<ReviewResponse> GetReviewSinglePoint(InterestPoint point) {
        List<Review> reviews = this.reviewRepository.findAllByReference(point);
        return convertResponse(reviews, point.getId());
    }

    private List<ReviewResponse> convertResponse(List<Review> reviews, long pointId) {
        List<ReviewResponse> listResponse = new ArrayList<>();
        for(Review review : reviews){
            ReviewResponse response = new ReviewResponse(review.getTitle(), review.getDescription(), review.getAuthor().getUsername());
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

}
