package com.buynow.service.impl;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buynow.model.Order;
import com.buynow.model.Seller;
import com.buynow.model.Transaction;
import com.buynow.repository.SellerRepository;
import com.buynow.repository.TransactionRepository;
import com.buynow.service.TransactionService;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    //@Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, SellerRepository sellerRepository) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();
        Transaction transaction = new Transaction();
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);
        transaction.setSeller(seller);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySeller(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
