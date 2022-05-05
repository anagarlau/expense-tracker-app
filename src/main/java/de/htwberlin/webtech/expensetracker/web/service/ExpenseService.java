package de.htwberlin.webtech.expensetracker.web.service;
import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.exceptions.TransactionOutOfBounds;
import de.htwberlin.webtech.expensetracker.persistence.entities.*;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.ExpenseRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;
    private IncomeRepository incomeRepository;
     private UserService userService;


    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository , CategoryRepository categoryRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.incomeRepository=incomeRepository;
        this.userService = userService;

    }

    public List<Transaction> findAllForLoggedInUser() {
        return this.expenseRepository.findAll().stream()
                .filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUserEntity().getUid())
                .map(expenseEntity -> this.mapToExpense(expenseEntity))
                .collect(Collectors.toList());
    }

    public Expense createExpense(TransactionManipulationRequest expenseRequest) {
        Optional<CategoryEntity> categoryById = expenseRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findById(expenseRequest.getCid());

        if (categoryById.isEmpty() ||   expenseRequest.getTransactionDate() == null  ) {
            throw new ResourceNotFound("");
        }

        if(categoryById.get().getCategoryType() != CategoryType.EXPENSE  )
            throw new TransactionOutOfBounds("Wrong Category ");

        ExpenseEntity expenseEntity =
                new ExpenseEntity(this.userService.getLoggedInUserEntity(),categoryById.get(),expenseRequest.getTransactionDescription(), expenseRequest.getTransactionTotal(), expenseRequest.getTransactionDate());
        ExpenseEntity savedExpense = this.expenseRepository.save(expenseEntity);
        if (savedExpense != null && savedExpense.getId() > 0) return mapToExpense(savedExpense);
        else return null;

    }


    public Expense fetchExpenseById(Long tid) {
        Optional<ExpenseEntity> expenseById = this.expenseRepository.findById(tid);

        return expenseById.map(expenseEntity -> {
            if(expenseById.get().getUser().getUid() == this.userService.getLoggedInUserEntity().getUid()){
                return  mapToExpense(expenseEntity);
            }
            return null;
        }).orElseThrow(()-> new ResourceNotFound("Expense " + tid + " not found"));
    }


    public Expense update(Long id, TransactionManipulationRequest expenseRequest) {
        Optional<ExpenseEntity> toBeUpdatedById = this.expenseRepository.findById(id);

        if (toBeUpdatedById.isPresent()) {
             ExpenseEntity expenseEntity = toBeUpdatedById.get();
             if(expenseEntity.getUser().getUid() != this.userService.getLoggedInUserEntity().getUid()) throw new ResourceNotFound("Resource not found");

            expenseEntity.setTransactionDescription(expenseRequest.getTransactionDescription() != null ? expenseRequest.getTransactionDescription() : expenseEntity.getTransactionDescription());

            expenseEntity.setTransactionTotal(expenseRequest.getTransactionTotal() != null ? expenseRequest.getTransactionTotal() : expenseEntity.getTransactionTotal());
            expenseEntity.setTransactionDate(expenseRequest.getTransactionDate() != null ? expenseRequest.getTransactionDate() : expenseEntity.getTransactionDate());
            if (expenseRequest.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findById(expenseRequest.getCid());
                if(catById.isEmpty() || catById.get().getCategoryType() != CategoryType.EXPENSE) throw new TransactionOutOfBounds("Wrong category");
                else expenseEntity.setCategory(catById.orElse(expenseEntity.getCategory()));
            }

            ExpenseEntity savedEntity = this.expenseRepository.save(expenseEntity);
            return mapToExpense(savedEntity);
        } else {
            return null;
        }

    }


    private ExpenseEntity mapToExpenseEntity(ExpenseManipulationRequest expense) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(expense.getCid());
        if (categoryById.isPresent() ) {
            return new ExpenseEntity(userService.getLoggedInUserEntity(), categoryById.get(), expense.getTransactionDescription(), expense.getTransactionTotal(), expense.getTransactionDate() );
        } else {
            return null;
        }

    }


    private Expense mapToExpense(ExpenseEntity expense) {

        Category cat = new Category(expense.getCategory().getCid(), expense.getCategory().getCategoryName(), expense.getCategory().getCategoryType().name(),expense.getCategory().getExpenses().stream().map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));


        return new Expense(expense.getId(),this.userService.getLoggedInUser().getUid(),cat, expense.getTransactionDate(), expense.getTransactionDescription(), expense.getTransactionTotal());

    }


}
