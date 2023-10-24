package appv2.core.view;

import appv2.core.View;
import appv2.core.PlayerController;
import appv2.core.GameState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class PlayersScoreController {

    @FXML private Button closebtn;
    @FXML private VBox vbox;
    @FXML private Label tokenstowinlabel;

    @FXML
    private void initialize() {
        this.vbox.getChildren().clear();

        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllers()) {
            Label label = new Label(
                    String.format("%s: %d", PC.getPlayerName(), PC.getAffectionTokens()));
            label.getStyleClass().add("text-lg");

            this.vbox.getChildren().add(label);
            continue;
        }

        this.closebtn.setOnAction(actionEvent -> {
            View.renderExistingScreen(View.getActiveGameScene().getFallback());
            return;
        });

        this.tokenstowinlabel.setText(
                String.format(
                        "In this game, a player must collect %d tokens to win.",
                        GameState.getActiveGameMode().getAmountOfTokensOfAffectionToWin()
                )
        );

        return;
    }

}
