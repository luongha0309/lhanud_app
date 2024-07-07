package lhanud_app.object;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private double balance;
    
    public Account(int accountID, String username, String password, double balance){
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
    
    public int getAccountId(){
        return accountID;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public double getBalance(){
        return balance;
    }
    
    public void setAccountId(int accountId){
        this.accountID = accountID;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public void setBalance(double balance){
        this.balance = balance;
    }
}
