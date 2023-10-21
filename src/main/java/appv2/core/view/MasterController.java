package appv2.core.view;

import appv2.core.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;


public class MasterController {

    @FXML
    private Label subtitle;

    @FXML
    protected void onHelloButtonClick() {
        this.subtitle.setText("Welcome to JavaFX Application!");
        View.screenController.activate("rules");
    }

    @FXML
    protected void onExitButtonClick() {
        Platform.exit();

        return;
    }

//    public void switchToRules(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("rules.fxml"));
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root, stage.getWidth() - 50, stage.getHeight() - 10);
//        stage.setScene(scene);
//        stage.show();
//
//        return;
//    }

}
