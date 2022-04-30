package de.htwberlin.webtech.expensetracker.persistence.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    @Column(name = "category_name")
    private String categoryName;

//    @ManyToOne(mappedBy = "category")
//    private Set<ExpenseEntity> expense;

    public CategoryEntity(  String categoryName) {
        this.categoryName = categoryName;
    }

//    public CategoryEntity( Long cid, String categoryName) {
//        this.categoryName = categoryName;
//        this.cid = cid;
//    }


}
