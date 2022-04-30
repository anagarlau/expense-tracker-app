package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntityModel{

    private Long cid;

    private Set<Expense> expense;
    private String categoryName;

    public Category(Long cid, String categoryName) {
        this.cid = cid;
        this.categoryName = categoryName;
    }

    public Category(String categoryName) {

        this.categoryName = categoryName;
    }
}
