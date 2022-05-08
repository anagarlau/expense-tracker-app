package de.htwberlin.webtech.expensetracker.persistence.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="category")

public class CategoryEntity extends BaseEntity{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @Column(nullable = false)
    private String categoryName;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;

    //dependency to user -> unidirectional?
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    @JoinColumn(name = "uid", referencedColumnName ="uid",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy ="category")
    private Set<TransactionEntity> transactions = new HashSet<>();



    public CategoryEntity(UserEntity user, String categoryName, CategoryType categoryType) {
        this.user=user;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }


}
