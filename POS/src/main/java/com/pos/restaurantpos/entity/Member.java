package com.pos.restaurantpos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@javax.persistence.Table(name = "member")
public class Member extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String phone;

    @Column(nullable = false)
    private Integer points = 100;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ENABLED;

    public enum Status {
        ENABLED, DISABLED
    }
}
