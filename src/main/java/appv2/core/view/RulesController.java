package appv2.core.view;

import appv2.core.View;

import javafx.event.ActionEvent;


public class RulesController {

    public void onContinueBtn(ActionEvent event) {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        return;
    }

}
