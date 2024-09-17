package com.example.gip_project_goudvissen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class POI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "waypoint_x")),
            @AttributeOverride(name = "y", column = @Column(name = "waypoint_y"))
    })
    private Coordinate location;
    @NotNull(message = "Type cannot be null")
    private String type;
    private Boolean visible;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-poi")
    private User createdByUser;

    @OneToMany(mappedBy = "reviewForPOI", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnoreProperties("reviewForPOI")
    private List<Review> linkedReviews = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public List<Review> getLinkedReviews() {
        return linkedReviews;
    }

    public void setLinkedReviews(List<Review> linkedReviews) {
        this.linkedReviews = linkedReviews;
    }
}
