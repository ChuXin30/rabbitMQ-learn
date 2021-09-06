package com.rabbitmq.hello.five;


import com.rabbitmq.client.Channel;
import com.rabbitmq.hello.util.MqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EmitLog {
    private static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws Exception {
        Channel channel = MqUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME , "fauout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME , "" , null , message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息："+message);
        }
    }

}
