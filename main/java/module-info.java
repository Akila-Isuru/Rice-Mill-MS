module lk.ijse.gdse74.mytest2.responsive {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires java.base;
    requires static lombok;
    requires java.mail;
    requires java.desktop;

    opens lk.ijse.gdse74.mytest2.responsive.Controller to javafx.fxml;
    opens lk.ijse.gdse74.mytest2.responsive.dto to javafx.fxml, javafx.base;
    exports lk.ijse.gdse74.mytest2.responsive;
    opens lk.ijse.gdse74.mytest2.responsive.dto.tm to javafx.base;
}