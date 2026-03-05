package bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private double amount;
    private String description;
    private LocalDateTime date = LocalDateTime.now();

    public Transaction (String type, Double amount, String description){
    this.type = type;
    this.amount = amount;
    this.description = description;
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    public String getDescription(){
        return description;
    }

    public LocalDateTime getCurrentDate(){
        return date;
    }

    public String toString(String type, Double amount, String desciption, LocalDateTime date){
        return type + " | " + amount + " EUR | " + date + " | " + description;
    }
}