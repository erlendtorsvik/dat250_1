package creditcard.project;

import lombok.Data;
import java.util.List;

import javax.persistence.*;


@Entity
@Data

public class Bank {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String name;
	
	@OnetoMany
	@JoinColumn
	private List<Creditcard> c;

}
