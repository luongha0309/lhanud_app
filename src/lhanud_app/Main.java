package lhanud_app;

import java.sql.Timestamp;
import java.util.Scanner;
import lhanud_app.model.Dao;
import lhanud_app.object.Account;
import lhanud_app.object.Transaction;

public class Main {
    private static boolean isLoggedIn = false;
    private static Account currentAccount = null;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Dao dao = new Dao();
        
        while(true){
            if(isLoggedIn == false){
                System.out.println("==========================WELCOME TO LHANUDAPP==============================");
                System.out.println("1. Dang ky tai khoan");
                System.out.println("2. Dang nhap");
                System.out.println("3. Thoat");
                System.out.print("Chon chuc nang: ");
                int choice = sc.nextInt();
                sc.nextLine();
                System.out.print("\n");

                switch(choice){
                    case 1:
                        while(true){
                            System.out.println("==========================REGISTER==============================");
                            boolean success = register(sc, dao);
                            if (success) {
                                break;
                            } else {
                                System.err.println("Dang ky that bai. Vui long thu lai!\n");
                            }
                        }
                        break;

                    case 2:
                        System.out.println("==========================LOGIN==============================");
                        boolean success = login(sc, dao);
                        if(success){
                            isLoggedIn = true;  // Cập nhật trạng thái đăng nhập
                        } else {
                            System.err.println("Dang nhap that bai. Vui long thu lai!\n");
                        }
                        break;

                    case 3:
                        System.out.println("Thoat chuong trinh thanh cong!\n");
                        sc.close();
                        dao.closeConnection();
                        System.exit(0);
                        break;

                    default:
                        System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                        break;
                }
            } else {
                userMenu(sc, dao, currentAccount);
            }
        }
    }
    
    private static boolean register(Scanner sc, Dao dao){
        System.out.print("Nhap ten nguoi dung: ");
        String username = sc.nextLine();
        
        Account existingAccount = dao.getAccountByUsername(username);
        if(existingAccount != null){
            System.err.println("Ten nguoi dung da ton tai. Vui long chon ten khac!\n");
            return false;
        }
        
        System.out.print("Nhap mat khau: ");
        String password = sc.nextLine();
        
        if(password.isEmpty()){
            System.err.println("Mat khau khong duoc de trong!\n");
            return false;
        }
        
        Account newAccount = new Account(0, username, password, 0.0);
        if(dao.insertAccount(newAccount)){
            System.out.println("Dang ky thanh cong tai khoan ID: " + dao.getAccountIdByUsername(username) + "\n");
            return true;
        }else{
            System.err.println("Dang ky that bai!\n");
            return false;
        }
    }
    
    private static boolean login(Scanner sc, Dao dao){
        System.out.print("Nhap ten nguoi dung: ");
        String username = sc.nextLine();
        
        System.out.print("Nhap mat khau: ");
        String password = sc.nextLine();
        
        Account account = dao.getAccountByUsername(username);
        if(account != null && account.getPassword().equals(password)){
            System.out.println("Dang nhap thanh cong!\n");
            isLoggedIn = true;
            currentAccount = account;
            return true;
        }else{
            System.err.println("\nTen nguoi dung hoac mat khau khong dung!");
            return false;
        }
    }

    @SuppressWarnings("fallthrough")
    private static void userMenu(Scanner sc, Dao dao, Account account) {
        String transactionType = "";
        String content = "";
        Timestamp timestamp;
        Transaction transaction;
        while(true){
            System.out.println("==========================HOMEPAGE==============================");
            System.out.println("1. Thong tin tai khoan");
            System.out.println("2. Nap tien");
            System.out.println("3. Rut tien");
            System.out.println("4. Chuyen tien");
            System.out.println("5. Dang xuat");
            System.out.println("6. Thoat");
            System.out.print("Chon chuc nang: ");
            int choice = sc.nextInt();
            sc.nextLine();
            System.out.print("\n");    
            
            switch(choice){
                case 1:
                    while(true){
                        System.out.println("==========================ACCOUNT INFOMATION==============================");
                        System.out.println("Thong tin tai khoan:");
                        dao.showInformation(account);
                        System.out.println("\n9. Xem giao dich gan day");
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");
                        int innerChoice1 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");                       
                            switch(innerChoice1){
                                case 0:
                                    return;
                                case 9:
                                    while(true){
                                        System.out.println("==========================RECENTLY TRANSACTION==============================");
                                        dao.showTransaction(account.getAccountId());
                                        System.out.println("\n\n0. Quay lai");
                                        System.out.print("Chon chuc nang: ");                                
                                        int case9Choice = sc.nextInt();
                                        sc.nextLine();
                                        System.out.print("\n");
                                            switch(case9Choice){
                                            case 0:
                                                return;
                                            default:
                                                System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                                        }
                                    }

                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                    }
                case 2:
                    while(true){
                        System.out.println("==========================DEPOSIT==============================");
                        System.out.print("Nhap so tien can nap: ");
                        double depositAmount = sc.nextDouble();
                        transactionType = "Nap tien";
                        timestamp = new Timestamp(System.currentTimeMillis());
                        if(dao.deposit(account.getAccountId(), depositAmount)){
                            content = "Nap tien thanh cong";
                            account.setBalance(account.getBalance() + depositAmount);
                            transaction = new Transaction(0,account.getAccountId(),transactionType,depositAmount,content,timestamp);
                            dao.insertTransaction(transaction);
                            System.out.println("Nap tien thanh cong! So du hien tai: " + account.getBalance() + "\n");
                        } else {
                            content = "Nap tien that bai";
                            transaction = new Transaction(0,account.getAccountId(),transactionType,depositAmount,content,timestamp);
                            dao.insertTransaction(transaction);
                            System.err.println("Nap tien that bai! \n");
                        }
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");                    
                        int innerChoice2 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");
                        while(true){
                            switch(innerChoice2){
                                case 0:
                                    return;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                        }
                    }
                case 3:
                    while(true){
                        System.out.println("==========================WITHDRAW==============================");
                        System.out.print("Nhap so tien can rut: ");
                        double withdrawAmount = sc.nextDouble();
                        transactionType = "Rut tien";
                        timestamp = new Timestamp(System.currentTimeMillis());
                        if(dao.withdraw(account.getAccountId(), withdrawAmount)){
                            account.setBalance(account.getBalance() - withdrawAmount);
                            content = "Rut tien thanh cong";
                            transaction = new Transaction(0,account.getAccountId(),transactionType,withdrawAmount,content,timestamp);
                            dao.insertTransaction(transaction);                        
                            System.out.println("Rut tien thanh cong! So du hien tai: " + account.getBalance() + "\n");
                        } else {
                            content = "Rut tien that bai";
                            transaction = new Transaction(0,account.getAccountId(),transactionType,withdrawAmount,content,timestamp);
                            dao.insertTransaction(transaction);                   
                            System.err.println("Rut tien that bai!\n");
                        }
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");                    
                        int innerChoice3 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");
                        while(true){
                            switch(innerChoice3){
                                case 0:
                                    return;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                        }
                    }
                case 4:
                    while(true){
                        System.out.println("==========================TRANSFER==============================");
                        System.out.print("Nhap tai khoan nhan tien: ");
                        String toUsername = sc.nextLine();
                        if(toUsername.equals(dao.getUsername(account.getAccountId()))){
                            System.err.println("Tai khoan khong duoc trung voi tai khoan nguoi gui!");
                            break;
                        } else if(dao.checkExistingUsername(toUsername)){
                            System.out.println("Tai khoan hop le!");
                            } else{
                            System.err.println("Tai khoan khong ton tai!");
                            break;
                        }
                        System.out.print("Nhap so tien can chuyen: ");
                        double transferAmount = sc.nextDouble();
                        sc.nextLine();
                        timestamp = new Timestamp(System.currentTimeMillis());
                        Transaction transactionRecieve;
                        transactionType = "Chuyen tien";
                        int toAccountId = dao.getAccountIdByUsername(toUsername);
                        content = account.getUsername() + "da chuyen tien thanh cong cho " + dao.getUsername(toAccountId);
                        String contentReciever = "Nhan tien thanh cong tu " + dao.getUsername(account.getAccountId());                    
                        if(toAccountId > 0 && dao.transfer(account.getAccountId(), toAccountId, transferAmount)){
                            account.setBalance(account.getBalance() - transferAmount);
                            transaction = new Transaction(0,account.getAccountId(),transactionType,transferAmount,content,timestamp);
                            dao.insertTransaction(transaction);
                            transactionRecieve = new Transaction(0,toAccountId,"Nhan tien",transferAmount,contentReciever,timestamp);
                            dao.insertTransaction(transactionRecieve);
                            System.out.println("\nChuyen tien thanh cong! So du hien tai: " + account.getBalance() + "\n");
                        } else{
                            content = "Chuyen tien that bai";
                            contentReciever = "Nhan tien that bai";
                            transaction = new Transaction(0,account.getAccountId(),transactionType,transferAmount,content,timestamp);
                            dao.insertTransaction(transaction);
                            transactionRecieve = new Transaction(0,toAccountId,"Nhan tien",transferAmount,contentReciever,timestamp);
                            dao.insertTransaction(transactionRecieve);                        
                            System.err.println("Chuyen tien that bai! \n");
                        }
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");                    
                        int innerChoice4 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");
                        while(true){
                            switch(innerChoice4){
                                case 0:
                                    return;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                        }
                    }
                case 5:
                    System.out.println("==========================LOGOUT==============================");
                    System.out.println("Dang xuat thanh cong!\n");
                    isLoggedIn = false;
                    currentAccount = null;
                    return;
                case 6:
                    System.out.println("Thoat chuong trinh thanh cong!\n");
//                    sc.close();
//                    dao.closeConnection();
                    System.exit(0);      
                default:
                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");                    
            }
        } 
    }
}
