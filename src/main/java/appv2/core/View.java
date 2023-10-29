package appv2.core;

import appv2.core.view.GameScene;
import appv2.core.view.MasterController;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;


/**
 * <p>Implements high-level methods relevant for the Graphical User Interface.</p>
 */
public class View extends Application {

    public static final String PATH_TO_RULES = "view/rules.fxml";
    public static final String PATH_TO_MAIN_MENU = "view/mainmenu.fxml";
    public static final String PATH_TO_GAME_INIT = "view/gameinit.fxml";
    public static final String PATH_TO_GAME = "view/game.fxml";
    public static final String PATH_TO_ROUND_ENDED = "view/roundended.fxml";
    public static final String PATH_TO_GAME_ENDED = "view/gameended.fxml";
    public static final String PATH_TO_PLAYERS_SCORE = "view/playersscore.fxml";
    public static final String PATH_TO_DISCARDED_PILE = "view/discardedpile.fxml";

    /**
     * <p>MasterController object.<br />
     * Is valid throughout the entirety of the application runtime
     * and not destroyed until the application is killed.</p>
     */
    private static MasterController masterController;

    /**
     * <p>Required constructor for JavaFX Application Child.</p>
     */
    public View() {
        super();
        return;
    }

    /**
     * <p> Called by the JavaFX Application Thread to initialize the GUI.<br />
     * This method initializes the MasterController and loads the MainMenu FXML file.</p>
     *
     * @param stage Stage object.
     * @throws IOException If the MainMenu FXML file cannot be loaded.
     * @see appv2.core.view.MasterController
     */
    @Override
    public void start(Stage stage) throws IOException {
        View.masterController = new MasterController(
                new Scene(
                        new Parent(){}, MasterController.PREF_HEIGHT, MasterController.PREF_WIDTH
                )
        );

        stage.setScene(View.masterController.getScene());
        stage.setTitle(MasterController.WIN_TITLE);

        View.renderNewScreen(new GameScene(MasterController.MAIN_MENU, View.loadFXML(View.PATH_TO_MAIN_MENU), false, null), false);
        stage.show();

        return;
    }

    public static void run() {
        Application.launch();

        return;
    }

    // region Utility methods

    public static <T> T loadFXML(String path) {
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
        View.masterController.activate(identifier, false);
        return;
    }

    public static void renderExistingScreenOfType(String prefix) {
        for (GameScene screen : View.masterController.getGameScenes()) {
            if (screen.getIdentifier().startsWith(prefix)) {
                View.masterController.activate(screen.getIdentifier(), false);

                return;
            }

            continue;
        }

        throw new RuntimeException(String.format("No screen with prefix %s found", prefix));
    }

    public static String renderNewScreen(GameScene gameScene, boolean bKeepOldAlive) {
        View.masterController.addScreen(gameScene);
        return View.masterController.activate(gameScene.getIdentifier(), bKeepOldAlive);
    }

    public static void killScreen(String identifier) {
        View.masterController.removeScreen(identifier);
        return;
    }

    public static GameScene getActiveGameScene() {
        return View.masterController.getActiveGameScene();
    }

    public static Scene getScene() {
        return View.masterController.getScene();
    }

    // endregion Controller interaction methods

}
