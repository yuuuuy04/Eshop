package com.yuuuuy.eshop.controller.admin;

import com.yuuuuy.eshop.model.entity.Order;
import com.yuuuuy.eshop.model.vo.OrderDetailVO;
import com.yuuuuy.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('manage_order')")
@RequestMapping("admin/ordermanager")
public class ManageOrderController {
    @Autowired
    OrderService orderService;

    /**
     * 根据用户id获取对应的订单列表
     * @param id
     * @return
     */
    @GetMapping("getorder")
    public List<Order> getOrderById(Integer id){
        return orderService.getListByUserId(id);
    }

    /**
     * 根据订单id获取订单详细信息
     * @param id
     * @return
     */
    @GetMapping("getdetail")
    public List<OrderDetailVO> getListByBatchIds(Integer id){
        return orderService.getGoodsListById(id);
    }
}
