package com.keduit.service;

import com.keduit.constant.ItemSellStatus;
import com.keduit.dto.CartItemDTO;
import com.keduit.entity.CartItem;
import com.keduit.entity.Item;
import com.keduit.entity.Member;
import com.keduit.repository.CartItemRepository;
import com.keduit.repository.ItemRepository;
import com.keduit.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
@Transactional

public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setItemDetail("테스트 상품 설명");
        item.setPrice(10000);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("someone@example.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setItemId(item.getId());
        cartItemDTO.setCount(5);

        Long cartItemId = cartService.addCart(cartItemDTO, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(item.getId(), cartItem.getItem().getId());
        Assertions.assertEquals(cartItemDTO.getCount(), cartItem.getCount());

    }
}
