package de.htwberlin.webtech.expensetracker.persistence.entities;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Description must not be blank")
    @Size(min=5, message="Description must be at least 5 characters long")
    protected String transactionDescription;

    @Column( columnDefinition="decimal", precision=5, scale=2, nullable = false)
    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.1", inclusive = false, message = "Amount must be greater than 0")
    protected BigDecimal transactionTotal;

    @Column(nullable = false)
    @NotNull(message = "Date must not be blank")
    protected LocalDate transactionDate;

    public TransactionEntity(UserEntity user, CategoryEntity category, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        this.category = category;
        this.transactionDescription = transactionDescription;
        this.transactionTotal = transactionTotal;
        this.transactionDate = transactionDate;
        this.user = user;
    }
}
