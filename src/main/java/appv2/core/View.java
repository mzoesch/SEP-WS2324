package appv2.core;

import appv2.core.GameInstance;
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

    public static final String PATH_TO_MAIN_MENU = "view/mainmenu.fxml";
    public static final String PATH_TO_RULES = "view/rules.fxml";
    public static final String PATH_TO_GAME_INIT = "view/gameinit.fxml";
    public static final String PATH_TO_GAME = "view/game.fxml";
    public static final String PATH_TO_PLAYERS_SCORE = "view/playersscore.fxml";
    public static final String PATH_TO_DISCARDED_PILE = "view/discardedpile.fxml";
    public static final String PATH_TO_ROUND_ENDED = "view/roundended.fxml";
    public static final String PATH_TO_GAME_ENDED = "view/gameended.fxml";

    private static final int EXIT_CODE_FXML_LOAD_ERROR = 1;

    /**
     * <p>MasterController object.<br />
     * Is valid throughout the entirety of the application runtime
     * and not destroyed until the application is killed.</p>
     *
     * @see appv2.core.view.MasterController
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
     * <p> Called by the JavaFX Application Thread to initialize the Graphical User Interface.<br />
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

        View.renderNewScreen(
            new GameScene(
                MasterController.MAIN_MENU, View.loadFXML(View.PATH_TO_MAIN_MENU), false, null),
            false
        );
        stage.show();

        return;
    }

    /**
     * <p>Entry point for the Graphical User Interface.<br />
     * Call this method to launch the GUI.</p>
     *
     * @see appv2.core.GameInstance#run()
     */
    public static void run() {
        Application.launch();
        return;
    }

    // region Utility methods

    /**
     * <p>Loads an FXML file from the resources folder.</p>
     *
     * @param path Path to the FXML file to load.
     * @param <T> Type of the FXML file to load.
     * @return The loaded FXML file.
     */
    public static <T> T loadFXML(String path) {
        try {
            return FXMLLoader.load(
                    Objects.requireNonNull(View.class.getResource(path))
            );
        }
        catch (IOException e) {
            e.printStackTrace();
            GameInstance.setExitCode(View.EXIT_CODE_FXML_LOAD_ERROR);
            GameInstance.quitApplication();

            return null;
        }
    }

    // endregion Utility methods

    // Controller interaction methods

    /**
     * <p>Renders an existing screen.</p>
     *
     * @param identifier Identifier of the screen to render.
     */
    public static void renderExistingScreen(String identifier) {
        View.masterController.activate(identifier, false);
        return;
    }

    /**
     * <p>Renders an existing screen of a specific type.<br />
     * This method is useful if you have multiple screens of the same type
     * and want to render one of them.</p>
     *
     * @param prefix Prefix of the screen to render.
     * @throws RuntimeException If no screen with the specified prefix exists.
     * @see appv2.core.view.MasterController#GAME
     * @see appv2.core.view.MasterController#getUniqueIdentifier(String)
     */
    public static void renderExistingScreenOfType(String prefix) throws RuntimeException {
        for (GameScene screen : View.masterController.getGameScenes()) {
            if (screen.getIdentifier().startsWith(prefix)) {
                View.masterController.activate(screen.getIdentifier(), false);

                return;
            }

            continue;
        }

        throw new RuntimeException(String.format("No screen with prefix %s found", prefix));
    }

    /**
     * <p>Renders a new screen.</p>
     *
     * @param gameScene GameScene object to render.
     * @param bKeepOldAlive Whether to keep the old screen alive or not.
     * @return Unique identifier of the old screen (only relevant if the old screen should be killed after use).
     * @see appv2.core.view.MasterController#activate(String, boolean)
     */
    public static String renderNewScreen(GameScene gameScene, boolean bKeepOldAlive) {
        View.masterController.addScreen(gameScene);
        return View.masterController.activate(gameScene.getIdentifier(), bKeepOldAlive);
    }

    /**
     * <p>Removes a screen from the rendered list.</p>
     *
     * @param identifier Unique identifier of the scree to remove.
     * @see appv2.core.view.MasterController#removeScreen(String)
     */
    public static void killScreen(String identifier) {
        View.masterController.removeScreen(identifier);
        return;
    }

    // endregion Controller interaction methods

    // region Getters and Setters

    /**
     * <p>Getter for the master Scene object.</p>
     *
     * @return The master Scene object.
     * @see appv2.core.view.MasterController#getScene()
     */
    public static Scene getScene() {
        return View.masterController.getScene();
    }

    /**
     * <p>Getter for the currently active screen.</p>
     *
     * @return The GameScene object of the currently active screen.
     * @throws RuntimeException If no active screen is found.
     * @see appv2.core.view.MasterController#getActiveGameScene()
     */
    public static GameScene getActiveGameScene() throws RuntimeException {
        return View.masterController.getActiveGameScene();
    }

    // endregion Getters and Setters

}
