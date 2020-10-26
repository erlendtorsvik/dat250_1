package assignmentB.restfulwebapi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class Poll {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.PROTECTED)
	private int pollID;
	private String name;
	private String description;
	private boolean isPublic;
	private int voteGreen;
	private int voteRed;
	private String status;
	private int timeLimit;

	@ManyToOne
	private User user;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<User> usersVoted = new ArrayList<User>();

	public Poll() {
	}

	public Poll(String name, String description, boolean isPublic, int voteGreen, int voteRed, String status,
			int timeLimit, User user) {
		this.name = name;
		this.description = description;
		this.isPublic = isPublic;
		this.voteGreen = voteGreen;
		this.voteRed = voteRed;
		this.status = status;
		this.timeLimit = timeLimit;
		this.user = user;
	}

	public void setUsersVoted(User userVoted) {
		this.usersVoted.add(userVoted);
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null)
			user.setPolls(this);
	}

	public void setVoteRed(int red) {
		this.voteRed += red;
	}

	public void setVoteGreen(int green) {
		this.voteGreen += green;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

}
