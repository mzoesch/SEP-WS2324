package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.PlayerController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class RoundEndedController {

    @FXML private Label roundwinnerstitle;
    @FXML private VBox roundwinnerscontainer;
    @FXML private VBox playerscorescontainer;

    @FXML
    protected void onNextRound() {
        GameState.getActiveGameMode().prepareForNextRound();
        View.renderNewScreen(
                new GameScene(
                        MasterController.getUniqueIdentifier(
                                String.format(
                                        "%s-player%s",
                                        MasterController.GAME,
                                        GameState.getActiveGameMode()
                                                .getMostRecentPlayerController().getPlayerName()
                                )
                        ),
                        View.loadFXML(View.PATH_TO_GAME),
                        true,
                        null
                ),
                false
        );

        return;
    }

    @FXML
    private void initialize() {
        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllers()) {
            Label label = new Label(String.format("%s: %s", PC.getPlayerName(), PC.getAffectionTokens()));
            label.getStyleClass().add("text-lg");

            this.playerscorescontainer.getChildren().add(label);
            continue;
        }

        this.roundwinnerstitle.setText(
                String.format("Round %d %s:",
                        GameState.getActiveGameMode().getCurrentRoundNumber(),
                        GameState.getActiveGameMode().getMostRecentRoundWinners().size() > 1 ? "Winners" : "Winner"
                )
        );
        for (PlayerController PC : GameState.getActiveGameMode().getMostRecentRoundWinners()) {
            Label label = new Label(String.format("%s", PC.getPlayerName()));
            label.getStyleClass().add("text-lg");

            this.roundwinnerscontainer.getChildren().add(label);
            continue;
        }

        return;
    }

}
