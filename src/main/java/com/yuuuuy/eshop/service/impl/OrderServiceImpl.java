package com.yuuuuy.eshop.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.OrderMapper;
import com.yuuuuy.eshop.model.dto.CartToOrderDTO;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.entity.Order;
import com.yuuuuy.eshop.model.entity.OrderGoods;
import com.yuuuuy.eshop.model.redisinfo.OrderInfo;
import com.yuuuuy.eshop.model.vo.OrderDetailVO;
import com.yuuuuy.eshop.model.vo.OrderGoodsVO;
import com.yuuuuy.eshop.model.vo.OrderInfoVO;
import com.yuuuuy.eshop.model.vo.OrderVO;
import com.yuuuuy.eshop.producer.OrderProduceService;
import com.yuuuuy.eshop.service.*;
import com.yuuuuy.eshop.utils.JWTUtils;
import com.yuuuuy.eshop.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final Map<Integer, Object> locks = new HashMap<>();
    @Autowired
    private OrderProduceService orderProduceService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartGoodsService cartGoodsService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

    private final Integer TIME_OUT = 1;
    @Override
    public List<Order> getListByUserId(Integer id) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, id);
        List<Order> orders = baseMapper.selectList(wrapper);
        return orders;
    }

    @Override
    public List<OrderDetailVO> getGoodsListById(Integer id) {
        //获取某个订单里的所有商品id、数量、总价(order_goods)
        List<OrderDetailVO> orders = orderGoodsService.getGoodsIdsByOrderId(id);
        //根据id列表获取商品信息
        ArrayList<Integer> list = new ArrayList<>();
        for(OrderDetailVO order : orders){
            list.add(order.getGoodsId());
        }
        HashMap<Integer, GoodsToOrderDTO> goodsInfo = goodsService.getGoodsInfoByids(list);
        for(OrderDetailVO order: orders){
            GoodsToOrderDTO goodsToOrderDTO = goodsInfo.get(order.getGoodsId());
            order.setName(goodsToOrderDTO.getName());
            order.setImgPath(goodsToOrderDTO.getImgPath());
        }
        return orders;
    }

    @Transactional
    @Override
    public OrderInfoVO addOrder(String token, List<Integer> cartGoodsIds) {
        //当订单为空时
        if(cartGoodsIds == null){
            throw new RuntimeException("没选中商品");
        }
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        Integer cartId = cartService.getCartId(userId);
        //获取并移除购物车商品
        List<CartToOrderDTO> cartToOrderDTOS = cartGoodsService.goodsInfoToOrd(cartId, cartGoodsIds);
        //TODO 异步
        cartGoodsService.delGoodsByIds(cartId, cartGoodsIds);
        //新建一个订单, 插入商品
        Snowflake snowflake = IdUtil.getSnowflake();
        long l = snowflake.nextId();
        Integer number = (int) l;
        Order order = new Order();
        order.setNumber(number);
        order.setUserId(userId);
        baseMapper.insert(order);
        //订单与商品进行关联
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getNumber, number)
                .select(Order::getId);
        Order selected = baseMapper.selectOne(wrapper);
        //保存并返回购物车商品信息和订单(状态为2)
        ArrayList<OrderGoodsVO> orderGoodsList = new ArrayList<>();
        BigDecimal totalPrices = new BigDecimal(0);
        for (CartToOrderDTO cartToOrderDTO : cartToOrderDTOS){
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setOrderId(selected.getId());
            orderGoods.setGoodsId(cartToOrderDTO.getGoodsId());
            orderGoods.setAmount(cartToOrderDTO.getAmount());
            orderGoods.setTotalPrices(cartToOrderDTO.getTotalPrices());
            totalPrices = totalPrices.add(cartToOrderDTO.getTotalPrices());
            //TODO 异步
            orderGoodsService.save(orderGoods);
            OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
            orderGoodsVO.setGoodsId(cartToOrderDTO.getGoodsId());
            orderGoodsVO.setAmount(cartToOrderDTO.getAmount());
            orderGoodsVO.setTotalPrices(cartToOrderDTO.getTotalPrices());
            orderGoodsVO.setPrice(cartToOrderDTO.getTotalPrices().divide(new BigDecimal(cartToOrderDTO.getAmount())));
            GoodsToOrderDTO goodsInfo = goodsService.getGoodsInfo(cartToOrderDTO.getGoodsId());
            orderGoodsVO.setImgPath(goodsInfo.getImgPath());
            orderGoodsVO.setName(goodsInfo.getName());
            orderGoodsList.add(orderGoodsVO);
        }
        //减少购物车总价
        //TODO 异步
        cartService.updatePrice(cartId, totalPrices.negate());
        //订单信息(订单号、订单商品id)存入redis
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsInfo(cartToOrderDTOS);
        orderInfo.setTotalPrices(totalPrices);
        redisUtils.setCacheObject(userId+"order:"+selected.getId(), orderInfo, TIME_OUT, TimeUnit.HOURS);
        selected.setTotalPrices(totalPrices);
        baseMapper.updateById(selected);
        //TODO 使用延迟队列,订单超时未支付时将订单状态改为2
        orderProduceService.sendOrderDelayMessage(selected.getId());
        //返回订单信息
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setOrderGoodsVOS(orderGoodsList);
        orderInfoVO.setOrderId(selected.getId());
        orderInfoVO.setTotalPrices(totalPrices);
        return orderInfoVO;
    }

    @Transactional
    @Override
    public Boolean confirmOrder(String token, Integer orderId) {
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        OrderInfo orderInfo = (OrderInfo) redisUtils.getCacheObject(userId + "order:" + orderId);
        //redis没找到订单信息
        if(Objects.isNull(orderInfo)){
            throw new RuntimeException("订单不存在或订单已过期");
        }
        //检查是否为未支付订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, orderId)
                .select(Order::getStatus);
        Order order = baseMapper.selectOne(wrapper);
        if(!order.getStatus().equals('2')){
            throw new RuntimeException("订单不属于未支付的订单");
        }
        for (CartToOrderDTO cartToOrderDTO : orderInfo.getGoodsInfo()) {
            Integer goodsId = cartToOrderDTO.getGoodsId();
            synchronized (getLock(goodsId)) {
                Integer sku = goodsService.getSKU(goodsId);
                // 库存不足
                if (cartToOrderDTO.getAmount() > sku) {
                    throw new RuntimeException("库存不足");
                }
                // 减少库存
                goodsService.subSKU(goodsId, cartToOrderDTO.getAmount());
            }
        }
//        for(CartToOrderDTO cartToOrderDTO : orderInfo.getGoodsInfo()){
//            Integer goodsId = cartToOrderDTO.getGoodsId();
//            Integer sku = goodsService.getSKU(goodsId);
//            //库存不足
//            if(cartToOrderDTO.getAmount() > sku){
//                throw new RuntimeException("库存不足");
//            }
//        }
        ////TODO 异步 分布式锁
        //减少库存
//        for(CartToOrderDTO cartToOrderDTO : orderInfo.getGoodsInfo()){
//            goodsService.subSKU(cartToOrderDTO.getGoodsId(), cartToOrderDTO.getAmount());
//        }
        BigDecimal balance = userService.getBalance(userId);
        //账号余额不足
        if(balance.compareTo(orderInfo.getTotalPrices()) == -1){
            throw new RuntimeException("账户余额不足");
        }
        //扣钱
        userService.payOrder(userId, orderInfo.getTotalPrices());
        //清除redis数据
        redisUtils.deleteObject(userId + "order:" + orderId);
        //改订单状态(1)
        updateStatus(orderId);
        return true;
    }

    @Override
    public List<OrderDetailVO> getOrderDetail(String token, Integer orderId) {
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        //检查该订单是否属于该用户
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
                .eq(Order::getId, orderId)
                .select(Order::getId);
        Order order = baseMapper.selectOne(wrapper);
        if (Objects.isNull(order)){
            throw new RuntimeException("该账户查不到对应订单");
        }
        List<OrderDetailVO> orderDetailVOList = getGoodsListById(orderId);
        return orderDetailVOList;
    }

    @Override
    public List<OrderVO> getOrderList(String token) {
        //获取用户id
        String id = JWTUtils.getID(token);
        int userId = Integer.parseInt(id);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
                .select(Order::getId, Order::getTotalPrices, Order::getStatus);
        List<OrderVO> collect = baseMapper.selectList(wrapper)
                .stream()
                .map(entity -> {
                    OrderVO orderVO = new OrderVO();
                    orderVO.setId(entity.getId());
                    orderVO.setTotalPrices(entity.getTotalPrices());
                    orderVO.setStatus(entity.getStatus());
                    return orderVO;
                })
                .collect(Collectors.toList());
        return collect;
    }
    //修改订单状态
    @Override
    public void updateStatus(Integer orderId){
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, orderId)
                .select(Order::getStatus);
        Order order = baseMapper.selectOne(wrapper);
        if(order.getStatus().equals('2')){
            Order upd = new Order();
            upd.setId(orderId);
            upd.setStatus('0');
            baseMapper.updateById(upd);
        }
    }
    private Object getLock(Integer goodsId) {
        synchronized (locks) {
            return locks.computeIfAbsent(goodsId, k -> new Object());
        }
    }
}
