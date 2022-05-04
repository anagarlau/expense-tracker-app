package de.htwberlin.webtech.expensetracker.web.service;


import de.htwberlin.webtech.expensetracker.exceptions.TransactionOutOfBounds;
import de.htwberlin.webtech.expensetracker.persistence.entities.*;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.ExpenseRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.WalletRepository;
import de.htwberlin.webtech.expensetracker.utils.DateUtils;
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
    private WalletRepository walletRepository;


    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository , CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
        this.incomeRepository=incomeRepository;

    }

    public List<Expense> findAll() {
        return this.expenseRepository.findAll().stream().map(expenseEntity ->
                        this.mapToExpense(expenseEntity))
                .collect(Collectors.toList());
    }

    public Expense createExpense(ExpenseManipulationRequest expenseRequest) {
        Optional<CategoryEntity> categoryById = expenseRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findById(expenseRequest.getCid());
        Optional<WalletEntity> walletById = expenseRequest.getWid() == null ? Optional.empty() : this.walletRepository.findById(expenseRequest.getWid());

        if (categoryById.isEmpty() || walletById.isEmpty() || expenseRequest.getTransactionDate() == null ) {
            return null;
        }

        boolean isDateInRange = DateUtils.isDateInRange(walletById.get().getValidFrom(), walletById.get().getValidUntil(), expenseRequest.getTransactionDate());
        //TODO: for date out of range throw Exception otherwise unclear where the problem comes from
        if(!isDateInRange) return null;
        //TODO: check for right category cid
        ExpenseEntity expenseEntity = new ExpenseEntity(categoryById.get(), walletById.get(),  expenseRequest.getTransactionDescription(), expenseRequest.getTransactionTotal(), expenseRequest.getTransactionDate());
        ExpenseEntity savedExpense = this.expenseRepository.save(expenseEntity);
        if (savedExpense != null && savedExpense.getId() > 0) return mapToExpense(savedExpense);
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


            expenseEntity.setTransactionDescription(expenseRequest.getTransactionDescription() != null ? expenseRequest.getTransactionDescription() : expenseEntity.getTransactionDescription());

            expenseEntity.setTransactionTotal(expenseRequest.getTransactionTotal() != null ? expenseRequest.getTransactionTotal() : expenseEntity.getTransactionTotal());
            expenseEntity.setTransactionDate(expenseRequest.getTransactionDate() != null ? expenseRequest.getTransactionDate() : expenseEntity.getTransactionDate());
            if (expenseRequest.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findById(expenseRequest.getCid());
                expenseEntity.setCategory(catById.orElse(expenseEntity.getCategory()));
            }

            if(expenseRequest.getWid() != null){
                Optional<WalletEntity> walletById = this.walletRepository.findById(expenseRequest.getWid());
                expenseEntity.setWallet(walletById.orElse(expenseEntity.getWallet()));
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
            return new ExpenseEntity(categoryById.get(),walletById.get(), expense.getTransactionDescription(), expense.getTransactionTotal(), expense.getTransactionDate() );
        } else {
            return null;
        }

    }


    private Expense mapToExpense(ExpenseEntity expense) {

        Category cat = new Category(expense.getCategory().getCid(), expense.getCategory().getCategoryName(), expense.getCategory().getCategoryType().name(),expense.getCategory().getExpenses().stream().map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        List<Long> expenses = expense.getWallet().getExpenses().stream().map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        List<Long> incomes = expense.getWallet().getIncomes().stream().map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        Wallet wallet = new Wallet(expense.getWallet().getWid(), expense.getWallet().getWalletName(), expense.getWallet().getBalance(),
                expense.getWallet().getValidFrom(), expense.getWallet().getValidUntil(), expenses, incomes);
        return new Expense(expense.getId(), cat, wallet, expense.getTransactionDate(), expense.getTransactionDescription(), expense.getTransactionTotal());

    }


}
