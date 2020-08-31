package com.ruigu.learn.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/7/29 10:48
 */
@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "type")
    private String type;








}
