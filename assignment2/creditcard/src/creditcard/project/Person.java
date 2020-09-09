package creditcard.project;


import java.util.List;
import javax.persistence.*;
import lombok.Data;


@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String firstName;
    
    @OneToMany
    @JoinColumn
    private List<CreditCard> creditcard;
   
    @ManyToMany
    @JoinTable
    private List <Address> address;
    
   

}