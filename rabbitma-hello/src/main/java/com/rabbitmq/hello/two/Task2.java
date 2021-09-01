package com.rabbitmq.hello.two;

import com.rabbitmq.client.Channel;
import com.rabbitmq.hello.util.MqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task2 {
    public static final String QUEUE_NAME = "hello ";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
        /*
        生产一个队列
        1.队列名称
        2.是否持久化
        3.该队列是否只供一个消费者进行消费，是否进行消息共享，
        4.是否自动删除
        5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        /*
        发送一个消费
        1.发送到哪个交换机
        2.路由的key是哪一个
        3.其他参数
        4.消息
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("" , QUEUE_NAME , null , message.getBytes(StandardCharsets.UTF_8) );
            System.out.println("发送完毕");
        }

    }
}
