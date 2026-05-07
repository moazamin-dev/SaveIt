package com.saveit.model;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @brief Represents a user within the SaveIt application.
 *
 * This class stores profile information for a user, including their name,
 * contact details, and credentials. It also provides utility methods for
 * secure password handling.
 */
public class User {

    /** @var int id Unique identifier for the user */
    private int id;

    /** @var String name The full name of the user */
    private String name;

    /** @var String phone The phone number associated with the user account */
    private String phone;

    /** @var String username The unique login name for the user */
    private String username;

    /**
     * @brief Sets the unique identifier for the user.
     * @param id The ID to assign to the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @brief Gets the unique identifier of the user.
     * @return int The user ID.
     */
    public int getId() {return id;}

    /**
     * @brief Sets the full name of the user.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Gets the full name of the user.
     * @return String The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Sets the login username.
     * @param uname The username to assign.
     */
    public void setUname(String uname) {
        this.username = uname;
    }

    /**
     * @brief Gets the login username.
     * @return String The username.
     */
    public String getUname() {
        return username;
    }

    /**
     * @brief Sets the contact phone number for the user.
     * @param phone The phone number to set.
     */
    public void setPhone(String phone) {this.phone = phone;}

    /**
     * @brief Gets the contact phone number of the user.
     * @return String The phone number.
     */
    public String getPhone() {return phone;}

    /**
     * @brief Hashes a plain-text password using the SHA-256 algorithm.
     *
     * This method takes a raw string, computes its SHA-256 hash, and returns
     * the result as a Base64 encoded string for secure storage.
     *
     * @param pass The plain-text password to be hashed.
     * @return String The Base64 encoded hash, or null if the algorithm is unavailable.
     */
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