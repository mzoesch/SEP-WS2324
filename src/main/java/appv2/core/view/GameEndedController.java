package appv2.core.view;

import appv2.core.GameState;
import appv2.core.View;
import appv2.core.PlayerController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/**
 * <p>Controller for the Game Ended Screen.</p>
 */
public class GameEndedController {

    @FXML private Label winnersubtitlelabel;
    @FXML private VBox playersscorecontainer;


    /**
     * <p>Exits the application.</p>
     */
    @FXML
    protected void onExitBtn() {
        appv2.core.GameInstance.quitApplication();
        return;
    }

    /**
     * <p>Routs the user back to the Main Menu Screen.</p>
     */
    @FXML
    protected void onMainMenuBtn() {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        return;
    }

    /**
     * <p>Initializes the Game Ended Screen.<br />
     * Also sets the winners subtitle label and renders the affection
     * tokens of all players as labels in descending order.</p>
     */
    @FXML
    private void initialize() {
        if (GameState.getActiveGameMode().getMostRecentRoundWinners().isEmpty())
            throw new RuntimeException("No winners were found in the most recent round!");
        this.winnersubtitlelabel.setText(String.format("%s won the game!",
                GameState.getActiveGameMode().getMostRecentRoundWinnersAsString()));

        this.playersscorecontainer.getChildren().clear();
        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllerByDSCAffection()) {
            Label label = new Label(String.format("%s: %s", PC.getPlayerName(), PC.getAffectionTokens()));
            label.getStyleClass().add("text-lg");

            this.playersscorecontainer.getChildren().add(label);
            continue;
        }

        return;
    }

}
