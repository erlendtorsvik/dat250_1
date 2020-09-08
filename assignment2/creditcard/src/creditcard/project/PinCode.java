package creditcard.project;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data

public class PinCode {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String pin;
	private int count;

}
