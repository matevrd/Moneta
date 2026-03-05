package bank.model;

import bank.exception.InsufficientFundsException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAccount implements Account {
    private double balance;
    private String accountnumber;
    private String ownername;
    private List<Transaction> transaction = new ArrayList<>();

    public BaseAccount(double balance, String accountnumber, String ownername){
        this.balance = balance;
        this.accountnumber = accountnumber;
        this.ownername = ownername;
    }

    @Override
    public void deposit(double amount){
       this.balance = balance + amount;
       transaction.add(new Transaction("DEPOSIT", amount, String.format("Erhöhung: %.2f EUR", amount)));
    }

    @Override
    public void withdraw(double amount){
        if (this.balance >= amount){
            this.balance -= amount;
            transaction.add(new Transaction("WITHDRAWAL", amount, String.format("Abhebung: %.2f EUR", amount)));
        } else {
         throw new InsufficientFundsException(String.format("Transaktion fehlgeschlagen - unzureichendes Guthaben.%nKontostand: %.2f EUR%nAngeforderter Betrag: %.2f EUR%nFehlender Betrag: %.2f EUR", balance, amount, (amount - balance)));}
    };

    @Override
    public double getBalance(){
        return balance;
    }

    @Override
    public String getAccountNumber(){
        return accountnumber;
    }

    @Override
    public String getOwnerName(){
        return ownername;
    }

    @Override
    public List<Transaction> getTransactions(){
        return transaction;
    }

    @Override
    public void printTransactions(){
        for (Transaction t : transaction){
            System.out.println(t);
        }
    }

    public abstract String getAccountType();
}
