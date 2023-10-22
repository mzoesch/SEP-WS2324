package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.GameMode;
import appv2.core.PlayerController;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class RoundEndedController {

    @FXML private VBox playerScoresContainer;
    @FXML private VBox winnersContainer;
    @FXML private Button continueBtn;

    @FXML
    private void initialize() {

        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllers()) {
            Label label = new Label(String.format("%s: %s", PC.getPlayerName(), PC.getAffectionTokens()));
            label.setId("basic-text");

            this.playerScoresContainer.getChildren().add(label);
            continue;
        }

        for (PlayerController PC : GameState.getActiveGameMode().getMostRecentRoundWinners()) {
            Label label = new Label(String.format("%s", PC.getPlayerName()));
            label.setId("basic-text");

            this.winnersContainer.getChildren().add(label);
            continue;
        }

        this.continueBtn.setOnAction(actionEvent -> {
            GameState.getActiveGameMode().prepareForNextRound();
            View.renderNewScreen(new GameScene(MasterController.getUniqueIdentifier(String.format("%s-player%s", MasterController.GAME, GameState.getActiveGameMode().getMostRecentPlayerController().getPlayerName())), View.loadFXML(View.PATH_TO_GAME), true, null), false);
            return;
        });

        return;
    }
}
