package com.pado.SpringBootrealpractice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    //== 생성 메서드 ==//  복잡한 생성은 별도의 생성 메서드가 있으면 좋다. order가 주문상태까지 삭 생성
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    //주문 취소
    // 데이터가 변경되면 jpa가 자동으로 db에 업데이트 쿼리를 날린다. jpa의 최고 강점!
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가합니다.");
        }
        this.setStatus(OrderStatus.CANCEL); // 이렇게만 하고 db를 건들지 않았는데, state가 바뀌었다. -> jpa가 트랜젝션 커밋 시점에 더티체킹을 해서 알아서 바꿔놓는다.
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 두 개 주문할 수 있으니 각각의 아이템에 캔슬 날려주는 것
        }
    }

    // 조회로직
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
