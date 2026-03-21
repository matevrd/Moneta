package bank.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:moneta.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void initialize() throws SQLException{
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users(
                        id          INTEGER PRIMARY KEY AUTOINCREMENT,
                        username    TEXT NOT NULL UNIQUE,
                        password    TEXT NOT NULL,
                        firstname   TEXT DEFAULT '',
                        lastname    TEXT DEFAULT '',
                        birthday    TEXT DEFAULT '',
                        address     TEXT DEFAULT ''
                        )
            """);

            stmt.executeUpdate("""
                    CREATE TABLE IF     NOT EXISTS accounts(
                        id              INTEGER PRIMARY KEY AUTOINCREMENT,
                        account_number  TEXT NOT NULL,
                        user_id         INTEGER NOT NULL,
                        type            TEXT NOT NULL,
                        balance         REAL NOT NULL,
                        interest_rate   REAL,
                        created_at      TEXT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id))
            """);
                } catch (SQLException e){
                    throw new RuntimeException("Database init failed: " + e.getMessage(), e);
                }
    }
}