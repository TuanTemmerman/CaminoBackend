package com.example.gip_project_goudvissen.DTO;

import com.example.gip_project_goudvissen.Entity.Coordinate;

public class POIDTO {

    private String name;

    private Coordinate location;

    private String type;

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
}
