package appv2.core.view;

import appv2.core.View;

import javafx.fxml.FXML;


public class MainMenuController {

    @FXML
    protected void onExitBtn() {
        appv2.core.GameInstance.quitApplication();
        return;
    }

    @FXML
    protected void onRulesBtn() {
        View.renderNewScreen(new GameScene(MasterController.RULES, View.loadFXML(View.PATH_TO_RULES), true, null), false);
        return;
    }

    @FXML
    protected void onPlayBtn() {
        View.renderNewScreen(new GameScene(MasterController.GAME_INIT, View.loadFXML(View.PATH_TO_GAME_INIT), true, null), false);
        return;
    }

}
