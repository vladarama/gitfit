package ca.mcgill.ecse321.gitfit.model;

import jakarta.persistence.*;

@Entity
public class Billing {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Billing Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String country;
  private String state;
  private String postalCode;
  private String cardNumber;
  private String address;

  // Billing Associations
  @OneToOne(optional = false)
  private Customer customer;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Billing() {
  }

  public Billing(String aCountry, String aState, String aPostalCode, String aCardNumber, String aAddress,
      Customer aCustomer) {
    country = aCountry;
    state = aState;
    postalCode = aPostalCode;
    cardNumber = aCardNumber;
    address = aAddress;
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer) {
      throw new RuntimeException(
          "Unable to create billing due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setId(int aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setCountry(String aCountry) {
    boolean wasSet = false;
    country = aCountry;
    wasSet = true;
    return wasSet;
  }

  public boolean setState(String aState) {
    boolean wasSet = false;
    state = aState;
    wasSet = true;
    return wasSet;
  }

  public boolean setPostalCode(String aPostalCode) {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardNumber(String aCardNumber) {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress) {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return id;
  }

  public String getCountry() {
    return country;
  }

  public String getState() {
    return state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getCardNumber() {
    return cardNumber;
  }


  public String getCardNumberEnd()
  {
    return cardNumber.substring(cardNumber.length()-4);
  }

  public String getAddress()
  {
    return address;
  }

  /* Code from template association_GetOne */
  public Customer getCustomer() {
    return customer;
  }

  /* Code from template association_SetOneToOptionalOne */
  public boolean setCustomer(Customer aNewCustomer) {
    boolean wasSet = false;
    if (aNewCustomer == null) {
      // Unable to setCustomer to null, as billing must always be associated to a
      // customer
      return wasSet;
    }

    Billing existingBilling = aNewCustomer.getBilling();
    if (existingBilling != null && !equals(existingBilling)) {
      // Unable to setCustomer, the current customer already has a billing, which
      // would be orphaned if it were re-assigned
      return wasSet;
    }

    Customer anOldCustomer = customer;
    customer = aNewCustomer;
    customer.setBilling(this);

    if (anOldCustomer != null) {
      anOldCustomer.setBilling(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Customer existingCustomer = customer;
    customer = null;
    if (existingCustomer != null) {
      existingCustomer.setBilling(null);
    }
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "," +
        "country" + ":" + getCountry() + "," +
        "state" + ":" + getState() + "," +
        "postalCode" + ":" + getPostalCode() + "," +
        "cardNumber" + ":" + getCardNumber() + "," +
        "address" + ":" + getAddress() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "customer = "
        + (getCustomer() != null ? Integer.toHexString(System.identityHashCode(getCustomer())) : "null");
  }
}