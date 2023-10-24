package appv2.core;

import appv2.core.view.MasterController;
import appv2.core.view.GameScene;

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
    public static final String PATH_TO_ROUND_ENDED = "view/roundended.fxml";
    public static final String PATH_TO_GAME_ENDED = "view/gameended.fxml";
    public static final String PATH_TO_PLAYERS_SCORE = "view/playersscore.fxml";
    public static final String PATH_TO_DISCARDED_PILE = "view/discardedpile.fxml";

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
