package de.htwberlin.webtech.expensetracker.persistence.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="category")
public class CategoryEntity extends BaseEntity{
    //TODO: add field expense or transaction mit ENUM
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @Column
    private String categoryName;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy ="category")
    private List<ExpenseEntity> expenses = new ArrayList<>();

    public CategoryEntity( String categoryName, CategoryType categoryType) {

        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }


}
