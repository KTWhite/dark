package com.github.dark.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yuanqu_ent_data")
@Data
public class ParkEntEntity {
    @Id
    @Column(name = "row_id")
    private Integer id;

    @Column(name = "ent_uid")
    private String entUid;

    @Column(name = "ent_name")
    private String entName;

    @Column(name = "ent_code")
    private String entCode;

    @Column(name = "ent_address")
    private String entAddress;

    @Column(name = "ent_reg_dt")
    private String entRegDt;

    @Column(name = "attrib02")
    private String attrib02;

    @Column(name = "ent_social_no")
    private String entSocialNo;

    @Column(name = "entCity")
    private String entCity;

    @Column(name = "entStatus")
    private String entStatus;

    @Column(name = "ent_reg_amount")
    private String entRegAmount;

    @Column(name = "industryphy")
    private String industryphy;

    @Column(name = "industrycoName")
    private String industrycoName;

    @Column(name = "yuanqu")
    private String yuanqu;

    @Column(name = "lng")
    private String lng;

    @Column(name = "lat")
    private String lat;

    @Column(name = "plate")
    private String plate;

    @Column(name = "sub_industry")
    private String subIndustry;

    @Column(name = "sub_industry_name")
    private String subIndustryName;
}
