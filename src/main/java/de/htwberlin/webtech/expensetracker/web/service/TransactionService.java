package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.*;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.TransactionRepository;
import de.htwberlin.webtech.expensetracker.utils.DateUtils;
import de.htwberlin.webtech.expensetracker.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    public IBalance calculateBalance() {
        IBalance iBalance = this.transactionRepository.sumAcrossCategory(userService.getLoggedInUserEntity().getUid());
        iBalance = iBalance == null ? iBalance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO) : iBalance;
        return iBalance;
    }


    public List<Transaction> fetchAll(LocalDate from, LocalDate to) {
        return this.transactionRepository.findByUserUid(this.userService.getLoggedInUser().getUid())
                .stream()
                .filter(tr -> DateUtils.isDateInRange(from, to, tr.getTransactionDate()))
                .map(tr -> mapToTransaction(tr)).collect(Collectors.toList());

    }

    public List<Transaction> fetchAllByType(CategoryType categoryType, LocalDate from, LocalDate to) {
        return this.transactionRepository.findByUserUidAndAndCategory_CategoryType(this.userService.getLoggedInUserEntity().getUid(), categoryType)
                .stream()
                .filter(tr -> DateUtils.isDateInRange(from, to, tr.getTransactionDate()))
                .map(tr -> mapToTransaction(tr)).collect(Collectors.toList());
    }


    public List<Transaction> fetchAllByCid(Long cid, LocalDate from, LocalDate to) {
        Long uid = this.userService.getLoggedInUserEntity().getUid();
        Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUid(cid, uid);
        CategoryEntity categoryEntity = catById.orElseThrow(() -> new ResourceNotFound("Category with ID " + cid + " not found for User " + uid));
        return categoryEntity.getTransactions()
                .stream()
                .filter(tr -> DateUtils.isDateInRange(from, to, tr.getTransactionDate()))
                .map(tr -> mapToTransaction(tr)).collect(Collectors.toList());
    }

    public Transaction fetchTransactionById(Long tid) {
        Long uid = this.userService.getLoggedInUserEntity().getUid();
        Optional<TransactionEntity> transaction =
                this.transactionRepository.findByIdAndUserUid(tid, uid);
        return transaction.map(transactionEntity -> mapToTransaction(transactionEntity)).orElseThrow(() -> new ResourceNotFound("Expense not found"));

    }


    /*TODO: replace switch with lambda switch */
    public Transaction createTransaction(String transactionType, TransactionManipulationRequest request) {

        Optional<CategoryEntity> categoryById =
                request.getCid() == null ? Optional.empty() :
                        this.categoryRepository.findByCidAndUserUidAndCategoryType(request.getCid(), this.userService.getLoggedInUser().getUid(), setCategoryType(transactionType));

        if (categoryById.isEmpty()) throw new ResourceNotFound("Wrong category");
        TransactionEntity transactionEntity =
                new TransactionEntity(this.userService.getLoggedInUserEntity(), categoryById.get(), request.getTransactionDescription(), request.getTransactionTotal(), LocalDate.parse(request.getTransactionDate()));
        TransactionEntity transaction = this.transactionRepository.save(transactionEntity);
        if (transaction != null && transaction.getId() > 0) return mapToTransaction(transaction);
        else return null;

    }


    public Transaction update(Long id, TransactionManipulationRequest request) {
        Long uid = this.userService.getLoggedInUserEntity().getUid();
        Optional<TransactionEntity> toBeUpdatedById = this.transactionRepository.findByIdAndUserUid(id, uid);
        if (toBeUpdatedById.isPresent()) {
            TransactionEntity transactionEntity = toBeUpdatedById.get();
            transactionEntity.setTransactionDescription(request.getTransactionDescription() != null ? request.getTransactionDescription() : transactionEntity.getTransactionDescription());
            transactionEntity.setTransactionTotal(request.getTransactionTotal() != null ? request.getTransactionTotal() : transactionEntity.getTransactionTotal());
            transactionEntity.setTransactionDate(request.getTransactionDate() != null ? LocalDate.parse(request.getTransactionDate()) : transactionEntity.getTransactionDate());
            if (request.getCid() != null) {
                Optional<CategoryEntity> catById = this.categoryRepository.findByCidAndUserUid(request.getCid(), uid);
                if (catById.isEmpty()) throw new ResourceNotFound("Category " + request.getCid() + " does not exist");
                else transactionEntity.setCategory(catById.orElse(transactionEntity.getCategory()));
            }
            TransactionEntity savedEntity = this.transactionRepository.save(transactionEntity);
            return mapToTransaction(savedEntity);
        } else {
            return null;
        }
    }


    public boolean deleteTransaction(Long tid) {
        Optional<TransactionEntity> transaction = this.transactionRepository.findByIdAndUserUid(tid, userService.getLoggedInUserEntity().getUid());
        if(transaction.isEmpty()) {
            throw new ResourceNotFound("Transaction " + tid + " does not exist in DB");
        }
        transactionRepository.deleteById(tid);
        return true;
    }

    /*Helper Methods*/
    private TransactionEntity mapToTransactionEntity(TransactionManipulationRequest request) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(request.getCid());
        if (categoryById.isPresent()) {
            UserEntity loggedInUserEntity = userService.getLoggedInUserEntity();
            return new TransactionEntity(loggedInUserEntity, categoryById.get(), request.getTransactionDescription(), request.getTransactionTotal(), LocalDate.parse(request.getTransactionDate()));
        } else {
            return null;
        }

    }


    private Transaction mapToTransaction(TransactionEntity transactionEntity) {

        Category cat = new Category(transactionEntity.getCategory().getCid(), transactionEntity.getUser().getUid(), transactionEntity.getCategory().getCategoryName(), transactionEntity.getCategory().getCategoryType().name(),
                transactionEntity.getCategory().getTransactions().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList()));
        return new Transaction(transactionEntity.getId(), this.userService.getLoggedInUser().getUid(), cat, transactionEntity.getTransactionDate(), transactionEntity.getTransactionDescription(), transactionEntity.getTransactionTotal());

    }

    private CategoryType setCategoryType(String transactionType) {
        CategoryType toCompare;
        switch (transactionType) {
            case "expenses":
                toCompare = CategoryType.EXPENSE;
                break;
            case "incomes":
                toCompare = CategoryType.INCOME;
                break;
            default:
                throw new ResourceNotFound("Category type " + transactionType + " does not exist");
        }
        return toCompare;
    }

}
