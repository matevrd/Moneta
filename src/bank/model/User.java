package bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final int ID;
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;
    private final String username;
    private final String password;
    private final List<Account> accounts = new ArrayList<>();

    public User(int ID, String username, String password) {
        this.ID = ID;
        this.firstName = "";
        this.lastName = "";
        this.birthday = "";
        this.address = "";
        this.username = username;
        this.password = password;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getID() {
    return ID;
    }
}
