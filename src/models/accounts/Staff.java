package models.accounts;

public class Staff {
    private String staffId;
    private String userId;
    private String name;

    public Staff(String staffId, String userId, String name) {
        this.staffId = staffId;
        this.userId = userId;
        this.name = name;
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
}
