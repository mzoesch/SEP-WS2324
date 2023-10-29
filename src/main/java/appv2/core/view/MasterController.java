package appv2.core.view;

import appv2.core.PlayerController;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Objects;


/**
 * <p>High-level manager object for the overall flow of screens in the Graphical User Interface. It manages the
 * creation, destruction and activation of screens.<br />
 * Spawned at the creation of the application main window and not destroyed until the application is killed.</p>
 *
 * @see appv2.core.View#start(Stage)
 */
public class MasterController {

    public static final String WIN_TITLE = "Love Letter @mzoesch";

    /**
     * <p>Preferred starting height of the main window. If allowed the main
     * window will start with this height in px.</p>
     */
    public static final int PREF_HEIGHT = 1_280;
    /**
     * <p>Preferred starting width of the main window. If allowed the main
     * window will start with this width in px.</p>
     */
    public static final int PREF_WIDTH = 720;

    /**
     * <p>Unique identifier for the Main Menu Screen in the application.</p>
     */
    public static final String MAIN_MENU = "main";
    /**
     * <p>Unique identifier for the Rules Screen in the application.</p>
     */
    public static final String RULES = "rules";
    /**
     * <p>Unique identifier for the Game Init Screen in the application.</p>
     */
    public static final String GAME_INIT = "gameinit";
    /**
     * <p>Unique identifier for the Game Screen in the application. We will attach a
     * UUID to this String, to differentiate between different player screen.</p>
     *
     * @see appv2.core.view.MasterController#getUniqueIdentifierForAPlayersTurn(PlayerController)
     */
    public static final String GAME = "game";
    /**
     * <p>Unique identifier for the Round Ended Screen in the application.</p>
     */
    public static final String ROUND_ENDED = "roundended";
    /**
     * <p>Unique identifier for the Game Ended Screen in the application.</p>
     */
    public static final String GAME_ENDED = "gameended";
    /**
     * <p>Unique identifier for the Players Score Screen  in the application.</p>
     */
    public static final String PLAYERS_SCORE = "playersscore";
    /**
     * <p>Unique identifier for the Discarded Pile Screen in the application.</p>
     */
    public static final String DISCARDED_PILE = "discardedpile";

    /**
     * <p>Master Scene object.<br />
     * This is the main window of the application and is valid throughout the entirety of
     * the application runtime and not destroyed until the application is killed.</p>
     */
    private final Scene master;
    /**
     * <p>List of all screens in the application.<br />
     * We use this list to keep track of all rendered screens, attach them to the master scene
     * and to be able to destroy them when they are no longer needed.</p>
     *
     * @see appv2.core.view.MasterController#master
     */
    private final ArrayList<GameScene> screens;
    /**
     * <p>Unique identifier of the currently active screen.<p>
     */
    private String currentScreen;

    /**
     * <p>Constructor. Initializes default values for this application window.</p>
     *
     * @param master Master Scene object.
     * @see appv2.core.view.MasterController#master
     */
    public MasterController(Scene master) {
        this.master = master;
        this.screens = new ArrayList<GameScene>();
        this.currentScreen = "";

        return;
    }

    /**
     * <p>Adds an already rendered Screen to the dependency array.</p>
     *
     * @param gameScene GameScene object to add.
     * @see appv2.core.view.GameScene
     * @see appv2.core.view.MasterController#screens
     */
    public void addScreen(GameScene gameScene) {
        this.screens.add(gameScene);
        return;
    }

    /**
     * <p>Removes a screen from the dependency array and destroys it if it is no longer needed.</p>
     *
     * @param identifier Unique identifier of the screen to remove.
     * @see appv2.core.view.MasterController#screens
     */
    public void removeScreen(String identifier) {
        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(identifier)) {
                this.screens.remove(screen);
                return;
            }

            continue;
        }

        return;
    }

    /**
     * <p>Activates a screen and deactivates the currently active screen.<br />
     * If the screen to activate is already active, nothing happens.</p>
     *
     * @param identifier Unique identifier of the screen to activate.
     * @param bKeepOldAlive Whether to keep the old screen alive or not
     *                      (only relevant if the old screen should be killed after use).
     * @return Unique identifier of the old screen. Only returns a valid String
     *         if the old screen should be killed but is prevented by bKeepOldAlive set to true.
     */
    public String activate(String identifier, boolean bKeepOldAlive) {
        String oldIdentifier = this.currentScreen;

        this.screens.forEach(gameScene -> {
            if (gameScene.getIdentifier().equals(identifier)) {
                this.master.setRoot(gameScene.getPane());
                this.currentScreen = identifier;
            }
        });

        if (Objects.equals(this.currentScreen, oldIdentifier))
            return "";

        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(oldIdentifier) && screen.killAfterUse()) {
                if (bKeepOldAlive) {
                    // TODO: Just for debug. Remove.
                    for (GameScene gs : this.screens)
                        System.out.printf("%s\n", gs.getIdentifier());

                    return oldIdentifier;
                }

                this.removeScreen(oldIdentifier);
                break;
            }
            continue;
        }

        // TODO: Just for debug. Remove.
        for (GameScene gs : this.screens)
            System.out.printf("%s\n", gs.getIdentifier());

        return "";
    }

    // region Utility methods

    /**
     * <p>Appends a UUID to a already existing String.</p>
     *
     * @param prefix String to append the UUID to.
     * @return String with the UUID appended.
     */
    public static String getUniqueIdentifier(String prefix) {
        return String.format("%s-%s", prefix, UUID.randomUUID().toString());
    }

    /**
     * <p>Generates a unique identifier for a player's turn.</p>
     *
     * @param PC PlayerController object to get the name from.
     * @return Unique identifier for a player's turn.
     */
    public static String getUniqueIdentifierForAPlayersTurn(PlayerController PC) {
        return MasterController.getUniqueIdentifier(
            String.format(
                "%s-player-%s",
                MasterController.GAME,
                PC.getPlayerName().toLowerCase().replace(" ", "-")
            )
        );
    }

    // endregion Utility methods

    // region Getters and Setters

    /**
     * <p>Getter for the master Scene object.</p>
     *
     * @return The master Scene object.
     * @see appv2.core.view.MasterController#master
     */
    public Scene getScene() {
        return this.master;
    }

    /**
     * <p>Getter for the list of all screens in the application.</p>
     *
     * @return The list of all screens in the application.
     * @see appv2.core.view.MasterController#screens
     */
    public ArrayList<GameScene> getGameScenes() {
        return this.screens;
    }

    /**
     * <p>Getter for the currently active screen.</p>
     *
     * @return The GameScene object of the currently active screen.
     * @throws RuntimeException If no active screen is found.
     * @see appv2.core.view.GameScene
     */
    public GameScene getActiveGameScene() throws RuntimeException {
        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(this.currentScreen))
                return screen;
        }

        throw new RuntimeException("No active screen found.");
    }

    // endregion Getters and setters

}
