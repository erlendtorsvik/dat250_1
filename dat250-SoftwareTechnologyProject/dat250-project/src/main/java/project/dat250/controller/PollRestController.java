package project.dat250.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import com.google.api.client.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;

import project.dat250.entity.Poll;
import project.dat250.entity.User;
import project.dat250.firebase.FBinitialize;
import project.dat250.rabbitmq.Sender;
import project.dat250.repository.IPollRepository;
import project.dat250.repository.IUserRepository;
import project.dat250.dweetio.*;
import com.google.gson.JsonObject;

@Controller
public class PollRestController {

	@Autowired
	private IPollRepository pollRepository;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private Sender sender;
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	@Autowired
	private FBinitialize fb;

	OAuth2AuthorizedClient client;

	mainRestController main = new mainRestController();

	/**
	 * @param model
	 * @param authentication
	 * @return index site with results of users subscribed polls if any. Shows
	 *         original index if nothing to show
	 */
	@GetMapping("/polls/results")
	public String getResult(Model model, OAuth2AuthenticationToken authentication) {
		StringBuilder str = new StringBuilder();
		Optional<User> userOpt = userRepository.findById(main.getUser(client));
		if (!userOpt.isPresent())
			return "index";
		User user = userOpt.get();
		str.append("");
		for (String s : user.getSubsMsg()) {
			str.insert(0, s + "<br>");
		}
		model.addAttribute("name", getUserName(authentication));
		model.addAttribute("message", str);
		return "index";

	}

	/**
	 * Add users vote to the poll, persisting it into the database
	 * 
	 * @param vote           User chosen vote
	 * @param id             the id of poll user voted on
	 * @param model
	 * @param authentication
	 * @return index site if user successfully voted or already voted on this poll
	 *         before pollVote screen if something else went wrong
	 */
	@PostMapping("/polls/{id}/setVotes")
	public String setVotes(@RequestParam String vote, @PathVariable int id, Model model,
			OAuth2AuthenticationToken authentication) {
		Poll pollOld = pollRepository.findById(id).get();
		User user = null;
		String uID = "";
		if (authentication == null) { // Check if a logged in user is voting
			if (!pollOld.isPublic()) {
				model.addAttribute("message", "You have to log in to vote on this poll");
				return "index";
			}
			model.addAttribute("name", "guest");
			Random random = new Random();
			boolean created = false;
			while (!created) { // Create a user to vote
				uID = String.valueOf(random.nextInt());
				if (userRepository.findById(uID).isEmpty()) {
					user = new User(uID, "anonym", "guest", "", false);
					created = true;
					userRepository.save(user);
					System.out.println(uID);
				}
			}
		} else {
			user = userRepository.findById(main.getUser(client)).get();
			model.addAttribute("name", getUserName(authentication));
		}

		if (!pollOld.getUsersVoted().contains(user)) {
			if (vote.equals("voteGreen")) { // user has voted green
				pollOld.setVoteGreen(1);
				pollOld.setUsersVoted(user);

				DocumentReference dr = fb.getFirebase().collection("Polls")
						.document(String.valueOf(pollOld.getPollID()));
				dr.update("voteGreen", pollOld.getVoteGreen());

				if (user.getUID().equals(uID)) { // Deleting user from database if it's a guest
					pollOld.getUsersVoted().remove(user);
					userRepository.deleteById(uID);
				}
				pollRepository.save(pollOld);

				model.addAttribute("message", "Thank you for voting on poll: " + id);
				return "index";
			} else if (vote.equals("voteRed")) { // user has voted red
				pollOld.setVoteRed(1);
				pollOld.setUsersVoted(user);

				DocumentReference dr = fb.getFirebase().collection("Polls")
						.document(String.valueOf(pollOld.getPollID()));
				dr.update("voteRed", pollOld.getVoteRed());

				if (user.getUID().equals(uID)) { // Deleting user from database if it's a guest
					pollOld.getUsersVoted().remove(user);
					userRepository.deleteById(uID);
				}

				pollRepository.save(pollOld);
				model.addAttribute("message", "Thank you for voting on poll: " + id);
				return "index";
			} else if (vote.equals("missing")) { // user didn't choose any option
				model.addAttribute("message", "Please select one option!");
				model.addAttribute("poll", pollOld);
				return "pollVote";
			}
			model.addAttribute("poll", pollOld);
			return "pollVote";
		}
		model.addAttribute("message", "You've already voted on poll " + id + " before");
		return "index";
	}

	/**
	 * Get poll update screen if the user editing is the poll creator
	 * 
	 * @param id    the id of poll to be updated
	 * @param model
	 * @return index site if you're not authenticated to edit this poll, pollUpdate
	 *         site otherwise
	 */
	@GetMapping("/polls/update/{id}")
	public String getUpdatePoll(@PathVariable int id, Model model, OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", getUserName(authentication));
		Optional<Poll> pollOpt = pollRepository.findById(id);
		if (!pollOpt.isPresent()) {
			model.addAttribute("message", "Poll not in system, try another ID!");
			return "index";
		}
		Poll pollOld = pollOpt.get();
		// checking if owner tried to edit poll
		if (!userRepository.findById(main.getUser(client)).get().getUID().equals(pollOld.getUser().getUID())) {
			model.addAttribute("message", "You can't edit someone elses poll");
			return "index";
		}
		model.addAttribute("poll", pollOld);
		return "pollUpdate";
	}

	/**
	 * Updates the poll and persists to the database
	 * 
	 * @param name        name to be updated
	 * @param description description to be updated
	 * @param isPublic    publicity of the poll to be updated
	 * @param status      status of the poll to be updated
	 * @param model
	 * @param id          id of the poll to be updated
	 * @return screen with the updated poll
	 */
	@PostMapping("/polls/{id}")
	public String updatePoll(@RequestParam String name, @RequestParam String description, @RequestParam String isPublic,
			@RequestParam String status, Model model, @PathVariable int id, OAuth2AuthenticationToken authentication) throws IOException {
		model.addAttribute("name", getUserName(authentication));
		Poll pollOld = pollRepository.findById(id).get();

		if (!name.isBlank())
			pollOld.setName(name);
		if (!description.isBlank())
			pollOld.setDescription(description);
		if (!isPublic.isBlank())
			pollOld.setPublic(Boolean.parseBoolean(isPublic));
		if (!status.isBlank() && !pollOld.getStatus().equals("past")) {
			pollOld.setStatus(status);
			if (status.equals("past"))
				sender.send(pollOld);
				String thingName = "unique_poll_dweet";
				JsonObject json = new JsonObject();
				json.addProperty("Poll name: " + name + "  Poll description: " + description,
						"Red votes: " + pollOld.getVoteRed() + "  Green votes: " + pollOld.getVoteGreen());
				DweetIO.publish1(thingName, json);
		} else {
			model.addAttribute("poll", pollOld);
			model.addAttribute("message", "Poll is already closed, you can't reopen it!");
			return "pollUpdate";
		}
		pollRepository.save(pollOld);
		model.addAttribute("poll", pollOld);
		model.addAttribute("message", "Successfully updated poll" + id);

		DocumentReference dr = fb.getFirebase().collection("Polls").document(String.valueOf(pollOld.getPollID()));
		dr.update("name", pollOld.getName());
		dr.update("description", pollOld.getDescription());
		dr.update("status", pollOld.getStatus()); // Updater databasen med ny status og ny public status.
		dr.update("public", pollOld.isPublic());

		return "pollUpdate";
	}

	@GetMapping("polls/delete/{id}")
	public String deletePoll(@PathVariable int id, Model model, OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", getUserName(authentication));
		Optional<Poll> optPoll = pollRepository.findById(id);
		if (!optPoll.isPresent()) {
			model.addAttribute("message", "Poll not in system, try another ID!");
			return "myPolls";
		}

		while (optPoll.isPresent()) {
			Poll poll = optPoll.get();
			poll.getUser().removePoll(poll);
			pollRepository.save(poll);
			optPoll = pollRepository.findById(id);
		}
		User user = userRepository.findById(main.getUser(client)).get();
		List<Poll> myPolls = user.getPolls();
		model.addAttribute("myPolls", myPolls);
		model.addAttribute("message", "Successfully deleted poll " + id);
		return "myPolls";
	}

	/**
	 * Get poll create screen
	 * 
	 * @param model
	 * @param authentication
	 * @return poll create screen
	 */
	@GetMapping("/pollCreate")
	public String pollCreate(Model model, OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", getUserName(authentication));
		return "pollCreate";

	}

	@GetMapping("/")
	public String polls(Model model, OAuth2AuthenticationToken authentication) {
		String name = "";
		List<Poll> polls = new ArrayList<>();
		if (authentication == null) {
			polls.addAll(pollRepository.findByIsPublic(true));
			polls.removeAll(pollRepository.findByStatus("past"));
			model.addAttribute("polls", polls);
			name = "guest";
		} else {
			client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
					authentication.getName());
			name = getUserName(authentication);
			Iterable<Poll> pollsIter = pollRepository.findAll();
			for (Poll p : pollsIter) {
				if (!p.getStatus().equals("past"))
					polls.add(p);
			}
			model.addAttribute("polls", polls);
		}

		model.addAttribute("name", name);
		return "index";
	}

	/**
	 * Adds a poll and persists it to the database
	 * 
	 * @param name           Name of the poll
	 * @param description    Description of the poll
	 * @param isPublic       Publicity of the poll
	 * @param status         Status of the Poll
	 * @param model
	 * @param authentication
	 * @return index site if user not existent or if poll created successfully,
	 *         pollCreate site if something else went wrong
	 */
	@PostMapping("/pollAdd")
	public String pollAdd(@RequestParam String name, @RequestParam String description, @RequestParam String isPublic,
			@RequestParam String status, Model model, OAuth2AuthenticationToken authentication) throws IOException {
		Optional<User> userOpt = userRepository.findById(main.getUser(client));
		if (!userOpt.isPresent()) {
			return "index";
		}
		User user = userOpt.get();
		model.addAttribute("name", getUserName(authentication));
		Poll newPoll = new Poll();
		if (description.isBlank() && name.isBlank()) {
			model.addAttribute("message", "Fill all attributes and try again!");
			return "pollCreate";
		}
		newPoll.setDescription(description);
		newPoll.setName(name);
		newPoll.setPublic(Boolean.parseBoolean(isPublic));
		if (status.equals("past")) {
			model.addAttribute("message", "You can't make an already closed poll!!");
			return "pollCreate";
		} else
			newPoll.setStatus(status);
		newPoll.setUser(user);
		pollRepository.save(newPoll);

		String thingName = "unique_poll_dweet";
		JsonObject json = new JsonObject();
		json.addProperty("Poll name: " + name, "Poll description: " + description);
		DweetIO.publish1(thingName, json);

		Poll fbPoll = new Poll();
		fbPoll.setDescription(description);
		fbPoll.setName(name);
		fbPoll.setPublic(Boolean.parseBoolean(isPublic));
		fbPoll.setStatus(status);

		CollectionReference pollCR = fb.getFirebase().collection("Polls");
		pollCR.document(String.valueOf((newPoll.getPollID()))).set(fbPoll);
		DocumentReference dr = fb.getFirebase().collection("Polls").document(String.valueOf(newPoll.getPollID()));
		dr.update("pollID", newPoll.getPollID());

		model.addAttribute("polls", newPoll);
		model.addAttribute("name", getUserName(authentication));
		return "index";
	}

	/**
	 * Searches poll with given name
	 * 
	 * @param name           Name to be searched for
	 * @param model
	 * @param authentication
	 * @return index site with results of the search
	 */
	@GetMapping("/polls")
	public String searchPolls(@RequestParam String name, Model model, OAuth2AuthenticationToken authentication) {
		List<Poll> polls = pollRepository.findByNameContainingIgnoreCase(name);
		polls.removeAll(pollRepository.findByStatus("past"));
		
		if (authentication == null) {
			polls = pollRepository.findByNameContainingIgnoreCase(name);
			polls.retainAll(pollRepository.findByIsPublic(true));
			model.addAttribute("name", "guest");
			model.addAttribute("polls", polls);
			return "index";
		}
		model.addAttribute("polls", polls);
		model.addAttribute("name", getUserName(authentication));
		return "index";
	}

	/**
	 * Gets voting screen if poll present
	 * 
	 * @param pollID         Id of the poll to be voted on
	 * @param model
	 * @param authentication
	 * @return index if something goes wrong, pollVote screen if successful
	 */
	@GetMapping("/poll")
	public String poll(@RequestParam String pollID, Model model, OAuth2AuthenticationToken authentication) {
		if (authentication == null) {
			model.addAttribute("name", "guest");
		} else {
			model.addAttribute("name", getUserName(authentication));
		}
		if (!pollID.isEmpty()) {
			Optional<Poll> pollOpt = pollRepository.findById(Integer.parseInt(pollID));
			if (pollOpt.isPresent()) {
				if (!pollOpt.get().getStatus().equals("past")) {
					if (!pollOpt.get().getStatus().equals("future")) {
						if (authentication == null && !pollOpt.get().isPublic()) {
							model.addAttribute("message", "This poll is private you have to login to vote!");
							return "index";
						}
						model.addAttribute("poll", pollOpt.get());
						return "pollVote";
					}
					model.addAttribute("message", "You can not vote on a future poll!");
					return "index";
				}
				model.addAttribute("message", "Poll is already closed, you cannot vote!");
				return "index";
			}
			model.addAttribute("message", "Poll not in system, try another ID!");
			return "index";
		}
		model.addAttribute("message", "No PollID specified, try again!");
		return "index";
	}

	@GetMapping("/poll/{pollID}/subscribe")
	public String subscribe(@PathVariable int pollID, Model model, OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", getUserName(authentication));
		Poll poll = pollRepository.findById(pollID).get();
		Optional<User> userOpt = userRepository.findById(main.getUser(client));
		if (!userOpt.isPresent()) {
			model.addAttribute("message", "Why are you not logged in trying to subscribe?!"); // Should never happen
			return "index";
		}
		User user = userOpt.get();
		if (user.getSubs().contains(pollID)) {
			model.addAttribute("poll", poll);
			model.addAttribute("message", "You've already subscribed to this poll");
			return "pollVote";
		}
		user.setSubs(pollID);
		userRepository.save(user);
		model.addAttribute("poll", poll);
		model.addAttribute("message", "Successfully subscribed to poll " + pollID);
		return "pollVote";
	}

	/**
	 * Returns name of logged in user
	 * 
	 * @param authentication
	 * @return First name + last name of user
	 */
	private String getUserName(OAuth2AuthenticationToken authentication) {
		main.getUser(client);
		Optional<User> userOpt = userRepository.findById(main.getUser(client));
		if (!userOpt.isPresent()) {
			return "null";
		}
		User user = userOpt.get();
		return user.getFname() + " " + user.getLname();
	}
}
