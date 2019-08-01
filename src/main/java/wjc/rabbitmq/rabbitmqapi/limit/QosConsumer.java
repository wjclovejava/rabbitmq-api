package wjc.rabbitmq.rabbitmqapi.limit;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: 消费端进行限流 QOS
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class QosConsumer {

    public static void main(String[] args) throws Exception {
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.192.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		//指定消息投递模式：消息的返回模式
		channel.confirmSelect();

		String exchangeName="test_qos_exchange";
		String rountingKey="qos.#";
		String queueName="test_qos_queue";

        channel.exchangeDeclare(exchangeName,"topic",true,false,null);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,rountingKey);

        //设置为1 表示一条一条确认，不是批量
		channel.basicQos(0,1,false);
        //消费端限流 必须要autoAck设置为false
        channel.basicConsume(queueName,false,new DefaultConsumer(channel) {
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
                        //手动确认ack false不批量确认
						channel.basicAck(deliveryTag,false);
                    }
                }
        );
    }
}
