package com.yuuuuy.eshop.consumer;

import com.yuuuuy.eshop.model.entity.Order;
import com.yuuuuy.eshop.service.OrderService;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(nameServer = "${rocketmq.name-server}",
        topic = "order-topic", consumerGroup = "${rocketmq.consumer.group}", messageModel = MessageModel.BROADCASTING)
public class OrderConsumerService implements RocketMQListener<Integer> {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(Integer orderId) {
        orderService.updateStatus(orderId);
    }
}
