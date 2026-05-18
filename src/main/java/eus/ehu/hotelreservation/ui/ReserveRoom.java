package eus.ehu.hotelreservation.ui;

import eus.ehu.hotelreservation.businessLogic.BlFacade;
import eus.ehu.hotelreservation.businessLogic.BlFacadeImplementation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class ReserveRoom extends Application {

    private static final Logger logger = LogManager.getLogger("ReserveRoom");
    private BlFacade businessLogic = BlFacadeImplementation.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting ReserveRoom application");
        FXMLLoader fxmlLoader = new FXMLLoader(ReserveRoom.class.getResource("reserve-room.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            try {
                Constructor<?> cons = controllerClass.getConstructor(BlFacade.class);
                return cons.newInstance(businessLogic);
            } catch (Exception exc) {
                exc.printStackTrace();
                throw new RuntimeException(exc);
            }
        });

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Hotel Reservation — Reserve Room");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
