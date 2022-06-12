package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryManipulationRequest {

    @NotBlank
    @Size(min=3, message="Name must be at least 3 characters long")
    private String categoryName;
    @NotBlank
    @Size(min=3, message="Icon must be at least 3 characters long")
    private String icon;

    @NotBlank
    @Size(min=6, message="Description must be at least 6 characters long: INCOME or EXPENSE")
    private String categoryType;


}
