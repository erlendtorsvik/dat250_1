package project.dat250.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.cloud.firestore.DocumentReference;

import project.dat250.entity.Poll;
import project.dat250.firebase.FBinitialize;
import project.dat250.repository.IPollRepository;

@Controller
public class IoTRestController {

	@Autowired
	private IPollRepository pollRepository;
	@Autowired
	private FBinitialize fb;

	@PostMapping("/polls/{id}/IoT/setVotes")
	public ResponseEntity<String> setVotesIoT(@RequestParam int voteRed, @RequestParam int voteGreen,
			@PathVariable int id, OAuth2AuthenticationToken authentication) {
		if (!pollRepository.findById(id).isPresent())
			return ResponseEntity.badRequest().body("Poll doesn't exist. Try another PollID!!,");
		Poll pollOld = pollRepository.findById(id).get();
		if (!pollOld.getStatus().equals("past")) {
			if (!pollOld.getStatus().equals("future")) {
				pollOld.setVoteRed(voteRed);
				pollOld.setVoteGreen(voteGreen);
				DocumentReference dr = fb.getFirebase().collection("Polls")
						.document(String.valueOf(pollOld.getPollID()));
				dr.update("voteRed", pollOld.getVoteRed());
				dr.update("voteGreen", pollOld.getVoteGreen());
				pollRepository.save(pollOld);
				return ResponseEntity.ok("Thank you for voting: Total votes Red: " + pollOld.getVoteRed() + " & Green: "
						+ pollOld.getVoteGreen());
			}
			return ResponseEntity.badRequest().body("Poll is yet not opened. You can not vote on future polls,");
		}
		return ResponseEntity.badRequest().body("Poll is already closed. You can not vote on past polls,");

	}

	@GetMapping("/polls/{id}/IoT/getPoll")
	public ResponseEntity<Object> getPollIoT(@PathVariable int id) {
		if (!pollRepository.findById(id).isPresent())
			return ResponseEntity.badRequest().body("Poll doesn't exist. Try another PollID!!,");

		Poll pollOld = pollRepository.findById(id).get();
		return ResponseEntity.ok(pollOld);
	}
}