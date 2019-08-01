package wjc.rabbitmq.rabbitmqapi.limit;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: 生产端
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class QosProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName="test_qos_exchange";
        String rountingKey="qos.save";

        //发送消息
        String msg="hello rabbitmq...qos";
        for(int i=0;i<5;i++){
			channel.basicPublish(exchangeName,rountingKey,null,msg.getBytes());
		}
    }
}
