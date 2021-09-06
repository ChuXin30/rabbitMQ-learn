package com.rabbitmq.hello.eight;



/*
死信队列
 */


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.hello.util.MqUtils;

import java.util.HashMap;
import java.util.Map;


public class Consumer01 {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    //死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String DEAD_QUEUE = "dead_queue";

    public static final String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws Exception {

        Channel channel =  MqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE , BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE , BuiltinExchangeType.DIRECT);

        Map<String , Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "lisi");
//        arguments.put("x-message-ttl", 100000);
        arguments.put("x-max-length", 6);


        channel.queueDeclare(NORMAL_QUEUE , false , false , false, arguments);
        channel.queueDeclare(DEAD_QUEUE , false , false , false , null);

        channel.queueBind(NORMAL_QUEUE , NORMAL_EXCHANGE , "zhangsan");

        channel.queueBind(DEAD_QUEUE , DEAD_EXCHANGE , "lisi");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台打印接收到的消息"+message);
        };

        channel.basicConsume(NORMAL_QUEUE , true , deliverCallback , consumerTag -> { });






    }
}
