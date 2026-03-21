package bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bank.model.Account;
import bank.model.CurrentAccount;
import bank.model.SavingsAccount;

public class AccountRepository {

    public List<Account> findByUserID(int user_id) {
        String sql = """
                SELECT *
                FROM accounts
                WHERE user_id = ?
                """;
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double balance = rs.getDouble("balance");
                String accountNumber = rs.getString("account_number");
                int userID = rs.getInt("user_id");

                Account account;
                if (type.equals("CURRENT")) {
                    account = new CurrentAccount(balance, accountNumber, userID);
                } else {
                    double interestRate = rs.getDouble("interest_rate");
                    account = new SavingsAccount(balance, accountNumber, userID, interestRate);
                }
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return accounts;
        }
    }

    public void save(Account account) {
        String sql = """
                INSERT INTO accounts (user_id, type, balance, account_number, interest_rate, created_at)
                VALUES (?, ?, ?, ?, ?, datetime('now'))
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account.getUserID());
            stmt.setString(2, account.getAccountType().name());
            stmt.setDouble(3, account.getBalance());
            stmt.setString(4, account.getAccountNumber());

            if (account instanceof SavingsAccount sa) {
                stmt.setDouble(5, sa.getInterestRate());
            } else {
                stmt.setNull(5, java.sql.Types.REAL);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}