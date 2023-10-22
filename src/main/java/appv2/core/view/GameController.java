package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.PlayerController;
import appv2.core.ECardResponse;
import appv2.core.EGameModeState;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
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
        Label remainingPlayersLabel = new Label(String.format("%d/%d", GameState.getActiveGameMode().getRemainingPlayerCount(), GameState.getActiveGameMode().getPlayerCount()));
        remainingPlayersLabel.setId("basic-text");

        Button showScoreButton = new Button("Show Score Of All Players");
        showScoreButton.setId("show-score-btn");
        showScoreButton.setOnAction(actionEvent -> {
            GameScene gameScene = new GameScene(MasterController.PLAYERS_SCORE, View.loadFXML(View.PATH_TO_PLAYERS_SCORE), true, null);
            String fallback = View.renderNewScreen(gameScene, true);
            gameScene.setArgs(fallback);
            return;
        });

        Label remainingCardsLabel = new Label(String.format("%d %s cards remaining", GameState.getActiveGameMode().getRemainingTableCardsCount(), GameState.getActiveGameMode().isHiddenCardAvailable() ? "(+1)" : ""));
        remainingCardsLabel.setId("basic-text");

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
                EGameModeState state = GameState.getActiveGameMode().selectNextValidPlayer();

                if (state == EGameModeState.ROUND_ENDED) {
                    View.renderNewScreen(new GameScene(MasterController.ROUND_ENDED, View.loadFXML(View.PATH_TO_ROUND_ENDED), true, null), false);
                    return;
                }

                if (state == EGameModeState.GAME_ENDED) {
                    View.renderNewScreen((new GameScene(MasterController.GAME_ENDED, View.loadFXML(View.PATH_TO_GAME_ENDED), true, null)), false);
                    return;
                }

                // TODO: Game ended

                View.renderNewScreen(new GameScene(MasterController.getUniqueIdentifier(String.format("%s-player%s", MasterController.GAME, GameState.getActiveGameMode().getMostRecentPlayerController().getPlayerName())), View.loadFXML(View.PATH_TO_GAME), true, null), false);
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
