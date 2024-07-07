package ca.mcgill.ecse321.gitfit.model;

import java.sql.Time;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Session {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Session Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private int price;
  private Time startTime;
  private Time endTime;
  private Date date;

  // Session Associations
  @ManyToOne(optional = false)
  private Instructor instructor;
  @ManyToOne(optional = false)
  private FitnessClass fitnessClass;
  @ManyToOne(optional = false)
  private SportCenter sportCenter;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Session() {
  }

  public Session(int aPrice, Time aStartTime, Time aEndTime, Date aDate, Instructor aInstructor,
      FitnessClass aFitnessClass, SportCenter aSportCenter) {
    price = aPrice;
    endTime = aEndTime;
    startTime = aStartTime;
    date = aDate;
    if (!setInstructor(aInstructor)) {
      throw new RuntimeException(
          "Unable to create Session due to aInstructor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setFitnessClass(aFitnessClass)) {
      throw new RuntimeException(
          "Unable to create Session due to aFitnessClass. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter) {
      throw new RuntimeException(
          "Unable to create session due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setPrice(int aPrice) {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime) {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime) {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate) {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return id;
  }

  public int getPrice() {
    return price;
  }

  public Time getStartTime() {
    return startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public Date getDate() {
    return date;
  }

  /* Code from template association_GetOne */
  public Instructor getInstructor() {
    return instructor;
  }

  /* Code from template association_GetOne */
  public FitnessClass getFitnessClass() {
    return fitnessClass;
  }

  /* Code from template association_GetOne */
  public SportCenter getSportCenter() {
    return sportCenter;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setInstructor(Instructor aNewInstructor) {
    boolean wasSet = false;
    if (aNewInstructor != null) {
      instructor = aNewInstructor;
      wasSet = true;
    }
    return wasSet;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setFitnessClass(FitnessClass aNewFitnessClass) {
    boolean wasSet = false;
    if (aNewFitnessClass != null) {
      fitnessClass = aNewFitnessClass;
      wasSet = true;
    }
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
      existingSportCenter.removeSession(this);
    }
    sportCenter.addSession(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    instructor = null;
    fitnessClass = null;
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if (placeholderSportCenter != null) {
      placeholderSportCenter.removeSession(this);
    }
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "," +
        "price" + ":" + getPrice() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "startTime" + "="
        + (getStartTime() != null
            ? !getStartTime().equals(this) ? getStartTime().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "endTime" + "="
        + (getEndTime() != null ? !getEndTime().equals(this) ? getEndTime().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "date" + "="
        + (getDate() != null ? !getDate().equals(this) ? getDate().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "instructor = "
        + (getInstructor() != null ? Integer.toHexString(System.identityHashCode(getInstructor())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "fitnessClass = "
        + (getFitnessClass() != null ? Integer.toHexString(System.identityHashCode(getFitnessClass())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "sportCenter = "
        + (getSportCenter() != null ? Integer.toHexString(System.identityHashCode(getSportCenter())) : "null");
  }
}