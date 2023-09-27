package com.keduit.dto;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {
    private static ModelMapper modelMapper = new ModelMapper();
    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private Integer stockNumber;
    private ItemSellStatus itemSellStatus;
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    public static ItemFormDTO of(Item item) {
        return modelMapper.map(item, ItemFormDTO.class);

    }

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

}
