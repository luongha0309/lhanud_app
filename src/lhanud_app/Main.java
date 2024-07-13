package lhanud_app;

import java.sql.Timestamp;
import java.util.Scanner;
import lhanud_app.model.Dao;
import lhanud_app.object.Account;
import lhanud_app.object.Admin;
import lhanud_app.object.SecurityQuestion;
import lhanud_app.object.Transaction;

public class Main {
    private static boolean isLoggedIn = false;
    private static Account currentAccount = null;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Dao dao = new Dao();
        
        while(true){
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
                    System.out.println("==========================REGISTER==============================");
                    while(!register(sc, dao)){
                    }
                    break;
                case 2:
                    System.out.println("==========================LOGIN==============================");
                    switch(login(sc, dao)){
                        case 0:
                            while(true){
                                System.out.println("\n9. Quen mat khau?");
                                System.out.println("0. Quay lai");
                                System.out.print("Chon chuc nang: ");
                                int innerChoiceLogin = sc.nextInt();
                                sc.nextLine();
                                System.out.print("\n");
                                switch(innerChoiceLogin){
                                    case 0:
                                        break;
                                    case 9:
                                        System.out.println("==========================FORGOT PASSWORD==============================");
                                        while(!forgotPassword(sc, dao)){
                                        }
                                        break;
                                    default:
                                        System.err.println("Lua chon khong hop le. Vui long chon lai!\n");    
                                }
                                break;
                            }
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
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
        System.out.print("\n");
        
        if(password.isEmpty()){
            System.err.println("Mat khau khong duoc de trong!\n");
            return false;
        }
        
        dao.showSecurityQuestion();
        int chosenQuestion;
        while(true){
            System.out.print("\nChon cau hoi bao mat: ");
            chosenQuestion = sc.nextInt();
            sc.nextLine();
            if(dao.getMaxQuestionId() <= 0){
                System.err.println("Khong lay ra duoc cau hoi bao mat!");
                break;
            }else if(chosenQuestion > dao.getMaxQuestionId()){
                System.err.println("Khong duoc chon so cau hoi lon hon " + dao.getMaxQuestionId() + ". Vui long chon lai!");
            } else {
                break;
            }
        }
        
        System.out.print("Nhap cau tra loi: ");
        String answer = sc.nextLine();
        
        Account newAccount = new Account(0, username, password, 0.0, chosenQuestion, answer);
        if(dao.insertAccount(newAccount)){
            System.out.println("Dang ky thanh cong tai khoan ID: " + dao.getAccountIdByUsername(username) + "\n");
            return true;
        }else{
            System.err.println("Dang ky that bai!\n");
            return false;
        }
    }
    
    private static int login(Scanner sc, Dao dao){
        System.out.print("Nhap ten nguoi dung: ");
        String username = sc.nextLine();
        
        System.out.print("Nhap mat khau: ");
        String password = sc.nextLine();
        
        if(dao.checkIfAdmin(username)){
                Admin admin = dao.getAdminByUsername(username);
                if(admin != null && admin.getPassword().equals(password)){
                    System.out.println("Dang nhap thanh cong voi tu cach la Admin!\n");
                    adminMenu(sc, dao, admin);
                    return 1;
                }else{
                    System.err.println("\nTen nguoi dung hoac mat khau khong dung!");
                    return 0;
                }
        } else {
            Account account = dao.getAccountByUsername(username);
            if(account != null && account.getPassword().equals(password)){
                System.out.println("Dang nhap thanh cong!\n");
                userMenu(sc, dao, account);
                return 2;
            }else{
                System.err.println("\nTen nguoi dung hoac mat khau khong dung!");
                return 0;
            }
        }
    }
    
    
    private static boolean updatePassword(Scanner sc, Dao dao, Account account){
        System.out.print("Nhap mat khau hien tai: ");
        String currentPassword = sc.nextLine();
        if(currentPassword.equals(account.getPassword())){
            System.out.print("\nNhap mat khau moi: ");
            String newPassword = sc.nextLine();
            if(newPassword.equals(currentPassword)){
                System.err.println("Mat khau moi khong duoc trung voi mat khau hien tai!");
                return false;
            } else{
                System.out.print("Nhap lai mat khau moi: ");
                String confirmNewPassword = sc.nextLine();
                if(!confirmNewPassword.equals(newPassword)){
                    System.err.println("\n\nMat khau khong trung khop!\n");
                    return false;
                }else{
                    if(!dao.updatePassword(account, newPassword)){
                        System.err.println("\n\nCap nhat mat khau khong thanh cong!");
                        return false;
                    }else{
                        System.out.println("\nCap nhat mat khau thanh cong!");
                        return true;
                    }
                }
            }
        } else{
            System.err.println("Mat khau khong dung!");
            return false;
        }
    }
    
    private static boolean forgotPassword(Scanner sc, Dao dao){
        System.out.print("Nhap tai khoan: ");
        String forgottenAccount = sc.nextLine();
        
        if(!dao.checkExistingUsername(forgottenAccount)){
            System.err.println("\nTai khoan khong ton tai!");
            return false;
        }
        Account theForgottenAccount = dao.getAccountByUsername(forgottenAccount);
        String theQuestion = dao.getQuestionByQuestionId(theForgottenAccount.getQuestionId());
        System.out.println("Cau hoi bao mat: " + theQuestion);
            System.out.print("Nhap cau tra loi: ");
            String answer = sc.nextLine();
            System.out.print("\n");
            if(!answer.equals(dao.getSecurityAnswerByUsername(forgottenAccount))){
                System.err.println("Cau tra loi khong dung!");
                return false;
            } else {
                System.out.println("Cau tra loi dung!");
                System.out.print("\nNhap mat khau moi: ");
                String newPassword = sc.nextLine();
                while(newPassword.equals(dao.getPasswordByUsername(forgottenAccount))){
                    System.err.println("Ban dang nhap mat khau cu!");
                    return false;
                }
                System.out.print("\nXac nhan mat khau: ");
                String confirmPassword = sc.nextLine();
                if(!confirmPassword.equals(newPassword)){
                    System.err.println("Mat khau xac thuc khong trung khop!");
                    return false;
                } else {
                    dao.updatePasswordByUsername(forgottenAccount, newPassword);
                    System.out.println("\nCap nhat mat khau thanh cong!\n");
                    return true;
                }
            }
}

    private static void adminMenu(Scanner sc, Dao dao, Admin admin){
        while(true){
            System.out.println("==========================ADMIN PAGE==============================");
            System.out.println("1. Quan ly tai khoan");
            System.out.println("2. Quan ly cau hoi bao mat");
            System.out.println("3. Thong ke giao dich");
            System.out.println("4. Quan ly giao dich");
            System.out.println("5. Dang xuat");
            System.out.println("6. Thoat");
            System.out.print("Chon chuc nang: ");
            int menuChoice = sc.nextInt();
            sc.nextLine();
            System.out.print("\n");
            switch(menuChoice){
                case 1:
                    break;
                case 2:
                    SecurityQuestion sq;
                    while(true){
                        System.out.println("==========================SECURITY QUESTION MANAGEMENT==============================");
                        System.out.println("1. Xem cau hoi bao mat");
                        System.out.println("2. Them cau hoi bao mat");
                        System.out.println("3. Sua cau hoi bao mat");
                        System.out.println("4. Xoa cau hoi bao mat");
                        System.out.println("\n0. Quay lai");
                        System.out.print("Chon chuc nang: ");
                        int sqmChoice = sc.nextInt();
                        sc.nextLine();
                        switch(sqmChoice){
                            case 0:
                                break;
                            case 1:
                                while(true){
                                    System.out.println("==========================ALL SECURITY QUESTIONS==============================");
                                    dao.showSecurityQuestion();
                                    System.out.println("\n0. Quay lai");
                                    System.out.print("Chon chuc nang: ");
                                    int sqmInnerChoice1 = sc.nextInt();
                                    sc.nextLine();
                                    System.out.println("\n");
                                    switch(sqmInnerChoice1){
                                        case 0:
                                            break;
                                        default:
                                            System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                                    }
                                    break;
                                }
                                break;
                            case 2:
                                System.out.println("==========================INSERT A NEW SECURITY QUESTION==============================");
                                while(true){
                                    System.out.print("Nhap cau hoi bao mat can them: ");
                                    String newSecurityQuestion = sc.nextLine();
                                    if(dao.checkExistingQuestion(newSecurityQuestion)){
                                        System.err.println("Cau hoi da ton tai. Vui long nhap lai\n");
                                    } else {
                                        sq = new SecurityQuestion(0, newSecurityQuestion);
                                        if(!dao.insertSecurityQuestion(sq)){
                                            System.err.println("Them cau hoi khong thanh cong!\n");
                                        }else{
                                            System.out.println("Them cau hoi thanh cong!\n");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                break;
                            case 3:
                                System.out.println("==========================UPDATE SECURITY QUESTIONS==============================");
                                dao.showSecurityQuestion();
                                while(true){
                                    System.out.print("Chon cau hoi can sua: ");
                                    int chosenQuestion = sc.nextInt();
                                    sc.nextLine();
                                    if(dao.getMaxQuestionId() <= 0){
                                        System.err.println("Khong lay ra duoc cau hoi bao mat!");
                                    }else if(chosenQuestion > dao.getMaxQuestionId()){
                                        System.err.println("Khong duoc chon so cau hoi lon hon " + dao.getMaxQuestionId() + ". Vui long chon lai!");
                                    } else {
                                        System.out.print("Nhap cau hoi moi: ");
                                        String newQuestion = sc.nextLine();
                                        if(newQuestion.equals(dao.getQuestionByQuestionId(chosenQuestion))){
                                            System.err.println("Cau hoi moi khong duoc giong voi cau hoi cu!");
                                        } else if(!dao.updateSecurityQuestion(chosenQuestion, newQuestion)){
                                            
                                            System.err.println("Cap nhat cau hoi that bai!");
                                        } else {
                                            System.out.println("Cap nhat cau hoi thanh cong!\n");
                                            break;
                                        }
                                    }    
                                }
                                break;
                            case 4:
                                System.out.println("==========================DELETE SECURITY QUESTIONS==============================");
                                break;
                            default:
                                System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                        }
                        break;
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("==========================LOGOUT==============================");
                    System.out.println("Dang xuat thanh cong!\n");
                    isLoggedIn = false;
                    currentAccount = null;
                    return;
                case 6:
                    System.out.println("Thoat chuong trinh thanh cong!\n");
                    sc.close();
                    dao.closeConnection();
                    System.exit(0);      
                default:
                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");    
            }
        }
    }
    
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
                    System.out.println("==========================ACCOUNT INFOMATION==============================");
                    System.out.println("Thong tin tai khoan:");
                    dao.showInformation(account);                    
                    while(true){
                        System.out.println("\n8. Doi mat khau");
                        System.out.println("9. Xem giao dich gan day");
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");
                        int innerChoice1 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");
                        switch(innerChoice1){
                            case 0:
                                break;
                            case 8:
                                System.out.println("==========================UPDATE PASSWORD==============================");
                                while(!updatePassword(sc, dao, account)){
                                }
                                break;
                            case 9:
                                System.out.println("==========================RECENTLY TRANSACTION==============================");
                                dao.showTransaction(account.getAccountId());
                                while(true){
                                    System.out.println("\n\n0. Quay lai");
                                    System.out.print("Chon chuc nang: ");                                
                                    int case9Choice = sc.nextInt();
                                    sc.nextLine();
                                    System.out.print("\n");
                                    switch(case9Choice){
                                        case 0:
                                            break;
                                        default:
                                            System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                                    }
                                    break;
                                }
                                break;
                            default:
                                System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                            break;
                    }
                    break;
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
                            System.err.println("Nap tien that bai!\n");
                        }
                        System.out.println("0. Quay lai");
                        System.out.print("Chon chuc nang: ");                    
                        int innerChoice2 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("\n");
                        while(true){
                            switch(innerChoice2){
                                case 0:
                                    break;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                            break;
                        }
                        break;
                    }
                    break;
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
                                    break;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                            break;
                        }
                        break;
                    }
                    break;
                case 4:
                    while(true){
                        System.out.println("==========================TRANSFER==============================");
                        System.out.print("Nhap tai khoan nhan tien: ");
                        String toUsername = sc.nextLine();
                        if(toUsername.equals(dao.getUsernameByAccountId(account.getAccountId()))){
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
                        content = account.getUsername() + " da chuyen tien thanh cong cho " + dao.getUsernameByAccountId(toAccountId);
                        String contentReciever = "Nhan tien thanh cong tu " + dao.getUsernameByAccountId(account.getAccountId());                    
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
                                    break;
                                default:
                                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");
                            }
                            break;
                        }
                        break;
                    }
                    break;
                case 5:
                    System.out.println("==========================LOGOUT==============================");
                    System.out.println("Dang xuat thanh cong!\n");
                    isLoggedIn = false;
                    currentAccount = null;
                    return;
                case 6:
                    System.out.println("Thoat chuong trinh thanh cong!\n");
                    sc.close();
                    dao.closeConnection();
                    System.exit(0);      
                default:
                    System.err.println("Lua chon khong hop le. Vui long chon lai!\n");                    
            }
        } 
    }
}
