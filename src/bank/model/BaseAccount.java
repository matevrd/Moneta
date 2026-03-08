package bank.model;

import bank.exception.InsufficientFundsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAccount implements Account {
    private double balance;
    private final String accountNumber;
    private final String ownerName;
    private final List<Transaction> transactions = new ArrayList<>();

    public BaseAccount(double balance, String accountNumber, String ownerName) {
        if (balance < 0) {
            throw new IllegalArgumentException("Anfangssaldo darf nicht negativ sein.");
        }
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Einzahlungsbetrag muss positiv sein.");
        }
        this.balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, String.format("Einzahlung: %.2f EUR", amount)));
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Abhebungsbetrag muss positiv sein.");
        }
        if (this.balance < amount) {
            throw new InsufficientFundsException(String.format(
                    "Transaktion fehlgeschlagen - unzureichendes Guthaben.%n"
                    + "Kontostand: %.2f EUR%n"
                    + "Angeforderter Betrag: %.2f EUR%n"
                    + "Fehlender Betrag: %.2f EUR",
                    balance, amount, amount - balance));
        }
        this.balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, String.format("Abhebung: %.2f EUR", amount)));
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
