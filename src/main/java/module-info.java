module app {
    requires javafx.controls;
    requires javafx.fxml;

    exports appv2;
    opens appv2 to javafx.fxml;

    exports appv2.core;
    opens appv2.core to javafx.fxml;

    exports appv2.core.view;
    opens appv2.core.view to javafx.fxml;
}
