package wjc.rabbitmq.quickstart.direct;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: direct 直连模式routingKey值相同
 * @author: wjc
 * @create: 2019-07-24 17:40
 **/
public class DirectConsumer {


    public static void main(String[] args) {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        try {
            connectionFactory.setHost("192.168.192.128");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            //2.ConnectionFactory创建连接
            Connection connection = connectionFactory.newConnection();
            //3.connection创建Channel
            Channel channel = connection.createChannel();
            String exchangeName="test_direct_exchange";
            String routingKey="test.direct";
            String queueName="test_direct_queue";
            String exchangeType="direct";
            //4.声明
            //声明交换机
            channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
            //声明队列
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,exchangeName,routingKey);

            //5.创建消费者
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
                            // 在这里处理消息组件......
                            String msg =new String(body);
                            System.out.println("消费者:"+msg);

                            channel.basicAck(deliveryTag, false); //确认收到消息
                        }
                    }
            );

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
