package com.keduit.entity;

import com.keduit.constant.ItemSellStatus;
import com.keduit.repository.ItemRepository;
import com.keduit.repository.MemberRepository;
import com.keduit.repository.OrderItemRepository;
import com.keduit.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setItemDetail("테스트 상품 설명");
        item.setPrice(10000);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(10000);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public Order createOrder() {
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderPrice(10000);
            orderItem.setCount(10);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }


    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderPrice(10000);
            orderItem.setCount(10);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        entityManager.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        Assertions.assertEquals(3, savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아 객체 삭제 테스트")
    public void orphanRemoval() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        entityManager.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void LazyLoadingTest() {
        Order order = createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        entityManager.flush();
        entityManager.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("orderItem.getOrder().getClass() = " + orderItem.getOrder().getClass());
        System.out.println("-------------------------------");
        LocalDateTime localDateTime = orderItem.getOrder().getOrderDate();
        System.out.println("-------------------------------");
        System.out.println("orderItem.getOrder().getOrderDate() = " + localDateTime);
    }
}
