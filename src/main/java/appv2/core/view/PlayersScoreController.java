package appv2.core.view;

import appv2.core.View;
import appv2.core.PlayerController;
import appv2.core.GameState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class PlayersScoreController {

    @FXML private Button closeBtn;
    @FXML private VBox vbox;

    @FXML
    private void initialize() {
        this.vbox.getChildren().clear();

        for (PlayerController PC : GameState.getActiveGameMode().getPlayerControllers()) {
            Label label = new Label(String.format("%s: %d", PC.getPlayerName(), PC.getAffectionTokens()));
            label.setId("basic-text");
            this.vbox.getChildren().add(label);
            continue;
        }

        this.closeBtn.setOnAction(actionEvent -> {
            System.out.println("Closing PlayersScoreController");
            GameScene active = View.getActiveGameScene();
            View.renderExistingScreen(active.getArgs());
            return;
        });

        return;
    }

}
