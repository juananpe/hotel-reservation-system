module eus.ehu.hotelreservation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;

    opens eus.ehu.hotelreservation.model to javafx.base;
    opens eus.ehu.hotelreservation.domain to org.hibernate.orm.core;

    exports eus.ehu.hotelreservation.model to org.hibernate.orm.core;
    opens eus.ehu.hotelreservation.ui to javafx.fxml;
    exports eus.ehu.hotelreservation.ui;
}
