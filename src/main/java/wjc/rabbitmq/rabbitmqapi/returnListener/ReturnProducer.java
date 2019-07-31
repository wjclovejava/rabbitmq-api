package wjc.rabbitmq.rabbitmqapi.returnListener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @description: 消息的返回机制
 * @author: wjc
 * @create: 2019-07-31 10:25
 **/
public class ReturnProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.192.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName="test_return_exchange";
        String rountingKey="return.save";
		String rountingKeyError="abc.save";

        //发送消息
        String msg="hello rabbitmq...";
        channel.basicPublish(exchangeName,rountingKey,null,msg.getBytes());

        //添加一个确认监听
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
				//对不可达的消息做后续处理
            	System.out.println("--------------handle return--------------");
				System.out.println("replyCode:"+replyCode);
				System.out.println("replyText:"+replyText);
				System.out.println("exchange:"+exchange);
				System.out.println("routingKey:"+routingKey);
				System.out.println("properties:"+properties);
				System.out.println("body:"+new String(body));
            }
        });
        //mandatory 设置为true  监听器会接收到路由不到不可达的消息 进行后续处理，false则broker会自动删除消息
        //channel.basicPublish(exchangeName,rountingKey,true,null,msg.getBytes());
		channel.basicPublish(exchangeName,rountingKeyError,true,null,msg.getBytes());
    }
}
