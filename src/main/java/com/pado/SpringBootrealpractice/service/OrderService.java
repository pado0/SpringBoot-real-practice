package com.pado.SpringBootrealpractice.service;

import com.pado.SpringBootrealpractice.domain.*;
import com.pado.SpringBootrealpractice.domain.item.Item;
import com.pado.SpringBootrealpractice.repository.ItemRepository;
import com.pado.SpringBootrealpractice.repository.MemberRepository;
import com.pado.SpringBootrealpractice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) { // 누가 무엇을 얼마나 주문할지,  // 여기서 멤버가 넘어와버리면 jpa와 관계 없는 멤버가 넘어와버리는 것!! id만 넘겨주자
        //엔티티 조회
        Member member = memberRepository.findOne(memberId); // 회원 선택해야해서 멤버값 꺼냄
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 원래 상품 생성, 주문 생성을 각각 레포지토리에 생성하고 주문 저장을 해줬는데 그렇지 않고있다. 이유는 order entity의 cascade all 때문
        // order를 persist 하면 orderitem, delivery를 다 persist 하도록 설정해놓음
        // 추가로, 생성시 new 로 하지 않고 생성메서드를 사용하는 이유는 기능의 집중, 유지보수성 떄문이다.
        // 파일마다 new Order 있으면 유지보수하기 쉽지 않아. 다 막아야할 것.
        // 주문할 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();

    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    //주문 검색
   public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
