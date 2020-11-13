package project.dat250.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import project.dat250.entity.Poll;

public class Sender {

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange topic;

	public void send(Poll poll) {
		StringBuilder builder = new StringBuilder();
		String key = poll.getStatus();
		int id = poll.getPollID();
		int green = poll.getVoteGreen();
		int red = poll.getVoteRed();
		builder.append("PollID: ").append(id).append(", Results: Green = ").append(green).append(" Red = ").append(red);
		String message = builder.toString();
		template.convertAndSend(topic.getName(), key, message);
		System.out.println("[x] Sent '" + message + "'");
	}

}