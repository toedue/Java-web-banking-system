package com.banking.model;

public class Customer {
    private int id;
    private String accountNumber;
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private double balance;
    
    public Customer() {}
    
    public Customer(int id, String accountNumber, int userId, String name, String email, 
                   String phone, String address, double balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

