package creditcard.project;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Entity
@Data

public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String street;
	private int number;
	
	@ManyToMany(mappedBy = "address")
	private List<Person> person = new ArrayList<>();

}
