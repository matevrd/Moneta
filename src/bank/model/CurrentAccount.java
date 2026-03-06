package bank.model;

public class CurrentAccount extends BaseAccount {
    public CurrentAccount(double balance, String accountNumber, String ownerName) {
        super(balance, accountNumber, ownerName);
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }
}