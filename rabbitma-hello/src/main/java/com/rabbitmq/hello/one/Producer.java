package com.rabbitmq.hello.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String QUEUE_NAME = "hello ";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.219.166");
        factory.setUsername("admin");
        factory.setPassword("171417");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /*
        生产一个队列
        1.队列名称
        2.是否持久化
        3.该队列是否只供一个消费者进行消费，是否进行消息共享，
        4.是否自动删除
        5.其他参数
         */

        channel.queueDeclare(QUEUE_NAME , false , false , false, null );

        String message = "hello word";

        /*
        发送一个消费
        1.发送到哪个交换机
        2.路由的key是哪一个
        3.其他参数
        4.消息
         */

        channel.basicPublish("" , QUEUE_NAME , null , message.getBytes(StandardCharsets.UTF_8) );
        System.out.println("发送完毕");
    }
}
