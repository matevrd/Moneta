package bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bank.model.User;

public class UserRepository {

    public User findByUsername(String username){
            String sql = """
                        SELECT *
                        FROM users
                        WHERE username = ?
                        """;
            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setString(1, username);
                    ResultSet rs = stmt.executeQuery();

                    if(rs.next()){
                        User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                        user.setFirstName(rs.getString("firstname"));
                        user.setLastName(rs.getString("lastname"));
                        user.setBirthday(rs.getString("birthday"));
                        user.setAddress(rs.getString("address"));
                        
                        return user;
                    }
                return null;
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
                return null;
            } 
    }

    public void save(User user){
        String sql = """
                INSERT INTO users (username, password)
                VALUES (?, ?);
                """;
          try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, user.getPassword());
                    stmt.executeUpdate();
                }
                catch (SQLException e){
                    System.err.println(e.getMessage());
                }
    }

     public void update(User user){
        String sql = """
                UPDATE users
                SET firstname = ?,
                    lastname = ?,
                    birthday = ?,
                    adress = ?
                WHERE id = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(4, user.getAddress());
                stmt.setInt(5, user.getID());
                stmt.setString(3, user.getBirthday());
                stmt.executeUpdate();
            } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
