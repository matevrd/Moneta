package bank.model;

import java.util.List;

public interface Account {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
    String getAccountNumber();
    int getUserID();
    AccountType getAccountType();
    List<Transaction> getTransactions();
}
