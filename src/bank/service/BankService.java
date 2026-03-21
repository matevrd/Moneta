package bank.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bank.exception.AccountNotFoundException;
import bank.exception.AuthenticationException;
import bank.model.Account;
import bank.model.CurrentAccount;
import bank.model.SavingsAccount;
import bank.model.User;
import bank.repository.AccountRepository;
import bank.repository.UserRepository;

public class BankService {
    UserRepository userRepository = new UserRepository();
    AccountRepository accountRepository = new AccountRepository();

    //
    private final Map<String, Account> accounts = new HashMap<>();
    //

    public BankService() {}

    public CurrentAccount createCurrentAccount(User owner, double initialBalance) {
        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        CurrentAccount account = new CurrentAccount(initialBalance, accountNumber, owner.getID());
        accounts.put(accountNumber, account);
        owner.addAccount(account);
        accountRepository.save(account);
        return account;
    }

    public SavingsAccount createSavingsAccount(User owner, double initialBalance, double interestRate) {
        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        SavingsAccount account = new SavingsAccount(initialBalance, accountNumber, owner.getID(), interestRate);
        accounts.put(accountNumber, account);
        owner.addAccount(account);
        accountRepository.save(account);
        return account;
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Benutzername bereits vergeben: " + username);
        }
        User user = new User(0 , username, password);
        userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Ungültiger Benutzername oder Passwort.");
        }
        List<Account> accounts = accountRepository.findByUserID(user.getID());
        for (Account account : accounts){
            user.addAccount(account);
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

    public void updatePorfile(User user){
        userRepository.update(user);
    }
}