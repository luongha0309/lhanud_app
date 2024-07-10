package lhanud_app.object;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private double balance;
    private int questionId;
    private String securityAnswer;
    
    public Account(int accountID, String username, String password, double balance){
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
    
    public Account(int accountID, String username, String password, double balance, int questionId, String securityAnswer){
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.questionId = questionId;
        this.securityAnswer = securityAnswer;
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

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}
