package ca.mcgill.ecse321.gitfit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FitnessClass {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // FitnessClass Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private String description;
  private FitnessClassApprovalStatus approvalStatus;
//  private boolean isApproved;

  // FitnessClass Associations
  @ManyToOne(optional = false)
  private SportCenter sportCenter;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public FitnessClass() {
  }

  public FitnessClass(String aName, String aDescription, SportCenter aSportCenter) {
    name = aName;
    description = aDescription;
    approvalStatus = FitnessClassApprovalStatus.PENDING;
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter) {
      throw new RuntimeException(
          "Unable to create fitnessClass due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription) {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setApprovalStatus(FitnessClassApprovalStatus aApprovalStatus) {
    boolean wasSet = false;
    approvalStatus = aApprovalStatus;
    wasSet = true;
    return wasSet;
  }

//  public boolean setIsApproved(boolean aIsApproved) {
//    boolean wasSet = false;
//    isApproved = aIsApproved;
//    wasSet = true;
//    return wasSet;
//  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public FitnessClassApprovalStatus getApprovalStatus() {
    return approvalStatus;
  }

//  public boolean getIsApproved() {
//    return isApproved;
//  }
//
//  /* Code from template attribute_IsBoolean */
//  public boolean isIsApproved() {
//    return isApproved;
//  }

  /* Code from template association_GetOne */
  public SportCenter getSportCenter() {
    return sportCenter;
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
      existingSportCenter.removeFitnessClass(this);
    }
    sportCenter.addFitnessClass(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if (placeholderSportCenter != null) {
      placeholderSportCenter.removeFitnessClass(this);
    }
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "," +
        "name" + ":" + getName() + "," +
        "description" + ":" + getDescription() + "," +
        "isApproved" + ":" + getApprovalStatus() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "sportCenter = "
        + (getSportCenter() != null ? Integer.toHexString(System.identityHashCode(getSportCenter())) : "null");
  }
}