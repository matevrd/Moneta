package bank.model;

public class SavingsAccount extends BaseAccount {
    private final double interestRate;

    public SavingsAccount(double balance, String accountNumber, String ownerName, double interestRate) {
        super(balance, accountNumber, ownerName);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Zinssatz darf nicht negativ sein.");
        }
        this.interestRate = interestRate;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void applyInterestRate() {
        double interest = getBalance() * interestRate;
        if (interest > 0) {
            deposit(interest);
        }
    }
}
