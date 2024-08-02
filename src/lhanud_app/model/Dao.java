package lhanud_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import lhanud_app.object.Account;
import java.sql.SQLException;
import lhanud_app.object.Admin;
import lhanud_app.object.SecurityQuestion;
import lhanud_app.object.Transaction;


public class Dao {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps, ps2;
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
    
    public boolean checkIfAdmin(String username){
        String sql = "select admin from admin where admin = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(!rs.next()){
                return false;
            } else {
                return true;
            }    
        }catch(SQLException e){
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
    
   public String getUsernameByAccountId(int accountId){
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
   
   public boolean updatePassword(Account account, String newPassword){
       String sql = "update account set password = ? where account_id = ?";
       try{
           ps = con.prepareStatement(sql);
           ps.setString(1, newPassword);
           ps.setInt(2, account.getAccountId());
           int rowAffected = ps.executeUpdate();
           if(rowAffected == 0){
               return false;
           }
           return true;
       }catch(SQLException e){
           e.printStackTrace();
           return false;
       }
   }
   
   public boolean updatePasswordByUsername(String username, String newPassword){
       String sql = "update account set password = ? where username = ?";
       try{
           ps = con.prepareStatement(sql);
           ps.setString(1, newPassword);
           ps.setString(2, username);
           int rowAffected = ps.executeUpdate();
           if(rowAffected == 0){
               return false;
           }
           return true;
       }catch(SQLException e){
           e.printStackTrace();
           return false;
       }   
   }
   
    public Admin getAdminByUsername(String username){
        String sql = "select * from admin where admin = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                return new Admin(rs.getInt("admin_id"), rs.getString("admin"), rs.getString("password"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

   public String getSecurityAnswerByUsername(String username){
       String sql = "select security_answer from account where username = ?";
       String securityAnswer;
       try{
           ps = con.prepareStatement(sql);
           ps.setString(1, username);
           rs = ps.executeQuery();   
           if(rs.next()){
               securityAnswer = rs.getString("security_answer");
               return securityAnswer;
           }else{
               return "Khong ton tai cau hoi bao mat nao!";
           }
       }catch(SQLException e){
           e.printStackTrace();
           return "Khong the lay ra cau hoi bao mat!";
       }
   }
   
   public int getMaxAdminId(){
       String sql = "select max(admin_id) as max_aid from admin";
       int maxAdminId = 0;
       try{
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           if(rs.next()){
               maxAdminId = rs.getInt("max_aid");
               return maxAdminId;
           }else{
               return maxAdminId;
           }
       }catch(SQLException e){
           e.printStackTrace();
           return maxAdminId;
       }
   }   
   
   public int getMaxAccountId(){
       String sql = "select max(account_id) as max_accid from account";
       int maxAccountId = 0;
       try{
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           if(rs.next()){
               maxAccountId = rs.getInt("max_accid");
               return maxAccountId;
           }else{
               return maxAccountId;
           }
       }catch(SQLException e){
           e.printStackTrace();
           return maxAccountId;
       }
   }   
   
   public int getMaxTransactionId(){
       String sql = "select max(transaction_id) as max_tid from transaction";
       int maxTransactionId = 0;
       try{
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           if(rs.next()){
               maxTransactionId = rs.getInt("max_tid");
               return maxTransactionId;
           }else{
               return maxTransactionId;
           }
       }catch(SQLException e){
           e.printStackTrace();
           return maxTransactionId;
       }
   }
   
   public int getMaxQuestionId(){
       String sql = "select max(question_id) as max_qid from security_question";
       int maxQuestionId = 0;
       try{
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           if(rs.next()){
               maxQuestionId = rs.getInt("max_qid");
               return maxQuestionId;
           }else{
               return maxQuestionId;
           }
       }catch(SQLException e){
           e.printStackTrace();
           return maxQuestionId;
       }
   }
   
    public boolean insertSecurityQuestion(SecurityQuestion sq){
        String sql = "insert into security_question (question_id, question) values (?, ?)";
        String sqlGetMaxId = "select max(question_id) as max_qid from security_question";
        try{
            int maxQuestionId = getMaxQuestionId();
            ps = con.prepareStatement(sql);
            ps.setInt(1, maxQuestionId + 1);
            ps.setString(2, sq.getQuestion());
            int rowAffected = ps.executeUpdate();
            if(rowAffected > 0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkExistingQuestion(String question){
        String sql = "select question from security_question where question = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, question);
            rs = ps.executeQuery();
            if(!rs.next()){
                return false;
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public String getQuestionByQuestionId(int questionId){
        String sql = "select question from security_question where question_id = ?";
        String question;
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();
            if(rs.next()){
                question = rs.getString("question");
                return question;
            }
            return "";
        }catch(SQLException e){
            e.printStackTrace();
            return "";
        }
    }
    
    public int getQuestionIdByUsername(String username){
        String sql = "select question_id from account where username = ?";
        int questionId = 0;
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                questionId = rs.getInt("question_id");
                return questionId;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return questionId;
    }
    
    public boolean updateSecurityQuestion(int chosenQuestionId, String newQuestion){
        String sql = "update security_question set question = ? where question_id = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, newQuestion);
            ps.setInt(2, chosenQuestionId);
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteSecurityQuestion(int questionId){
        try{
            String sql = "update account set question_id = 0, security_answer = 'null' where question_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, questionId);
            ps.executeUpdate();

            
            sql = "delete from security_question where question_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, questionId);
            int rowAffected = ps.executeUpdate();
            if(rowAffected == 0){
                System.err.println("Khong co dong nao duoc xoa trong co so du lieu!");
                return false;
            }
            
            int newQuestionId = questionId + 1;
            int maxQuestionId = getMaxQuestionId();
            while(newQuestionId <= maxQuestionId){
                sql = "update security_question set question_id = ? - 1 where question_id = ?";
                ps = con.prepareStatement(sql); 
                ps.setInt(1, newQuestionId);
                ps.setInt(2, newQuestionId);
                rowAffected = ps.executeUpdate();
                if(rowAffected == 0){
                    System.err.println("Khong cap nhat duoc tai cau hoi: " + newQuestionId + "!");
                    return false;
                }
                newQuestionId++;
            }
            return true;
            
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertAccount(Account a){
        String sql = "insert into account (account_id, username, password, balance, question_id, security_answer) values (?,?,?,?,?,?,?)";
        try{
            int maxAccountId = getMaxAccountId();
            ps = con.prepareStatement(sql);
            ps.setInt(1, maxAccountId + 1);
            ps.setString(2, a.getUsername());
            ps.setString(3, a.getPassword());
            ps.setDouble(4, a.getBalance());
            ps.setInt(5, a.getQuestionId());
            ps.setString(6, a.getSecurityAnswer());
            ps.setString(7, "Active");
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
                }
            return false;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public String getPasswordByUsername(String username){
        String sql = "select password from account where username = ?";
        String password;
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                password = rs.getString("password");
                return password;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;    
    }
    
    public Account getAccountByUsername(String username){
        String sql = "select * from account where username = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"), rs.getDouble("balance"), rs.getInt("question_id"), rs.getString("security_answer"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void showInformation(Account account){
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
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void showAllAccount(){
        String sql = "select * from account";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("Ma tai khoan: " + rs.getInt("account_id")
                    + "| Ten tai khoan: " + rs.getString("username") 
                    + " | So du: " + rs.getDouble("balance") 
                    + " | Cau hoi bao mat: " + getQuestionByQuestionId(rs.getInt("question_id")) 
                    + " | Trang thai: " + rs.getString("status"));
            }
        }catch(SQLException e){
            e.printStackTrace();
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
    
   public boolean insertTransaction(Transaction t){
       String sql = "insert into transaction (transaction_id, account_id, transaction_type, amount, content, transaction_date) values (?,?,?,?,?,?)";
       try{
           int maxTransactionId = getMaxTransactionId();
           ps = con.prepareStatement(sql);
           ps.setInt(1, maxTransactionId + 1);
           ps.setInt(2, t.getAccountId());
           ps.setString(3, t.getTransactionType());
           ps.setDouble(4, t.getAmount());
           ps.setString(5, t.getContent());
           ps.setTimestamp(6, t.getTransactionDate());
           int rowAffected = ps.executeUpdate();
            if(rowAffected > 0){
                return true;
            }
            return false;
           
       }catch(SQLException e){
           e.printStackTrace();
           return false;
       }
   }
   
   public void showTransaction(int accountId){
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
       }catch(SQLException e){
           e.printStackTrace();
       }
   }
   
   public void showSecurityQuestion(){
       String sql = "select * from security_question where question_id > 0";
       try{
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while(rs.next()){
               System.out.println(rs.getInt("question_id") + ". " + rs.getString("question"));
           }
       }catch(SQLException e){
           e.printStackTrace();
           System.err.println("Khong the lay ra cau hoi bao mat do loi co so du lieu!");
       }
   }    
   
   public boolean updateSecurityFromAccount(int questionId, String securityAnswer, String username){
       String sql = "update account set question_id = ? and security_answer = ? where username = ?";
       try{
           ps = con.prepareStatement(sql);
           ps.setInt(1, questionId);
           ps.setString(2, securityAnswer);
           ps.setString(3, username);
           int rowAffected = ps.executeUpdate();
           if(rowAffected == 0){
               System.err.println("Khong co dong nao duoc cap nhat!");
               return false;
           }
           return true;
           
       }catch(SQLException e){
           e.printStackTrace();
           return true;
       }
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
        } catch(SQLException e) {
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
    
   

}
