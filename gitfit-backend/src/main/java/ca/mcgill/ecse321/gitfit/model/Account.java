package ca.mcgill.ecse321.gitfit.model;

import java.util.*;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;

@MappedSuperclass
public abstract class Account {

  // ------------------------
  // STATIC VARIABLES
  // ------------------------

  private static Map<String, Account> accountsByUsername = new HashMap<String, Account>();

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Account Attributes
  @Column(nullable = false, unique = true)
  private String username;
  private String email;
  private String password;
  private String lastName;
  private String firstName;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Account() {
  }

  public Account(String aUsername, String aEmail, String aPassword, String aLastName, String aFirstName) {
    email = aEmail;
    password = aPassword;
    lastName = aLastName;
    firstName = aFirstName;
    if (!setUsername(aUsername)) {
      throw new RuntimeException(
          "Cannot create due to duplicate username. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setUsername(String aUsername) {
    boolean wasSet = false;
    String anOldUsername = getUsername();
    if (anOldUsername != null && anOldUsername.equals(aUsername)) {
      return true;
    }
    if (hasWithUsername(aUsername)) {
      return wasSet;
    }
    username = aUsername;
    wasSet = true;
    if (anOldUsername != null) {
      accountsByUsername.remove(anOldUsername);
    }
    accountsByUsername.put(aUsername, this);
    return wasSet;
  }

  public boolean setEmail(String aEmail) {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setLastName(String aLastName) {
    boolean wasSet = false;
    lastName = aLastName;
    wasSet = true;
    return wasSet;
  }

  public boolean setFirstName(String aFirstName) {
    boolean wasSet = false;
    firstName = aFirstName;
    wasSet = true;
    return wasSet;
  }

  public String getUsername() {
    return username;
  }

  /* Code from template attribute_GetUnique */
  public static Account getWithUsername(String aUsername) {
    return accountsByUsername.get(aUsername);
  }

  /* Code from template attribute_HasUnique */
  public static boolean hasWithUsername(String aUsername) {
    return getWithUsername(aUsername) != null;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void delete() {
    accountsByUsername.remove(getUsername());
  }

  public String toString() {
    return super.toString() + "[" +
        "username" + ":" + getUsername() + "," +
        "email" + ":" + getEmail() + "," +
        "password" + ":" + getPassword() + "," +
        "lastName" + ":" + getLastName() + "," +
        "firstName" + ":" + getFirstName() + "]";
  }
}