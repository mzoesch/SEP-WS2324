package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;


/**
 * <p>Controller for the Discarded Pile Screen.</p>
 */
public class DiscardedPileController {

    @FXML private Button closebtn;
    @FXML private HBox discardedpilecontainer;

    /**
     * <p>Renders the discarded cards of all players to the screen.</p>
     */
    private void renderDiscardedPiles() {
        this.discardedpilecontainer.getChildren().clear();

        for (int i = 0; i < GameState.getActiveGameMode().getPlayerCount(); i++) {
            Label label = new Label(
                String.format("%s",
                    GameState.getActiveGameMode().getPlayerControllerByID(i).getPlayerName()));
            label.getStyleClass().add("text-lg");
            VBox vbox = new VBox(label);

            if (GameState.getActiveGameMode().getPlayerControllerByID(i).getDiscardedCardsPile().length == 0) {
                vbox.getChildren().add(new Label("No cards discarded yet."));
                vbox.getChildren().get(vbox.getChildren().size() - 1).getStyleClass().add("text-base");

                this.discardedpilecontainer.getChildren().add(vbox);
                continue;
            }

            for (
                int j = 0;
                j < GameState.getActiveGameMode().getPlayerControllerByID(i).getDiscardedCardsPile().length;
                j++
            ) {
                Label discardedcardlabel = new Label(
                    String.format(
                        "-> %s",
                        GameState.getActiveGameMode().getPlayerControllerByID(i)
                            .getDiscardedCardsPile()[j].getAsString()
                    )
                );
                discardedcardlabel.getStyleClass().add("text-base");

                vbox.getChildren().add(discardedcardlabel);
                continue;
            }

            this.discardedpilecontainer.getChildren().add(vbox);
            continue;
        }

        return;
    }

    /**
     * <p>Initializes the Discarded Pile Screen.<br />
     * Adds the closing button for this screen and renders the discarded cards of all players.</p>
     */
    @FXML
    private void initialize() {
        this.renderDiscardedPiles();

        this.closebtn.setOnAction(actionEvent -> {
            View.renderExistingScreen(View.getActiveGameScene().getFallback());
            return;
        });

        return;
    }

}
