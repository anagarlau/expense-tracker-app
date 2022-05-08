package de.htwberlin.webtech.expensetracker.web.service;
import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.exceptions.TransactionOutOfBounds;
import de.htwberlin.webtech.expensetracker.persistence.entities.*;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.TransactionRepository;
import de.htwberlin.webtech.expensetracker.utils.DateUtils;
import de.htwberlin.webtech.expensetracker.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TransactionService {
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private UserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
       this.userService = userService;

    }


    public List<Transaction> fetchAllTransactions(LocalDate from, LocalDate to){
          return this.transactionRepository.findByUserUid(this.userService.getLoggedInUser().getUid())
                    .stream()
                    .filter(tr-> DateUtils.isDateInRange(from, to, tr.getTransactionDate()))
                    .map(tr-> mapToTransaction(tr)).collect(Collectors.toList());

    }
    public List<Transaction> fetchAllByType(CategoryType categoryType, LocalDate from, LocalDate to){
        return this.transactionRepository.findByUserUidAndAndCategory_CategoryType(this.userService.getLoggedInUserEntity().getUid(), categoryType)
                .stream()
                .filter(tr-> DateUtils.isDateInRange(from, to, tr.getTransactionDate()))
                .map(tr-> mapToTransaction(tr)).collect(Collectors.toList());
    }


    public Transaction fetchTransactionById(Long tid) {
        Optional<TransactionEntity> expenseById =
                this.transactionRepository.findByIdAndUserUid(tid, this.userService.getLoggedInUserEntity().getUid());
        return expenseById.map(expenseEntity ->  mapToTransaction(expenseEntity)).orElseThrow(() -> new ResourceNotFound("Expense not found"));

    }


    /*TODO: replace switch with lambda switch */
    public Transaction createTransaction(String transactionType, TransactionManipulationRequest request) {
        Optional<CategoryEntity> categoryById =
                request.getCid() == null ? Optional.empty() :
                        this.categoryRepository.findByCidAndUserUidAndCategoryType(request.getCid(), this.userService.getLoggedInUser().getUid(), setCategoryType(transactionType));

        if (categoryById.isEmpty() ||   request.getTransactionDate() == null  ) {
            throw new ResourceNotFound("Wrong category");
        }

        TransactionEntity transactionEntity =
                new TransactionEntity(this.userService.getLoggedInUserEntity(),categoryById.get(),request.getTransactionDescription(), request.getTransactionTotal(), request.getTransactionDate());
        TransactionEntity savedExpense = this.transactionRepository.save(transactionEntity);
        if (savedExpense != null && savedExpense.getId() > 0) return mapToTransaction(savedExpense);
        else return null;

    }




    public Transaction update(Long id, TransactionManipulationRequest request) {
        Optional<TransactionEntity> toBeUpdatedById = this.transactionRepository.findByIdAndUserUid(id, this.userService.getLoggedInUserEntity().getUid());
        if (toBeUpdatedById.isPresent()) {
             TransactionEntity expenseEntity = toBeUpdatedById.get();
             expenseEntity.setTransactionDescription(request.getTransactionDescription() != null ? request.getTransactionDescription() : expenseEntity.getTransactionDescription());
             expenseEntity.setTransactionTotal(request.getTransactionTotal() != null ? request.getTransactionTotal() : expenseEntity.getTransactionTotal());
             expenseEntity.setTransactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : expenseEntity.getTransactionDate());
            if (request.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUid(request.getCid(), this.userService.getLoggedInUserEntity().getUid());
                if(catById.isEmpty()) throw new TransactionOutOfBounds("Wrong category");
                else expenseEntity.setCategory(catById.orElse(expenseEntity.getCategory()));
            }
            TransactionEntity savedEntity = this.transactionRepository.save(expenseEntity);
            return mapToTransaction(savedEntity);
        } else {
            return null;
        }
    }



    /*Helper Methods*/
    private TransactionEntity mapToTransactionEntity(TransactionManipulationRequest expense) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(expense.getCid());
        if (categoryById.isPresent() ) {
            return new TransactionEntity(userService.getLoggedInUserEntity(), categoryById.get(), expense.getTransactionDescription(), expense.getTransactionTotal(), expense.getTransactionDate() );
        } else {
            return null;
        }

    }


    private Transaction mapToTransaction(TransactionEntity expense) {

        Category cat = new Category(expense.getCategory().getCid(),expense.getUser().getUid(),expense.getCategory().getCategoryName(), expense.getCategory().getCategoryType().name(),
                expense.getCategory().getTransactions().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        return new Expense(expense.getId(),this.userService.getLoggedInUser().getUid(),cat, expense.getTransactionDate(), expense.getTransactionDescription(), expense.getTransactionTotal());

    }

    private CategoryType setCategoryType(String transactionType){
        CategoryType toCompare;
        switch (transactionType) {
            case "expenses":
                toCompare = CategoryType.EXPENSE;
                break;
            case "incomes":
                toCompare = CategoryType.INCOME;
                break;
            default:
                throw new ResourceNotFound("Wrong Category");
        }
        return toCompare;
    }

}
