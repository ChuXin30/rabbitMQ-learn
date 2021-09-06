package com.rabbitmq.hello.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.hello.util.MqUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel =  MqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台打印接收到的消息"+message);
        };

        channel.basicConsume(DEAD_QUEUE , true , deliverCallback , consumerTag -> { });
    }
}
