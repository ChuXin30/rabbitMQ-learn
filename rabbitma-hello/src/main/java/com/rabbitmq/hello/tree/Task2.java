package com.rabbitmq.hello.tree;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.hello.util.MqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
消息不丢失、放回队列种重新消费
消费者处理慢可以重新消费（两个消费者一个消费快，一个消费慢，如果消费慢的消息丢失，可以
把消息交个处理快的消费者消费）
 */
public class Task2 {
    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
        //开启发布确认
        /*
        1.单个发布确认， 非常慢

         */
        boolean durable = true;
        //声明队列
        channel.queueDeclare(task_queue_name , durable , false , false , null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //设置生产者发送消息为持久化
            channel.basicPublish( "" , task_queue_name , MessageProperties.PERSISTENT_TEXT_PLAIN , message.getBytes(StandardCharsets.UTF_8) );
            System.out.println("生产者发出消息： " + message);
        }

    }
}
