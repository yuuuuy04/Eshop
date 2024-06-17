package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.Cart;
import com.yuuuuy.eshop.model.vo.CartVO;

import java.math.BigDecimal;

public interface CartService extends IService<Cart> {
    public Boolean createCart(Integer userId);
    public Boolean addGoods(String token, Integer goodsId, Integer amount);
    public CartVO getCart(String token);
    public Boolean updatePrice(Integer cartId, BigDecimal gapPrice);
    public Boolean delGoods(String token, Integer goodsId, Integer amount);
    public Integer getCartId(Integer userId);

}
