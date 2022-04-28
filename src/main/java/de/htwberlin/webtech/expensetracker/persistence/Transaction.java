package de.htwberlin.webtech.expensetracker.persistence;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Entity
//@Table(name="transaction")
public class Transaction {
    //add validation
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private  Integer tid;



    private String name;


    private String description;


    private BigDecimal total;



    private String regretted;

}
