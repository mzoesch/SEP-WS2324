package appv2.core;

import appv2.core.view.MasterController;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


import java.io.IOException;
import java.util.Objects;


public class View extends Application {

    public static final String PATH_TO_RULES = "view/rules.fxml";
    public static final String PATH_TO_MAIN_MENU = "view/mainmenu.fxml";
    public static final String PATH_TO_GAME_INIT = "view/gameinit.fxml";
    public static final String PATH_TO_GAME = "view/game.fxml";

    private static MasterController masterController;

    public View() {
        super();
        return;
    }

    @Override
    public void start(Stage stage) throws IOException {
        View.masterController = new MasterController(
                new Scene(
                        new Parent(){}, MasterController.PREF_HEIGHT, MasterController.PREF_WIDTH
                )
        );

        stage.setScene(View.masterController.getScene());
        stage.setTitle(MasterController.WIN_TITLE);

        View.renderNewScreen(MasterController.MAIN_MENU, View.PATH_TO_MAIN_MENU);
        stage.show();

        return;
    }

    public static void run() {
        Application.launch();

        return;
    }

    // region Utility methods

    private static <T> T loadFXML(String path) {
        try {
            return FXMLLoader.load(Objects.requireNonNull(View.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    // endregion Utility methods

    // Controller interaction methods

    public static void renderExistingScreen(String identifier) {
        View.masterController.activate(identifier);
        return;
    }

    public static void renderNewScreen(String identifier, String path) {
        View.masterController.addScreen(identifier, View.loadFXML(path));
        View.masterController.activate(identifier);
        return;
    }

    public static void killScreen(String identifier) {
        View.masterController.removeScreen(identifier);
        return;
    }

    // endregion Controller interaction methods

}
