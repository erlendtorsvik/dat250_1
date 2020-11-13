package project.dat250.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project.dat250.entity.Poll;
import project.dat250.entity.User;
import project.dat250.repository.IUserRepository;

@Controller
public class UserRestController {

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	OAuth2AuthorizedClient client;

	mainRestController main = new mainRestController();

	// @DeleteMapping("/users/{uname}")
	// public ResponseEntity<User> deleteUser(@PathVariable String uname) {
	// Optional<User> optUser = userRepository.findById(uname);
	// if (!optUser.isPresent())
	// return ResponseEntity.notFound().build();
	//
	// User user = optUser.get();
	// for (Poll p : user.getPollsVoted()) {
	// p.getUsersVoted().remove(user);
	// }
	//
	// userRepository.deleteById(uname);
	// return ResponseEntity.ok().build();
	// }

	/**
	 * Persists a user into the database
	 * 
	 * @param model
	 * @param authentication
	 * @return Welcome screen for user to continue to the web app
	 */
	@GetMapping("/userAdd")
	public String userAdd(Model model, OAuth2AuthenticationToken authentication) {
		client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
		User newUser = main.saveUser(client);
		if (!userRepository.findById(newUser.getUID()).isPresent())
			userRepository.save(newUser);
		model.addAttribute("name", newUser.getFname() + " " + newUser.getLname());
		return "welcome";
	}

	/**
	 * Return a website with user created polls
	 * 
	 * @param model
	 * @param authenticationToken
	 * @return myPolls site with user polls
	 */
	@GetMapping("/myPolls")
	public String myPolls(Model model, OAuth2AuthenticationToken authenticationToken) {
		User user = userRepository.findById(main.getUser(client)).get();
		List<Poll> myPolls = user.getPolls();
		model.addAttribute("myPolls", myPolls);
		model.addAttribute("name", user.getFname() + " " + user.getLname());
		return "myPolls";
	}

}
