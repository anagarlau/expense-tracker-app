package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.exceptions.TransactionOutOfBounds;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class IncomeService implements TransactionService{
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
                .stream().map(expenseEntity -> mapToSpecificTransaction(expenseEntity)).collect(Collectors.toList());
    }



    public Transaction createTransaction(TransactionManipulationRequest incomeRequest) {
        System.out.println(incomeRequest.getCid());
        Optional<CategoryEntity> categoryById = incomeRequest.getCid() == null ? Optional.empty() : this.categoryRepository.findByCidAndUserUidAndCategoryType(incomeRequest.getCid(), this.userService.getLoggedInUser().getUid(), CategoryType.INCOME);

        if (categoryById.isEmpty() ||   incomeRequest.getTransactionDate() == null  ) {
            throw new ResourceNotFound("");
        }


        IncomeEntity incomeEntity =
                new IncomeEntity(this.userService.getLoggedInUserEntity(),categoryById.get(),incomeRequest.getTransactionDescription(), incomeRequest.getTransactionTotal(), incomeRequest.getTransactionDate());
        IncomeEntity savedIncome = this.incomeRepository.save(incomeEntity);
        if (savedIncome != null && savedIncome.getId() > 0) return mapToSpecificTransaction(savedIncome);
        else return null;

    }


    public Transaction fetchTransactionById(Long tid) {
        Optional<IncomeEntity> incomeById =
                this.incomeRepository.findByIdAndUserUidAndAndCategory_CategoryType(tid, this.userService.getLoggedInUserEntity().getUid(), CategoryType.INCOME);
         return incomeById.map(expenseEntity ->  mapToSpecificTransaction(expenseEntity)).orElseThrow(() -> new ResourceNotFound("Income not found"));

    }


    public Transaction update(Long id, TransactionManipulationRequest request) {
        Optional<IncomeEntity> toBeUpdatedById = this.incomeRepository.findByIdAndUserUidAndAndCategory_CategoryType(id, this.userService.getLoggedInUserEntity().getUid(), CategoryType.INCOME);

        if (toBeUpdatedById.isPresent()) {
             IncomeEntity incomeEntity = toBeUpdatedById.get();
             incomeEntity.setTransactionDescription(request.getTransactionDescription() != null ? request.getTransactionDescription() : incomeEntity.getTransactionDescription());
             incomeEntity.setTransactionTotal(request.getTransactionTotal() != null ? request.getTransactionTotal() : incomeEntity.getTransactionTotal());
             incomeEntity.setTransactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : incomeEntity.getTransactionDate());
            if (request.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUidAndCategoryType(request.getCid(), this.userService.getLoggedInUserEntity().getUid(),CategoryType.INCOME);
                if(catById.isEmpty()) throw new TransactionOutOfBounds("Wrong category");
                else incomeEntity.setCategory(catById.orElse(incomeEntity.getCategory()));
            }

            IncomeEntity savedEntity = this.incomeRepository.save(incomeEntity);
            return mapToSpecificTransaction(savedEntity);
        } else {
            return null;
        }

    }


    private TransactionEntity mapToIncomeEntity(TransactionManipulationRequest income) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(income.getCid());
        if (categoryById.isPresent() ) {
            return new IncomeEntity(userService.getLoggedInUserEntity(), categoryById.get(), income.getTransactionDescription(), income.getTransactionTotal(), income.getTransactionDate() );
        } else {
            return null;
        }

    }


    private Transaction mapToSpecificTransaction(TransactionEntity income) {

        Category cat = new Category(income.getCategory().getCid(),income.getUser().getUid(),income.getCategory().getCategoryName(), income.getCategory().getCategoryType().name(),
                income.getCategory().getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()),
                income.getCategory().getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        return new Income(income.getId(),this.userService.getLoggedInUser().getUid(),cat, income.getTransactionDate(), income.getTransactionDescription(), income.getTransactionTotal());

    }


}
