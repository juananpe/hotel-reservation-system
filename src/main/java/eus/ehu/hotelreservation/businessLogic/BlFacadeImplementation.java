package eus.ehu.hotelreservation.businessLogic;

import eus.ehu.hotelreservation.configuration.Config;
import eus.ehu.hotelreservation.dataAccess.DataAccess;
import eus.ehu.hotelreservation.domain.Customer;
import eus.ehu.hotelreservation.domain.Hotel;
import eus.ehu.hotelreservation.model.AvailableRoomModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Singleton implementation of the business-logic facade.
 * <p>
 * Provides the application layer with high-level operations such as
 * searching available rooms, making reservations and joining the
 * waiting list. Internally delegates to {@link DataAccess} for
 * persistence and keeps track of the currently logged-in customer.
 *
 * @see BlFacade
 */
public class BlFacadeImplementation implements BlFacade {

    private DataAccess dbManager;
    private Config config = Config.getInstance();
    private Customer currentUser;

    private static BlFacadeImplementation bl = new BlFacadeImplementation();

    /**
     * Returns the singleton instance of the business-logic facade.
     *
     * @return the unique {@link BlFacadeImplementation} instance
     */
    public static BlFacadeImplementation getInstance() {
        return bl;
    }

    private BlFacadeImplementation() {
        System.out.println("Creating BlFacadeImplementation instance");
        boolean initialize = config.getDataBaseOpenMode().equals("initialize");
        dbManager = new DataAccess();
        if (initialize)
            dbManager.initializeDB();

        // Load a test customer (nationalId "12345678A" is created in test data)
        currentUser = dbManager.getCustomer("12345678A");
    }

    @Override
    public List<Hotel> getHotels() {
        return dbManager.getHotels();
    }

    @Override
    public List<AvailableRoomModel> getAvailableRooms(String hotelName, LocalDate startDate, LocalDate endDate) {
        return dbManager.getAvailableRooms(hotelName, startDate, endDate);
    }

    @Override
    public void reserveRoom(AvailableRoomModel room, LocalDate startDate, LocalDate endDate) {
        dbManager.reserveRoom(room, startDate, endDate, currentUser);
    }

    @Override
    public void joinWaitingList(String hotelName, String roomType, LocalDate startDate, LocalDate endDate) {
        dbManager.joinWaitingList(hotelName, roomType, startDate, endDate, currentUser);
    }

}
