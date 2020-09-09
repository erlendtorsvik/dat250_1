package creditcard.project;

import lombok.Data;
import java.util.List;

import javax.persistence.*;


@Entity
@Data

public class Bank {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String name;
	
	@OneToMany
	@JoinColumn
	private List<CreditCard> creditcard;

}
