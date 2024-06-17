package com.yuuuuy.eshop.controller.user;

import com.yuuuuy.eshop.model.vo.CartGoodsVO;
import com.yuuuuy.eshop.model.vo.CartVO;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import com.yuuuuy.eshop.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/user/cart")
public class CartController {
    @Autowired
    CartService cartService;
    /**
     * 往购物车添加商品
     * @param token
     * @param goodsId
     * @param amount
     * @return
     */
    @PostMapping ("addgoods")
    public Boolean addGoods(@RequestHeader("token") String token, Integer goodsId, Integer amount){
        return cartService.addGoods(token, goodsId, amount);
    }

    /**
     * 删除购物车里的商品
     * @param token
     * @param goodsId
     * @param amount
     * @return
     */
    @PostMapping("deletegoods")
    public Boolean deleteGoods(@RequestHeader("token") String token, Integer goodsId, Integer amount){
        return cartService.delGoods(token, goodsId, amount);
    }

    /**
     * 获取购物车商品列表
     * @param token
     */
    @GetMapping("getcart")
    public CartVO getCart(@RequestHeader("token") String token){
        return cartService.getCart(token);
    }
}
