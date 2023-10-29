package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;
import appv2.core.PlayerController;
import appv2.core.ECardResponse;
import appv2.core.EGameModeState;
import appv2.cards.ACard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;

import java.util.Objects;


public class GameController {

    @FXML private Label turntitle;
    @FXML private AnchorPane mainarea;
    @FXML private AnchorPane leftarea;
    @FXML private AnchorPane rightarea;
    @FXML private AnchorPane bottomarea;

    private void playCard(boolean bHandCard) {
        StringBuilder stderrPipeline = new StringBuilder();
        StringBuilder stdoutPipeline = new StringBuilder();
        ECardResponse res = GameState.getActiveGameMode()
                .getMostRecentPlayerController().playCard(bHandCard, stdoutPipeline, stderrPipeline);

        if (res == ECardResponse.RC_OK) {
            Label label = new Label(
                String.format(
                    "You played %s\n%s",
                    GameState.getActiveGameMode().getMostRecentPlayerController()
                            .getDiscardedCardsPile()[GameState.getActiveGameMode().getMostRecentPlayerController()
                            .getDiscardedCardsPile().length - 1].getName(),
                    stdoutPipeline.toString()
                )
            );
            label.getStyleClass().add("text-lg");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 00.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);

            this.mainarea.getChildren().clear();
            this.mainarea.getChildren().add(label);

            return;
        }

        if (res == ECardResponse.RC_OK_KNOCKED_OUT) {
            Label label = new Label(
                String.format(
                    "You played %s and knocked yourself out",
                    GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile()[GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length - 1].getName()
                )
            );
            label.getStyleClass().add("text-lg");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 00.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);

            this.mainarea.getChildren().clear();
            this.mainarea.getChildren().add(label);

            return;
        }

        if (res == ECardResponse.RC_ERR) {
            Label label = new Label(
                String.format(
                    "You played %s but it failed.\n%s",
                    bHandCard
                            ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getName()
                            : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getName(),

                    stderrPipeline.toString()
                )
            );
            label.getStyleClass().add("text-lg-warning");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 00.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);

            this.mainarea.getChildren().add(label);

            GameState.getActiveGameMode().getMostRecentPlayerController().setPlayedCard(false);
            return;
        }

        if (res == ECardResponse.RC_CHOOSE_ANY_PLAYER) {
            Label label = new Label(
                String.format(
                    "You played %s.\n%s",
                        (bHandCard
                            ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getName()
                            : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getName()
                        ),
                        stdoutPipeline.toString()
                )
            );
            label.getStyleClass().add("text-lg");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);

            HBox hbox = new HBox();
            hbox.setId("main-area-vbox");

            AnchorPane.setLeftAnchor(hbox, 0.0);
            AnchorPane.setRightAnchor(hbox, 0.0);
            AnchorPane.setBottomAnchor(hbox, 75.0);

            for (int i = 0; i < GameState.getActiveGameMode().getPlayerCount(); i++) {
                if (GameState.getActiveGameMode().getPlayerControllerByID(i).isKnockedOut())
                    continue;

                Button button = new Button(
                    GameState.getActiveGameMode().getPlayerControllerByID(i).getPlayerName()
                );
                button.getStyleClass().add("primary-mini-btn");

                int finalI = i;
                button.setOnAction(actionEvent -> {
                    stdoutPipeline.setLength(0);
                    stderrPipeline.setLength(0);

                    int RC = bHandCard
                            ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().callback(
                                GameState.getActiveGameMode().getMostRecentPlayerController(),
                                GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                                stdoutPipeline,
                                stderrPipeline,
                                null
                            )
                            : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().callback(
                                GameState.getActiveGameMode().getMostRecentPlayerController(),
                                GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                                stdoutPipeline,
                                stderrPipeline,
                                null
                            );

                    if (RC == ACard.RC_OK_HANDS_UPDATED) {
                        this.mainarea.getChildren().clear();

                        Label labelSuccess = new Label(String.format("You played %s.\n%s",
                                GameState.getActiveGameMode()
                                        .getMostRecentPlayerController().getDiscardedCardsPile()
                                        [GameState.getActiveGameMode().getMostRecentPlayerController()
                                        .getDiscardedCardsPile().length - 1].getName(),
                                stdoutPipeline.toString()
                                ));
                        labelSuccess.getStyleClass().add("text-lg");
                        labelSuccess.getStyleClass().add("title-wrap");

                        AnchorPane.setLeftAnchor(labelSuccess, 0.0);
                        AnchorPane.setRightAnchor(labelSuccess, 0.0);
                        AnchorPane.setTopAnchor(labelSuccess, 0.0);
                        AnchorPane.setBottomAnchor(labelSuccess, 0.0);

                        this.mainarea.getChildren().add(labelSuccess);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setIsPlaying(false);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setPlayedCard(true);
                        this.renderChoiceAreaScreen();
                        this.renderDiscardedPileAreaScreen();

                        return;
                    }

                    if (RC == ACard.RC_ERR) {
                        Label labelErr = new Label(String.format("You have chosen %s but it failed.\n%s",
                            GameState.getActiveGameMode().getPlayerControllerByID(finalI).getPlayerName(),
                            stderrPipeline.toString()
                        ));
                        labelErr.getStyleClass().add("text-base-warning");
                        labelErr.getStyleClass().add("title-wrap");

                        if (this.mainarea.getChildren().get(this.mainarea.getChildren().size() - 1) instanceof Label)
                            this.mainarea.getChildren().remove(this.mainarea.getChildren().size() - 1);
                        this.mainarea.getChildren().add(labelErr);

                        AnchorPane.setLeftAnchor(labelErr, 0.0);
                        AnchorPane.setRightAnchor(labelErr, 0.0);
                        AnchorPane.setBottomAnchor(labelErr, 0.0);

                        button.setDisable(true);

                        return;
                    }

                    throw new RuntimeException("Unhandled return code from ACard.callback().");
                });

                hbox.getChildren().add(button);
                continue;
            }

            this.mainarea.getChildren().add(label);
            this.mainarea.getChildren().add(hbox);

            return;
        }

        if (res == ECardResponse.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED) {
            Label label = new Label(
                    String.format(
                            "You played %s.\n%s",
                            (bHandCard
                                    ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getName()
                                    : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getName()
                            ),
                            stdoutPipeline.toString()
                    )
            );
            label.getStyleClass().add("text-lg");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);

            HBox hbox = new HBox();
            hbox.setId("main-area-vbox");

            AnchorPane.setLeftAnchor(hbox, 0.0);
            AnchorPane.setRightAnchor(hbox, 0.0);
            AnchorPane.setBottomAnchor(hbox, 75.0);

            for (int i = 0; i < GameState.getActiveGameMode().getPlayerCount(); i++) {
                if (GameState.getActiveGameMode().getPlayerControllerByID(i).isKnockedOut())
                    continue;
                if (Objects.equals(
                        GameState.getActiveGameMode().getPlayerControllerByID(i),
                        GameState.getActiveGameMode().getMostRecentPlayerController()
                ))
                    continue;

                Button button = new Button(
                        GameState.getActiveGameMode().getPlayerControllerByID(i).getPlayerName()
                );
                button.getStyleClass().add("primary-mini-btn");

                int finalI = i;
                button.setOnAction(actionEvent -> {
                    stdoutPipeline.setLength(0);
                    stderrPipeline.setLength(0);

                    int RC = bHandCard
                            ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().callback(
                            GameState.getActiveGameMode().getMostRecentPlayerController(),
                            GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                            stdoutPipeline,
                            stderrPipeline,
                            null
                    )
                            : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().callback(
                            GameState.getActiveGameMode().getMostRecentPlayerController(),
                            GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                            stdoutPipeline,
                            stderrPipeline,
                            null
                    );

                    if (RC == ACard.RC_OK_HANDS_UPDATED) {
                        this.mainarea.getChildren().clear();

                        Label labelSuccess = new Label(String.format("You played %s.\n%s",
                                GameState.getActiveGameMode()
                                        .getMostRecentPlayerController().getDiscardedCardsPile()
                                        [GameState.getActiveGameMode().getMostRecentPlayerController()
                                        .getDiscardedCardsPile().length - 1].getName(),
                                stdoutPipeline.toString()
                        ));
                        labelSuccess.getStyleClass().add("text-lg");
                        labelSuccess.getStyleClass().add("title-wrap");

                        AnchorPane.setLeftAnchor(labelSuccess, 0.0);
                        AnchorPane.setRightAnchor(labelSuccess, 0.0);
                        AnchorPane.setTopAnchor(labelSuccess, 0.0);
                        AnchorPane.setBottomAnchor(labelSuccess, 0.0);

                        this.mainarea.getChildren().add(labelSuccess);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setIsPlaying(false);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setPlayedCard(true);
                        this.renderChoiceAreaScreen();
                        this.renderDiscardedPileAreaScreen();

                        return;
                    }

                    if (RC == ACard.RC_ERR) {
                        Label labelErr = new Label(String.format("You have chosen %s but it failed.\n%s",
                                GameState.getActiveGameMode().getPlayerControllerByID(finalI).getPlayerName(),
                                stderrPipeline.toString()
                        ));
                        labelErr.getStyleClass().add("text-base-warning");
                        labelErr.getStyleClass().add("title-wrap");

                        if (this.mainarea.getChildren().get(this.mainarea.getChildren().size() - 1) instanceof Label)
                            this.mainarea.getChildren().remove(this.mainarea.getChildren().size() - 1);
                        this.mainarea.getChildren().add(labelErr);

                        AnchorPane.setLeftAnchor(labelErr, 0.0);
                        AnchorPane.setRightAnchor(labelErr, 0.0);
                        AnchorPane.setBottomAnchor(labelErr, 0.0);

                        button.setDisable(true);

                        return;
                    }

                    if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
                        Label labelKnockedOut = new Label(String.format("You have been knocked out of the game.\n%s",
                                stdoutPipeline.toString()
                        ));
                        labelKnockedOut.getStyleClass().add("text-lg-warning");
                        labelKnockedOut.getStyleClass().add("title-wrap");

                        this.mainarea.getChildren().clear();
                        this.mainarea.getChildren().add(labelKnockedOut);

                        AnchorPane.setLeftAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setRightAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setTopAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setBottomAnchor(labelKnockedOut, 0.0);

                        this.renderDiscardedPileAreaScreen();
                        this.renderChoiceAreaScreen();

                        return;
                    }

                    throw new RuntimeException("Unhandled return code from ACard.callback().");
                });

                hbox.getChildren().add(button);
                continue;
            }

            this.mainarea.getChildren().add(label);
            this.mainarea.getChildren().add(hbox);

            return;
        }

        if (res == ECardResponse.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER) {
            Label label = new Label(
                    String.format(
                            "You played %s.\n%s",
                            (bHandCard
                                    ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().getName()
                                    : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().getName()
                            ),
                            stdoutPipeline.toString()
                    )
            );
            label.getStyleClass().add("text-lg");
            label.getStyleClass().add("title-wrap");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 150.0);

            TextField textField = new TextField();
            textField.setPromptText("Enter a number (2-8): ");
            textField.setId("main-area-text-field");

            AnchorPane.setLeftAnchor(textField, 150.0);
            AnchorPane.setRightAnchor(textField, 150.0);
            AnchorPane.setBottomAnchor(textField, 150.0);

            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (!t1.matches("\\d*")) {
                        textField.setText(t1.replaceAll("[^\\d]", ""));
                    }

                    return;
                }
            });

            HBox hbox = new HBox();
            hbox.setId("main-area-vbox");

            AnchorPane.setLeftAnchor(hbox, 0.0);
            AnchorPane.setRightAnchor(hbox, 0.0);
            AnchorPane.setBottomAnchor(hbox, 75.0);

            for (int i = 0; i < GameState.getActiveGameMode().getPlayerCount(); i++) {
                if (GameState.getActiveGameMode().getPlayerControllerByID(i).isKnockedOut())
                    continue;
                if (Objects.equals(
                        GameState.getActiveGameMode().getPlayerControllerByID(i),
                        GameState.getActiveGameMode().getMostRecentPlayerController()
                ))
                    continue;

                Button button = new Button(
                        GameState.getActiveGameMode().getPlayerControllerByID(i).getPlayerName()
                );
                button.getStyleClass().add("primary-mini-btn");

                int finalI = i;
                button.setOnAction(actionEvent -> {
                    stdoutPipeline.setLength(0);
                    stderrPipeline.setLength(0);

                    if (textField.getText().isEmpty()) {
                        Label labelErr = new Label("You must enter a number.");
                        labelErr.getStyleClass().add("text-base-warning");
                        labelErr.getStyleClass().add("title-wrap");

                        if (this.mainarea.getChildren().get(this.mainarea.getChildren().size() - 1) instanceof Label)
                            this.mainarea.getChildren().remove(this.mainarea.getChildren().size() - 1);
                        this.mainarea.getChildren().add(labelErr);

                        AnchorPane.setLeftAnchor(labelErr, 0.0);
                        AnchorPane.setRightAnchor(labelErr, 0.0);
                        AnchorPane.setBottomAnchor(labelErr, 0.0);

                        return;
                    }

                    // We can do this because of the regex in the listener.
                    int guessAffection = Integer.parseInt(textField.getText());
                    if (guessAffection < 2 || guessAffection > 8) {
                        Label labelErr = new Label("You must enter a number between 2 and 8.");
                        labelErr.getStyleClass().add("text-base-warning");
                        labelErr.getStyleClass().add("title-wrap");

                        if (this.mainarea.getChildren().get(this.mainarea.getChildren().size() - 1) instanceof Label)
                            this.mainarea.getChildren().remove(this.mainarea.getChildren().size() - 1);
                        this.mainarea.getChildren().add(labelErr);

                        AnchorPane.setLeftAnchor(labelErr, 0.0);
                        AnchorPane.setRightAnchor(labelErr, 0.0);
                        AnchorPane.setBottomAnchor(labelErr, 0.0);

                        return;
                    }


                    int RC = bHandCard
                            ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().callback(
                            GameState.getActiveGameMode().getMostRecentPlayerController(),
                            GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                            stdoutPipeline,
                            stderrPipeline,
                            new String[] { String.valueOf(guessAffection) }
                    )
                            : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().callback(
                            GameState.getActiveGameMode().getMostRecentPlayerController(),
                            GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                            stdoutPipeline,
                            stderrPipeline,
                            new String[] { String.valueOf(guessAffection) }
                    );

                    if (RC == ACard.RC_OK_HANDS_UPDATED) {
                        this.mainarea.getChildren().clear();

                        Label labelSuccess = new Label(String.format("You played %s.\n%s",
                                GameState.getActiveGameMode()
                                        .getMostRecentPlayerController().getDiscardedCardsPile()
                                        [GameState.getActiveGameMode().getMostRecentPlayerController()
                                        .getDiscardedCardsPile().length - 1].getName(),
                                stdoutPipeline.toString()
                        ));
                        labelSuccess.getStyleClass().add("text-lg");
                        labelSuccess.getStyleClass().add("title-wrap");

                        AnchorPane.setLeftAnchor(labelSuccess, 0.0);
                        AnchorPane.setRightAnchor(labelSuccess, 0.0);
                        AnchorPane.setTopAnchor(labelSuccess, 0.0);
                        AnchorPane.setBottomAnchor(labelSuccess, 0.0);

                        this.mainarea.getChildren().add(labelSuccess);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setIsPlaying(false);
                        GameState.getActiveGameMode().getMostRecentPlayerController().setPlayedCard(true);
                        this.renderChoiceAreaScreen();
                        this.renderDiscardedPileAreaScreen();

                        return;
                    }

                    if (RC == ACard.RC_ERR) {
                        Label labelErr = new Label(String.format("You have chosen %s but it failed.\n%s",
                                GameState.getActiveGameMode().getPlayerControllerByID(finalI).getPlayerName(),
                                stderrPipeline.toString()
                        ));
                        labelErr.getStyleClass().add("text-base-warning");
                        labelErr.getStyleClass().add("title-wrap");

                        if (this.mainarea.getChildren().get(this.mainarea.getChildren().size() - 1) instanceof Label)
                            this.mainarea.getChildren().remove(this.mainarea.getChildren().size() - 1);
                        this.mainarea.getChildren().add(labelErr);

                        AnchorPane.setLeftAnchor(labelErr, 0.0);
                        AnchorPane.setRightAnchor(labelErr, 0.0);
                        AnchorPane.setBottomAnchor(labelErr, 0.0);

                        button.setDisable(true);

                        return;
                    }

                    if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
                        Label labelKnockedOut = new Label(String.format("You have been knocked out of the game.\n%s",
                                stdoutPipeline.toString()
                        ));
                        labelKnockedOut.getStyleClass().add("text-lg-warning");
                        labelKnockedOut.getStyleClass().add("title-wrap");

                        this.mainarea.getChildren().clear();
                        this.mainarea.getChildren().add(labelKnockedOut);

                        AnchorPane.setLeftAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setRightAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setTopAnchor(labelKnockedOut, 0.0);
                        AnchorPane.setBottomAnchor(labelKnockedOut, 0.0);

                        this.renderDiscardedPileAreaScreen();
                        this.renderChoiceAreaScreen();

                        return;
                    }

                    throw new RuntimeException("Unhandled return code from ACard.callback().");
                });

                hbox.getChildren().add(button);
                continue;
            }

            this.mainarea.getChildren().add(label);
            this.mainarea.getChildren().add(hbox);
            this.mainarea.getChildren().add(textField);

            return;
        }



        this.mainarea.getChildren().clear();
        this.bottomarea.getChildren().clear();
        this.mainarea.getChildren().add(new Label(res.toString()));

        return;
    }

    // region Utility Methods

    private static boolean showEndTurnButton(PlayerController PC) {
        return PC.hasPlayedCard() && !PC.isPlaying();
    }

    private static boolean showHandCardButton(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getHandCard() != null && !PC.isPlaying();
    }

    private static boolean showTableCardButton(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getTableCard() != null && !PC.isPlaying();
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
            GameScene gameScene = new GameScene(
                    MasterController.DISCARDED_PILE,
                    View.loadFXML(View.PATH_TO_DISCARDED_PILE),
                    true,
                    null
            );
            gameScene.setFallback(View.renderNewScreen(gameScene, true));

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
        vbox.setId("right-area-vbox");

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
        btnContainer.setId("hbox-bottom-btn-container");

        AnchorPane.setBottomAnchor(btnContainer, 0.0);
        AnchorPane.setLeftAnchor(btnContainer, 0.0);
        AnchorPane.setRightAnchor(btnContainer, 0.0);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().isPlaying()) {
            this.bottomarea.getChildren().clear();
            return;
        }
        if (GameController.showEndTurnButton(GameState.getActiveGameMode().getMostRecentPlayerController())) {
            Button endTurn = new Button("End Your Turn");
            endTurn.getStyleClass().add("danger-btn");

            endTurn.setOnAction(actionEvent -> {
                GameState.getActiveGameMode().getMostRecentPlayerController().setMessageForPlayerNextTurn("");
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
        if (GameController.showHandCardButton(GameState.getActiveGameMode().getMostRecentPlayerController())) {
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
        if (GameController.showTableCardButton(GameState.getActiveGameMode().getMostRecentPlayerController())) {
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

    private void renderKnockedOutScreen() {
        this.clearScreen();

        this.renderDiscardedPileAreaScreen();
        this.renderGameStateOverviewAreaScreen();
        this.renderChoiceAreaScreen();

        Label label =
                new Label("You have been knocked out of this round.\nYou may only end your turn now. You will be able to play again next round.");
        label.getStyleClass().add("text-lg-warning");
        label.getStyleClass().add("title-wrap");

        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);

        this.mainarea.getChildren().add(label);

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

        AnchorPane.setLeftAnchor(button, 60.0);
        AnchorPane.setRightAnchor(button, 60.0);
        AnchorPane.setTopAnchor(button, 160.0);
        AnchorPane.setBottomAnchor(button, 160.0);

        button.setOnAction(actionEvent -> {
            this.renderPlayTurnScreen();
            return;
        });

        this.clearScreen();
        this.mainarea.getChildren().add(button);

        if (GameState.getActiveGameMode().getMostRecentPlayerController().getMessageForPlayerNextTurn() == null ||
                GameState.getActiveGameMode().getMostRecentPlayerController().getMessageForPlayerNextTurn().isEmpty())
            return;

        Label label = new Label(
                GameState.getActiveGameMode().getMostRecentPlayerController().getMessageForPlayerNextTurn());
        label.getStyleClass().add("text-lg-warning");
        label.getStyleClass().add("title-wrap");

        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);

        this.bottomarea.getChildren().add(label);

        return;
    }

    private void renderPlayTurnScreen() {
        this.mainarea.getChildren().clear();

        if (GameState.getActiveGameMode().getMostRecentPlayerController().isKnockedOut()) {
            this.renderKnockedOutScreen();
            return;
        }

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
