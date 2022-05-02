package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Wallet;
import de.htwberlin.webtech.expensetracker.web.model.WalletManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    private WalletService walletService;


    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }




    @GetMapping("/wallets")
    public ResponseEntity<List<Wallet>> fetchAllWallets(){
        List<Wallet> wallets = this.walletService.fetchAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/wallets")
    public ResponseEntity<Void> addWallet(@RequestBody WalletManipulationRequest walletReq) throws URISyntaxException {
        Wallet wallet = this.walletService.addWallet(walletReq);
        if(wallet !=null){
            URI uri = new URI("/api/v1/wallets/" + wallet.getWid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<Wallet> fetchWalletById(@PathVariable Long id){
        Wallet walletById = this.walletService.findWalletById(id);
        return ResponseEntity.ok(walletById);
    }
}
