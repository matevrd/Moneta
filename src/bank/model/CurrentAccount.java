import bank.model.BaseAccount;

public class CurrentAccount extends BaseAccount {
    public CurrentAccount(double balance, String accountname, String ownername){
        super(balance, accountname, ownername);
    }

    @Override
    public String getAccountType(){
        return "CURRENT";
    }
}