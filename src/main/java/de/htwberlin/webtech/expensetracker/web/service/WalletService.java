package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.WalletEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.WalletRepository;
import de.htwberlin.webtech.expensetracker.web.model.Wallet;
import de.htwberlin.webtech.expensetracker.web.model.WalletManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public List<Wallet> fetchAllWallets() {
        return this.walletRepository.findAll().stream().map(walletEntity -> mapToWallet(walletEntity)).collect(Collectors.toList());
    }

    public Wallet addWallet(WalletManipulationRequest walletReq) {
        LocalDate validUntil = walletReq.getValidFrom() == null ? null : walletReq.getValidUntil();
        WalletEntity newWallet = new WalletEntity(walletReq.getWalletName(), walletReq.getBalance(), walletReq.getValidFrom(), validUntil);
        WalletEntity savedWallet = this.walletRepository.save(newWallet);
        if (savedWallet != null && savedWallet.getWid() > 0) return mapToWallet(savedWallet);
        else return null;
    }

    public Wallet findWalletById(Long id){
        Optional<WalletEntity> walletById = this.walletRepository.findById(id);
        return walletById.map(walletEntity -> mapToWallet(walletEntity)).orElseThrow(()-> new ResourceNotFound("Wallet " + id + " not found"));
    }


    public Wallet mapToWallet(WalletEntity walletEntity) {
        List<Long> expenses =  walletEntity.getExpenses().stream().map(expenseEntity -> expenseEntity.getTid()).collect(Collectors.toList());
        List<Long> incomes = walletEntity.getIncomes().stream().map(incomeEntity -> incomeEntity.getIid()).collect(Collectors.toList());
        Wallet wallet = new Wallet(walletEntity.getWid(), walletEntity.getWalletName(), walletEntity.getBalance(), walletEntity.getValidFrom(), walletEntity.getValidUntil(), expenses, incomes);
        return wallet;
    }
}
