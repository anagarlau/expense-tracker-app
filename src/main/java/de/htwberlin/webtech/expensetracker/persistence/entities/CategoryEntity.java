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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @Column
    private String categoryName;


    @OneToMany(fetch = FetchType.EAGER, mappedBy ="category")
    private List<ExpenseEntity> expenses = new ArrayList<>();

    public CategoryEntity( String categoryName) {
        this.categoryName = categoryName;
    }


}
