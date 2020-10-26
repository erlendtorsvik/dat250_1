package assignmentB.restfulwebapi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	private String uname;
	private String fname;
	private String lname;
	private String password;
	private String email;
	private boolean admin;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Poll> polls = new ArrayList<Poll>();

	@ManyToMany(mappedBy = "usersVoted", fetch = FetchType.EAGER)
	private List<Poll> pollsVoted = new ArrayList<Poll>();

	public User() {
	}

	public User(String uname, String fname, String lname, String password, String email, boolean admin) {
		this.uname = uname;
		this.fname = fname;
		this.lname = lname;
		this.password = password;
		this.email = email;
		this.admin = admin;
	}

	public void setPolls(Poll poll) {
		if (!polls.contains(poll))
			this.polls.add(poll);
	}

	public void removePoll(Poll poll) {
		this.polls.remove(poll);
		poll.getUsersVoted().clear();
	}

	public void setPollsVoted(Poll poll) {
		this.pollsVoted.add(poll);
	}

}
