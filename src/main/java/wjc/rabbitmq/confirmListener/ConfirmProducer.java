package wjc.rabbitmq.confirmListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @description: 消息的确认机制
 * @author: wjc
 * @create: 2019-07-25 16:31
 **/
public class ConfirmProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName="test_confirm_exchange";
        String rountingKey="confirm.save";

        //发送消息
        String msg="hello rabbitmq...";
        channel.basicPublish(exchangeName,rountingKey,null,msg.getBytes());

        //添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            //成功
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Ack");
            }
            //失败
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("no Ack");
            }
        });

    }
}
