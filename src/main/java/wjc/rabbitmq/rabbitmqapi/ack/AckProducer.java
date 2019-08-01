package wjc.rabbitmq.rabbitmqapi.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 生产端
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class AckProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName="test_ack_exchange";
        String rountingKey="ack.save";

        //发送消息

        for(int i=0;i<5;i++){
        	Map<String,Object> headers=new HashMap<>();
			headers.put("num",i);
			AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.headers(headers)
					.build();
			String msg="hello rabbitmq...ack"+i;
			channel.basicPublish(exchangeName,rountingKey,properties,msg.getBytes());
		}
    }
}
