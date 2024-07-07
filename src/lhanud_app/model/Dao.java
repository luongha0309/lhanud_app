package lhanud_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import lhanud_app.object.Account;
import java.sql.SQLException;
import lhanud_app.object.Transaction;


public class Dao {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    
    public boolean insertAccount(Account a){
        String sql = "insert into account (username, password, balance) values (?,?,?)";
        try{
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setDouble(3, a.getBalance());
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        a.setAccountId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public Account getAccountByUsername(String username){
        String sql = "select * from account where username = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"), rs.getDouble("balance"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public String showInformation(Account account){
        String sql = "select * from account where account_id = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, account.getAccountId());
            rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("ID: " + rs.getInt("account_id"));
                System.out.println("Ten dang nhap: " + rs.getString("username"));
                System.out.println("So du: " + rs.getDouble("balance"));
            }
            return "";
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    public int getAccountIdByUsername(String username){
        String sql = "select account_id from account where username = ?";
        int account_id;
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                account_id = rs.getInt("account_id");
                return account_id;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean deposit(int accountId, double amount){
        String sqlSelect = "select balance from account where account_id = ?";
        String sqlUpdate = "update account set balance = ? where account_id = ?";
        double balance = 0;
        try{
            ps = con.prepareStatement(sqlSelect);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            if(rs.next()){
                balance = rs.getDouble("balance");
            }
            if(amount > 0){
                balance  += amount;
                ps = con.prepareStatement(sqlUpdate);
                ps.setDouble(1, balance);
                ps.setInt(2, accountId);
                ps.executeUpdate();
                return true;
            } else{
                System.err.println("So tien nap phai lon hon 0");
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Rut tien that bai!");
            return false;
        }
    }
    
    public boolean withdraw(int accountId, double amount) {
        String sqlSelect = "SELECT balance FROM account WHERE account_id = ?";
        String sqlUpdate = "UPDATE account SET balance = ? WHERE account_id = ?";
        double balance = 0;
        try {
            ps = con.prepareStatement(sqlSelect);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                ps = con.prepareStatement(sqlUpdate);
                ps.setDouble(1, balance);
                ps.setInt(2, accountId);
                ps.executeUpdate();
                return true;
            } else {
                System.err.println("So tien rut khong hop le! Vui long thu lai");
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Rut tien that bai!");
            return false;
        }
    }
    
    public boolean transfer(int fromAccountId, int toAccountId, double amount){

        try{
            String withdrawSQL = "update account set balance = balance - ? where account_id = ? and balance > ?";
            ps = con.prepareStatement(withdrawSQL);
            ps.setDouble(1, amount);
            ps.setInt(2, fromAccountId);
            ps.setDouble(3, amount);
            int rowAffected = ps.executeUpdate();
            if(rowAffected == 0){
                return false;
            }
            
            String depositSQL = "update account set balance = balance + ? where account_id = ?";
            ps = con.prepareStatement(depositSQL);
            ps.setDouble(1, amount);
            ps.setInt(2, toAccountId);
            int rowAffected2 = ps.executeUpdate();
            if(rowAffected2 == 0){
                return false;
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkExistingUsername(String toUsername){
        String sql = "select * from account where username = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, toUsername);
            rs = ps.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Khong the kiem tra tai khoan. Vui long thu lai!");
            return false;
        }
    }
    
   public boolean insertTransaction(Transaction t){
       String sql = "insert into transaction (transaction_id, account_id, transaction_type, amount, content, transaction_date) values (?,?,?,?,?,?)";
       try{
           ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ps.setInt(1, t.getTransactionId());
           ps.setInt(2, t.getAccountId());
           ps.setString(3, t.getTransactionType());
           ps.setDouble(4, t.getAmount());
           ps.setString(5, t.getContent());
           ps.setTimestamp(6, t.getTransactionDate());
           int rowAffected = ps.executeUpdate();
            if(rowAffected > 0){
                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        t.setTransactionId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
           
       }catch(SQLException e){
           e.printStackTrace();
           return false;
       }
   }
   
   public String showTransaction(int accountId){
       String sql = "select * from transaction where account_id = ? order by transaction_date desc limit 10";
       try{
           ps = con.prepareStatement(sql);
           ps.setInt(1, accountId);
           rs = ps.executeQuery();
           int i = 1;
        while(rs.next()){
            System.out.println("STT: " + i
                + "| Ma giao dich: " + rs.getInt("transaction_id") 
                + " | Loai giao dich: " + rs.getString("transaction_type") 
                + " | So tien: " + rs.getDouble("amount") 
                + " | Noi dung: " + rs.getString("content") 
                + " | Ngay giao dich: " + rs.getString("transaction_date"));
            i++;
        }
        return "";
       }catch(SQLException e){
           e.printStackTrace();
           return "";
       }
   }
   
   public String getUsername(int accountId){
       String sql = "select username from account where account_id = ?";
       try{
           ps = con.prepareStatement(sql);
           ps.setInt(1, accountId);
           rs = ps.executeQuery();
           if(!rs.next()){
               return "null";
           }
           return rs.getString("username");
       }catch(SQLException e){
           e.printStackTrace();
           return "Khong the in ra ten nguoi nhan!";
       }
   }
}
