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
import javafx.scene.layout.VBox;
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

    // region Utility Methods
    
    private static boolean hasHandCard(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getHandCard() != null;
    }

    private static boolean hasTableCard(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getTableCard() != null;
    }
    
    // endregion Utility Methods
    
    // region Render Utils

    private void clearScreen() {
        this.mainarea.getChildren().clear();
        this.leftarea.getChildren().clear();
        this.rightarea.getChildren().clear();
        this.bottomarea.getChildren().clear();

        return;
    }

    private void setTurnTitle(String playerName) {
        this.turntitle.setText(String.format("%s's Turn", playerName));
        return;
    }

    private void renderKnockedOutScreen() {
        this.clearScreen();
        return;
    }

    private void renderDiscardedPileAreaScreen() {
        Label label = new Label("Your discarded cards pile.");
        label.getStyleClass().add("text-base");
        VBox vbox = new VBox(label);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length == 0) {
            Label discardedCard = new Label("You have not yet discarded any cards");
            discardedCard.getStyleClass().add("text-base");

            vbox.getChildren().add(discardedCard);
        }
        else for (
                int i = 0;
                i < GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length;
                i++
        ) {
            Label discardedCard = new Label(
                    String.format(
                            "-> %s",
                            GameState.getActiveGameMode()
                                    .getMostRecentPlayerController().getDiscardedCardsPile()[i].getAsString()
                    )
            );
            discardedCard.getStyleClass().add("text-base");

            vbox.getChildren().add(discardedCard);
            continue;
        }

        Button button = new Button("Show discarded pile of all players.");
        button.getStyleClass().add("secondary-mini-btn");
        button.setOnAction(actionEvent -> {
            System.out.println("Show discarded pile of all players.");
            return;
        });
        vbox.getChildren().add(button);

        this.leftarea.getChildren().clear();
        this.leftarea.getChildren().add(vbox);

        return;
    }

    private void renderGameStateOverviewAreaScreen() {
        Label remainingPlayersLabel = new Label(
                String.format(
                        "%d/%d player remaining",
                        GameState.getActiveGameMode().getRemainingPlayerCount(),
                        GameState.getActiveGameMode().getPlayerCount()
                )
        );
        remainingPlayersLabel.getStyleClass().add("text-base");

        Button showScoreButton = new Button("Show Score Of All Players");
        showScoreButton.getStyleClass().add("secondary-mini-btn");
        showScoreButton.setOnAction(actionEvent -> {
            GameScene gameScene = new GameScene(
                    MasterController.PLAYERS_SCORE,
                    View.loadFXML(View.PATH_TO_PLAYERS_SCORE),
                    true,
                    null
            );
            gameScene.setFallback(View.renderNewScreen(gameScene, true));
            return;
        });

        Label remainingCardsLabel = new Label(
                String.format(
                        "%d %s cards remaining",
                        GameState.getActiveGameMode().getRemainingTableCardsCount(),
                        GameState.getActiveGameMode().isHiddenCardAvailable() ? "(+1)" : ""
                )
        );
        remainingCardsLabel.getStyleClass().add("text-base");

        VBox vbox = new VBox(remainingPlayersLabel, showScoreButton, remainingCardsLabel);
        this.rightarea.getChildren().add(vbox);

        return;
    }

    private void renderChoiceAreaScreen() {
        VBox vbox = new VBox();
        vbox.setId("vbox-bottom-area");

        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

        if (!GameState.getActiveGameMode().getExaminingCards().isEmpty()) {
            Label examiningTitle = new Label("The current examining cards are:");
            examiningTitle.getStyleClass().add("text-lg");

            HBox hbox = new HBox();
            hbox.setId("examining-hbox");

            AnchorPane.setBottomAnchor(hbox, 0.0);
            AnchorPane.setLeftAnchor(hbox, 0.0);
            AnchorPane.setRightAnchor(hbox, 0.0);

            for (int i = 0; i < GameState.getActiveGameMode().getExaminingCards().size(); i++) {
                Label examiningCardLabel = new Label(
                        GameState.getActiveGameMode().getExaminingCards().get(i).getAsString()
                );
                examiningCardLabel.getStyleClass().add("text-lg");

                hbox.getChildren().add(examiningCardLabel);
                continue;
            }

            vbox.getChildren().addAll(examiningTitle, hbox);
        }

        HBox btnContainer = new HBox();
        btnContainer.setId("hbox-btn-container");

        AnchorPane.setBottomAnchor(btnContainer, 0.0);
        AnchorPane.setLeftAnchor(btnContainer, 0.0);
        AnchorPane.setRightAnchor(btnContainer, 0.0);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().hasPlayedCard()) {
            Button endTurn = new Button("End Your Turn");
            endTurn.getStyleClass().add("danger-btn");

            endTurn.setOnAction(actionEvent -> {
                EGameModeState state = GameState.getActiveGameMode().selectNextValidPlayer();

                if (state == EGameModeState.ROUND_ENDED) {
                    View.renderNewScreen(
                            new GameScene(
                                    MasterController.ROUND_ENDED,
                                    View.loadFXML(View.PATH_TO_ROUND_ENDED),
                                    true,
                                    null
                            ),
                            false
                    );

                    return;
                }

                if (state == EGameModeState.GAME_ENDED) {
                    View.renderNewScreen(new GameScene(
                            MasterController.GAME_ENDED,
                                    View.loadFXML(View.PATH_TO_GAME_ENDED),
                                    true,
                                    null
                            ),
                            false
                    );

                    return;
                }

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
            });

            btnContainer.getChildren().add(endTurn);
        }
        if (GameController.hasHandCard(GameState.getActiveGameMode().getMostRecentPlayerController())) {
            Button handCardButton = new Button(
                String.format(
                    "Play %s",
                    GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getAsString()
                )
            );
            handCardButton.getStyleClass().add("primary-btn");

            handCardButton.setOnAction(actionEvent -> {
                this.playCard(true);
                this.renderDiscardedPileAreaScreen();
                this.renderChoiceAreaScreen();

                return;
            });

            btnContainer.getChildren().add(handCardButton);
        }
        if (GameController.hasTableCard(GameState.getActiveGameMode().getMostRecentPlayerController())) {
            Button tableCardButton = new Button(
                String.format(
                    "Play %s",
                    GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getAsString()
                )
            );
            tableCardButton.getStyleClass().add("primary-btn");

            tableCardButton.setOnAction(actionEvent -> {
                this.playCard(false);
                this.renderDiscardedPileAreaScreen();
                this.renderChoiceAreaScreen();

                return;
            });

            btnContainer.getChildren().add(tableCardButton);
        }

        vbox.getChildren().add(btnContainer);

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
        Button button = new Button("Press To Play Your Turn");
        button.getStyleClass().add("primary-btn");

        AnchorPane.setBottomAnchor(button, 160.0);
        AnchorPane.setLeftAnchor(button, 60.0);
        AnchorPane.setRightAnchor(button, 60.0);
        AnchorPane.setTopAnchor(button, 160.0);

        button.setOnAction(actionEvent -> {
            this.renderPlayTurnScreen();
            return;
        });

        this.clearScreen();
        this.mainarea.getChildren().add(button);

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
        GameState.getActiveGameMode().getMostRecentPlayerController().prepareForNextTurn();
        this.setTurnTitle(GameState.getActiveGameMode().getMostRecentPlayerController().getPlayerName());
        this.renderFirstPlayerInteraction();

        return;
    }

}
