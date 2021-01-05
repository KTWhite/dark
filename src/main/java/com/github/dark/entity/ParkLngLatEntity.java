package com.github.dark.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yuanqu_lgt")
@Data
public class ParkLngLatEntity {
    @Id
    private Integer id;

    @Column(name = "park_name")
    private String parkName;

    @Column(name = "park_plate")
    private String parkPlate;

    @Column(name = "lng")
    private String lng;

    @Column(name = "lat")
    private String lat;

    @Column(name = "ranges")
    private String range;
}
