module org.example.motionsim {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires javafx.graphics;
    opens org.example.motionsim to javafx.fxml;
    opens org.example.motionsim.Controller to javafx.fxml;
    exports org.example.motionsim;
    exports org.example.motionsim.Controller;
    exports org.example.motionsim.Model;
    opens org.example.motionsim.Model to javafx.fxml, com.google.gson;

}