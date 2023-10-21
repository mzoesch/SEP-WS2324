package appv2.core;

import appv2.core.view.ScreenController;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


import java.io.IOException;


public class View extends Application {


    public static ScreenController screenController;

    public View() {
        super();
        return;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Parent(){}, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Love Letter @mzoesch");

        View.screenController = new ScreenController(scene);
        View.screenController.addScreen("main", FXMLLoader.load(getClass().getResource( "view/master.fxml" )));
        View.screenController.addScreen("rules", FXMLLoader.load(getClass().getResource( "view/rules.fxml" )));

        View.screenController.activate("main");


        stage.show();
        return;
    }

    public static void run() {
        Application.launch();

        return;
    }

}
