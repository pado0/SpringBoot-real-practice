package com.pado.SpringBootrealpractice.controller;

import com.pado.SpringBootrealpractice.domain.Member;
import com.pado.SpringBootrealpractice.domain.Order;
import com.pado.SpringBootrealpractice.domain.OrderSearch;
import com.pado.SpringBootrealpractice.domain.item.Item;
import com.pado.SpringBootrealpractice.service.ItemService;
import com.pado.SpringBootrealpractice.service.MemberService;
import com.pado.SpringBootrealpractice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.fineMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count); // 컨트롤러에서는 딱 식별자만 넘기게 하도록. 엔티티를 서비스 안에서 찾도록 하는게 더 깔끔함.
        // 엔티티를 찾는것도 트랜젝션 안에서 진행해야 영속될 수 있으므로, 가급적 모든 로직을 서비스에서 짜자.

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch); // 서비스가 리퍼지토리를 위임해서 조회하는 정도면, 바로 리포지토리로 붙어도 됨.
        model.addAttribute("orders", orders);
        System.out.println("찍어보자 orders = " + orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
