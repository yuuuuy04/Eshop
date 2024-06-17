package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.OrderGoodsMapper;
import com.yuuuuy.eshop.model.entity.OrderGoods;
import com.yuuuuy.eshop.model.vo.OrderDetailVO;
import com.yuuuuy.eshop.service.OrderGoodsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements OrderGoodsService {
    @Override
    public List<OrderDetailVO> getGoodsIdsByOrderId(Integer orderId){
        LambdaQueryWrapper<OrderGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderGoods::getOrderId, orderId)
                .select(OrderGoods::getGoodsId, OrderGoods::getAmount, OrderGoods::getTotalPrices);
        List<OrderDetailVO> collect = baseMapper.selectList(wrapper)
                .stream()
                .map(entity -> {
                    OrderDetailVO orderDetailVO = new OrderDetailVO();
                    orderDetailVO.setGoodsId(entity.getGoodsId());
                    orderDetailVO.setAmount(entity.getAmount());
                    orderDetailVO.setTotalPrices(entity.getTotalPrices());
                    orderDetailVO.setPrice(entity.getTotalPrices().divide(new BigDecimal(entity.getAmount())));
                    return orderDetailVO;
                })
                .collect(Collectors.toList());
        return collect;
    }
}
