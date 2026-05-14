package com.pos.restaurantpos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@javax.persistence.Table(name = "sys_user")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String realName;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Role {
        ADMIN, CASHIER
    }

    public enum Status {
        ENABLED, DISABLED
    }
}
