package eus.ehu.hotelreservation.dataAccess;

import eus.ehu.hotelreservation.configuration.Config;
import eus.ehu.hotelreservation.domain.*;
import eus.ehu.hotelreservation.model.AvailableRoomModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements the Data Access utility using Hibernate / JPA with H2.
 */
public class DataAccess {

    private Config config = Config.getInstance();

    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DataAccess() {
        this.open();
    }

    public void open() {
        open(false);
    }

    public void open(boolean initializeMode) {
        System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDataBaseOpenMode: " + config.getDataBaseOpenMode());

        if (config.isDataAccessLocal()) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            try {
                emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
            }

            db = emf.createEntityManager();
            System.out.println("DataBase opened");
        }
    }

    public void reset() {
        db.getTransaction().begin();
        db.getTransaction().commit();
    }

    public void close() {
        db.close();
        System.out.println("DataBase is closed");
    }

    // ==================== Initialization ====================

    public void initializeDB() {
        try {
            db.getTransaction().begin();
            generateTestingData();
            db.getTransaction().commit();
            System.out.println("The database has been initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateTestingData() {
        // --- Room Types ---
        RoomType simple = new RoomType();
        simple.setType("simple");
        db.persist(simple);

        RoomType doble = new RoomType();
        doble.setType("doble");
        db.persist(doble);

        RoomType triple = new RoomType();
        triple.setType("triple");
        db.persist(triple);

        RoomType suite = new RoomType();
        suite.setType("suite");
        db.persist(suite);

        // --- Hotels ---
        Hotel hotel1 = new Hotel();
        hotel1.setName("Gran Hotel Donostia");
        hotel1.setCategory("5 estrellas");
        hotel1.setAddress("Paseo de la Concha 1, San Sebastián");
        hotel1.setPhone("+34943000111");
        hotel1.setEmail("donostia@granhotel.com");
        db.persist(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel Pamplona");
        hotel2.setCategory("4 estrellas");
        hotel2.setAddress("Calle Estafeta 15, Pamplona");
        hotel2.setPhone("+34948000222");
        hotel2.setEmail("info@hotelpamplona.com");
        db.persist(hotel2);

        // --- HotelRoomType (additionalDayPrice per hotel+roomType) ---
        HotelRoomType hrt1 = new HotelRoomType();
        hrt1.setHotel(hotel1);
        hrt1.setRoomType(simple);
        hrt1.setAdditionalDayPrice(80.0);
        db.persist(hrt1);

        HotelRoomType hrt2 = new HotelRoomType();
        hrt2.setHotel(hotel1);
        hrt2.setRoomType(doble);
        hrt2.setAdditionalDayPrice(120.0);
        db.persist(hrt2);

        HotelRoomType hrt3 = new HotelRoomType();
        hrt3.setHotel(hotel2);
        hrt3.setRoomType(simple);
        hrt3.setAdditionalDayPrice(70.0);
        db.persist(hrt3);

        HotelRoomType hrt4 = new HotelRoomType();
        hrt4.setHotel(hotel2);
        hrt4.setRoomType(doble);
        hrt4.setAdditionalDayPrice(100.0);
        db.persist(hrt4);

        HotelRoomType hrt5 = new HotelRoomType();
        hrt5.setHotel(hotel2);
        hrt5.setRoomType(triple);
        hrt5.setAdditionalDayPrice(150.0);
        db.persist(hrt5);

        HotelRoomType hrt6 = new HotelRoomType();
        hrt6.setHotel(hotel2);
        hrt6.setRoomType(suite);
        hrt6.setAdditionalDayPrice(250.0);
        db.persist(hrt6);

        // --- Rooms ---
        for (int i = 1; i <= 3; i++) {
            Room r = new Room();
            r.setNumber("H1-S-" + i);
            r.setStatus(RoomStatus.AVAILABLE);
            r.setHotel(hotel1);
            r.setRoomType(simple);
            db.persist(r);
        }
        for (int i = 1; i <= 2; i++) {
            Room r = new Room();
            r.setNumber("H1-D-" + i);
            r.setStatus(RoomStatus.AVAILABLE);
            r.setHotel(hotel1);
            r.setRoomType(doble);
            db.persist(r);
        }
        for (int i = 1; i <= 2; i++) {
            Room r = new Room();
            r.setNumber("H2-S-" + i);
            r.setStatus(RoomStatus.AVAILABLE);
            r.setHotel(hotel2);
            r.setRoomType(simple);
            db.persist(r);
        }
        for (int i = 1; i <= 2; i++) {
            Room r = new Room();
            r.setNumber("H2-D-" + i);
            r.setStatus(RoomStatus.AVAILABLE);
            r.setHotel(hotel2);
            r.setRoomType(doble);
            db.persist(r);
        }
        for (int i = 1; i <= 2; i++) {
            Room r = new Room();
            r.setNumber("H2-T-" + i);
            r.setStatus(RoomStatus.AVAILABLE);
            r.setHotel(hotel2);
            r.setRoomType(triple);
            db.persist(r);
        }
        // One suite at hotel2
        Room suiteRoom = new Room();
        suiteRoom.setNumber("H2-SU-1");
        suiteRoom.setStatus(RoomStatus.AVAILABLE);
        suiteRoom.setHotel(hotel2);
        suiteRoom.setRoomType(suite);
        db.persist(suiteRoom);

        // --- Rates ---
        // Hotel1 rates
        LocalDate jan1 = LocalDate.of(2026, 1, 1);
        LocalDate dec31 = LocalDate.of(2026, 12, 31);

        Rate rate1 = new Rate();
        rate1.setRateCode("H1-SIMPLE-STD");
        rate1.setDailyPrice(80.0);
        rate1.setStartDate(toDate(jan1));
        rate1.setEndDate(toDate(dec31));
        rate1.setRoomType(simple);
        db.persist(rate1);

        Rate rate2 = new Rate();
        rate2.setRateCode("H1-DOBLE-STD");
        rate2.setDailyPrice(120.0);
        rate2.setStartDate(toDate(jan1));
        rate2.setEndDate(toDate(dec31));
        rate2.setRoomType(doble);
        db.persist(rate2);

        // Hotel2 rates — higher during San Fermín (July 6–14)
        LocalDate sfStart = LocalDate.of(2026, 7, 6);
        LocalDate sfEnd = LocalDate.of(2026, 7, 14);
        LocalDate beforeSFEnd = LocalDate.of(2026, 7, 5);
        LocalDate afterSFStart = LocalDate.of(2026, 7, 15);

        // Simple room outside San Fermín
        Rate rate3 = new Rate();
        rate3.setRateCode("H2-SIMPLE-LOW");
        rate3.setDailyPrice(60.0);
        rate3.setStartDate(toDate(jan1));
        rate3.setEndDate(toDate(beforeSFEnd));
        rate3.setRoomType(simple);
        db.persist(rate3);

        Rate rate3b = new Rate();
        rate3b.setRateCode("H2-SIMPLE-LOW2");
        rate3b.setDailyPrice(60.0);
        rate3b.setStartDate(toDate(afterSFStart));
        rate3b.setEndDate(toDate(dec31));
        rate3b.setRoomType(simple);
        db.persist(rate3b);

        // Simple room during San Fermín
        Rate rate3SF = new Rate();
        rate3SF.setRateCode("H2-SIMPLE-SF");
        rate3SF.setDailyPrice(200.0);
        rate3SF.setStartDate(toDate(sfStart));
        rate3SF.setEndDate(toDate(sfEnd));
        rate3SF.setRoomType(simple);
        db.persist(rate3SF);

        // Doble room outside San Fermín
        Rate rate4 = new Rate();
        rate4.setRateCode("H2-DOBLE-LOW");
        rate4.setDailyPrice(90.0);
        rate4.setStartDate(toDate(jan1));
        rate4.setEndDate(toDate(beforeSFEnd));
        rate4.setRoomType(doble);
        db.persist(rate4);

        Rate rate4b = new Rate();
        rate4b.setRateCode("H2-DOBLE-LOW2");
        rate4b.setDailyPrice(90.0);
        rate4b.setStartDate(toDate(afterSFStart));
        rate4b.setEndDate(toDate(dec31));
        rate4b.setRoomType(doble);
        db.persist(rate4b);

        // Doble room during San Fermín
        Rate rate4SF = new Rate();
        rate4SF.setRateCode("H2-DOBLE-SF");
        rate4SF.setDailyPrice(350.0);
        rate4SF.setStartDate(toDate(sfStart));
        rate4SF.setEndDate(toDate(sfEnd));
        rate4SF.setRoomType(doble);
        db.persist(rate4SF);

        // Triple room
        Rate rate5 = new Rate();
        rate5.setRateCode("H2-TRIPLE-STD");
        rate5.setDailyPrice(120.0);
        rate5.setStartDate(toDate(jan1));
        rate5.setEndDate(toDate(dec31));
        rate5.setRoomType(triple);
        db.persist(rate5);

        // Suite room
        Rate rate6 = new Rate();
        rate6.setRateCode("H2-SUITE-STD");
        rate6.setDailyPrice(250.0);
        rate6.setStartDate(toDate(jan1));
        rate6.setEndDate(toDate(dec31));
        rate6.setRoomType(suite);
        db.persist(rate6);

        // --- Customers ---
        Customer customer1 = new Customer();
        customer1.setNationalId("12345678A");
        customer1.setName("John Doe");
        customer1.setCreditCard("1111222233334444");
        db.persist(customer1);

        Customer customer2 = new Customer();
        customer2.setNationalId("87654321B");
        customer2.setName("Jane Doe");
        customer2.setCreditCard("5555666677778888");
        db.persist(customer2);
    }

    // ==================== Queries ====================

    public List<Hotel> getHotels() {
        return db.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();
    }

    public Customer getCustomer(String nationalId) {
        return db.find(Customer.class, nationalId);
    }

    /**
     * Returns available room types (not specific rooms) for a hotel and date range.
     * Only room types that have at least one available room for ALL days in the range are returned.
     * For each available room type, it calculates the total price across all days (considering
     * different rates on different dates).
     */
    public List<AvailableRoomModel> getAvailableRooms(String hotelName, LocalDate startDate, LocalDate endDate) {
        List<AvailableRoomModel> result = new ArrayList<>();
        Hotel hotel = db.createQuery("SELECT h FROM Hotel h WHERE h.name = :name", Hotel.class)
                .setParameter("name", hotelName)
                .getSingleResult();

        // Get all room types that exist at this hotel
        List<HotelRoomType> hotelRoomTypes = db.createQuery(
                        "SELECT hrt FROM HotelRoomType hrt WHERE hrt.hotel = :hotel", HotelRoomType.class)
                .setParameter("hotel", hotel)
                .getResultList();

        for (HotelRoomType hrt : hotelRoomTypes) {
            RoomType rt = hrt.getRoomType();

            // Count available rooms of this type at this hotel
            Long availableCount = db.createQuery(
                            "SELECT COUNT(r) FROM Room r WHERE r.hotel = :hotel AND r.roomType = :roomType AND r.status = :status",
                            Long.class)
                    .setParameter("hotel", hotel)
                    .setParameter("roomType", rt)
                    .setParameter("status", RoomStatus.AVAILABLE)
                    .getSingleResult();

            if (availableCount > 0) {
                // Calculate total price for the stay
                double totalPrice = calculateTotalPrice(rt, startDate, endDate);
                double dailyPrice = totalPrice / (double) (endDate.toEpochDay() - startDate.toEpochDay());
                result.add(new AvailableRoomModel(hotelName, rt.getType(), dailyPrice, totalPrice));
            }
        }

        return result;
    }

    /**
     * Calculates the total price for a room type over a date range,
     * respecting different rates on different dates.
     */
    private double calculateTotalPrice(RoomType rt, LocalDate startDate, LocalDate endDate) {
        double total = 0.0;
        LocalDate current = startDate;
        while (current.isBefore(endDate)) {
            Date day = toDate(current);
            // Find the rate that covers this date
            List<Rate> rates = db.createQuery(
                            "SELECT r FROM Rate r WHERE r.roomType = :roomType AND r.startDate <= :day AND r.endDate >= :day",
                            Rate.class)
                    .setParameter("roomType", rt)
                    .setParameter("day", day)
                    .getResultList();

            if (!rates.isEmpty()) {
                total += rates.get(0).getDailyPrice();
            }
            current = current.plusDays(1);
        }
        return total;
    }

    // ==================== Reserve Room ====================

    /**
     * Reserves a room of the given type at the given hotel for the date range.
     * The customer pays 20% upfront via credit card.
     */
    public void reserveRoom(AvailableRoomModel room, LocalDate startDate, LocalDate endDate, Customer customer) {
        db.getTransaction().begin();

        // Find the hotel
        Hotel hotel = db.createQuery("SELECT h FROM Hotel h WHERE h.name = :name", Hotel.class)
                .setParameter("name", room.getHotelNameForQuery())
                .getSingleResult();

        // Find the room type
        RoomType roomType = db.createQuery("SELECT rt FROM RoomType rt WHERE rt.type = :type", RoomType.class)
                .setParameter("type", room.getRoomType())
                .getSingleResult();

        // Find an available room of that type at that hotel
        Room availableRoom = db.createQuery(
                        "SELECT r FROM Room r WHERE r.hotel = :hotel AND r.roomType = :roomType AND r.status = :status",
                        Room.class)
                .setParameter("hotel", hotel)
                .setParameter("roomType", roomType)
                .setParameter("status", RoomStatus.AVAILABLE)
                .setMaxResults(1)
                .getSingleResult();

        // Create the reservation
        Reservation reservation = new Reservation();
        reservation.setReservationCode("RES-" + System.currentTimeMillis());
        reservation.setReservationStart(toDate(startDate));
        reservation.setReservationEnd(toDate(endDate));
        reservation.setRoom(availableRoom);
        reservation.setCustomer(customer);
        reservation.setAmountDue(room.getTotalPrice());
        // Charge 20% upfront
        reservation.setAmountPaid(room.getTotalPrice() * 0.20);

        // Mark room as RESERVED
        availableRoom.setStatus(RoomStatus.RESERVED);

        db.persist(reservation);
        db.getTransaction().commit();

        System.out.println("Reservation created: " + reservation.getReservationCode()
                + " | 20% paid: " + reservation.getAmountPaid() + "€"
                + " | Remaining: " + (reservation.getAmountDue() - reservation.getAmountPaid()) + "€");
    }

    // ==================== Waiting List ====================

    /**
     * Adds a customer to the waiting list for a room type at a hotel.
     */
    public void joinWaitingList(String hotelName, String roomTypeStr, LocalDate startDate, LocalDate endDate, Customer customer) {
        db.getTransaction().begin();

        Hotel hotel = db.createQuery("SELECT h FROM Hotel h WHERE h.name = :name", Hotel.class)
                .setParameter("name", hotelName)
                .getSingleResult();

        RoomType roomType = db.createQuery("SELECT rt FROM RoomType rt WHERE rt.type = :type", RoomType.class)
                .setParameter("type", roomTypeStr)
                .getSingleResult();

        WaitingListEntry entry = new WaitingListEntry();
        entry.setEntryCode("WL-" + System.currentTimeMillis());
        entry.setRequestedOn(new Date());
        entry.setFromDate(toDate(startDate));
        entry.setToDate(toDate(endDate));
        entry.setCustomer(customer);
        entry.setRoomType(roomType);
        entry.setHotel(hotel);

        db.persist(entry);
        db.getTransaction().commit();

        System.out.println("Added to waiting list: " + entry.getEntryCode());
    }

    // ==================== Utility ====================

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
