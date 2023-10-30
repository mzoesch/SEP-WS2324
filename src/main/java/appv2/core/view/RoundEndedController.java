package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.PlayerController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/**
 * <p>Controller for the Round Ended Screen.</p>
 */
public class RoundEndedController {

    @FXML private Label roundwinnerstitle;
    @FXML private VBox roundwinnerscontainer;
    @FXML private VBox playerscorescontainer;

    /**
     * <p>Starts a new round.</p>
     */
    @FXML
    protected void onNextRound() {
        GameState.getActiveGameMode().prepareForNextRound();

        View.renderNewScreen(
            new GameScene(
                MasterController.getUniqueIdentifierForAPlayersTurn(
                        GameState.getActiveGameMode().getMostRecentPlayerController()),
                View.loadFXML(View.PATH_TO_GAME),
                true,
                null
            ),
            false
        );

        return;
    }

    // region Utility methods for initialize()

    /**
     * <p>Renders the affection tokens of all players as labels in descending order.</p>
     */
    private void renderAffectionTokensOfAllPlayers() {
        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllerByDSCAffection()) {
            Label label = new Label(String.format("%s: %s", PC.getPlayerName(), PC.getAffectionTokens()));
            label.getStyleClass().add("text-lg");

            this.playerscorescontainer.getChildren().add(label);
            continue;
        }

        return;
    }

    // endregion Utility methods for initialize()

    /**
     * <p>Initializes the Round Ended Screen and renders the round winners to the screen.</p>
     */
    @FXML
    private void initialize() {
        this.renderAffectionTokensOfAllPlayers();

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
