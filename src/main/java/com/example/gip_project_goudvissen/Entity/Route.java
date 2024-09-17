package com.example.gip_project_goudvissen.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "start_location_x")),
            @AttributeOverride(name = "y", column = @Column(name = "start_location_y"))
    })
    private Coordinate startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "end_location_x")),
            @AttributeOverride(name = "y", column = @Column(name = "end_location_y"))
    })
    private Coordinate endLocation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "route_id")
    private List<Waypoint> waypoints = new ArrayList<>();

    private double distance;

    private String transport;

    @OneToMany(mappedBy = "route")
    @JsonManagedReference
    private List<POI> pois = new ArrayList<>();

    @OneToMany(mappedBy = "route")
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordinate getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Coordinate startLocation) {
        this.startLocation = startLocation;
    }

    public Coordinate getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Coordinate endLocation) {
        this.endLocation = endLocation;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public List<POI> getPois() {
        return pois;
    }

    public void setPois(List<POI> pois) {
        this.pois = pois;
    }
}
