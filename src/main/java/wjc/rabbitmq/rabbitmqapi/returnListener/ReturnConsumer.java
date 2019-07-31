package wjc.rabbitmq.rabbitmqapi.returnListener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: 消费端
 * @author: wjc
 * @create: 2019-07-31 10:25
 **/
public class ReturnConsumer {

    public static void main(String[] args) throws Exception {
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.192.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		//指定消息投递模式：消息的返回模式
		channel.confirmSelect();

		String exchangeName="test_return_exchange";
		String rountingKey="return.#";
		String queueName="test_return_queue";

        channel.exchangeDeclare(exchangeName,"topic",true,false,null);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,rountingKey);

        //5.创建消费者
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
                        // 在这里处理消息组件......
                        String msg =new String(body);
                        System.out.println("消费者:"+msg);
                    }
                }
        );
    }
}
