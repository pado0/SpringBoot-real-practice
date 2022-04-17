package com.pado.SpringBootrealpractice.service;

import com.pado.SpringBootrealpractice.domain.Address;
import com.pado.SpringBootrealpractice.domain.Member;
import com.pado.SpringBootrealpractice.domain.Order;
import com.pado.SpringBootrealpractice.domain.OrderStatus;
import com.pado.SpringBootrealpractice.domain.item.Book;
import com.pado.SpringBootrealpractice.domain.item.Item;
import com.pado.SpringBootrealpractice.exception.NoEnoughStockException;
import com.pado.SpringBootrealpractice.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        //Given, 정보를 입력받을 수 없는 것들만 임의 입력으로 쳐준다
        Member member = createMember();
        Item item = createBook("jpa book1", 10000, 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        //assertEquals("ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(OrderStatus.ORDER, getOrder.getStatus()); // 주문상태
        assertEquals(1, getOrder.getOrderItems().size()); // 주문개수
        assertEquals(2* 10000, getOrder.getTotalPrice()); //주문가격
        assertEquals(8, item.getStockQuantity()); // 주문 개수
    }

    // 재고수량 초과
    @Test(expected = NoEnoughStockException.class)
    public void 재고수량_초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("jpa book1", 10000, 10);
        int orderCount = 11;

        orderService.order(member.getId(), item.getId(), orderCount);

        fail("재고수량 부족 예외가 발생해야한다");

    }

    // 주문취소
    @Test
    public void 주문취소(){
        //given
        Member member = createMember();
        Item item = createBook("asd", 10000, 32);
        int orderCount = 10;
        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
    }

    private Member createMember() {

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "용산", "12393"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

}