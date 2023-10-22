package appv2.core.view;

import appv2.core.GameState;
import appv2.core.View;
import appv2.core.PlayerController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class GameEndedController {

    @FXML private Label winnersubtitlelabel;
    @FXML private HBox playersscorecontainer;

    @FXML
    private void initialize() {
        this.winnersubtitlelabel.setText(String.format("%s won the game!", GameState.getActiveGameMode().getMostRecentRoundWinners().get(0).getPlayerName()));

        this.playersscorecontainer.getChildren().clear();
        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllerByDSCAffection()) {
            Label label = new Label(String.format("%s: %s", PC.getPlayerName(), PC.getAffectionTokens()));
            label.setId("basic-text");

            this.playersscorecontainer.getChildren().add(label);
            continue;
        }

        return;
    }

    @FXML
    protected void onExitBtn() {
        appv2.core.GameInstance.quitApplication();
        return;
    }

    @FXML
    protected void onMainMenuBtn() {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        return;
    }

}
