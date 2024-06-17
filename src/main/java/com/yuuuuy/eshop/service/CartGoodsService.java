package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.dto.CartGoodsDTO;
import com.yuuuuy.eshop.model.dto.CartToOrderDTO;
import com.yuuuuy.eshop.model.dto.UdPriceDTO;
import com.yuuuuy.eshop.model.entity.CartGoods;

import java.math.BigDecimal;
import java.util.List;

public interface CartGoodsService extends IService<CartGoods> {
    public Boolean addGoods(Integer cartId, Integer goodsId, BigDecimal goodsPrice, Integer amount);
    public List<CartGoodsDTO> getGoodsInfoById(Integer cartId);
    public List<UdPriceDTO> updatePrice(Integer goodsId, BigDecimal price);
    public BigDecimal delGoods(Integer cartId, Integer goodsId, Integer amount);
    public List<CartToOrderDTO> goodsInfoToOrd(Integer cartId, List<Integer> cartGoodsIds);
    public Boolean delGoodsByIds(Integer cartId, List<Integer> cartGoodsIds);
}
