module com.edd.treemap {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.edd.treemap to javafx.fxml;
    exports com.edd.treemap;
}
