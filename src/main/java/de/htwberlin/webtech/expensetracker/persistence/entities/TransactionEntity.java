package de.htwberlin.webtech.expensetracker.persistence.entities;

import com.fasterxml.jackson.databind.ser.Serializers;
import de.htwberlin.webtech.expensetracker.web.model.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="transaction")
@EntityListeners(AuditingEntityListener.class)
public  class TransactionEntity extends BaseEntity {

    //category
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="cid", referencedColumnName = "cid", nullable = false)
    protected CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String transactionDescription;

    @Column( columnDefinition="decimal", precision=5, scale=2, nullable = false)
    protected BigDecimal transactionTotal;

    @Column(nullable = false)
    protected LocalDate transactionDate;

    public TransactionEntity(UserEntity user, CategoryEntity category, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        this.category = category;
        this.transactionDescription = transactionDescription;
        this.transactionTotal = transactionTotal;
        this.transactionDate = transactionDate;
        this.user = user;
    }
}
