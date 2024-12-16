package com.unicam.Repository.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface ReviewRepository /**extends JpaRepository<Review, Long> */{
    List<Review> findAllByReference(InterestPoint interestPoint);
}
