package com.github.dark.entity;


import lombok.Data;

import javax.persistence.*;

@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer id;
    private String userName;
    private String passWord;
    private String phone;
}
