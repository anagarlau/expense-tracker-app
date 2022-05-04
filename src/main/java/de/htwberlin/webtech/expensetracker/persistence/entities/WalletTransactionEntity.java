package de.htwberlin.webtech.expensetracker.persistence.entities;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class WalletTransactionEntity  extends BaseEntity {

    //category
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="cid", referencedColumnName = "cid", nullable = false)
    protected CategoryEntity category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="wid", referencedColumnName = "wid", nullable = false)
    protected WalletEntity wallet;

    @Column
    protected String transactionDescription;

    @Column( columnDefinition="decimal", precision=5, scale=2)
    protected BigDecimal transactionTotal;

    @Column
    protected LocalDate transactionDate;

    public WalletTransactionEntity(CategoryEntity category, WalletEntity wallet, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        this.category = category;
        this.wallet = wallet;
        this.transactionDescription = transactionDescription;
        this.transactionTotal = transactionTotal;
        this.transactionDate = transactionDate;
    }
}