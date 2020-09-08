package creditcard.project;

import javax.persistence.*;
import lombok.Data;


@Entity
@Data
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String firstName;
	
	@OneToMany
	@JoinColumn
	private List<CreditCard> creditcards = new ArrayList<>();
	
	@ManyToMany
	@JoinTable
	private List <Address> address = new ArrayList<>();
	
}
