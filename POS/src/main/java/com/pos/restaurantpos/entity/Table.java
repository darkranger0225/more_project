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
@javax.persistence.Table(name = "restaurant_table")
public class Table extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String tableNo;

    @Column(length = 50)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 50)
    private String area;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Integer sortOrder;

    public enum Status {
        FREE, OCCUPIED
    }
}
