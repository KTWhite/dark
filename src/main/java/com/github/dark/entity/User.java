package com.github.dark.entity;


import lombok.Data;

import javax.persistence.*;

@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer id;

    @Column(name = "username")
    private String userName;

    @Column(name = "passWord")
    private String passWord;

    @Column(name = "phone")
    private String phone;
}
