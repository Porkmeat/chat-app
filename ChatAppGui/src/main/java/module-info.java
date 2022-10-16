module com.mariano.chatapp.chatappgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.apache.commons.lang3;
    requires com.jfoenix;
    requires org.json;

    opens com.mariano.chatapp.chatappgui to javafx.fxml;
    exports com.mariano.chatapp.chatappgui;
}
