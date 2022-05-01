package de.htwberlin.webtech.expensetracker.web.service;


import de.htwberlin.webtech.expensetracker.exceptions.IllegalCategoryException;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.ExpenseRepository;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.Expense;
import de.htwberlin.webtech.expensetracker.web.model.ExpenseManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;


    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;

    }

    public List<Expense> findAll() {
        return this.expenseRepository.findAll().stream().map(expenseEntity ->
                        this.mapToExpense(expenseEntity))
                .collect(Collectors.toList());
    }

    public Expense createExpense(ExpenseManipulationRequest expenseRequest) {

        Optional<CategoryEntity> categoryById = this.categoryRepository.findById(expenseRequest.getCid());
        if(categoryById.isEmpty()){
                return null;

        }
            ExpenseEntity expenseEntity = new ExpenseEntity(categoryById.get(), expenseRequest.getExpenseName(), expenseRequest.getDescription(), expenseRequest.getExpenseTotal(), expenseRequest.getExpenseDate(), expenseRequest.getRegretted());
            ExpenseEntity savedExpense = this.expenseRepository.save(expenseEntity);
            if (savedExpense != null && savedExpense.getTid() > 0) return mapToExpense(savedExpense);
            else return null;

    }


    public Expense fetchExpenseById(Long id) {
        Optional<ExpenseEntity> expenseById = this.expenseRepository.findById(id);
        return expenseById.map(expenseEntity -> mapToExpense(expenseEntity)).orElse(null);
    }


    public Expense update(Long id, ExpenseManipulationRequest expenseRequest){
        Optional<ExpenseEntity> toBeUpdatedById = this.expenseRepository.findById(id);
         if(toBeUpdatedById.isPresent()) {

             ExpenseEntity  expenseEntity = toBeUpdatedById.get();

                 expenseEntity.setExpenseName(expenseRequest.getExpenseName());
                 expenseEntity.setDescription(expenseRequest.getDescription());
                 expenseEntity.setRegretted(expenseRequest.getRegretted());
                 expenseEntity.setExpenseTotal(expenseRequest.getExpenseTotal());
                 expenseEntity.setExpenseDate(expenseRequest.getExpenseDate());

             ExpenseEntity savedEntity = this.expenseRepository.save(expenseEntity);
             return mapToExpense(savedEntity) ;
         }else{
             return null;
         }

    }






    public ExpenseEntity mapToExpenseEntity(ExpenseManipulationRequest expense ) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(expense.getCid());
         if(categoryById.isPresent()){
            return new ExpenseEntity(categoryById.get(), expense.getExpenseName(), expense.getDescription(), expense.getExpenseTotal(),expense.getExpenseDate(),expense.getRegretted());
        }else{
             System.out.println("hi from mapToExpenseEntity");
           return null;
        }

    }


    public Expense mapToExpense(ExpenseEntity expense) {

        Category cat = new Category( expense.getCategory().getCid(), expense.getCategory().getCategoryName(), expense.getCategory().getExpenses().stream().map(expenseEntity -> expenseEntity.getTid()).collect(Collectors.toList()));

        return new Expense(expense.getTid(),cat,expense.getExpenseName(), expense.getExpenseDate(), expense.getDescription(),expense.getExpenseTotal(), expense.getRegretted());

    }

}
