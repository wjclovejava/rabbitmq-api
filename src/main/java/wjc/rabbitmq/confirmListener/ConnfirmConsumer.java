package wjc.rabbitmq.confirmListener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: 消费端
 * @author: wjc
 * @create: 2019-07-25 16:39
 **/
public class ConnfirmConsumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName="test_confirm_exchange";
        String rountingKey="confirm.#";
        String queueName="test_confirm_queue";

        channel.exchangeDeclare(exchangeName,"topic",true);
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
