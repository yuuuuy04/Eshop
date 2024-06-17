package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.CartMapper;
import com.yuuuuy.eshop.model.dto.CartGoodsDTO;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.entity.Cart;
import com.yuuuuy.eshop.model.factory.CartGoodsVOFactory;
import com.yuuuuy.eshop.model.vo.CartGoodsVO;
import com.yuuuuy.eshop.model.vo.CartVO;
import com.yuuuuy.eshop.service.CartGoodsService;
import com.yuuuuy.eshop.service.CartService;
import com.yuuuuy.eshop.service.GoodsService;
import com.yuuuuy.eshop.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    CartGoodsService cartGoodsService;
    @Lazy
    @Autowired
    GoodsService goodsService;
    @Override
    public Boolean createCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        baseMapper.insert(cart);
        return true;
    }

    @Override
    public Boolean addGoods(String token, Integer goodsId, Integer amount) {
        //从token中获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        //查询购物车id
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Cart::getId)
                .eq(Cart::getUserId, userId);
        Cart cart = baseMapper.selectOne(wrapper);
        //获取商品价格
        BigDecimal goodsPrice = goodsService.getGoodsPrice(goodsId);
        //往购物车中加入商品
        //TODO 验证结果
        //TODO 异步
        cartGoodsService.addGoods(cart.getId(), goodsId, goodsPrice, amount);
        //TODO 修改购物车总价
        BigDecimal decimal = new BigDecimal(amount);
        BigDecimal totalPrices = goodsPrice.multiply(decimal);
        this.updatePrice(cart.getId(), totalPrices);
        return true;
    }

    @Override
    public CartVO getCart(String token) {
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        //获取购物车id
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Cart::getId, Cart::getTotalPrices)
                .eq(Cart::getUserId, userId);
        Cart cart = baseMapper.selectOne(wrapper);
        Integer cartId = cart.getId();
        //获取购物车商品id列表,数量和总金额
        List<CartGoodsDTO> cGoodsInfo = cartGoodsService.getGoodsInfoById(cartId);
        List<Integer> idList = new ArrayList<>();
        for(CartGoodsDTO cartGoodsDTO : cGoodsInfo){
            idList.add(cartGoodsDTO.getGoodsId());
        }
        //获取商品信息
        HashMap<Integer, GoodsToOrderDTO> goodsInfoByids = goodsService.getGoodsInfoByids(idList);
        List<CartGoodsVO> cartGoodsList = new ArrayList<>();
        for(CartGoodsDTO cartGoodsDTO : cGoodsInfo){
            GoodsToOrderDTO goodsToOrderDTO = goodsInfoByids.get(cartGoodsDTO.getGoodsId());
            CartGoodsVO cartGoodsVO = CartGoodsVOFactory.createCartGoodsVO(cartGoodsDTO, goodsToOrderDTO);
            cartGoodsList.add(cartGoodsVO);
        }
        CartVO cartVO = new CartVO();
        cartVO.setList(cartGoodsList);
        cartVO.setTotalPrices(cart.getTotalPrices());
        return cartVO;
    }

    @Override
    public Boolean updatePrice(Integer cartId, BigDecimal price) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Cart::getTotalPrices)
                .eq(Cart::getId, cartId);
        Cart selectedOne = baseMapper.selectOne(wrapper);
        BigDecimal totalPrices = selectedOne.getTotalPrices().add(price);
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setTotalPrices(totalPrices);
        baseMapper.updateById(cart);
        return true;
    }

    @Override
    public Boolean delGoods(String token, Integer goodsId, Integer amount) {
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        //获取购物车id
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Cart::getId, Cart::getTotalPrices)
                .eq(Cart::getUserId, userId);
        Cart cart = baseMapper.selectOne(wrapper);
        Integer cartId = cart.getId();
        //修改购物车商品的数量、价格
        BigDecimal decimal = cartGoodsService.delGoods(cartId, goodsId, amount);
        if (Objects.isNull(decimal)){
            return false;
        }
        //修改购物车总价
        Cart updCart = new Cart();
        updCart.setId(cartId);
        BigDecimal subtracted = cart.getTotalPrices().subtract(decimal);
        updCart.setTotalPrices(subtracted);
        baseMapper.updateById(updCart);
        return true;
    }

    @Override
    public Integer getCartId(Integer userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Cart::getId)
                .eq(Cart::getUserId, userId);
        Cart cart = baseMapper.selectOne(wrapper);
        Integer cartId = cart.getId();
        return cartId;
    }
}
