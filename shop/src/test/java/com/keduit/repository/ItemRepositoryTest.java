package com.keduit.repository;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import com.keduit.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세 정보");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(10000);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println("savedItem = " + savedItem);
    }

    @Test
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    private void createItemList() {
        for (int i = 1; i < 21; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("상세 정보" + i);
            if (i < 11) {
                item.setItemSellStatus(ItemSellStatus.SELL);
                item.setStockNumber(10000);
            } else {
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
                item.setStockNumber(0);
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트", "상세 정보");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println("item = " + item);

        }
    }

    @Test
    @DisplayName("@query를 이용한 상품조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("price").descending());
        List<Item> itemList = itemRepository.findByItemDetail("상세", pageable);
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemByNative("상세");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void testSelect() {
        this.createItemList();
        Long id = 10L;

        Optional<Item> result = itemRepository.findById(id);
        System.out.println("=============================");
        if (result.isPresent()) {
            Item item = result.get();
            System.out.println("item = " + item);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        this.createItemList();
        Long id = 40L;

        Item item = itemRepository.getOne(id);
        System.out.println("==========================");
        System.out.println("item = " + item);
    }

    @Test
    public void testUpdate() {
        Item item = Item.builder().id(31L).itemNm("수정된 상품명")
                .itemDetail("수정된 상세내용").price(25000).build();
        System.out.println("itemRepository.save(item) = " + itemRepository.save(item));
    }

    @Test
    public void testDelete() {
        Long id = 31L;
        itemRepository.deleteById(id);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> result = itemRepository.findAll(pageable);
        System.out.println("result = " + result);
        System.out.println(" ================================= ");
        for (Item item : result.getContent()) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Item> result = itemRepository.findAll(pageable);
        result.get().forEach(item -> {
            System.out.println("item = " + item);
        });
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        List<Item> list = queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "상세" + "%"))
                .orderBy(qItem.price.desc())
                .fetch();
        for (Item item : list) {
            System.out.println("item = " + item);
        }

    }

    @Test
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2() {
        this.createItemList();

        BooleanBuilder builder = new BooleanBuilder();
        QItem qItem = QItem.item;
        String itemDetail = "상세";
        int price = 10001;
        String itemSellStatus = "SELL";

        builder.and(qItem.itemDetail.like("%" + itemDetail + "%"));
        builder.and(qItem.price.gt(price));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)) {
            builder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> result = itemRepository.findAll(builder, pageable);

        System.out.println("전체 페이지 수 = " + result.getTotalPages());
        System.out.println("조회한 전체 상품 수 = " + result.getTotalElements());
        System.out.println("현재 페이지의 게시물 수 = " + result.getSize());
        System.out.println("현재 페이지 번호 = " + result.getNumber());
        System.out.println("content = " + result.getContent());

        List<Item> list = result.getContent();
        for(Item item : list) {
            System.out.println("item = " + item);
        }
    }


}