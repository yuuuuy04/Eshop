package com.yuuuuy.eshop.controller.user;

import com.yuuuuy.eshop.model.vo.OrderDetailVO;
import com.yuuuuy.eshop.model.vo.OrderInfoVO;
import com.yuuuuy.eshop.model.vo.OrderVO;
import com.yuuuuy.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @PostMapping("add")
    public OrderInfoVO addOrder(@RequestHeader("token") String token, @RequestBody List<Integer> cartGoodsIds){
        return orderService.addOrder(token, cartGoodsIds);
    }
    @PostMapping("confirm")
    public Boolean confirmOrder(@RequestHeader("token") String token, Integer orderId){
        return orderService.confirmOrder(token, orderId);
    }
    @GetMapping("list")
    public List<OrderVO> getOrderList(@RequestHeader("token") String token){
        return orderService.getOrderList(token);
    }

    @GetMapping("orderdetail")
    public List<OrderDetailVO> getOrderDetail(@RequestHeader("token") String token, Integer orderId){
        return orderService.getOrderDetail(token, orderId);
    }

}
