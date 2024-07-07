package ca.mcgill.ecse321.gitfit;

import java.sql.Date;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ca.mcgill.ecse321.gitfit.dao.BillingRepository;
import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.FitnessClassRepository;
import ca.mcgill.ecse321.gitfit.dao.InstructorRepository;
import ca.mcgill.ecse321.gitfit.dao.OwnerRepository;
import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.model.Billing;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.FitnessClassApprovalStatus;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private FitnessClassRepository fitnessClassRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    public DataLoader(SportCenterRepository sportCenterRepository, OwnerRepository ownerRepository,
            CustomerRepository customerRepository, BillingRepository billingRepository,
            InstructorRepository instructorRepository, FitnessClassRepository fitnessClassRepository,
            SessionRepository sessionRepository, RegistrationRepository registrationRepository) {
        this.sportCenterRepository = sportCenterRepository;
        this.ownerRepository = ownerRepository;
        this.customerRepository = customerRepository;
        this.billingRepository = billingRepository;
        this.instructorRepository = instructorRepository;
        this.fitnessClassRepository = fitnessClassRepository;
        this.sessionRepository = sessionRepository;
        this.registrationRepository = registrationRepository;
    }

    public void run(ApplicationArguments args) {
        // Create sport center with owner
        SportCenter sportCenter = new SportCenter(
                "GitFit",
                100,
                Time.valueOf("06:00:00"),
                Time.valueOf("22:00:00"),
                "admin",
                "admin@gitfit.com",
                "securepassword",
                "Doe",
                "John"

        );
        sportCenter = sportCenterRepository.save(sportCenter);
        ownerRepository.save(sportCenter.getOwner());

        // Create customers
        Customer customer1 = new Customer();
        customer1.setUsername("john_smith");
        customer1.setEmail("johnsmith@gmail.com");
        customer1.setPassword("JohnSmith123");
        customer1.setLastName("Smith");
        customer1.setFirstName("John");
        customer1.setSportCenter(sportCenter);
        customer1 = customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setUsername("jane_doe");
        customer2.setEmail("janedoe@gmail.com");
        customer2.setPassword("JaneDoe123");
        customer2.setLastName("Doe");
        customer2.setFirstName("Jane");
        customer2.setSportCenter(sportCenter);
        customer2 = customerRepository.save(customer2);

        // Create billing for the customers
        Billing billing1 = new Billing();
        billing1.setCountry("USA");
        billing1.setState("California");
        billing1.setPostalCode("90001");
        billing1.setCardNumber("1234567812345678");
        billing1.setAddress("123 Main St");
        billing1.setCustomer(customer1);
        billing1 = billingRepository.save(billing1);

        Billing billing2 = new Billing();
        billing2.setCountry("USA");
        billing2.setState("New York");
        billing2.setPostalCode("10001");
        billing2.setCardNumber("2345678923456789");
        billing2.setAddress("456 Broadway");
        billing2.setCustomer(customer2);
        billing2 = billingRepository.save(billing2);

        // Create instructors
        Instructor instructor1 = new Instructor();
        instructor1.setUsername("adam_smith");
        instructor1.setEmail("adam.smith@gitfit.com");
        instructor1.setPassword("AdamRocks2022");
        instructor1.setLastName("Smith");
        instructor1.setFirstName("Adam");
        instructor1.setSportCenter(sportCenter);
        instructor1 = instructorRepository.save(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setUsername("bella_johnson");
        instructor2.setEmail("bella.johnson@gitfit.com");
        instructor2.setPassword("Bella2022");
        instructor2.setLastName("Johnson");
        instructor2.setFirstName("Bella");
        instructor2.setSportCenter(sportCenter);
        instructor2 = instructorRepository.save(instructor2);

        Instructor instructor3 = new Instructor();
        instructor3.setUsername("charlie_kent");
        instructor3.setEmail("charlie.kent@gitfit.com");
        instructor3.setPassword("Charlie2022");
        instructor3.setLastName("Kent");
        instructor3.setFirstName("Charlie");
        instructor3.setSportCenter(sportCenter);
        instructor3 = instructorRepository.save(instructor3);

        Instructor instructor4 = new Instructor();
        instructor4.setUsername("diana_murphy");
        instructor4.setEmail("diana.murphy@gitfit.com");
        instructor4.setPassword("Diana2022");
        instructor4.setLastName("Murphy");
        instructor4.setFirstName("Diana");
        instructor4.setSportCenter(sportCenter);
        instructor4 = instructorRepository.save(instructor4);

        Instructor instructor5 = new Instructor();
        instructor5.setUsername("edward_jones");
        instructor5.setEmail("edward.jones@gitfit.com");
        instructor5.setPassword("Edward2022");
        instructor5.setLastName("Jones");
        instructor5.setFirstName("Edward");
        instructor5.setSportCenter(sportCenter);
        instructor5 = instructorRepository.save(instructor5);

        Instructor instructor6 = new Instructor();
        instructor6.setUsername("fiona_white");
        instructor6.setEmail("fiona.white@gitfit.com");
        instructor6.setPassword("Fiona2022");
        instructor6.setLastName("White");
        instructor6.setFirstName("Fiona");
        instructor6.setSportCenter(sportCenter);
        instructor6 = instructorRepository.save(instructor6);

        Instructor instructor7 = new Instructor();
        instructor7.setUsername("george_harris");
        instructor7.setEmail("george.harris@gitfit.com");
        instructor7.setPassword("George2022");
        instructor7.setLastName("Harris");
        instructor7.setFirstName("George");
        instructor7.setSportCenter(sportCenter);
        instructor7 = instructorRepository.save(instructor7);

        Instructor instructor8 = new Instructor();
        instructor8.setUsername("hannah_baker");
        instructor8.setEmail("hannah.baker@gitfit.com");
        instructor8.setPassword("Hannah2022");
        instructor8.setLastName("Baker");
        instructor8.setFirstName("Hannah");
        instructor8.setSportCenter(sportCenter);
        instructor8 = instructorRepository.save(instructor8);

        Instructor instructor9 = new Instructor();
        instructor9.setUsername("ian_davis");
        instructor9.setEmail("ian.davis@gitfit.com");
        instructor9.setPassword("Ian2022");
        instructor9.setLastName("Davis");
        instructor9.setFirstName("Ian");
        instructor9.setSportCenter(sportCenter);
        instructor9 = instructorRepository.save(instructor9);

        Instructor instructor10 = new Instructor();
        instructor10.setUsername("jessica_taylor");
        instructor10.setEmail("jessica.taylor@gitfit.com");
        instructor10.setPassword("Jessica2022");
        instructor10.setLastName("Taylor");
        instructor10.setFirstName("Jessica");
        instructor10.setSportCenter(sportCenter);
        instructor10 = instructorRepository.save(instructor10);

        FitnessClass fitnessClass1 = new FitnessClass();
        fitnessClass1.setId(1);
        fitnessClass1.setName("Yoga Beginners");
        fitnessClass1.setDescription("A class for beginners to learn basic yoga poses");
        fitnessClass1.setSportCenter(sportCenter);
        fitnessClass1.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass1 = fitnessClassRepository.save(fitnessClass1);

        FitnessClass fitnessClass2 = new FitnessClass();
        fitnessClass2.setId(2);
        fitnessClass2.setName("Advanced Yoga");
        fitnessClass2.setDescription("A class for experienced yoga practitioners");
        fitnessClass2.setSportCenter(sportCenter);
        fitnessClass2.setApprovalStatus(FitnessClassApprovalStatus.PENDING);
        fitnessClass2 = fitnessClassRepository.save(fitnessClass2);

        FitnessClass fitnessClass3 = new FitnessClass();
        fitnessClass3.setId(3);
        fitnessClass3.setName("Pilates");
        fitnessClass3.setDescription("A class to strengthen your core with pilates");
        fitnessClass3.setSportCenter(sportCenter);
        fitnessClass3.setApprovalStatus(FitnessClassApprovalStatus.REJECTED);
        fitnessClass3 = fitnessClassRepository.save(fitnessClass3);

        FitnessClass fitnessClass4 = new FitnessClass();
        fitnessClass4.setId(4);
        fitnessClass4.setName("Zumba");
        fitnessClass4.setDescription("A fun, high-energy dance workout");
        fitnessClass4.setSportCenter(sportCenter);
        fitnessClass4.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass4 = fitnessClassRepository.save(fitnessClass4);

        FitnessClass fitnessClass5 = new FitnessClass();
        fitnessClass5.setId(5);
        fitnessClass5.setName("Kickboxing");
        fitnessClass5.setDescription("A class combining boxing and martial arts");
        fitnessClass5.setSportCenter(sportCenter);
        fitnessClass5.setApprovalStatus(FitnessClassApprovalStatus.PENDING);
        fitnessClass5 = fitnessClassRepository.save(fitnessClass5);

        FitnessClass fitnessClass6 = new FitnessClass();
        fitnessClass6.setId(6);
        fitnessClass6.setName("Spin Class");
        fitnessClass6.setDescription("A high-intensity cycling workout");
        fitnessClass6.setSportCenter(sportCenter);
        fitnessClass6.setApprovalStatus(FitnessClassApprovalStatus.REJECTED);
        fitnessClass6 = fitnessClassRepository.save(fitnessClass6);

        FitnessClass fitnessClass7 = new FitnessClass();
        fitnessClass7.setId(7);
        fitnessClass7.setName("HIIT");
        fitnessClass7.setDescription("High Intensity Interval Training for maximum fat burn");
        fitnessClass7.setSportCenter(sportCenter);
        fitnessClass7.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass7 = fitnessClassRepository.save(fitnessClass7);

        FitnessClass fitnessClass8 = new FitnessClass();
        fitnessClass8.setId(8);
        fitnessClass8.setName("Boot Camp");
        fitnessClass8.setDescription("A military-style physical training class");
        fitnessClass8.setSportCenter(sportCenter);
        fitnessClass8.setApprovalStatus(FitnessClassApprovalStatus.PENDING);
        fitnessClass8 = fitnessClassRepository.save(fitnessClass8);

        FitnessClass fitnessClass9 = new FitnessClass();
        fitnessClass9.setId(9);
        fitnessClass9.setName("Aerobics");
        fitnessClass9.setDescription("A class to improve all elements of fitness");
        fitnessClass9.setSportCenter(sportCenter);
        fitnessClass9.setApprovalStatus(FitnessClassApprovalStatus.REJECTED);
        fitnessClass9 = fitnessClassRepository.save(fitnessClass9);

        FitnessClass fitnessClass10 = new FitnessClass();
        fitnessClass10.setId(10);
        fitnessClass10.setName("Tai Chi");
        fitnessClass10.setDescription("A class for gentle and calming martial arts");
        fitnessClass10.setSportCenter(sportCenter);
        fitnessClass10.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass10 = fitnessClassRepository.save(fitnessClass10);

        FitnessClass fitnessClass11 = new FitnessClass();
        fitnessClass11.setId(11);
        fitnessClass11.setName("Barre");
        fitnessClass11.setDescription("A low-impact workout combining ballet, Pilates, and yoga");
        fitnessClass11.setSportCenter(sportCenter);
        fitnessClass11.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass11 = fitnessClassRepository.save(fitnessClass11);

        FitnessClass fitnessClass12 = new FitnessClass();
        fitnessClass12.setId(12);
        fitnessClass12.setName("CrossFit");
        fitnessClass12.setDescription("A high-intensity fitness program incorporating various exercises");
        fitnessClass12.setSportCenter(sportCenter);
        fitnessClass12.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass12 = fitnessClassRepository.save(fitnessClass12);

        FitnessClass fitnessClass13 = new FitnessClass();
        fitnessClass13.setId(13);
        fitnessClass13.setName("Pole Dancing");
        fitnessClass13.setDescription("A fun and challenging workout using a vertical pole");
        fitnessClass13.setSportCenter(sportCenter);
        fitnessClass13.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass13 = fitnessClassRepository.save(fitnessClass13);

        FitnessClass fitnessClass14 = new FitnessClass();
        fitnessClass14.setId(14);
        fitnessClass14.setName("TRX Suspension Training");
        fitnessClass14.setDescription("A total-body workout using suspension straps");
        fitnessClass14.setSportCenter(sportCenter);
        fitnessClass14.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass14 = fitnessClassRepository.save(fitnessClass14);

        FitnessClass fitnessClass15 = new FitnessClass();
        fitnessClass15.setId(15);
        fitnessClass15.setName("Bodyweight Training");
        fitnessClass15.setDescription("A strength training workout using only your body weight");
        fitnessClass15.setSportCenter(sportCenter);
        fitnessClass15.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass15 = fitnessClassRepository.save(fitnessClass15);

        FitnessClass fitnessClass16 = new FitnessClass();
        fitnessClass16.setId(16);
        fitnessClass16.setName("Parkour");
        fitnessClass16.setDescription("An urban sport involving obstacle course running");
        fitnessClass16.setSportCenter(sportCenter);
        fitnessClass16.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass16 = fitnessClassRepository.save(fitnessClass16);

        FitnessClass fitnessClass17 = new FitnessClass();
        fitnessClass17.setId(17);
        fitnessClass17.setName("Capoeira");
        fitnessClass17.setDescription("A Brazilian martial art combining dance and acrobatics");
        fitnessClass17.setSportCenter(sportCenter);
        fitnessClass17.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass17 = fitnessClassRepository.save(fitnessClass17);

        FitnessClass fitnessClass18 = new FitnessClass();
        fitnessClass18.setId(18);
        fitnessClass18.setName("Circuit Training");
        fitnessClass18.setDescription("A series of exercises targeting different muscle groups");
        fitnessClass18.setSportCenter(sportCenter);
        fitnessClass18.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass18 = fitnessClassRepository.save(fitnessClass18);

        FitnessClass fitnessClass19 = new FitnessClass();
        fitnessClass19.setId(19);
        fitnessClass19.setName("Hula Hoop Fitness");
        fitnessClass19.setDescription("A fun way to exercise using hula hoops");
        fitnessClass19.setSportCenter(sportCenter);
        fitnessClass19.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass19 = fitnessClassRepository.save(fitnessClass19);

        FitnessClass fitnessClass20 = new FitnessClass();
        fitnessClass20.setId(20);
        fitnessClass20.setName("Salsa Fitness");
        fitnessClass20.setDescription("A dance fitness class incorporating salsa moves");
        fitnessClass20.setSportCenter(sportCenter);
        fitnessClass20.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        fitnessClass20 = fitnessClassRepository.save(fitnessClass20);

        // Create sessions for fitness classes
        Session session1 = new Session();
        session1.setPrice(50);
        session1.setStartTime(Time.valueOf("10:00:00"));
        session1.setEndTime(Time.valueOf("11:00:00"));
        session1.setDate(Date.valueOf("2024-06-01"));
        session1.setInstructor(instructor1);
        session1.setFitnessClass(fitnessClass1);
        session1.setSportCenter(sportCenter);
        session1 = sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setPrice(60);
        session2.setStartTime(Time.valueOf("12:00:00"));
        session2.setEndTime(Time.valueOf("13:00:00"));
        session2.setDate(Date.valueOf("2024-06-02"));
        session2.setInstructor(instructor2);
        session2.setFitnessClass(fitnessClass14);
        session2.setSportCenter(sportCenter);
        session2 = sessionRepository.save(session2);

        Session session3 = new Session();
        session3.setPrice(70);
        session3.setStartTime(Time.valueOf("14:00:00"));
        session3.setEndTime(Time.valueOf("15:00:00"));
        session3.setDate(Date.valueOf("2024-06-03"));
        session3.setInstructor(instructor3);
        session3.setFitnessClass(fitnessClass7);
        session3.setSportCenter(sportCenter);
        session3 = sessionRepository.save(session3);

        Session session4 = new Session();
        session4.setPrice(80);
        session4.setStartTime(Time.valueOf("16:00:00"));
        session4.setEndTime(Time.valueOf("17:00:00"));
        session4.setDate(Date.valueOf("2024-06-04"));
        session4.setInstructor(instructor4);
        session4.setFitnessClass(fitnessClass10);
        session4.setSportCenter(sportCenter);
        session4 = sessionRepository.save(session4);

        Session session5 = new Session();
        session5.setPrice(90);
        session5.setStartTime(Time.valueOf("18:00:00"));
        session5.setEndTime(Time.valueOf("19:00:00"));
        session5.setDate(Date.valueOf("2024-06-05"));
        session5.setInstructor(instructor5);
        session5.setFitnessClass(fitnessClass11);
        session5.setSportCenter(sportCenter);
        session5 = sessionRepository.save(session5);

        Session session6 = new Session();
        session6.setPrice(100);
        session6.setStartTime(Time.valueOf("20:00:00"));
        session6.setEndTime(Time.valueOf("21:00:00"));
        session6.setDate(Date.valueOf("2024-06-06"));
        session6.setInstructor(instructor6);
        session6.setFitnessClass(fitnessClass10);
        session6.setSportCenter(sportCenter);
        session6 = sessionRepository.save(session6);

        Session session7 = new Session();
        session7.setPrice(110);
        session7.setStartTime(Time.valueOf("10:00:00"));
        session7.setEndTime(Time.valueOf("11:00:00"));
        session7.setDate(Date.valueOf("2024-06-07"));
        session7.setInstructor(instructor7);
        session7.setFitnessClass(fitnessClass16);
        session7.setSportCenter(sportCenter);
        session7 = sessionRepository.save(session7);

        Session session8 = new Session();
        session8.setPrice(120);
        session8.setStartTime(Time.valueOf("12:00:00"));
        session8.setEndTime(Time.valueOf("13:00:00"));
        session8.setDate(Date.valueOf("2024-06-08"));
        session8.setInstructor(instructor8);
        session8.setFitnessClass(fitnessClass19);
        session8.setSportCenter(sportCenter);
        session8 = sessionRepository.save(session8);

        Session session9 = new Session();
        session9.setPrice(130);
        session9.setStartTime(Time.valueOf("14:00:00"));
        session9.setEndTime(Time.valueOf("15:00:00"));
        session9.setDate(Date.valueOf("2024-06-09"));
        session9.setInstructor(instructor9);
        session9.setFitnessClass(fitnessClass15);
        session9.setSportCenter(sportCenter);
        session9 = sessionRepository.save(session9);

        Session session10 = new Session();
        session10.setPrice(140);
        session10.setStartTime(Time.valueOf("16:00:00"));
        session10.setEndTime(Time.valueOf("17:00:00"));
        session10.setDate(Date.valueOf("2024-06-10"));
        session10.setInstructor(instructor10);
        session10.setFitnessClass(fitnessClass12);
        session10.setSportCenter(sportCenter);
        session10 = sessionRepository.save(session10);

        Session session11 = new Session();
        session11.setPrice(120);
        session11.setStartTime(Time.valueOf("09:30:00"));
        session11.setEndTime(Time.valueOf("10:30:00"));
        session11.setDate(Date.valueOf("2024-06-01"));
        session11.setInstructor(instructor3);
        session11.setFitnessClass(fitnessClass14);
        session11.setSportCenter(sportCenter);
        session11 = sessionRepository.save(session11);

        Session session12 = new Session();
        session12.setPrice(130);
        session12.setStartTime(Time.valueOf("10:30:00"));
        session12.setEndTime(Time.valueOf("11:30:00"));
        session12.setDate(Date.valueOf("2024-06-02"));
        session12.setInstructor(instructor8);
        session12.setFitnessClass(fitnessClass17);
        session12.setSportCenter(sportCenter);
        session12 = sessionRepository.save(session12);

        Session session13 = new Session();
        session13.setPrice(110);
        session13.setStartTime(Time.valueOf("11:30:00"));
        session13.setEndTime(Time.valueOf("12:30:00"));
        session13.setDate(Date.valueOf("2024-06-03"));
        session13.setInstructor(instructor2);
        session13.setFitnessClass(fitnessClass18);
        session13.setSportCenter(sportCenter);
        session13 = sessionRepository.save(session13);

        Session session14 = new Session();
        session14.setPrice(140);
        session14.setStartTime(Time.valueOf("12:30:00"));
        session14.setEndTime(Time.valueOf("13:30:00"));
        session14.setDate(Date.valueOf("2024-06-04"));
        session14.setInstructor(instructor7);
        session14.setFitnessClass(fitnessClass4);
        session14.setSportCenter(sportCenter);
        session14 = sessionRepository.save(session14);

        Session session15 = new Session();
        session15.setPrice(100);
        session15.setStartTime(Time.valueOf("13:30:00"));
        session15.setEndTime(Time.valueOf("14:30:00"));
        session15.setDate(Date.valueOf("2024-06-05"));
        session15.setInstructor(instructor4);
        session15.setFitnessClass(fitnessClass16);
        session15.setSportCenter(sportCenter);
        session15 = sessionRepository.save(session15);

        Session session16 = new Session();
        session16.setPrice(120);
        session16.setStartTime(Time.valueOf("14:30:00"));
        session16.setEndTime(Time.valueOf("15:30:00"));
        session16.setDate(Date.valueOf("2024-06-06"));
        session16.setInstructor(instructor9);
        session16.setFitnessClass(fitnessClass20);
        session16.setSportCenter(sportCenter);
        session16 = sessionRepository.save(session16);

        Session session17 = new Session();
        session17.setPrice(130);
        session17.setStartTime(Time.valueOf("15:30:00"));
        session17.setEndTime(Time.valueOf("16:30:00"));
        session17.setDate(Date.valueOf("2024-06-07"));
        session17.setInstructor(instructor1);
        session17.setFitnessClass(fitnessClass12);
        session17.setSportCenter(sportCenter);
        session17 = sessionRepository.save(session17);

        Session session18 = new Session();
        session18.setPrice(110);
        session18.setStartTime(Time.valueOf("16:30:00"));
        session18.setEndTime(Time.valueOf("17:30:00"));
        session18.setDate(Date.valueOf("2024-06-08"));
        session18.setInstructor(instructor6);
        session18.setFitnessClass(fitnessClass14);
        session18.setSportCenter(sportCenter);
        session18 = sessionRepository.save(session18);

        Session session19 = new Session();
        session19.setPrice(100);
        session19.setStartTime(Time.valueOf("17:30:00"));
        session19.setEndTime(Time.valueOf("18:30:00"));
        session19.setDate(Date.valueOf("2024-06-09"));
        session19.setInstructor(instructor5);
        session19.setFitnessClass(fitnessClass7);
        session19.setSportCenter(sportCenter);
        session19 = sessionRepository.save(session19);

        Session session20 = new Session();
        session20.setPrice(140);
        session20.setStartTime(Time.valueOf("18:30:00"));
        session20.setEndTime(Time.valueOf("19:30:00"));
        session20.setDate(Date.valueOf("2024-06-10"));
        session20.setInstructor(instructor10);
        session20.setFitnessClass(fitnessClass4);
        session20.setSportCenter(sportCenter);
        session20 = sessionRepository.save(session20);

        Session session21 = new Session();
        session21.setPrice(130);
        session21.setStartTime(Time.valueOf("09:00:00"));
        session21.setEndTime(Time.valueOf("10:00:00"));
        session21.setDate(Date.valueOf("2024-06-11"));
        session21.setInstructor(instructor1);
        session21.setFitnessClass(fitnessClass19);
        session21.setSportCenter(sportCenter);
        session21 = sessionRepository.save(session21);

        Session session22 = new Session();
        session22.setPrice(120);
        session22.setStartTime(Time.valueOf("10:00:00"));
        session22.setEndTime(Time.valueOf("11:00:00"));
        session22.setDate(Date.valueOf("2024-06-12"));
        session22.setInstructor(instructor8);
        session22.setFitnessClass(fitnessClass12);
        session22.setSportCenter(sportCenter);
        session22 = sessionRepository.save(session22);

        Session session23 = new Session();
        session23.setPrice(100);
        session23.setStartTime(Time.valueOf("11:00:00"));
        session23.setEndTime(Time.valueOf("12:00:00"));
        session23.setDate(Date.valueOf("2024-06-13"));
        session23.setInstructor(instructor2);
        session23.setFitnessClass(fitnessClass7);
        session23.setSportCenter(sportCenter);
        session23 = sessionRepository.save(session23);

        Session session24 = new Session();
        session24.setPrice(140);
        session24.setStartTime(Time.valueOf("12:00:00"));
        session24.setEndTime(Time.valueOf("13:00:00"));
        session24.setDate(Date.valueOf("2024-06-14"));
        session24.setInstructor(instructor3);
        session24.setFitnessClass(fitnessClass17);
        session24.setSportCenter(sportCenter);
        session24 = sessionRepository.save(session24);

        Session session25 = new Session();
        session25.setPrice(120);
        session25.setStartTime(Time.valueOf("13:00:00"));
        session25.setEndTime(Time.valueOf("14:00:00"));
        session25.setDate(Date.valueOf("2024-06-01"));
        session25.setInstructor(instructor7);
        session25.setFitnessClass(fitnessClass16);
        session25.setSportCenter(sportCenter);
        session25 = sessionRepository.save(session25);

        Session session26 = new Session();
        session26.setPrice(130);
        session26.setStartTime(Time.valueOf("14:00:00"));
        session26.setEndTime(Time.valueOf("15:00:00"));
        session26.setDate(Date.valueOf("2024-06-02"));
        session26.setInstructor(instructor9);
        session26.setFitnessClass(fitnessClass4);
        session26.setSportCenter(sportCenter);
        session26 = sessionRepository.save(session26);

        Session session27 = new Session();
        session27.setPrice(110);
        session27.setStartTime(Time.valueOf("15:00:00"));
        session27.setEndTime(Time.valueOf("16:00:00"));
        session27.setDate(Date.valueOf("2024-06-03"));
        session27.setInstructor(instructor10);
        session27.setFitnessClass(fitnessClass17);
        session27.setSportCenter(sportCenter);
        session27 = sessionRepository.save(session27);

        Session session28 = new Session();
        session28.setPrice(100);
        session28.setStartTime(Time.valueOf("16:00:00"));
        session28.setEndTime(Time.valueOf("17:00:00"));
        session28.setDate(Date.valueOf("2024-06-04"));
        session28.setInstructor(instructor4);
        session28.setFitnessClass(fitnessClass11);
        session28.setSportCenter(sportCenter);
        session28 = sessionRepository.save(session28);

        Session session29 = new Session();
        session29.setPrice(140);
        session29.setStartTime(Time.valueOf("17:00:00"));
        session29.setEndTime(Time.valueOf("18:00:00"));
        session29.setDate(Date.valueOf("2024-06-05"));
        session29.setInstructor(instructor6);
        session29.setFitnessClass(fitnessClass10);
        session29.setSportCenter(sportCenter);
        session29 = sessionRepository.save(session29);

        Session session30 = new Session();
        session30.setPrice(120);
        session30.setStartTime(Time.valueOf("18:00:00"));
        session30.setEndTime(Time.valueOf("19:00:00"));
        session30.setDate(Date.valueOf("2024-06-06"));
        session30.setInstructor(instructor5);
        session30.setFitnessClass(fitnessClass11);
        session30.setSportCenter(sportCenter);
        session30 = sessionRepository.save(session30);

        Session session31 = new Session();
        session31.setPrice(130);
        session31.setStartTime(Time.valueOf("09:30:00"));
        session31.setEndTime(Time.valueOf("10:30:00"));
        session31.setDate(Date.valueOf("2024-06-07"));
        session31.setInstructor(instructor10);
        session31.setFitnessClass(fitnessClass15);
        session31.setSportCenter(sportCenter);
        session31 = sessionRepository.save(session31);

        Session session32 = new Session();
        session32.setPrice(100);
        session32.setStartTime(Time.valueOf("10:30:00"));
        session32.setEndTime(Time.valueOf("11:30:00"));
        session32.setDate(Date.valueOf("2024-06-08"));
        session32.setInstructor(instructor1);
        session32.setFitnessClass(fitnessClass7);
        session32.setSportCenter(sportCenter);
        session32 = sessionRepository.save(session32);

        Session session33 = new Session();
        session33.setPrice(120);
        session33.setStartTime(Time.valueOf("11:30:00"));
        session33.setEndTime(Time.valueOf("12:30:00"));
        session33.setDate(Date.valueOf("2024-06-09"));
        session33.setInstructor(instructor8);
        session33.setFitnessClass(fitnessClass14);
        session33.setSportCenter(sportCenter);
        session33 = sessionRepository.save(session33);

        Session session34 = new Session();
        session34.setPrice(130);
        session34.setStartTime(Time.valueOf("12:30:00"));
        session34.setEndTime(Time.valueOf("13:30:00"));
        session34.setDate(Date.valueOf("2024-06-10"));
        session34.setInstructor(instructor2);
        session34.setFitnessClass(fitnessClass1);
        session34.setSportCenter(sportCenter);
        session34 = sessionRepository.save(session34);

        Session session35 = new Session();
        session35.setPrice(110);
        session35.setStartTime(Time.valueOf("13:30:00"));
        session35.setEndTime(Time.valueOf("14:30:00"));
        session35.setDate(Date.valueOf("2024-06-11"));
        session35.setInstructor(instructor7);
        session35.setFitnessClass(fitnessClass3);
        session35.setSportCenter(sportCenter);
        session35 = sessionRepository.save(session35);

        Session session36 = new Session();
        session36.setPrice(100);
        session36.setStartTime(Time.valueOf("14:30:00"));
        session36.setEndTime(Time.valueOf("15:30:00"));
        session36.setDate(Date.valueOf("2024-06-12"));
        session36.setInstructor(instructor9);
        session36.setFitnessClass(fitnessClass16);
        session36.setSportCenter(sportCenter);
        session36 = sessionRepository.save(session36);

        Session session37 = new Session();
        session37.setPrice(140);
        session37.setStartTime(Time.valueOf("15:30:00"));
        session37.setEndTime(Time.valueOf("16:30:00"));
        session37.setDate(Date.valueOf("2024-06-13"));
        session37.setInstructor(instructor10);
        session37.setFitnessClass(fitnessClass10);
        session37.setSportCenter(sportCenter);
        session37 = sessionRepository.save(session37);

        Session session38 = new Session();
        session38.setPrice(130);
        session38.setStartTime(Time.valueOf("16:30:00"));
        session38.setEndTime(Time.valueOf("17:30:00"));
        session38.setDate(Date.valueOf("2024-06-14"));
        session38.setInstructor(instructor4);
        session38.setFitnessClass(fitnessClass20);
        session38.setSportCenter(sportCenter);
        session38 = sessionRepository.save(session38);

        Session session39 = new Session();
        session39.setPrice(100);
        session39.setStartTime(Time.valueOf("17:30:00"));
        session39.setEndTime(Time.valueOf("18:30:00"));
        session39.setDate(Date.valueOf("2024-06-01"));
        session39.setInstructor(instructor6);
        session39.setFitnessClass(fitnessClass15);
        session39.setSportCenter(sportCenter);
        session39 = sessionRepository.save(session39);

        Session session40 = new Session();
        session40.setPrice(140);
        session40.setStartTime(Time.valueOf("18:30:00"));
        session40.setEndTime(Time.valueOf("19:30:00"));
        session40.setDate(Date.valueOf("2024-06-02"));
        session40.setInstructor(instructor5);
        session40.setFitnessClass(fitnessClass18);
        session40.setSportCenter(sportCenter);
        session40 = sessionRepository.save(session40);

        Session session41 = new Session();
        session41.setPrice(120);
        session41.setStartTime(Time.valueOf("09:00:00"));
        session41.setEndTime(Time.valueOf("10:00:00"));
        session41.setDate(Date.valueOf("2024-06-03"));
        session41.setInstructor(instructor10);
        session41.setFitnessClass(fitnessClass14);
        session41.setSportCenter(sportCenter);
        session41 = sessionRepository.save(session41);

        Session session42 = new Session();
        session42.setPrice(130);
        session42.setStartTime(Time.valueOf("10:00:00"));
        session42.setEndTime(Time.valueOf("11:00:00"));
        session42.setDate(Date.valueOf("2024-06-04"));
        session42.setInstructor(instructor1);
        session42.setFitnessClass(fitnessClass7);
        session42.setSportCenter(sportCenter);
        session42 = sessionRepository.save(session42);

        Session session43 = new Session();
        session43.setPrice(100);
        session43.setStartTime(Time.valueOf("11:00:00"));
        session43.setEndTime(Time.valueOf("12:00:00"));
        session43.setDate(Date.valueOf("2024-06-05"));
        session43.setInstructor(instructor8);
        session43.setFitnessClass(fitnessClass4);
        session43.setSportCenter(sportCenter);
        session43 = sessionRepository.save(session43);

        Session session44 = new Session();
        session44.setPrice(140);
        session44.setStartTime(Time.valueOf("12:00:00"));
        session44.setEndTime(Time.valueOf("13:00:00"));
        session44.setDate(Date.valueOf("2024-06-06"));
        session44.setInstructor(instructor2);
        session44.setFitnessClass(fitnessClass15);
        session44.setSportCenter(sportCenter);
        session44 = sessionRepository.save(session44);

        Session session45 = new Session();
        session45.setPrice(130);
        session45.setStartTime(Time.valueOf("13:00:00"));
        session45.setEndTime(Time.valueOf("14:00:00"));
        session45.setDate(Date.valueOf("2024-06-07"));
        session45.setInstructor(instructor7);
        session45.setFitnessClass(fitnessClass7);
        session45.setSportCenter(sportCenter);
        session45 = sessionRepository.save(session45);

        Session session46 = new Session();
        session46.setPrice(100);
        session46.setStartTime(Time.valueOf("14:00:00"));
        session46.setEndTime(Time.valueOf("15:00:00"));
        session46.setDate(Date.valueOf("2024-06-08"));
        session46.setInstructor(instructor9);
        session46.setFitnessClass(fitnessClass1);
        session46.setSportCenter(sportCenter);
        session46 = sessionRepository.save(session46);

        Session session47 = new Session();
        session47.setPrice(120);
        session47.setStartTime(Time.valueOf("15:00:00"));
        session47.setEndTime(Time.valueOf("16:00:00"));
        session47.setDate(Date.valueOf("2024-06-09"));
        session47.setInstructor(instructor10);
        session47.setFitnessClass(fitnessClass17);
        session47.setSportCenter(sportCenter);
        session47 = sessionRepository.save(session47);

        Session session48 = new Session();
        session48.setPrice(130);
        session48.setStartTime(Time.valueOf("16:00:00"));
        session48.setEndTime(Time.valueOf("17:00:00"));
        session48.setDate(Date.valueOf("2024-06-10"));
        session48.setInstructor(instructor4);
        session48.setFitnessClass(fitnessClass12);
        session48.setSportCenter(sportCenter);
        session48 = sessionRepository.save(session48);

        Session session49 = new Session();
        session49.setPrice(100);
        session49.setStartTime(Time.valueOf("17:00:00"));
        session49.setEndTime(Time.valueOf("18:00:00"));
        session49.setDate(Date.valueOf("2024-06-11"));
        session49.setInstructor(instructor6);
        session49.setFitnessClass(fitnessClass20);
        session49.setSportCenter(sportCenter);
        session49 = sessionRepository.save(session49);

        Session session50 = new Session();
        session50.setPrice(110);
        session50.setStartTime(Time.valueOf("18:00:00"));
        session50.setEndTime(Time.valueOf("19:00:00"));
        session50.setDate(Date.valueOf("2024-06-12"));
        session50.setInstructor(instructor5);
        session50.setFitnessClass(fitnessClass16);
        session50.setSportCenter(sportCenter);
        session50 = sessionRepository.save(session50);


        // Create registrations for customers
        Registration registration1 = new Registration();
        registration1.setDate(Date.valueOf("2024-01-05"));
        registration1.setSession(session1);
        registration1.setCustomer(customer1);
        registration1.setSportCenter(sportCenter);
        registration1 = registrationRepository.save(registration1);

        Registration registration2 = new Registration();
        registration2.setDate(Date.valueOf("2024-02-10"));
        registration2.setSession(session2);
        registration2.setCustomer(customer1);
        registration2.setSportCenter(sportCenter);
        registration2 = registrationRepository.save(registration2);

        Registration registration3 = new Registration();
        registration3.setDate(Date.valueOf("2024-03-15"));
        registration3.setSession(session3);
        registration3.setCustomer(customer1);
        registration3.setSportCenter(sportCenter);
        registration3 = registrationRepository.save(registration3);

        // Registrations for customer2
        Registration registration4 = new Registration();
        registration4.setDate(Date.valueOf("2024-01-10"));
        registration4.setSession(session1);
        registration4.setCustomer(customer2);
        registration4.setSportCenter(sportCenter);
        registration4 = registrationRepository.save(registration4);

        Registration registration5 = new Registration();
        registration5.setDate(Date.valueOf("2024-02-15"));
        registration5.setSession(session2);
        registration5.setCustomer(customer2);
        registration5.setSportCenter(sportCenter);
        registration5 = registrationRepository.save(registration5);

        Registration registration6 = new Registration();
        registration6.setDate(Date.valueOf("2024-03-20"));
        registration6.setSession(session3);
        registration6.setCustomer(customer2);
        registration6.setSportCenter(sportCenter);
        registration6 = registrationRepository.save(registration6);
    }
}
