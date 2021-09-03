package com.rabbitmq.hello.four;

//开启发布确认
        /*
        1.单个发布确认， 非常慢
        2. 批量确认
        3. 异步批量确认
         */

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.hello.util.MqUtils;
import com.rabbitmq.hello.util.SleepUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    public static int count = 1000;

    public static void main(String[] args) throws Exception {
//        1.单个发布确认， 非常慢
//        ConfirmMessage.f1();//发布1000个单独确认，耗时 时间：478
//        2. 批量确认
//        ConfirmMessage.f2();//发布1000确认，耗时 时间：60


//        3. 异步批量确认
        ConfirmMessage.f3();//发布1000个单独确认，耗时 时间：32

    }

    public static void f1() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName , true , false, false , null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0 ; i < count ; i++){
            String message = i + " ";
            channel.basicPublish("" , queueName , null , message.getBytes(StandardCharsets.UTF_8));
            boolean flag =  channel.waitForConfirms();
            if(flag){
//                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("发布"+ count + "个单独确认，耗时 时间："+(end-begin));
    }

    public static void f2() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName , true , false, false , null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        //批量确认的长度
        int batchSize = 100;


        for (int i = 0 ; i < count ; i++){
            String message = i + " ";
            channel.basicPublish("" , queueName , null , message.getBytes(StandardCharsets.UTF_8));

            //判断100条确认一次
            if( i % batchSize == 0  ){
                channel.waitForConfirms();

//                System.out.println("消息发送成功");
            }

        }
        long end = System.currentTimeMillis();

        System.out.println("发布"+ count + "个单独确认，耗时 时间："+(end-begin));
    }

    public static void f3() throws Exception {
        Channel channel = MqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName , true , false, false , null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        /*
        线程安全有序的哈希表，适用与高并发的情况
         1.轻松的将序号与消息进行关联
         2.轻松批量删除条目 只要给到序号
         3. 支持高并发
         */

        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //批量确认的长度
        int batchSize = 100;

        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (tag , multiple) ->{
            //删除确认的消息
            System.out.println("确认的消息，"+tag);

            if(multiple){
                ConcurrentNavigableMap<Long , String> confirm =  outstandingConfirms.headMap(tag );//返回一个子集 删除小于tag的
                confirm.clear();
            }else {
                outstandingConfirms.remove(tag);
            }

        };

        //消息确认失败 回调函数
        /*
        1.消息的标识
        2.是否为批量确认
         */
        ConfirmCallback nackCallback = (tag , multiple) ->{
            System.out.println("未确认的消息，"+tag);
        };
        //准备消息的监听器 监听哪些消息成功 哪些消息失败
        /*
        1.监听哪些消息成功了
        2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback , nackCallback);

        //批量发送
        for (int i = 0 ; i < count ; i++){
            String message = i + " ";
            channel.basicPublish("" , queueName , null , message.getBytes(StandardCharsets.UTF_8));
            //1、此处记录下所有需要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo() , message);
        }

        //发布确认


        long end = System.currentTimeMillis();

        System.out.println("发布"+ count + "个单独确认，耗时 时间："+(end-begin));
    }

}
