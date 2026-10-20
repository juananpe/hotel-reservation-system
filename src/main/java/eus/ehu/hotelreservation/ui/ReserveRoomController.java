package eus.ehu.hotelreservation.ui;

import eus.ehu.hotelreservation.businessLogic.BlFacade;
import eus.ehu.hotelreservation.domain.Hotel;
import eus.ehu.hotelreservation.model.AvailableRoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;

/**
 * FXML controller for the <em>Reserve Room</em> view.
 * <p>
 * Handles user interactions for searching available rooms, making
 * reservations (with 20% upfront payment) and joining the waiting list.
 * Receives the {@link BlFacade} through constructor injection so that
 * the controller remains decoupled from the concrete facade implementation.
 */
public class ReserveRoomController {

    private BlFacade bl;

    private ObservableList<AvailableRoomModel> availableRooms = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Hotel> hotelComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<AvailableRoomModel> roomsTableView;

    @FXML
    private TableColumn<AvailableRoomModel, String> hotelColumn;

    @FXML
    private TableColumn<AvailableRoomModel, String> roomTypeColumn;

    @FXML
    private TableColumn<AvailableRoomModel, Number> dailyPriceColumn;

    @FXML
    private TableColumn<AvailableRoomModel, Number> totalPriceColumn;

    @FXML
    private Button searchButton;

    @FXML
    private Button reserveButton;

    @FXML
    private Button waitingListButton;

    @FXML
    private Label infoLabel;

    @FXML
    void initialize() {
        setColumns();
        setHotelComboBox();

        // Search button action
        searchButton.setOnAction(this::searchAvailability);

        // Reserve button action
        reserveButton.setOnAction(event -> {
            AvailableRoomModel selected = roomsTableView.getSelectionModel().getSelectedItem();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (selected == null) {
                infoLabel.setText("Please select a room type first.");
                return;
            }
            if (startDate == null || endDate == null) {
                infoLabel.setText("Please select both start and end dates.");
                return;
            }
            if (!startDate.isBefore(endDate)) {
                infoLabel.setText("Start date must be before end date.");
                return;
            }

            // Reserve the room — 20% upfront payment
            bl.reserveRoom(selected, startDate, endDate);
            double upfront = selected.getTotalPrice() * 0.20;
            infoLabel.setText("Reservation successful! 20% paid: " + String.format("%.2f", upfront)
                    + "€. Remaining: " + String.format("%.2f", selected.getTotalPrice() - upfront) + "€");

            // Refresh the list
            searchAvailability(null);
        });

        // Waiting list button action
        waitingListButton.setOnAction(event -> {
            Hotel hotel = hotelComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (hotel == null || startDate == null || endDate == null) {
                infoLabel.setText("Please select hotel and dates before joining the waiting list.");
                return;
            }
            if (!startDate.isBefore(endDate)) {
                infoLabel.setText("Start date must be before end date.");
                return;
            }

            // Ask which room type they want to wait for (simplified: use first available or prompt)
            AvailableRoomModel selected = roomsTableView.getSelectionModel().getSelectedItem();
            String roomType;
            if (selected != null) {
                roomType = selected.getRoomType();
            } else {
                roomType = "doble"; // default fallback
            }

            bl.joinWaitingList(hotel.getName(), roomType, startDate, endDate);
            infoLabel.setText("Added to waiting list for " + roomType + " at " + hotel.getName());
        });

        // Selection listener
        roomsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, room) -> {
            if (room != null) {
                double upfront = room.getTotalPrice() * 0.20;
                infoLabel.setText("Selected: " + room.getRoomType() + " | Total: "
                        + String.format("%.2f", room.getTotalPrice()) + "€ | 20% upfront: "
                        + String.format("%.2f", upfront) + "€");
            }
        });

        roomsTableView.setItems(availableRooms);

        // Set defaults
        hotelComboBox.getSelectionModel().selectFirst();
        startDatePicker.setValue(LocalDate.now().plusDays(1));
        endDatePicker.setValue(LocalDate.now().plusDays(4));
    }

    private void setHotelComboBox() {
        hotelComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Hotel> call(ListView<Hotel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Hotel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        hotelComboBox.setConverter(new StringConverter<Hotel>() {
            @Override
            public String toString(Hotel hotel) {
                return hotel == null ? null : hotel.getName();
            }

            @Override
            public Hotel fromString(String string) {
                return null;
            }
        });

        List<Hotel> hotels = bl.getHotels();
        for (Hotel hotel : hotels) {
            hotelComboBox.getItems().add(hotel);
        }
    }

    private void setColumns() {
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        dailyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    /**
     * Searches for available room types at the selected hotel and date range,
     * then populates the results table.
     * <p>
     * Validates that a hotel and both dates are selected and that the start
     * date is before the end date before querying the business layer.
     *
     * @param event the action event triggered by the Search button
     */
    @FXML
    void searchAvailability(ActionEvent event) {
        Hotel hotel = hotelComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (hotel == null || startDate == null || endDate == null) {
            infoLabel.setText("Please select a hotel and both dates.");
            return;
        }
        if (!startDate.isBefore(endDate)) {
            infoLabel.setText("Start date must be before end date.");
            return;
        }

        List<AvailableRoomModel> results = bl.getAvailableRooms(hotel.getName(), startDate, endDate);
        availableRooms.clear();
        availableRooms.addAll(results);

        if (results.isEmpty()) {
            infoLabel.setText("No rooms available. You can join the waiting list.");
        } else {
            infoLabel.setText("Found " + results.size() + " room type(s) available.");
        }
    }

    /**
     * Constructs the controller with the given business-logic facade.
     *
     * @param bl the {@link BlFacade} used to execute business operations
     */
    public ReserveRoomController(BlFacade bl) {
        this.bl = bl;
    }
}
