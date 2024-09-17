package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.POI;
import com.example.gip_project_goudvissen.Entity.Review;
import com.example.gip_project_goudvissen.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCreatedByUser_Id(Long userId);

    List<Review> findByReviewForPOI_Id(Long poiId);
}
