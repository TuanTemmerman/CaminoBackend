package com.example.gip_project_goudvissen.Entity;

import javax.persistence.*;

@Entity
public class Waypoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "waypoint_x")),
            @AttributeOverride(name = "y", column = @Column(name = "waypoint_y"))
    })
    private Coordinate coordinate;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;


}
