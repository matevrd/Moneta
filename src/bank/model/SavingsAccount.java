package bank.model;

public class SavingsAccount extends BaseAccount {
    public SavingsAccount(double balance, String accountname, String ownername, double interestRate){
        super(balance, accountname, ownername);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType(){
        return "SAVINGS"; 
    }

    private double interestRate;
    public double getInterestRate(){
        return interestRate;
    };

    public void applyInterestRate(){
        double interest = getBalance() * interestRate;
        deposit(interest);
    }

}
