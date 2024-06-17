package com.yuuuuy.eshop.model.factory;

import com.yuuuuy.eshop.model.dto.CartGoodsDTO;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.vo.CartGoodsVO;

public class CartGoodsVOFactory {
    public static CartGoodsVO createCartGoodsVO(CartGoodsDTO cartGoodsDTO, GoodsToOrderDTO goodsToOrderDTO) {
        CartGoodsVO cartGoodsVO = new CartGoodsVO();
        cartGoodsVO.setId(cartGoodsDTO.getId());
        cartGoodsVO.setGoodsId(cartGoodsDTO.getGoodsId());
        cartGoodsVO.setTotalPrices(cartGoodsDTO.getTotalPrices());
        cartGoodsVO.setPrice(cartGoodsDTO.getPrice());
        cartGoodsVO.setAmount(cartGoodsDTO.getAmount());
        cartGoodsVO.setImgPath(goodsToOrderDTO.getImgPath());
        cartGoodsVO.setName(goodsToOrderDTO.getName());
        return cartGoodsVO;
    }
}
