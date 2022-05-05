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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        return this.expenseRepository.findByUserUid(userService.getLoggedInUser().getUid())
                .stream().map(expenseEntity -> mapToExpense(expenseEntity)).collect(Collectors.toList());
    }


    public Expense createExpense(TransactionManipulationRequest expenseRequest) {
        Optional<CategoryEntity> categoryById = expenseRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findByCidAndUserUidAndCategoryType(expenseRequest.getCid(), this.userService.getLoggedInUser().getUid(), CategoryType.EXPENSE);

        if (categoryById.isEmpty() ||   expenseRequest.getTransactionDate() == null  ) {
            throw new ResourceNotFound("");
        }


        ExpenseEntity expenseEntity =
                new ExpenseEntity(this.userService.getLoggedInUserEntity(),categoryById.get(),expenseRequest.getTransactionDescription(), expenseRequest.getTransactionTotal(), expenseRequest.getTransactionDate());
        ExpenseEntity savedExpense = this.expenseRepository.save(expenseEntity);
        if (savedExpense != null && savedExpense.getId() > 0) return mapToExpense(savedExpense);
        else return null;

    }


    public Expense fetchExpenseById(Long tid) {
        Optional<ExpenseEntity> expenseById =
                this.expenseRepository.findByIdAndUserUidAndAndCategory_CategoryType(tid, this.userService.getLoggedInUserEntity().getUid(), CategoryType.EXPENSE);
         return expenseById.map(expenseEntity ->  mapToExpense(expenseEntity)).orElseThrow(() -> new ResourceNotFound("Expense not found"));

    }


    public Expense update(Long id, TransactionManipulationRequest expenseRequest) {
        Optional<ExpenseEntity> toBeUpdatedById = this.expenseRepository.findByIdAndUserUidAndAndCategory_CategoryType(id, this.userService.getLoggedInUserEntity().getUid(), CategoryType.EXPENSE);

        if (toBeUpdatedById.isPresent()) {
             ExpenseEntity expenseEntity = toBeUpdatedById.get();
             expenseEntity.setTransactionDescription(expenseRequest.getTransactionDescription() != null ? expenseRequest.getTransactionDescription() : expenseEntity.getTransactionDescription());
             expenseEntity.setTransactionTotal(expenseRequest.getTransactionTotal() != null ? expenseRequest.getTransactionTotal() : expenseEntity.getTransactionTotal());
             expenseEntity.setTransactionDate(expenseRequest.getTransactionDate() != null ? expenseRequest.getTransactionDate() : expenseEntity.getTransactionDate());
            if (expenseRequest.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUidAndCategoryType(expenseRequest.getCid(), this.userService.getLoggedInUserEntity().getUid(),CategoryType.EXPENSE);
                if(catById.isEmpty()) throw new TransactionOutOfBounds("Wrong category");
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

        Category cat = new Category(expense.getCategory().getCid(), expense.getUser().getUid(),expense.getCategory().getCategoryName(), expense.getCategory().getCategoryType().name(),
                expense.getCategory().getExpenses().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()),
                expense.getCategory().getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        return new Expense(expense.getId(),this.userService.getLoggedInUser().getUid(),cat, expense.getTransactionDate(), expense.getTransactionDescription(), expense.getTransactionTotal());

    }


}
