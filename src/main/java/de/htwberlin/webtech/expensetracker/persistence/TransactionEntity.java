package de.htwberlin.webtech.expensetracker.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transaction" )
public class TransactionEntity {
    //add validation
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long tid;


    @Column(name="name", nullable = false)
    private String name;


    @Column(name="total", nullable = false)
    private Double sum;


    @Column(name="regretted")
    private String regretted;

}
