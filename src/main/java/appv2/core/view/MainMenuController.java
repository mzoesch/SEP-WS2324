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
        View.renderNewScreen(MasterController.RULES, View.PATH_TO_RULES);
        return;
    }

}
