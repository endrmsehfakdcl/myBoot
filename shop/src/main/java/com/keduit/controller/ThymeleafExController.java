package com.keduit.controller;

import com.keduit.dto.ItemDTO;
import com.keduit.entity.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafEx01(Model model) {
        model.addAttribute("data", "타임리프 예제입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafEx02(Model model) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemDetail("상품 상세 설명");
        itemDTO.setItemNm("테스트 상품1");
        itemDTO.setPrice(10000);
        itemDTO.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDTO", itemDTO);
        return "thymeleafEx/thymeleafEx02";

    }

    @GetMapping("/ex03")
    public String thymeleafEx03(Model model) {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for (int i = 0; i < 21; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId((long) i);
            itemDTO.setItemNm("테스트 상품" + i);
            itemDTO.setItemDetail("상품 상세 설명" + i);
            itemDTO.setPrice(1000 * i);
            if (i < 11) {
                itemDTO.setSellStatCd("SELL");
            } else {
                itemDTO.setSellStatCd("SOLD_OUT");
            }
            itemDTO.setRegTime(LocalDateTime.now());
            itemDTO.setUpdateTime(LocalDateTime.now());

            itemDTOList.add(itemDTO);
        }
        model.addAttribute("itemDTOList", itemDTOList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafEx04(Model model) {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for (int i = 0; i < 21; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemNm("테스트 상품" + i);
            itemDTO.setItemDetail("상품 상세 설명" + i);
            itemDTO.setPrice(1000 * i);
            if (i < 11) {
                itemDTO.setSellStatCd("SELL");
            } else {
                itemDTO.setSellStatCd("SOLD_OUT");
            }
            itemDTO.setRegTime(LocalDateTime.now());
            itemDTO.setUpdateTime(LocalDateTime.now());

            itemDTOList.add(itemDTO);
        }
        model.addAttribute("itemDTOList", itemDTOList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05() {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);

        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07() {
        return "thymeleafEx/thymeleafEx07";
    }

    @GetMapping(value = "/exInline")
    public String exInline(RedirectAttributes redirectAttributes) {
        System.out.println("exInline---------------------------");
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemDetail("상품 상세 설명");
        itemDTO.setItemNm("테스트 상품1");
        itemDTO.setPrice(10000);
        itemDTO.setRegTime(LocalDateTime.now());

        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("itemDTO", itemDTO);

        return "redirect:/thymeleaf/thymeleafEx08";
    }

    @GetMapping(value = "/thymeleafEx08")
    public String ex08() {
        System.out.println("ex08");
        return "thymeleafEx/thymeleafEx08";
    }

}
