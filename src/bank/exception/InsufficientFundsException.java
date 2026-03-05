package bank.exception;

public class InsufficientFundsException extends RunTimeException{
    public InsufficientFundsException(String message){
        super(message);
    }
}