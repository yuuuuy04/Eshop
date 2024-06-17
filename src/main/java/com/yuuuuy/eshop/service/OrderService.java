package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.Order;
import com.yuuuuy.eshop.model.vo.OrderDetailVO;
import com.yuuuuy.eshop.model.vo.OrderInfoVO;
import com.yuuuuy.eshop.model.vo.OrderVO;

import java.util.List;

public interface OrderService extends IService<Order> {
    public List<Order> getListByUserId(Integer id);
    public List<OrderDetailVO> getGoodsListById(Integer id);
    public OrderInfoVO addOrder(String token, List<Integer> cartGoodsIds);
    public Boolean confirmOrder(String token, Integer orderId);
    public List<OrderDetailVO> getOrderDetail(String token, Integer orderId);
    public List<OrderVO> getOrderList(String token);
    public void updateStatus(Integer orderId);
}
