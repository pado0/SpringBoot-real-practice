package com.pado.SpringBootrealpractice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 명이 "member_id", member랑 order는 양방향 의존관계. 여기서 변경하면 반영됨
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 엔티티 당 각각 Persist를 호출해야하는데, 케스케이드 설정하면 order만 persist 하면 된다.
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // java8에서는 LocalDateTime으로만 설정하면 하이버네이트가ㅒ알아서 해줌

    @Enumerated(EnumType.STRING) // ORDINAL EnumType는 절대 쓰면안된다.
    private OrderStatus status; //주문상태 (ORDER, CANCEL)

    //연관관계 메서드, 양방향 연관관계 넣어주는 메서드, 양방향 편의 메서드//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
