package wjc.rabbitmq.quickstart.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @description: 生产着
 * @author: wjc
 * @create: 2019-07-24 17:40
 **/
public class TopicProducer {

    public static void main(String[] args) {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory=new ConnectionFactory();
        try {
            connectionFactory.setHost("192.168.192.128");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");
            connectionFactory.setAutomaticRecoveryEnabled(true);
            connectionFactory.setNetworkRecoveryInterval(3000);
            //2.ConnectionFactory创建连接
            Connection connection = connectionFactory.newConnection();
            //3.connection创建Channel
            Channel channel = connection.createChannel();
            //4.声明
            String exchangeName="test_topic_exchange";
            String routingKey1="user.save";
            String routingKey2="user.update";
            String routingKey3="user.delete.abc";

            //5.通过channel发送数据
            String msg="hello ,rabbitmq";
            channel.basicPublish(exchangeName,routingKey1,null,msg.getBytes());
            channel.basicPublish(exchangeName,routingKey2,null,msg.getBytes());
            channel.basicPublish(exchangeName,routingKey3,null,msg.getBytes());

            //5.关闭连接
            channel.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
