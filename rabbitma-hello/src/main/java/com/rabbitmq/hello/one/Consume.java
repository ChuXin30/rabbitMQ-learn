package com.rabbitmq.hello.one;

import com.rabbitmq.client.*;
import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consume {
    public static final String QUEUE_NAME = "hello ";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.219.166");
        factory.setUsername("admin");
        factory.setPassword("171417");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //接收消息
        DeliverCallback deliverCallback = ( tag , message) ->{
            System.out.println(new String(message.getBody()));
            System.out.println("11111111111");
        };

        //取消消息时回调
        CancelCallback cancelCallback = tag ->{
            System.out.println("消息中断");
        };

        /*
        消费者接收消息
        1.消费哪个队列
        2.消费成功之后是否要自动应答
        3.消费者未成功消费回调
        4.消费者取消消费的回调
         */

        channel.basicConsume(QUEUE_NAME , true , deliverCallback , cancelCallback);

    }

}
