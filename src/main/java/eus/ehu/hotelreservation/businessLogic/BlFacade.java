package eus.ehu.hotelreservation.businessLogic;

import eus.ehu.hotelreservation.domain.Hotel;
import eus.ehu.hotelreservation.model.AvailableRoomModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface that specifies the business logic for the Hotel Reservation system.
 */
public interface BlFacade {

    List<Hotel> getHotels();

    List<AvailableRoomModel> getAvailableRooms(String hotelName, LocalDate startDate, LocalDate endDate);

    void reserveRoom(AvailableRoomModel room, LocalDate startDate, LocalDate endDate);

    void joinWaitingList(String hotelName, String roomType, LocalDate startDate, LocalDate endDate);

}
