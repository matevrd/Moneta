package bank.model;

public class CurrentAccount extends BaseAccount {
    public CurrentAccount(double balance, String accountNumber, int userID) {
        super(balance, accountNumber, userID);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }
}