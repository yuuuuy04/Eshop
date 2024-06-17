package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.CartGoodsMapper;
import com.yuuuuy.eshop.model.dto.CartGoodsDTO;
import com.yuuuuy.eshop.model.dto.CartToOrderDTO;
import com.yuuuuy.eshop.model.dto.UdPriceDTO;
import com.yuuuuy.eshop.model.entity.CartGoods;
import com.yuuuuy.eshop.service.CartGoodsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartGoodsServiceImpl extends ServiceImpl<CartGoodsMapper, CartGoods> implements CartGoodsService {
    @Override
    public Boolean addGoods(Integer cartId, Integer goodsId, BigDecimal goodsPrice, Integer amount) {
        //查看购物车中是否有该商品存在, 有 数量+amount, 无 新增一行
        LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartGoods::getCartId, cartId)
                .eq(CartGoods::getGoodsId, goodsId);
        if(baseMapper.exists(wrapper)){
            //商品存在, 改数量, 改总价
            wrapper.select(CartGoods::getId, CartGoods::getAmount, CartGoods::getTotalPrices);
            CartGoods cartGoods = baseMapper.selectOne(wrapper);
            cartGoods.setAmount(cartGoods.getAmount() + amount);
            BigDecimal decimal = new BigDecimal(amount);
            BigDecimal totalPrices = goodsPrice.multiply(decimal);
            cartGoods.setTotalPrices(cartGoods.getTotalPrices().add(totalPrices));
            baseMapper.updateById(cartGoods);
        }else {
            //商品不存在, 新添一个商品
            CartGoods cartGoods = new CartGoods();
            cartGoods.setCartId(cartId);
            cartGoods.setGoodsId(goodsId);
            BigDecimal decimal = new BigDecimal(amount);
            BigDecimal totalPrices = goodsPrice.multiply(decimal);
            cartGoods.setTotalPrices(totalPrices);
            cartGoods.setAmount(amount);
            baseMapper.insert(cartGoods);
        }
        return true;
    }

    @Override
    public List<CartGoodsDTO> getGoodsInfoById(Integer cartId) {
        LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartGoods::getCartId, cartId)
                .select(CartGoods::getId, CartGoods::getGoodsId, CartGoods::getAmount, CartGoods::getTotalPrices);
        List<CartGoodsDTO> list = baseMapper.selectList(wrapper)
                .stream()
                .map(entity -> {
                    CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
                    cartGoodsDTO.setId(entity.getId());
                    cartGoodsDTO.setGoodsId(entity.getGoodsId());
                    cartGoodsDTO.setAmount(entity.getAmount());
                    cartGoodsDTO.setTotalPrices(entity.getTotalPrices());
                    BigDecimal decimal = new BigDecimal(entity.getAmount());
                    cartGoodsDTO.setPrice(entity.getTotalPrices().divide(decimal));
                    return cartGoodsDTO;
                })
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 查出所有包含该物品的购物车id,并修改购物车商品价格, 返回物品价格、数量和购物车id
     * @param goodsId
     * @param price
     * @return
     */
    @Override
    public List<UdPriceDTO> updatePrice(Integer goodsId, BigDecimal price) {
        LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartGoods::getGoodsId, goodsId)
                .select(CartGoods::getTotalPrices, CartGoods::getAmount, CartGoods::getCartId, CartGoods::getId);
        List<UdPriceDTO> collect = baseMapper.selectList(wrapper)
                .stream()
                .map(entity -> {
                    //修改总价格
                    CartGoods cartGoods = new CartGoods();
                    cartGoods.setId(entity.getId());
                    Integer amount = entity.getAmount();
                    BigDecimal decimal = new BigDecimal(amount);
                    cartGoods.setTotalPrices(price.multiply(decimal));
                    baseMapper.updateById(cartGoods);
                    //返回物品价格、数量和购物车id
                    UdPriceDTO priceDTO = new UdPriceDTO();
                    priceDTO.setAmount(entity.getAmount());
                    priceDTO.setCartId(entity.getCartId());
                    priceDTO.setPrePrice(entity.getTotalPrices().divide(decimal));
                    return priceDTO;
                })
                .collect(Collectors.toList());

        return collect;
    }

    /**
     *返回cartid对应的差价
     * @param cartId
     * @param goodsId
     * @param amount
     * @return
     */
    @Override
    public BigDecimal delGoods(Integer cartId, Integer goodsId, Integer amount) {
        if(amount <= 0){
            throw new RuntimeException("删除失败");
        }
        LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CartGoods::getId, CartGoods::getTotalPrices, CartGoods::getAmount)
                .eq(CartGoods::getCartId, cartId)
                .eq(CartGoods::getGoodsId, goodsId);
        CartGoods cartGoods = baseMapper.selectOne(wrapper);
        if(cartGoods.getAmount() < amount){
            throw new RuntimeException("删除失败");
        }
        if (cartGoods.getAmount() == amount){
            baseMapper.deleteById(cartGoods.getId());
            return cartGoods.getTotalPrices();
        }
        Integer goodsAmount = cartGoods.getAmount();
        BigDecimal decimal = new BigDecimal(goodsAmount);
        BigDecimal price = cartGoods.getTotalPrices().divide(decimal);
        BigDecimal num = new BigDecimal(amount);
        BigDecimal total = price.multiply(num);
        //修改购物车商品表数据
        CartGoods updGoods = new CartGoods();
        updGoods.setId(cartGoods.getId());
        BigDecimal totalPrices = cartGoods.getTotalPrices();
        BigDecimal subtracted = totalPrices.subtract(total);
        updGoods.setTotalPrices(subtracted);
        updGoods.setAmount(goodsAmount - amount);
        baseMapper.updateById(updGoods);
        return total;
    }

    @Override
    public List<CartToOrderDTO> goodsInfoToOrd(Integer cartId, List<Integer> cartGoodsIds) {
        ArrayList<CartToOrderDTO> list = new ArrayList<>();
        for (Integer cartGoodsId : cartGoodsIds){
            LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CartGoods::getGoodsId, CartGoods::getAmount, CartGoods::getTotalPrices)
                    .eq(CartGoods::getCartId, cartId)
                    .eq(CartGoods::getId, cartGoodsId);
            CartGoods cartGoods = baseMapper.selectOne(wrapper);
            CartToOrderDTO cartToOrderDTO = new CartToOrderDTO();
            cartToOrderDTO.setGoodsId(cartGoods.getGoodsId());
            cartToOrderDTO.setAmount(cartGoods.getAmount());
            cartToOrderDTO.setTotalPrices(cartGoods.getTotalPrices());
            list.add(cartToOrderDTO);
        }
        return list;
    }

    @Override
    public Boolean delGoodsByIds(Integer cartId, List<Integer> cartGoodsIds) {
        for (Integer cartGoodsId : cartGoodsIds){
            LambdaQueryWrapper<CartGoods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CartGoods::getCartId, cartId)
                    .eq(CartGoods::getId, cartGoodsId);
            baseMapper.delete(wrapper);
        }
        return true;
    }
}
