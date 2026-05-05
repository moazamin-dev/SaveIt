package com.saveit.model;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class User {

    private int id;
    private String name;
    private String phone;
    private String username;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {return id;}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUname(String uname) {
        this.username = uname;
    }

    public String getUname() {
        return username;
    }

    public void setPhone(String phone) {this.phone = phone;}

    public String getPhone() {return phone;}

    public static String hashPassword(String pass) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(pass.getBytes());
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Critical Error: Hashing algorithm not found!");
            return null;
        }
    }
}