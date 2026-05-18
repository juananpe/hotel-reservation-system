package eus.ehu.hotelreservation.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String type;  // "simple", "doble", "triple", "suite"

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;

    @OneToMany(mappedBy = "roomType")
    private List<HotelRoomType> hotelRoomTypes;

    @OneToMany(mappedBy = "roomType")
    private List<Rate> rates;

    @OneToMany(mappedBy = "roomType")
    private List<WaitingListEntry> waitingListEntries;

    public RoomType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<HotelRoomType> getHotelRoomTypes() {
        return hotelRoomTypes;
    }

    public void setHotelRoomTypes(List<HotelRoomType> hotelRoomTypes) {
        this.hotelRoomTypes = hotelRoomTypes;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public List<WaitingListEntry> getWaitingListEntries() {
        return waitingListEntries;
    }

    public void setWaitingListEntries(List<WaitingListEntry> waitingListEntries) {
        this.waitingListEntries = waitingListEntries;
    }
}
