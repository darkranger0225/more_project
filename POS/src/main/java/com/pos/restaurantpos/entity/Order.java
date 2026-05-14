package com.pos.restaurantpos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@javax.persistence.Table(name = "orders")
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(name = "table_id", insertable = false, updatable = false)
    private Long tableId;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Integer personCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    private LocalDateTime payTime;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;

    @Column(name = "cashier_id", insertable = false, updatable = false)
    private Long cashierId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @Column(nullable = false)
    private Integer pointsUsed = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal pointsDiscount = BigDecimal.ZERO;

    @Column(length = 500)
    private String remark;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    public enum Status {
        PENDING, COOKING, SERVED, PAID, CANCELLED
    }

    public enum PayMethod {
        CASH, WECHAT, ALIPAY
    }
}
