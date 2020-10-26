package assignmentB.restfulwebapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import assignmentB.restfulwebapi.entity.Poll;
import assignmentB.restfulwebapi.entity.User;
import assignmentB.restfulwebapi.repository.IUserRepository;

@Controller
public class UserRestController {

	@Autowired
	private IUserRepository userRepository;

	@PutMapping("/users/{uname}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String uname) {
		Optional<User> userOpt = userRepository.findById(uname);
		if (!userOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		User userOld = userOpt.get();
		if (user.getFname() != null)
			userOld.setFname(user.getFname());
		if (user.getLname() != null)
			userOld.setLname(user.getLname());
		if (user.getEmail() != null)
			userOld.setEmail(user.getEmail());
		if (user.getPassword() != null)
			userOld.setPassword(user.getPassword());
		if (user.isAdmin() != userOld.isAdmin())
			userOld.setAdmin(user.isAdmin());

		userRepository.save(userOld);
		return ResponseEntity.ok().build();

	}
	
	@DeleteMapping("/users/{uname}")
	public ResponseEntity<User> deleteUser(@PathVariable String uname) {
		Optional<User> optUser = userRepository.findById(uname);
		if(!optUser.isPresent()) return ResponseEntity.notFound().build();
		
		User user = optUser.get();
		for(Poll p : user.getPollsVoted()) {
			p.getUsersVoted().remove(user);
		}
		
		userRepository.deleteById(uname);
		return ResponseEntity.ok().build();
	}

}
