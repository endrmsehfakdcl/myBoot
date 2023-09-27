package com.keduit.dto;

import com.keduit.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDTO {

    private static ModelMapper modelMapper = new ModelMapper();
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    public static ItemImgDTO of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }
}
