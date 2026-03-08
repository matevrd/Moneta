package bank.service;

import java.util.HashMap;
import java.util.Map;

import bank.exception.AccountNotFoundException;
import bank.exception.AuthenticationException;
import bank.model.Account;
import bank.model.CurrentAccount;
import bank.model.SavingsAccount;
import bank.model.User;

public class BankService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private int accountCounter = 1;

    public BankService() {}

    /** Seeds a test user for local development. Do NOT call in production. */
    public static BankService withDevUser() {
        BankService svc = new BankService();
        User dev = new User("test", "test");
        dev.setFirstName("Max");
        dev.setLastName("Mustermann");
        dev.setBirthday("01.01.1990");
        dev.setAddress("Musterstraße 1, Berlin");
        svc.users.put("test", dev);
        svc.createCurrentAccount(dev, 1500.00);
        return svc;
    }

    public CurrentAccount createCurrentAccount(User owner, double initialBalance) {
        String accountNumber = String.format("ACC-%04d", accountCounter++);
        CurrentAccount account = new CurrentAccount(initialBalance, accountNumber, owner.getUsername());
        accounts.put(accountNumber, account);
        owner.addAccount(account);
        return account;
    }

    public SavingsAccount createSavingsAccount(User owner, double initialBalance, double interestRate) {
        String accountNumber = String.format("ACC-%04d", accountCounter++);
        SavingsAccount account = new SavingsAccount(initialBalance, accountNumber, owner.getUsername(), interestRate);
        accounts.put(accountNumber, account);
        owner.addAccount(account);
        return account;
    }

    public void register(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Benutzername bereits vergeben: " + username);
        }
        User user = new User(username, password);
        users.put(username, user);
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Ungültiger Benutzername oder Passwort.");
        }
        return user;
    }

    public void deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
    }

    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Konto nicht gefunden: " + accountNumber);
        }
        return account;
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);
        from.withdraw(amount);
        to.deposit(amount);
    }
}