package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.PlayerController;
import appv2.core.ECardResponse;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;


public class GameController {

    @FXML private Label turntitle;
    @FXML private AnchorPane mainarea;
    @FXML private AnchorPane leftarea;
    @FXML private AnchorPane rightarea;
    @FXML private AnchorPane bottomarea;

    private void playCard(boolean bHandCard) {
        PlayerController PC = GameState.getActiveGameMode().getMostRecentPlayerController();
        ECardResponse res = PC.playCard(bHandCard);


        return;
    }

    // region Render Utils

    private void clearScreen() {
        this.mainarea.getChildren().clear();
        this.leftarea.getChildren().clear();
        this.rightarea.getChildren().clear();
        this.bottomarea.getChildren().clear();

        return;
    }

    private void setTurnTitle(String playerName) {
        this.turntitle.setText(playerName + "'s turn");
        return;
    }

    private void renderKnockedOutScreen() {
        this.clearScreen();
        return;
    }

    private void renderDiscardedPileAreaScreen() {
        Label label = new Label("Your discarded cards pile.");
        label.setId("discarded-pile-title");
        VBox vbox = new VBox(label);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length == 0) {
            Label discardedCard = new Label("You have not discarded any cards");
            discardedCard.setId("discarded-pile-cards");

            vbox.getChildren().add(discardedCard);
        }
        else for (int i = 0; i < GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length; i++) {
            Label discardedCard = new Label(String.format("-> %s", GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile()[i].getAsString()));
            discardedCard.setId("discarded-pile-cards");

            vbox.getChildren().add(discardedCard);
            continue;
        }

        Button button = new Button("Show discarded pile of all players.");
        button.setId("show-discarded-pile-btn");
        vbox.getChildren().add(button);

        this.leftarea.getChildren().clear();
        this.leftarea.getChildren().add(vbox);

        return;
    }

    private void renderGameStateOverviewAreaScreen() {
        Label remainingPlayersLabel = new Label();
        Text remainingPlayerText = new Text("4/4 remaining players.");
        remainingPlayerText.setFill(Color.WHITE);
        remainingPlayersLabel.setText(remainingPlayerText.getText());
        remainingPlayersLabel.setTextFill(remainingPlayerText.getFill());
        Font font = new Font(20.0);
        remainingPlayersLabel.setFont(font);

        Button showScoreButton = new Button();
        Text btnText = new Text("Show score of all players.");
        btnText.setFill(Color.BLACK);
        showScoreButton.setText(btnText.getText());
        showScoreButton.setTextFill(btnText.getFill());

        Label remainingCardsLabel = new Label();
        Text remainingCardsText = new Text("10 (+1) cards remaining.");
        remainingCardsText.setFill(Color.WHITE);
        remainingCardsLabel.setText(remainingCardsText.getText());
        remainingCardsLabel.setTextFill(remainingCardsText.getFill());
        remainingCardsLabel.setFont(font);

        VBox vbox = new VBox(remainingPlayersLabel, showScoreButton, remainingCardsLabel);
        this.rightarea.getChildren().add(vbox);
        return;
    }

    private void renderChoiceAreaScreen() {
        VBox vbox = new VBox();
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

        if (!GameState.getActiveGameMode().getExaminingCards().isEmpty()) {
            Label exTitle = new Label("The current examining cards are:");
            exTitle.setId("ex-title");

            HBox hbox = new HBox();
            hbox.setId("ex-hbox");
            AnchorPane.setBottomAnchor(hbox, 0.0);
            AnchorPane.setLeftAnchor(hbox, 0.0);
            AnchorPane.setRightAnchor(hbox, 0.0);

            for (int i = 0; i < GameState.getActiveGameMode().getExaminingCards().size(); i++) {
                Label exCardLabel = new Label(GameState.getActiveGameMode().getExaminingCards().get(i).getAsString());
                exCardLabel.setId("ex-card-label");

                hbox.getChildren().add(exCardLabel);
                continue;
            }

            vbox.getChildren().addAll(exTitle, hbox);
        }

        HBox hBox = new HBox();
        hBox.setId("choice-btn-area");
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        hBox.setAlignment(Pos.CENTER);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().hasPlayedCard()) {
            Button endTurn = new Button("End Your Turn");
            endTurn.setId("end-turn-btn");
            endTurn.setOnAction(actionEvent -> {
                GameState.getActiveGameMode().selectNextValidPlayer();
                return;
            });

            hBox.getChildren().add(endTurn);
        }

        if (!GameState.getActiveGameMode().getMostRecentPlayerController().hasPlayedCard() && GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard() != null) {
            Button hCard = new Button(String.format("Play %s",
                    GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getAsString()));
            hCard.setId("play-card-btn");
            hCard.setOnAction(actionEvent -> {
                this.playCard(true);
                this.renderDiscardedPileAreaScreen();
                this.renderChoiceAreaScreen();

                return;
            });

            hBox.getChildren().add(hCard);
        }

        if (!GameState.getActiveGameMode().getMostRecentPlayerController().hasPlayedCard() && GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard() != null) {
            Button tCard = new Button(String.format("Play %s",
                    GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getAsString()));
            tCard.setId("play-card-btn");
            tCard.setOnAction(actionEvent -> {
                this.playCard(false);
                this.renderDiscardedPileAreaScreen();
                this.renderChoiceAreaScreen();

                return;
            });

            hBox.getChildren().add(tCard);
        }

        vbox.getChildren().add(hBox);

        this.bottomarea.getChildren().clear();
        this.bottomarea.getChildren().add(vbox);

        return;
    }

    private void renderStandardScreen() {
        this.clearScreen();

        this.renderDiscardedPileAreaScreen();
        this.renderGameStateOverviewAreaScreen();
        this.renderChoiceAreaScreen();

        return;
    }

    // endregion Render Utils

    private void renderFirstPlayerInteraction() {
        Button button = new Button();
        button.setText("Press To Play Your Turn");
        button.setWrapText(true);
        Font font = new Font(30.0);
        button.setFont(font);

        AnchorPane.setBottomAnchor(button, 160.0);
        AnchorPane.setLeftAnchor(button, 60.0);
        AnchorPane.setRightAnchor(button, 60.0);
        AnchorPane.setTopAnchor(button, 160.0);

        this.clearScreen();
        this.mainarea.getChildren().add(button);

        button.setOnAction(actionEvent -> {
            this.renderPlayTurnScreen();
            return;
        });

        return;
    }

    private void renderPlayTurnScreen() {
        this.mainarea.getChildren().clear();

        // Here logic what exact scree to implement
        // if isKnockedOut do: [knocked out screen]

        this.renderStandardScreen();

        return;
    }

    @FXML
    private void initialize() {
        PlayerController PC = GameState.getActiveGameMode().getMostRecentPlayerController();
        PC.prepareForNextTurn();

        this.setTurnTitle(PC.getPlayerName());
        this.renderFirstPlayerInteraction();

        return;
    }

}
