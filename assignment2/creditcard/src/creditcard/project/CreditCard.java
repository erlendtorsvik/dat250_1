package creditcard.project;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private int number;
	private int balance;
	private int limit;
	
	@ManytoOne
	private Bank b;
	
	@OnetoOne
	private Pincode pin;

}
