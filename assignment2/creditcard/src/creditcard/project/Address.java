package creditcard.project;

import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Entity
@Data

public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String street;
	private int number;
	
	@ManyToMany(mappedBy = "address")
	private List<Person> persons;

}
