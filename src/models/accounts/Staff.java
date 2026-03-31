package models.accounts;

public class Staff {
    private String staffId;
    private String userId;
    private String name;
    private String phone;

    public Staff(String staffId, String userId, String name, String phone) {
        this.staffId = staffId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
