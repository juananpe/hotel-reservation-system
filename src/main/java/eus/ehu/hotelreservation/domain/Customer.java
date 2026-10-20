package eus.ehu.hotelreservation.domain;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a hotel customer identified by national ID.
 * <p>
 * A customer may have multiple {@link Reservation reservations} and
 * {@link WaitingListEntry waiting-list entries}, and stores a credit
 * card number for advance payments.
 */
@Entity
public class Customer {
    @Id
    private String nationalId;

    private String name;
    private String creditCard;

    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "customer")
    private List<WaitingListEntry> waitingListEntries;

    public Customer() {
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<WaitingListEntry> getWaitingListEntries() {
        return waitingListEntries;
    }

    public void setWaitingListEntries(List<WaitingListEntry> waitingListEntries) {
        this.waitingListEntries = waitingListEntries;
    }
}
