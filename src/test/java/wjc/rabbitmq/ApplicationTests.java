package wjc.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
	private RabbitAdmin rabbitAdmin;

    @Test
    public void test(){
    	//声明交换机
    	rabbitAdmin.declareExchange(new DirectExchange("test.direct",false,false));
		rabbitAdmin.declareExchange(new TopicExchange("test.topic",false,false));
		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout",false,false));
		//声明队列
		rabbitAdmin.declareQueue(new Queue("test.direct.queue",false));
		rabbitAdmin.declareQueue(new Queue("test.topic.queue",false));
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue",false));
		//绑定
		rabbitAdmin.declareBinding(new Binding("test.direct.queue",Binding.DestinationType.QUEUE
				,"test.direct","direct",new HashMap<>()));
		//绑定
		rabbitAdmin.declareBinding(BindingBuilder
					.bind(new Queue("test.topic.queue",false))
					.to(new TopicExchange("test.topic",false,false))
					.with("direct"));

		rabbitAdmin.declareBinding(BindingBuilder
				.bind(new Queue("test.fanout.queue",false))
				.to(new FanoutExchange("test.fanout",false,false)));

		//清空队列
		rabbitAdmin.purgeQueue("test.topic.queue",false);

    }
}
