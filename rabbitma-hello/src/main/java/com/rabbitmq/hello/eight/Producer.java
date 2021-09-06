package com.rabbitmq.hello.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.hello.util.MqUtils;

import java.nio.charset.StandardCharsets;

public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel =  MqUtils.getChannel();
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 0; i < 10; i++) {
            String message = "info "+i;
            channel.basicPublish(NORMAL_EXCHANGE , "zhangsan" , null , message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
