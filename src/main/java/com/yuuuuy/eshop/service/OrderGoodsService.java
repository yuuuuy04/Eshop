package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.OrderGoods;
import com.yuuuuy.eshop.model.vo.OrderDetailVO;

import java.util.List;

public interface OrderGoodsService extends IService<OrderGoods> {
    public List<OrderDetailVO> getGoodsIdsByOrderId(Integer orderId);

}
