package creditcard.project;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private int number;
	private int balance;
	private int limit;
	
	@ManyToOne
	private Bank banks;
	
	@OneToOne
	private PinCode pin;

}
