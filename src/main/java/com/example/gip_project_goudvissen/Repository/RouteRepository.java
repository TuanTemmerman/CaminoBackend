package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
