package wjc.rabbitmq.ack;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: ack nack 重回队列
 * @author: wjc
 * @create: 2019-07-31 10:33
 **/
public class AckConsumer {

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.192.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		//指定消息投递模式：消息的返回模式
		channel.confirmSelect();

		String exchangeName = "test_ack_exchange";
		String rountingKey = "ack.#";
		String queueName = "test_ack_queue";

		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, rountingKey);

		//消费端限流 必须要autoAck设置为false
		channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				System.out.println("msg:" + new String(body));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if ((Integer) properties.getHeaders().get("num") == 0){
					// 确认ack失败 requeue 是否重回队列   true 重回队列到队尾
					channel.basicNack(envelope.getDeliveryTag(),false,true);
				}else {
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		});
	}
}
