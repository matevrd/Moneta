package bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final String firstName;
    private final String lastName;
    private final String birthday;
    private final String address;
    private final String username;
    private final String password;
    private final List<Account> accounts = new ArrayList<>();

    public User(String firstName, String lastName, String birthday, String address, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}
