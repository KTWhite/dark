package com.github.dark.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yuanqu_industry")
@Data
public class ParkIndustryEntity {
    @Id
    private Integer id;

    @Column(name = "industry_name")
    private String industryName;

    @Column(name = "industry_code")
    private String industryCode;

    @Column(name = "industry_type")
    private String industryType;
}
