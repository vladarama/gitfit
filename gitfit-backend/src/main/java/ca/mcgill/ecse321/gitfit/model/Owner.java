package ca.mcgill.ecse321.gitfit.model;

import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class Owner extends Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Owner Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  // Owner Associations
  @OneToOne(optional = false)
  private SportCenter sportCenter;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Owner() {
    super();
  }

  public Owner(String aUsername, String aEmail, String aPassword, String aLastName, String aFirstName,
      SportCenter aSportCenter) {
    super(aUsername, aEmail, aPassword, aLastName, aFirstName);
    if (aSportCenter == null || aSportCenter.getOwner() != null) {
      throw new RuntimeException(
          "Unable to create Owner due to aSportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    sportCenter = aSportCenter;
  }

  public Owner(String aUsername, String aEmail, String aPassword, String aLastName, String aFirstName,
      String aNameForSportCenter, int aMaxCapacityForSportCenter,
      Time aOpeningTimeForSportCenter, Time aClosingTimeForSportCenter) {
    super(aUsername, aEmail, aPassword, aLastName, aFirstName);
    sportCenter = new SportCenter(aNameForSportCenter, aMaxCapacityForSportCenter,
        aOpeningTimeForSportCenter, aClosingTimeForSportCenter, this);
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
  public SportCenter getSportCenter() {
    return sportCenter;
  }

  public void delete() {
    SportCenter existingSportCenter = sportCenter;
    sportCenter = null;
    if (existingSportCenter != null) {
      existingSportCenter.delete();
    }
    super.delete();
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "sportCenter = "
        + (getSportCenter() != null ? Integer.toHexString(System.identityHashCode(getSportCenter())) : "null");
  }
}