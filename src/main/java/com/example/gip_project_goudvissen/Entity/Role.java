package com.example.gip_project_goudvissen.Entity;

import javax.persistence.*;

@Entity
public class Role {

    public static final String ROLE_ADMIN = "Admin";

    public static final String ROLE_USER = "User";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60)
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
