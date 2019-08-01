package wjc.rabbitmq.rabbitmqapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:  死信队列 DLX
 * 消息变成死信的三种情况：
 * 	1.消息被拒绝 basic.reject/nack 并且 requeue=false
 * 	2.消息TTL(Time To Live)过期
 * 	3.队列达到最大长度
 *
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class DlxProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName="test_dlx_exchange";
        String rountingKey="dlx.save";

        //发送消息， 设置10秒的有效期
        for(int i=0;i<5;i++){
			AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.expiration("10000")
					.build();
			String msg="hello rabbitmq...ack"+i;
			channel.basicPublish(exchangeName,rountingKey,properties,msg.getBytes());
		}
    }
}
