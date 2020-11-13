package project.dat250.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	private String uID;
	private String fname;
	private String lname;
	private String email;
	private boolean admin;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Integer> subs = new HashSet();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> subsMsg = new HashSet();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Poll> polls = new ArrayList<>();

	public User() {
	}

	public User(String uID, String fname, String lname, String email, boolean admin) {
		this.uID = uID;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.admin = admin;
	}

	public void setSubs(int pollID) {
		subs.add(pollID);
	}

	public void setSubsMsg(String message) {
		subsMsg.add(message);
	}

	public void setPolls(Poll poll) {
		boolean contains = false;
		for (Poll p : polls) {
			if (poll.getPollID() == p.getPollID()) {
				contains = true;
				break;
			}
		}
		if (!contains)
			this.polls.add(poll);
	}

	public void removePoll(Poll poll) {
		this.polls.remove(poll);
		poll.getUsersVoted().clear();
	}
}
