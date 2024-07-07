package ca.mcgill.ecse321.gitfit.model;

import java.sql.Time;
import java.util.*;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Entity
public class SportCenter {

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // SportCenter Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int maxCapacity;
    private Time openingTime;
    private Time closingTime;

    // SportCenter Associations
    @OneToOne(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private Owner owner;
    @OneToMany(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private List<Instructor> instructors;
    @OneToMany(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private List<Customer> customers;
    @OneToMany(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private List<Registration> registrations;
    @OneToMany(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private List<Session> sessions;
    @OneToMany(mappedBy = "sportCenter", cascade = { CascadeType.ALL })
    private List<FitnessClass> fitnessClasses;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public SportCenter() {
        this.instructors = new ArrayList<Instructor>();
        this.customers = new ArrayList<Customer>();
        this.registrations = new ArrayList<Registration>();
        this.sessions = new ArrayList<Session>();
        this.fitnessClasses = new ArrayList<FitnessClass>();
    }

    public SportCenter(String aName, int aMaxCapacity, Time aOpeningTime, Time aClosingTime, Owner aOwner) {
        name = aName;
        maxCapacity = aMaxCapacity;
        openingTime = aOpeningTime;
        closingTime = aClosingTime;
        if (aOwner == null || aOwner.getSportCenter() != null) {
            throw new RuntimeException(
                    "Unable to create SportCenter due to aOwner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
        owner = aOwner;
        instructors = new ArrayList<Instructor>();
        customers = new ArrayList<Customer>();
        registrations = new ArrayList<Registration>();
        sessions = new ArrayList<Session>();
        fitnessClasses = new ArrayList<FitnessClass>();
    }

    public SportCenter(String aName, int aMaxCapacity, Time aOpeningTime, Time aClosingTime,
            String aUsernameForOwner, String aEmailForOwner, String aPasswordForOwner, String aLastNameForOwner,
            String aFirstNameForOwner) {
        name = aName;
        maxCapacity = aMaxCapacity;
        openingTime = aOpeningTime;
        closingTime = aClosingTime;
        owner = new Owner(aUsernameForOwner, aEmailForOwner, aPasswordForOwner, aLastNameForOwner, aFirstNameForOwner,
                this);
        instructors = new ArrayList<Instructor>();
        customers = new ArrayList<Customer>();
        registrations = new ArrayList<Registration>();
        sessions = new ArrayList<Session>();
        fitnessClasses = new ArrayList<FitnessClass>();
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

    public boolean setMaxCapacity(int aMaxCapacity) {
        boolean wasSet = false;
        maxCapacity = aMaxCapacity;
        wasSet = true;
        return wasSet;
    }

    public boolean setOpeningTime(Time aOpeningTime) {
        boolean wasSet = false;
        openingTime = aOpeningTime;
        wasSet = true;
        return wasSet;
    }

    public boolean setClosingTime(Time aClosingTime) {
        boolean wasSet = false;
        closingTime = aClosingTime;
        wasSet = true;
        return wasSet;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    /* Code from template association_GetOne */
    public Owner getOwner() {
        return owner;
    }

    /* Code from template association_GetMany */
    public Instructor getInstructor(int index) {
        Instructor aInstructor = instructors.get(index);
        return aInstructor;
    }

    public List<Instructor> getInstructors() {
        List<Instructor> newInstructors = Collections.unmodifiableList(instructors);
        return newInstructors;
    }

    public int numberOfInstructors() {
        int number = instructors.size();
        return number;
    }

    public boolean hasInstructors() {
        boolean has = instructors.size() > 0;
        return has;
    }

    public int indexOfInstructor(Instructor aInstructor) {
        int index = instructors.indexOf(aInstructor);
        return index;
    }

    /* Code from template association_GetMany */
    public Customer getCustomer(int index) {
        Customer aCustomer = customers.get(index);
        return aCustomer;
    }

    public List<Customer> getCustomers() {
        List<Customer> newCustomers = Collections.unmodifiableList(customers);
        return newCustomers;
    }

    public int numberOfCustomers() {
        int number = customers.size();
        return number;
    }

    public boolean hasCustomers() {
        boolean has = customers.size() > 0;
        return has;
    }

    public int indexOfCustomer(Customer aCustomer) {
        int index = customers.indexOf(aCustomer);
        return index;
    }

    /* Code from template association_GetMany */
    public Registration getRegistration(int index) {
        Registration aRegistration = registrations.get(index);
        return aRegistration;
    }

    public List<Registration> getRegistrations() {
        List<Registration> newRegistrations = Collections.unmodifiableList(registrations);
        return newRegistrations;
    }

    public int numberOfRegistrations() {
        int number = registrations.size();
        return number;
    }

    public boolean hasRegistrations() {
        boolean has = registrations.size() > 0;
        return has;
    }

    public int indexOfRegistration(Registration aRegistration) {
        int index = registrations.indexOf(aRegistration);
        return index;
    }

    /* Code from template association_GetMany */
    public Session getSession(int index) {
        Session aSession = sessions.get(index);
        return aSession;
    }

    public List<Session> getSessions() {
        List<Session> newSessions = Collections.unmodifiableList(sessions);
        return newSessions;
    }

    public int numberOfSessions() {
        int number = sessions.size();
        return number;
    }

    public boolean hasSessions() {
        boolean has = sessions.size() > 0;
        return has;
    }

    public int indexOfSession(Session aSession) {
        int index = sessions.indexOf(aSession);
        return index;
    }

    /* Code from template association_GetMany */
    public FitnessClass getFitnessClass(int index) {
        FitnessClass aFitnessClass = fitnessClasses.get(index);
        return aFitnessClass;
    }

    public List<FitnessClass> getFitnessClasses() {
        List<FitnessClass> newFitnessClasses = Collections.unmodifiableList(fitnessClasses);
        return newFitnessClasses;
    }

    public int numberOfFitnessClasses() {
        int number = fitnessClasses.size();
        return number;
    }

    public boolean hasFitnessClasses() {
        boolean has = fitnessClasses.size() > 0;
        return has;
    }

    public int indexOfFitnessClass(FitnessClass aFitnessClass) {
        int index = fitnessClasses.indexOf(aFitnessClass);
        return index;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfInstructors() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Instructor addInstructor(String aUsername, String aEmail, String aPassword, String aLastName,
            String aFirstName) {
        return new Instructor(aUsername, aEmail, aPassword, aLastName, aFirstName, this);
    }

    public boolean addInstructor(Instructor aInstructor) {
        boolean wasAdded = false;
        if (instructors.contains(aInstructor)) {
            return false;
        }
        SportCenter existingSportCenter = aInstructor.getSportCenter();
        boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
        if (isNewSportCenter) {
            aInstructor.setSportCenter(this);
        } else {
            instructors.add(aInstructor);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeInstructor(Instructor aInstructor) {
        boolean wasRemoved = false;
        // Unable to remove aInstructor, as it must always have a sportCenter
        if (!this.equals(aInstructor.getSportCenter())) {
            instructors.remove(aInstructor);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addInstructorAt(Instructor aInstructor, int index) {
        boolean wasAdded = false;
        if (addInstructor(aInstructor)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfInstructors()) {
                index = numberOfInstructors() - 1;
            }
            instructors.remove(aInstructor);
            instructors.add(index, aInstructor);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveInstructorAt(Instructor aInstructor, int index) {
        boolean wasAdded = false;
        if (instructors.contains(aInstructor)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfInstructors()) {
                index = numberOfInstructors() - 1;
            }
            instructors.remove(aInstructor);
            instructors.add(index, aInstructor);
            wasAdded = true;
        } else {
            wasAdded = addInstructorAt(aInstructor, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfCustomers() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Customer addCustomer(String aUsername, String aEmail, String aPassword, String aLastName,
            String aFirstName) {
        return new Customer(aUsername, aEmail, aPassword, aLastName, aFirstName, this);
    }

    public boolean addCustomer(Customer aCustomer) {
        boolean wasAdded = false;
        if (customers.contains(aCustomer)) {
            return false;
        }
        SportCenter existingSportCenter = aCustomer.getSportCenter();
        boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
        if (isNewSportCenter) {
            aCustomer.setSportCenter(this);
        } else {
            customers.add(aCustomer);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCustomer(Customer aCustomer) {
        boolean wasRemoved = false;
        // Unable to remove aCustomer, as it must always have a sportCenter
        if (!this.equals(aCustomer.getSportCenter())) {
            customers.remove(aCustomer);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addCustomerAt(Customer aCustomer, int index) {
        boolean wasAdded = false;
        if (addCustomer(aCustomer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCustomers()) {
                index = numberOfCustomers() - 1;
            }
            customers.remove(aCustomer);
            customers.add(index, aCustomer);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveCustomerAt(Customer aCustomer, int index) {
        boolean wasAdded = false;
        if (customers.contains(aCustomer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCustomers()) {
                index = numberOfCustomers() - 1;
            }
            customers.remove(aCustomer);
            customers.add(index, aCustomer);
            wasAdded = true;
        } else {
            wasAdded = addCustomerAt(aCustomer, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRegistrations() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Registration addRegistration(Date aDate, Session aSession, Customer aCustomer) {
        return new Registration(aDate, aSession, aCustomer, this);
    }

    public boolean addRegistration(Registration aRegistration) {
        boolean wasAdded = false;
        if (registrations.contains(aRegistration)) {
            return false;
        }
        SportCenter existingSportCenter = aRegistration.getSportCenter();
        boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
        if (isNewSportCenter) {
            aRegistration.setSportCenter(this);
        } else {
            registrations.add(aRegistration);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeRegistration(Registration aRegistration) {
        boolean wasRemoved = false;
        // Unable to remove aRegistration, as it must always have a sportCenter
        if (!this.equals(aRegistration.getSportCenter())) {
            registrations.remove(aRegistration);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addRegistrationAt(Registration aRegistration, int index) {
        boolean wasAdded = false;
        if (addRegistration(aRegistration)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRegistrations()) {
                index = numberOfRegistrations() - 1;
            }
            registrations.remove(aRegistration);
            registrations.add(index, aRegistration);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveRegistrationAt(Registration aRegistration, int index) {
        boolean wasAdded = false;
        if (registrations.contains(aRegistration)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRegistrations()) {
                index = numberOfRegistrations() - 1;
            }
            registrations.remove(aRegistration);
            registrations.add(index, aRegistration);
            wasAdded = true;
        } else {
            wasAdded = addRegistrationAt(aRegistration, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfSessions() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Session addSession(int aPrice, Time aEndTime, Time aStartTime, Date aDate, Instructor aInstructor,
            FitnessClass aFitnessClass) {
        return new Session(aPrice, aEndTime, aStartTime, aDate, aInstructor, aFitnessClass, this);
    }

    public boolean addSession(Session aSession) {
        boolean wasAdded = false;
        if (sessions.contains(aSession)) {
            return false;
        }
        SportCenter existingSportCenter = aSession.getSportCenter();
        boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
        if (isNewSportCenter) {
            aSession.setSportCenter(this);
        } else {
            sessions.add(aSession);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeSession(Session aSession) {
        boolean wasRemoved = false;
        // Unable to remove aSession, as it must always have a sportCenter
        if (!this.equals(aSession.getSportCenter())) {
            sessions.remove(aSession);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addSessionAt(Session aSession, int index) {
        boolean wasAdded = false;
        if (addSession(aSession)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfSessions()) {
                index = numberOfSessions() - 1;
            }
            sessions.remove(aSession);
            sessions.add(index, aSession);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveSessionAt(Session aSession, int index) {
        boolean wasAdded = false;
        if (sessions.contains(aSession)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfSessions()) {
                index = numberOfSessions() - 1;
            }
            sessions.remove(aSession);
            sessions.add(index, aSession);
            wasAdded = true;
        } else {
            wasAdded = addSessionAt(aSession, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfFitnessClasses() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public FitnessClass addFitnessClass(String aName, String aDescription) {
        return new FitnessClass(aName, aDescription, this);
    }

    public boolean addFitnessClass(FitnessClass aFitnessClass) {
        boolean wasAdded = false;
        if (fitnessClasses.contains(aFitnessClass)) {
            return false;
        }
        SportCenter existingSportCenter = aFitnessClass.getSportCenter();
        boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
        if (isNewSportCenter) {
            aFitnessClass.setSportCenter(this);
        } else {
            fitnessClasses.add(aFitnessClass);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeFitnessClass(FitnessClass aFitnessClass) {
        boolean wasRemoved = false;
        // Unable to remove aFitnessClass, as it must always have a sportCenter
        if (!this.equals(aFitnessClass.getSportCenter())) {
            fitnessClasses.remove(aFitnessClass);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addFitnessClassAt(FitnessClass aFitnessClass, int index) {
        boolean wasAdded = false;
        if (addFitnessClass(aFitnessClass)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfFitnessClasses()) {
                index = numberOfFitnessClasses() - 1;
            }
            fitnessClasses.remove(aFitnessClass);
            fitnessClasses.add(index, aFitnessClass);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveFitnessClassAt(FitnessClass aFitnessClass, int index) {
        boolean wasAdded = false;
        if (fitnessClasses.contains(aFitnessClass)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfFitnessClasses()) {
                index = numberOfFitnessClasses() - 1;
            }
            fitnessClasses.remove(aFitnessClass);
            fitnessClasses.add(index, aFitnessClass);
            wasAdded = true;
        } else {
            wasAdded = addFitnessClassAt(aFitnessClass, index);
        }
        return wasAdded;
    }

    public void delete() {
        Owner existingOwner = owner;
        owner = null;
        if (existingOwner != null) {
            existingOwner.delete();
        }
        while (instructors.size() > 0) {
            Instructor aInstructor = instructors.get(instructors.size() - 1);
            aInstructor.delete();
            instructors.remove(aInstructor);
        }

        while (customers.size() > 0) {
            Customer aCustomer = customers.get(customers.size() - 1);
            aCustomer.delete();
            customers.remove(aCustomer);
        }

        while (registrations.size() > 0) {
            Registration aRegistration = registrations.get(registrations.size() - 1);
            aRegistration.delete();
            registrations.remove(aRegistration);
        }

        while (sessions.size() > 0) {
            Session aSession = sessions.get(sessions.size() - 1);
            aSession.delete();
            sessions.remove(aSession);
        }

        while (fitnessClasses.size() > 0) {
            FitnessClass aFitnessClass = fitnessClasses.get(fitnessClasses.size() - 1);
            aFitnessClass.delete();
            fitnessClasses.remove(aFitnessClass);
        }

    }

    public String toString() {
        return super.toString() + "[" +
                "id" + ":" + getId() + "," +
                "name" + ":" + getName() + "," +
                "maxCapacity" + ":" + getMaxCapacity() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "openingTime" + "="
                + (getOpeningTime() != null
                        ? !getOpeningTime().equals(this) ? getOpeningTime().toString().replaceAll("  ", "    ") : "this"
                        : "null")
                + System.getProperties().getProperty("line.separator") +
                "  " + "closingTime" + "="
                + (getClosingTime() != null
                        ? !getClosingTime().equals(this) ? getClosingTime().toString().replaceAll("  ", "    ") : "this"
                        : "null")
                + System.getProperties().getProperty("line.separator") +
                "  " + "owner = "
                + (getOwner() != null ? Integer.toHexString(System.identityHashCode(getOwner())) : "null");
    }
}