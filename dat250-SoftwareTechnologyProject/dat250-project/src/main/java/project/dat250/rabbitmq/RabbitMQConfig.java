package project.dat250.rabbitmq;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	@Bean
	public TopicExchange topic() {
		return new TopicExchange("tut.topic");
	}

	private static class ReceiverConfig {

		@Bean
		public Receiver receiver() {
			return new Receiver();
		}

		@Bean
		public Queue queue1() {
			return new AnonymousQueue();
		}

		@Bean
		public Queue queue2() {
			return new AnonymousQueue();
		}

		@Bean
		public Binding pastBinding(TopicExchange topic, Queue queue1) {
			return BindingBuilder.bind(queue1).to(topic).with("past");
		}

		@Bean
		public Binding futureBinding(TopicExchange topic, Queue queue2) {
			return BindingBuilder.bind(queue2).to(topic).with("future");
		}

		@Bean
		public Binding presentBinding(TopicExchange topic, Queue queue2) {
			return BindingBuilder.bind(queue2).to(topic).with("present");
		}

	}

	@Bean
	public Sender sender() {
		return new Sender();
	}
}
