package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.ReviewDTO;
import com.example.gip_project_goudvissen.Entity.Review;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.ReviewService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/Review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addReview/{poiId}")
    public ResponseEntity<Review> addReview(@RequestBody ReviewDTO reviewDTO, @PathVariable Long poiId, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Review createdReview = reviewService.addReview(userId, poiId, reviewDTO);
            if (createdReview != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){
        Review review = reviewService.findReviewById(reviewId);
        if (review != null){
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reviewsByUser")
    public ResponseEntity<List<Review>> getReviewsFromUser(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<Review> reviews = reviewService.findReviewsByUser(userId);
            if (reviews != null){
                return ResponseEntity.ok(reviews);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{poiId}/reviewsByPOI")
    public ResponseEntity<List<Review>> getReviewsByPOI(@PathVariable Long poiId){
        List<Review> reviews = reviewService.findReviewsByPOI(poiId);
        if (reviews != null){
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewDTO reviewDTO, @PathVariable Long reviewId, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Review review = reviewService.updateReview(userId, reviewId, reviewDTO);
            if (review != null){
                return ResponseEntity.ok(review);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Review> deleteReview(@PathVariable Long reviewId, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            reviewService.deleteReview(userId, reviewId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
