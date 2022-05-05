package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.exceptions.TransactionOutOfBounds;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class IncomeService {
    private CategoryRepository categoryRepository;
    private IncomeRepository incomeRepository;
    private UserService userService;


    @Autowired
    public IncomeService( IncomeRepository incomeRepository , CategoryRepository categoryRepository, UserService userService) {
       this.categoryRepository = categoryRepository;
        this.incomeRepository=incomeRepository;
        this.userService = userService;

    }


    /*all incomes*/
    public List<Transaction> findAllForLoggedInUser() {
        return this.incomeRepository.findByUserUid(userService.getLoggedInUser().getUid())
                .stream().map(expenseEntity -> mapToIncome(expenseEntity)).collect(Collectors.toList());
    }



    public Income createIncome(TransactionManipulationRequest incomeRequest) {
        System.out.println(incomeRequest.getCid());
        Optional<CategoryEntity> categoryById = incomeRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findByCidAndUserUidAndCategoryType(incomeRequest.getCid(), this.userService.getLoggedInUser().getUid(), CategoryType.INCOME);

        if (categoryById.isEmpty() ||   incomeRequest.getTransactionDate() == null  ) {
            throw new ResourceNotFound("");
        }


        IncomeEntity expenseEntity =
                new IncomeEntity(this.userService.getLoggedInUserEntity(),categoryById.get(),incomeRequest.getTransactionDescription(), incomeRequest.getTransactionTotal(), incomeRequest.getTransactionDate());
        IncomeEntity savedIncome = this.incomeRepository.save(expenseEntity);
        if (savedIncome != null && savedIncome.getId() > 0) return mapToIncome(savedIncome);
        else return null;

    }


    public Income fetchIncomeById(Long tid) {
        Optional<IncomeEntity> expenseById =
                this.incomeRepository.findByIdAndUserUidAndAndCategory_CategoryType(tid, this.userService.getLoggedInUserEntity().getUid(), CategoryType.INCOME);
         return expenseById.map(expenseEntity ->  mapToIncome(expenseEntity)).orElseThrow(() -> new ResourceNotFound("Income not found"));

    }


    public Income update(Long id, TransactionManipulationRequest expenseRequest) {
        Optional<IncomeEntity> toBeUpdatedById = this.incomeRepository.findByIdAndUserUidAndAndCategory_CategoryType(id, this.userService.getLoggedInUserEntity().getUid(), CategoryType.INCOME);

        if (toBeUpdatedById.isPresent()) {
             IncomeEntity expenseEntity = toBeUpdatedById.get();
             expenseEntity.setTransactionDescription(expenseRequest.getTransactionDescription() != null ? expenseRequest.getTransactionDescription() : expenseEntity.getTransactionDescription());
             expenseEntity.setTransactionTotal(expenseRequest.getTransactionTotal() != null ? expenseRequest.getTransactionTotal() : expenseEntity.getTransactionTotal());
             expenseEntity.setTransactionDate(expenseRequest.getTransactionDate() != null ? expenseRequest.getTransactionDate() : expenseEntity.getTransactionDate());
            if (expenseRequest.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUidAndCategoryType(expenseRequest.getCid(), this.userService.getLoggedInUserEntity().getUid(),CategoryType.INCOME);
                if(catById.isEmpty()) throw new TransactionOutOfBounds("Wrong category");
                else expenseEntity.setCategory(catById.orElse(expenseEntity.getCategory()));
            }

            IncomeEntity savedEntity = this.incomeRepository.save(expenseEntity);
            return mapToIncome(savedEntity);
        } else {
            return null;
        }

    }


    private IncomeEntity mapToIncomeEntity(IncomeManipulationRequest income) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(income.getCid());
        if (categoryById.isPresent() ) {
            return new IncomeEntity(userService.getLoggedInUserEntity(), categoryById.get(), income.getTransactionDescription(), income.getTransactionTotal(), income.getTransactionDate() );
        } else {
            return null;
        }

    }


    private Income mapToIncome(IncomeEntity expense) {

        Category cat = new Category(expense.getCategory().getCid(),expense.getUser().getUid(),expense.getCategory().getCategoryName(), expense.getCategory().getCategoryType().name(),
                expense.getCategory().getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()),
                expense.getCategory().getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        return new Income(expense.getId(),this.userService.getLoggedInUser().getUid(),cat, expense.getTransactionDate(), expense.getTransactionDescription(), expense.getTransactionTotal());

    }


}
