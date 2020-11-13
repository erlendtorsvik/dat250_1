package project.dat250.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import project.dat250.entity.User;
import project.dat250.repository.IUserRepository;

public class Receiver {

	@Autowired
	private IUserRepository userRepository;

	@RabbitListener(queues = "#{queue1.name}")
	public void queue1Listener(String in) {
		receive(in);
	}

	public void receive(String in) {
		Iterable<User> users = userRepository.findAll();
		String[] msg = in.split("\\W");
		for (User u : users) {
			if (u.getSubs().contains(Integer.parseInt(msg[2]))) {
				u.setSubsMsg(in);
				userRepository.save(u);
			}
		}
		System.out.println("[x] Received '" + in + "'");
	}
}