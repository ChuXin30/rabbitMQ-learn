package com.rabbitmq.hello.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.hello.util.MqUtils;
import org.omg.CORBA.MARSHAL;

//一个工作线程
public class Worker01 {
    public static final String QUEUE_NAME = "hello ";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();

        //接收消息
        DeliverCallback deliverCallback = (tag , message) ->{
            System.out.println("接收到的消息："+new String(message.getBody()));
            System.out.println();
        };

        //取消消息时回调
        CancelCallback cancelCallback = tag ->{
            System.out.println("消息中断");
        };

        System.out.println("c2启动");
        //接收消息
        channel.basicConsume(QUEUE_NAME , true , deliverCallback , cancelCallback);
    }
}
