package com.pado.SpringBootrealpractice.domain;

import com.pado.SpringBootrealpractice.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // order가 연관관계 주인
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    // 비즈니스 로직//
    // 취소
    public void cancel(){
        getItem().addStock(count);
    }

    //전체 가격 조회
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
