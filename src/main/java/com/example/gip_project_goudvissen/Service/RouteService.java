package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.Entity.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Coordinate getStartCoordinate() {
        String routeSql = "SELECT start_location_x as x, start_location_y as y FROM route LIMIT 1";
        return jdbcTemplate.queryForObject(routeSql, new BeanPropertyRowMapper<>(Coordinate.class));
    }

    public Coordinate getEndCoordinate() {
        String endCoordinateSql = "SELECT end_location_x as x, end_location_y as y FROM route LIMIT 1";
        return jdbcTemplate.queryForObject(endCoordinateSql, new BeanPropertyRowMapper<>(Coordinate.class));
    }

    public List<Coordinate> getWaypointCoordinates() {
        String waypointsSql = "SELECT waypoint_x as x, waypoint_y as y FROM waypoint";
        return jdbcTemplate.query(waypointsSql, new BeanPropertyRowMapper<>(Coordinate.class));
    }

}
