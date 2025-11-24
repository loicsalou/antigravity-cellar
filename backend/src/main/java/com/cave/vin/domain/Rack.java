package com.cave.vin.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Rack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type; // METAL, POLYSTYRENE, WOOD, SHELF

    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cellar_id")
    private Cellar cellar;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL)
    private List<Bottle> bottles = new ArrayList<>();

    public Rack() {
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Cellar getCellar() {
        return cellar;
    }

    public void setCellar(Cellar cellar) {
        this.cellar = cellar;
    }

    public List<Bottle> getBottles() {
        return bottles;
    }

    public void setBottles(List<Bottle> bottles) {
        this.bottles = bottles;
    }
}
