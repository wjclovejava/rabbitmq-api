package wjc.rabbitmq.rabbitmqapi.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 死信队列
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class DlxConsumer {

    public static void main(String[] args) throws Exception {
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.192.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		//指定消息投递模式：消息的返回模式
		channel.confirmSelect();

		//普通的交换机和队列
		String exchangeName="test_dlx_exchange";
		String rountingKey="dlx.#";
		String queueName="test_dlx_queue";

        //这个agruments属性，要声明到队列上
		channel.exchangeDeclare(exchangeName,"topic",true,false,null);
		Map<String,Object> agruments =new HashMap<>();
		agruments.put("x-dead-letter-exchange","dlx.exchange");
        channel.queueDeclare(queueName,true,false,false,agruments);
        channel.queueBind(queueName,exchangeName,rountingKey);
        //要进行死信队列的声明
		String dlxExchangeName="dlx.exchange";
		String dlxQueueName="dlx.exchange";
		channel.exchangeDeclare(dlxExchangeName,"topic",true,false,null);
		channel.queueDeclare(dlxQueueName,true,false,false,null);
		//绑定
		channel.queueBind(dlxQueueName,dlxExchangeName,"#");

        channel.basicConsume(queueName,true,new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException
                    {
                        String routingKey = envelope.getRoutingKey();
                        String contentType = properties.getContentType();
                        long deliveryTag = envelope.getDeliveryTag();
						System.out.println("routingKey:"+routingKey);
						System.out.println("contentType:"+contentType);
						System.out.println("properties:"+properties);
						System.out.println("body:"+new String(body));
                        System.out.println("消费者:"+new String(body));
                    }
                }
        );
    }
}
