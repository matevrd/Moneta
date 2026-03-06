package bank.service;

import bank.exception.AccountNotFoundException;
import bank.model.Account;
import bank.model.CurrentAccount;
import bank.model.SavingsAccount;
import bank.model.User;
import java.util.HashMap;
import java.util.Map;

public class BankService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private int accountCounter = 1;

    public CurrentAccount createCurrentAccount(String ownerName, double initialBalance) {
        String accountNumber = String.format("ACC-%04d", accountCounter++);
        CurrentAccount account = new CurrentAccount(initialBalance, accountNumber, ownerName);
        accounts.put(accountNumber, account);
        return account;
    }

    public SavingsAccount createSavingsAccount(String ownerName, double initialBalance, double interestRate) {
        String accountNumber = String.format("ACC-%04d", accountCounter++);
        SavingsAccount account = new SavingsAccount(initialBalance, accountNumber, ownerName, interestRate);
        accounts.put(accountNumber, account);
        return account;
    }

    public void register(String firstName, String lastName, String birthday, String address, String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Benutzername bereits vergeben: " + username);
        }
        User user = new User(firstName, lastName, birthday, address, username, password);
        users.put(username, user);
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new AccountNotFoundException("Ungültiger Benutzername oder Passwort.");
        }
        return user;
    }

    public void deposit(String accountNumber, double amount){
        Account account = getAccount(accountNumber);
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount){
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
    }

    public Account getAccount(String accountNumber){
        Account account = accounts.get(accountNumber);
        if (account == null) {
        throw new AccountNotFoundException("Konto nicht gefunden: " + accountNumber);
        }
        return account;
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount){
        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);
        from.withdraw(amount);
        to.deposit(amount);
    }
}