package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.POI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POIRepository extends JpaRepository<POI, Long> {
    List<POI> findByRouteIdAndVisibleTrue(Long routeId);

    List<POI> findByVisibleFalse();

    List<POI> findByVisibleTrue();

    POI findByIdAndVisibleTrue(Long id);
}
