package appv2.core.view;

import appv2.core.View;

import javafx.fxml.FXML;


/**
 * <p>Controller for the main menu screen.</p>
 */
public class MainMenuController {

    /**
     * <p>Exits the application.</p>
     */
    @FXML
    protected void onExitBtn() {
        appv2.core.GameInstance.quitApplication();
        return;
    }

    /**
     * <p>Opens the rules screen.</p>
     */
    @FXML
    protected void onRulesBtn() {
        View.renderNewScreen(
            new GameScene(MasterController.RULES, View.loadFXML(View.PATH_TO_RULES), true, null),
            false
        );

        return;
    }

    /**
     * <p>Opens the Game Init Screen.</p>
     */
    @FXML
    protected void onPlayBtn() {
        View.renderNewScreen(
            new GameScene(MasterController.GAME_INIT, View.loadFXML(View.PATH_TO_GAME_INIT), true, null),
            false
        );

        return;
    }

}
