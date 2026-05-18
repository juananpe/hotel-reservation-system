package eus.ehu.hotelreservation.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * JavaFX model class for displaying available room types in the TableView.
 * Represents a room type available at a hotel for a given date range with its total price.
 */
public class AvailableRoomModel {
    private final SimpleStringProperty hotelName;
    private final SimpleStringProperty roomType;
    private final SimpleDoubleProperty dailyPrice;
    private final SimpleDoubleProperty totalPrice;
    private final SimpleStringProperty hotelNameForQuery;

    public AvailableRoomModel(String hotelName, String roomType, double dailyPrice, double totalPrice) {
        this.hotelName = new SimpleStringProperty(hotelName);
        this.roomType = new SimpleStringProperty(roomType);
        this.dailyPrice = new SimpleDoubleProperty(dailyPrice);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.hotelNameForQuery = new SimpleStringProperty(hotelName);
    }

    public String getHotelName() {
        return hotelName.get();
    }

    public SimpleStringProperty hotelNameProperty() {
        return hotelName;
    }

    public String getRoomType() {
        return roomType.get();
    }

    public SimpleStringProperty roomTypeProperty() {
        return roomType;
    }

    public double getDailyPrice() {
        return dailyPrice.get();
    }

    public SimpleDoubleProperty dailyPriceProperty() {
        return dailyPrice;
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public String getHotelNameForQuery() {
        return hotelNameForQuery.get();
    }

    public SimpleStringProperty hotelNameForQueryProperty() {
        return hotelNameForQuery;
    }
}
