package lhanud_app.object;


public class Admin {
    private int adminId;
    private String admin;
    private String password;
    
    public Admin(int adminId, String admin, String password) {
        this.adminId = adminId;
        this.admin = admin;
        this.password = password;
    }    

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
