package de.htwberlin.webtech.expensetracker.web.service;


import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.WalletEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.ExpenseRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.WalletRepository;
import de.htwberlin.webtech.expensetracker.utils.DateUtils;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.Expense;
import de.htwberlin.webtech.expensetracker.web.model.ExpenseManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;
    private WalletRepository walletRepository;


    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

    }

    public List<Expense> findAll() {
        return this.expenseRepository.findAll().stream().map(expenseEntity ->
                        this.mapToExpense(expenseEntity))
                .collect(Collectors.toList());
    }

    public Expense createExpense(ExpenseManipulationRequest expenseRequest) {
        Optional<CategoryEntity> categoryById = expenseRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findById(expenseRequest.getCid());
        Optional<WalletEntity> walletById = expenseRequest.getWid() == null ? Optional.empty() : this.walletRepository.findById(expenseRequest.getWid());

        if (categoryById.isEmpty() || walletById.isEmpty() || expenseRequest.getExpenseDate() == null ) {
            return null;
        }

        boolean isDateInRange = DateUtils.isDateInRange(walletById.get().getValidFrom(), walletById.get().getValidUntil(), expenseRequest.getExpenseDate());
        if(!isDateInRange) return null;
        ExpenseEntity expenseEntity = new ExpenseEntity(walletById.get(),categoryById.get(), expenseRequest.getExpenseName(), expenseRequest.getDescription(), expenseRequest.getExpenseTotal(), expenseRequest.getExpenseDate(), expenseRequest.getRegretted());
        ExpenseEntity savedExpense = this.expenseRepository.save(expenseEntity);
        if (savedExpense != null && savedExpense.getTid() > 0) return mapToExpense(savedExpense);
        else return null;

    }


    public Expense fetchExpenseById(Long tid) {
        Optional<ExpenseEntity> expenseById = this.expenseRepository.findById(tid);
        return expenseById.map(expenseEntity -> mapToExpense(expenseEntity)).orElse(null);
    }


    public Expense update(Long id, ExpenseManipulationRequest expenseRequest) {
        Optional<ExpenseEntity> toBeUpdatedById = this.expenseRepository.findById(id);
        if (toBeUpdatedById.isPresent()) {

            ExpenseEntity expenseEntity = toBeUpdatedById.get();

            expenseEntity.setExpenseName(expenseRequest.getExpenseName() != null ? expenseRequest.getExpenseName() : expenseEntity.getExpenseName());
            expenseEntity.setDescription(expenseRequest.getDescription() != null ? expenseRequest.getDescription() : expenseEntity.getDescription());
            expenseEntity.setRegretted(expenseRequest.getRegretted() != null ? expenseRequest.getRegretted() : expenseEntity.getRegretted());
            expenseEntity.setExpenseTotal(expenseRequest.getExpenseTotal() != null ? expenseRequest.getExpenseTotal() : expenseEntity.getExpenseTotal());
            expenseEntity.setExpenseDate(expenseRequest.getExpenseDate() != null ? expenseRequest.getExpenseDate() : expenseEntity.getExpenseDate());
            if (expenseRequest.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findById(expenseRequest.getCid());
                expenseEntity.setCategory(catById.orElse(expenseEntity.getCategory()));
            }


            ExpenseEntity savedEntity = this.expenseRepository.save(expenseEntity);
            return mapToExpense(savedEntity);
        } else {
            return null;
        }

    }


    private ExpenseEntity mapToExpenseEntity(ExpenseManipulationRequest expense) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(expense.getCid());
        Optional<WalletEntity> walletById = walletRepository.findById(expense.getWid());
        if (categoryById.isPresent() && walletById.isPresent()) {
            return new ExpenseEntity(walletById.get(), categoryById.get(),expense.getExpenseName(), expense.getDescription(), expense.getExpenseTotal(), expense.getExpenseDate(), expense.getRegretted());
        } else {
            return null;
        }

    }


    private Expense mapToExpense(ExpenseEntity expense) {

        Category cat = new Category(expense.getCategory().getCid(), expense.getCategory().getCategoryName(), expense.getCategory().getExpenses().stream().map(expenseEntity -> expenseEntity.getTid()).collect(Collectors.toList()));
        List<Long> expenses = expense.getWallet().getExpenses().stream().map(expenseEntity -> expenseEntity.getTid()).collect(Collectors.toList());
        List<Long> incomes = expense.getWallet().getIncomes().stream().map(expenseEntity -> expenseEntity.getIid()).collect(Collectors.toList());
        Wallet wallet = new Wallet(expense.getWallet().getWid(), expense.getWallet().getWalletName(), expense.getWallet().getBalance(),
                expense.getWallet().getValidFrom(), expense.getWallet().getValidUntil(), expenses, incomes);
        return new Expense(expense.getTid(), cat, wallet, expense.getExpenseName(), expense.getExpenseDate(), expense.getDescription(), expense.getExpenseTotal(), expense.getRegretted());

    }

}
