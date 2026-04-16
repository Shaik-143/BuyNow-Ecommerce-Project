package com.buynow.service;

import java.util.List;

import com.buynow.model.Order;
import com.buynow.model.Seller;
import com.buynow.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySeller(Seller seller);
    List<Transaction>getAllTransactions();
}
