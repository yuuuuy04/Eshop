package com.yuuuuy.eshop.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProduceService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    //延迟一小时后改变支付状态
    public void sendOrderDelayMessage(Integer orderId){
        rocketMQTemplate.syncSend("order-topic", MessageBuilder.withPayload(orderId).build(), 3000, 17);
    }
}
