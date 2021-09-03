package com.rabbitmq.hello.tree;

/*
消息
 */

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.hello.util.MqUtils;
import com.rabbitmq.hello.util.SleepUtils;

import java.io.IOException;

public class Work03 {
    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
        System.out.println("c1 等待接收消息处理的时间");

        //接收消息
        DeliverCallback deliverCallback = (tag , message) ->{

            SleepUtils.sleep(1);

            System.out.println("接收到的消息："+new String(message.getBody()));
            System.out.println();

            //手动应答
            /*
            1.消息的标记
            2.是否批量应答
             */
            channel.basicAck( message.getEnvelope().getDeliveryTag() , false);
        };

        //取消消息时回调
        CancelCallback cancelCallback = tag ->{
            System.out.println("消息中断");
        };

        //设置为不公平分发
        //预取值为5
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoACK = false;

        channel.basicConsume(task_queue_name , autoACK , deliverCallback , cancelCallback);
    }
}
