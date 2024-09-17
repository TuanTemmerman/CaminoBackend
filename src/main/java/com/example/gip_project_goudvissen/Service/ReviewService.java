package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.ReviewDTO;
import com.example.gip_project_goudvissen.Entity.POI;
import com.example.gip_project_goudvissen.Entity.Review;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.POIRepository;
import com.example.gip_project_goudvissen.Repository.ReviewRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    public Review addReview(Long userId, Long poiId, ReviewDTO reviewDTO){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        POI poi = poiRepository.findById(poiId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found"));
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        review.setSummary(reviewDTO.getSummary());
        review.setScore(reviewDTO.getScore());
        review.setCreatedByUser(user);
        review.setReviewForPOI(poi);
        return reviewRepository.save(review);
    }

    public Review findReviewById(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    public List<Review> findReviewsByUser(Long userId){
        return reviewRepository.findByCreatedByUser_Id(userId);
    }

    public List<Review> findReviewsByPOI(Long poiId){
        return reviewRepository.findByReviewForPOI_Id(poiId);
    }

    public Review updateReview(Long userId, Long reviewId, ReviewDTO reviewDTO){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        if (Objects.equals(userId, review.getCreatedByUser().getId())) {
            review.setTitle(reviewDTO.getTitle());
            review.setSummary(reviewDTO.getSummary());
            review.setScore(reviewDTO.getScore());
            return reviewRepository.save(review);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to edit this review");
        }
    }

    public void deleteReview(Long userId, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        if (Objects.equals(userId, review.getCreatedByUser().getId())){
            reviewRepository.delete(review);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to delete this review");
        }
    }

}
