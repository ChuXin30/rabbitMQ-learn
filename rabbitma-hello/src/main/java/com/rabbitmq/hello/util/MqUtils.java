package com.rabbitmq.hello.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqUtils {
    public static Channel getChannel() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.219.166");
        factory.setUsername("admin");
        factory.setPassword("171417");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        return channel;
    }
}
