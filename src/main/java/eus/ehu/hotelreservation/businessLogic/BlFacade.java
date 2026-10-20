package eus.ehu.hotelreservation.businessLogic;

import eus.ehu.hotelreservation.domain.Hotel;
import eus.ehu.hotelreservation.model.AvailableRoomModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface that specifies the business logic for the Hotel Reservation system.
 */
public interface BlFacade {

    /**
     * Returns the list of all hotels registered in the system.
     *
     * @return a list of {@link Hotel} entities, never {@code null}
     */
    List<Hotel> getHotels();

    /**
     * Queries available room types for a given hotel and date range.
     * <p>
     * Only room types that have at least one available room for the entire
     * stay (all days between {@code startDate} and {@code endDate}) are returned.
     * The total price is calculated day-by-day, respecting different rates
     * on different dates (e.g. peak-season pricing).
     *
     * @param hotelName the name of the hotel
     * @param startDate check-in date (inclusive)
     * @param endDate   check-out date (exclusive)
     * @return list of available room types with daily and total prices
     */
    List<AvailableRoomModel> getAvailableRooms(String hotelName, LocalDate startDate, LocalDate endDate);

    /**
     * Creates a reservation for the selected room type and charges 20% of
     * the total price upfront as an advance payment.
     * <p>
     * A concrete {@link eus.ehu.hotelreservation.domain.Room} is assigned
     * from the pool of available rooms of that type at the hotel.
     *
     * @param room      the room type model selected by the user
     * @param startDate check-in date
     * @param endDate   check-out date
     */
    void reserveRoom(AvailableRoomModel room, LocalDate startDate, LocalDate endDate);

    /**
     * Registers the current customer on the waiting list for the given
     * room type at the specified hotel and date range.
     * <p>
     * This is typically used when no rooms of the desired type are available.
     *
     * @param hotelName the hotel name
     * @param roomType  the desired room type (e.g. "simple", "doble", "suite")
     * @param startDate desired check-in date
     * @param endDate   desired check-out date
     */
    void joinWaitingList(String hotelName, String roomType, LocalDate startDate, LocalDate endDate);

}
