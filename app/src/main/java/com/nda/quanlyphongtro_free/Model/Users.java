package com.nda.quanlyphongtro_free.Model;

public class Users {
    private String id;
    private String name, phoneNumber, email, password, accountType,
            premiumPackageName;
    // premium package name : p1m (1 tháng), p3m (3 tháng), p1y (1 năm)
    // account type : Thường , VIP

    public Users(){}

    public Users(String id, String name, String phoneNumber, String email, String password,
                 String accountType, String premiumPackageName) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.premiumPackageName = premiumPackageName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPremiumPackageName() {
        return premiumPackageName;
    }

    public void setPremiumPackageName(String premiumPackageName) {
        this.premiumPackageName = premiumPackageName;
    }
}
