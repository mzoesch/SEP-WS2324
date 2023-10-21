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

import java.io.IOException;


public class RulesController {

    public void switchToMaster(ActionEvent event) throws IOException {
        View.screenController.activate("main");

        return;
    }

}
