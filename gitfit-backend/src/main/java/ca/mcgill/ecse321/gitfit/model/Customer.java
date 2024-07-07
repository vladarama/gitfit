package ca.mcgill.ecse321.gitfit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import jakarta.persistence.CascadeType;

@Entity
public class Customer extends Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Customer Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  // Customer Associations
  @OneToOne(mappedBy = "customer", optional = true, cascade = { CascadeType.ALL }, orphanRemoval = true)
  private Billing billing;
  @ManyToOne(optional = false)
  private SportCenter sportCenter;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Customer() {
    super();
  }

  public Customer(String aUsername, String aEmail, String aPassword, String aLastName, String aFirstName,
      SportCenter aSportCenter) {
    super(aUsername, aEmail, aPassword, aLastName, aFirstName);
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter) {
      throw new RuntimeException(
          "Unable to create customer due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Customer(String aUsername, String aEmail, String aPassword, String aLastName, String aFirstName,
      String country, String state, String postalCode, String cardNumber, String address,
      SportCenter aSportCenter) {
    super(aUsername, aEmail, aPassword, aLastName, aFirstName);
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter) {
      throw new RuntimeException(
          "Unable to create customer due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public int getId() {
    return id;
  }

  /* Code from template association_GetOne */
  public Billing getBilling() {
    return billing;
  }

  public boolean hasBilling() {
    boolean has = billing != null;
    return has;
  }

  /* Code from template association_GetOne */
  public SportCenter getSportCenter() {
    return sportCenter;
  }

  /* Code from template association_SetOptionalOneToOne */
  public boolean setBilling(Billing aNewBilling) {
    boolean wasSet = false;
    if (billing != null && !billing.equals(aNewBilling) && equals(billing.getCustomer())) {
      // Unable to setBilling, as existing billing would become an orphan
      return wasSet;
    }

    billing = aNewBilling;
    Customer anOldCustomer = aNewBilling != null ? aNewBilling.getCustomer() : null;

    if (!this.equals(anOldCustomer)) {
      if (anOldCustomer != null) {
        anOldCustomer.billing = null;
      }
      if (billing != null) {
        billing.setCustomer(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_SetOneToMany */
  public boolean setSportCenter(SportCenter aSportCenter) {
    boolean wasSet = false;
    if (aSportCenter == null) {
      return wasSet;
    }

    SportCenter existingSportCenter = sportCenter;
    sportCenter = aSportCenter;
    if (existingSportCenter != null && !existingSportCenter.equals(aSportCenter)) {
      existingSportCenter.removeCustomer(this);
    }
    sportCenter.addCustomer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Billing existingBilling = billing;
    billing = null;
    if (existingBilling != null) {
      existingBilling.delete();
      existingBilling.setCustomer(null);
    }
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if (placeholderSportCenter != null) {
      placeholderSportCenter.removeCustomer(this);
    }
    super.delete();
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "billing = "
        + (getBilling() != null ? Integer.toHexString(System.identityHashCode(getBilling())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "sportCenter = "
        + (getSportCenter() != null ? Integer.toHexString(System.identityHashCode(getSportCenter())) : "null");
  }
}