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


/**
 * <p>Controller for the Game Screen.<br />
 * Handles the dynamic rendering of the Game Screen and the logic behind it.</p>
 */
public class GameController {

    /**
     * <p>Main Title Label of the Game Screen.</p>
     */
    @FXML private Label turntitle;
    /**
     * <p>The Middle Area of the Game Screen where we display Card effect choices.</p>
     */
    @FXML private AnchorPane mainarea;
    /**
     * <p>The Left Area of the Game Screen where we display the Player's Discarded Pile.</p>
     */
    @FXML private AnchorPane leftarea;
    /**
     * <p>The Right Area of the Game Screen where we display
     * overall usefully game information for the player.</p>
     */
    @FXML private AnchorPane rightarea;
    /**
     * <p>The Bottom Area of the Game Screen where we display the Player's Cards.</p>
     */
    @FXML private AnchorPane bottomarea;

    // region Utility methods for playCard()

    /**
     * <p>Standard method to render the title of the Game Screen when a card is played.</p>
     *
     * @param label Label to render.
     * @see #playCard(boolean)
     */
    private void renderStandardTitleOnPlayCard(Label label) {
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

    /**
     * <p>Base title creation when choosing a player.</p>
     *
     * @param bHandCard Whether the card to play is a hand card or a table card.
     * @param stdoutPipeline The stdout pipeline to get the information from.
     * @return The just created title label.
     * @see #renderStandardChoosePlayerTitle(boolean, StringBuilder) 
     * @see #playCard(boolean)
     */
    private Label baseChoosePlayerTitle(boolean bHandCard, StringBuilder stdoutPipeline) {
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

        return label;
    }

    /**
     * <p>Standard method to render the title of the Game Screen when a card's
     * side effect needs a player to be chosen.</p>
     *
     * @param bHandCard Whether the card to play is a hand card or a table card.
     * @param stdoutPipeline The stdout pipeline to get the information from.
     * @see #playCard(boolean)
     */
    private void renderStandardChoosePlayerTitle(boolean bHandCard, StringBuilder stdoutPipeline) {
        Label label = baseChoosePlayerTitle(bHandCard, stdoutPipeline);
        AnchorPane.setBottomAnchor(label, 0.0);

        this.mainarea.getChildren().add(label);

        return;
    }

    /**
     * <p>Renders a label with an error message in the main area.</p>
     *
     * @param labelErr The label to render.
     */
    private void renderErrorInMainArea(Label labelErr) {
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

    /**
     * <p>Gets the return code fo a callback of a played card.</p>
     *
     * @param bHandCard Whether the card to play is a hand card or a table card.
     * @param stderrPipeline The stderr pipeline of the card.
     * @param stdoutPipeline The stdout pipeline of the card.
     * @param finalI The index of the player to choose.
     * @param args The arguments to pass to the callback.
     * @return The return code of the card's callback.
     * @see appv2.cards.ACard#callback(PlayerController, PlayerController, StringBuilder, StringBuilder, String[])
     */
    private int getReturnCodeFromCallback(
            boolean bHandCard,
            StringBuilder stderrPipeline,
            StringBuilder stdoutPipeline,
            int finalI,
            String[] args
    ) {
        return bHandCard
                ? GameState.getActiveGameMode().getMostRecentPlayerController().getHandCard().callback(
                GameState.getActiveGameMode().getMostRecentPlayerController(),
                GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                stdoutPipeline,
                stderrPipeline,
                args
        )
                : GameState.getActiveGameMode().getMostRecentPlayerController().getTableCard().callback(
                GameState.getActiveGameMode().getMostRecentPlayerController(),
                GameState.getActiveGameMode().getPlayerControllerByID(finalI),
                stdoutPipeline,
                stderrPipeline,
                args
        );
    }

    /**
     * <p>Standard behaviour of the return codes from a card's callback.</p>
     *
     * @param button The button to disable if failed.
     * @param finalI The index of the player to choose.
     * @param RC The return code of the card's callback.
     * @param stdoutPipeline The stdout pipeline to get the information from.
     * @param stderrPipeline The stderr pipeline to get the information from.
     * @return Whether the return code was handled or not.
     */
    private boolean isStandardPlayerButtonBehaviourWhenChoosingValid(
            Button button,
            int finalI,
            int RC,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    ) {
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

            return true;
        }

        if (RC == ACard.RC_ERR) {
            Label labelErr = new Label(String.format("You have chosen %s but it failed.\n%s",
                GameState.getActiveGameMode().getPlayerControllerByID(finalI).getPlayerName(),
                stderrPipeline.toString()
            ));
            this.renderErrorInMainArea(labelErr);

            button.setDisable(true);

            return true;
        }

        return false;
    }

    /**
     * <p>Checks if the return code of a card's callback is
     * RC_OK_PLAYER_KNOCKED_OUT and handles it if so.</p>
     *
     * @param stdoutPipeline The stdout pipeline to get the information from.
     * @param RC The return code of the card's callback.
     * @return Whether the return code was handled or not.
     */
    private boolean isStandardPlayerButtonBehaviourWhenChoosingValidOnlyKnockedOut(
            StringBuilder stdoutPipeline,
            int RC
    ) {
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

            return true;
        }

        return false;
    }

    /**
     * <p>Standard behaviour if the number text field is invalid
     * (only used if the card's callback requires a number text field).</p>
     *
     * @param labelErr The label to render.
     * @see #playCard(boolean)
     */
    private void standardBehaviourIfNumberTextFieldIsInvalid(Label labelErr) {
        this.renderErrorInMainArea(labelErr);
        return;
    }

    // endregion Utility methods for playCard()

    /**
     * <p>Handles the GUI-Logic if the player is playing a card.<br />
     * This also includes the rendering of the side effects of the cards.</p>
     *
     * @param bHandCard Whether the card to play is a hand card or a table card.
     */
    private void playCard(boolean bHandCard) {
        StringBuilder stderrPipeline = new StringBuilder();
        StringBuilder stdoutPipeline = new StringBuilder();
        ECardResponse res = GameState.getActiveGameMode()
                .getMostRecentPlayerController()
                .playCard(bHandCard, stdoutPipeline, stderrPipeline);

        switch (res) {
            case RC_OK -> {
                Label label = new Label(
                    String.format(
                        "You played %s\n%s",
                        GameState.getActiveGameMode().getMostRecentPlayerController()
                            .getDiscardedCardsPile()[GameState.getActiveGameMode().getMostRecentPlayerController()
                            .getDiscardedCardsPile().length - 1].getName(),
                        stdoutPipeline.toString()
                    )
                );
                renderStandardTitleOnPlayCard(label);

                return;
            }

            case RC_OK_KNOCKED_OUT -> {
                Label label = new Label(
                    String.format(
                        "You played %s and knocked yourself out",
                        GameState.getActiveGameMode().getMostRecentPlayerController()
                            .getDiscardedCardsPile()
                            [GameState.getActiveGameMode().getMostRecentPlayerController()
                                .getDiscardedCardsPile().length - 1]
                            .getName()
                    )
                );
                renderStandardTitleOnPlayCard(label);

                return;
            }

            case RC_ERR -> {
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

            case RC_CHOOSE_ANY_PLAYER -> {
                this.renderStandardChoosePlayerTitle(bHandCard, stdoutPipeline);

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

                        int RC = this.getReturnCodeFromCallback(
                                bHandCard,
                                stderrPipeline,
                                stdoutPipeline,
                                finalI,
                                null
                        );

                        if (
                            this.isStandardPlayerButtonBehaviourWhenChoosingValid(
                                button,
                                finalI,
                                RC,
                                stdoutPipeline,
                                stderrPipeline
                            )
                        )
                            return;

                        throw new RuntimeException("Unhandled return code from ACard.callback().");
                    });

                    hbox.getChildren().add(button);
                    continue;
                }

                this.mainarea.getChildren().add(hbox);

                return;
            }

            case RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED -> {
                this.renderStandardChoosePlayerTitle(bHandCard, stdoutPipeline);

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

                        int RC = this.getReturnCodeFromCallback(
                                bHandCard,
                                stderrPipeline,
                                stdoutPipeline,
                                finalI,
                                null
                        );

                        if (
                            this.isStandardPlayerButtonBehaviourWhenChoosingValid(
                                button,
                                finalI,
                                RC,
                                stdoutPipeline,
                                stderrPipeline
                            )
                        )
                            return;

                        if (
                            isStandardPlayerButtonBehaviourWhenChoosingValidOnlyKnockedOut(stdoutPipeline, RC)
                        )
                            return;

                        throw new RuntimeException("Unhandled return code from ACard.callback().");
                    });

                    hbox.getChildren().add(button);
                    continue;
                }

                this.mainarea.getChildren().add(hbox);

                return;
            }

            case RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER -> {
                Label label = baseChoosePlayerTitle(bHandCard, stdoutPipeline);
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
                            this.standardBehaviourIfNumberTextFieldIsInvalid(labelErr);
                            return;
                        }

                        // We can do this because of the regex in the listener.
                        int guessAffection = Integer.parseInt(textField.getText());
                        if (guessAffection < 2 || guessAffection > 8) {
                            Label labelErr = new Label("You must enter a number between 2 and 8.");
                            this.standardBehaviourIfNumberTextFieldIsInvalid(labelErr);
                            return;
                        }

                        int RC = this.getReturnCodeFromCallback(
                            bHandCard,
                            stderrPipeline,
                            stdoutPipeline,
                            finalI,
                            new String[] { String.valueOf(guessAffection) }
                        );

                        if (
                            this.isStandardPlayerButtonBehaviourWhenChoosingValid(
                                button,
                                finalI,
                                RC,
                                stdoutPipeline,
                                stderrPipeline
                            )
                        )
                            return;

                        if (
                            isStandardPlayerButtonBehaviourWhenChoosingValidOnlyKnockedOut(stdoutPipeline, RC)
                        )
                            return;

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

            default -> throw new RuntimeException("Unhandled return code from PlayerController.playCard().");
        }
    }

    // region Render Utils

    /**
     * <p>Resets the Game Screen to its initial state.</p>
     */
    private void clearScreen() {
        this.mainarea.getChildren().clear();
        this.leftarea.getChildren().clear();
        this.rightarea.getChildren().clear();
        this.bottomarea.getChildren().clear();

        return;
    }

    /**
     * <p>Renders the main title on the player's turn screen.</p>
     *
     * @param playerName The name of the player to set the turn title to.
     */
    private void setTurnTitle(String playerName) {
        this.turntitle.setText(String.format("%s's Turn", playerName));
        return;
    }

    // region Utility methods for renderDiscardedPileAreaScreen()

    /**
     * <p>Renders the discarded pile in the given VBox.</p>
     *
     * @param vbox The VBox to render the discarded pile in.
     */
    private static void renderDiscardPile(VBox vbox) {
        if (GameState.getActiveGameMode().getMostRecentPlayerController().getDiscardedCardsPile().length == 0) {
            Label discardedCard = new Label("You have not yet discarded any cards");
            discardedCard.getStyleClass().add("text-base");

            vbox.getChildren().add(discardedCard);

            return;
        }

        for (
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

        return;
    }

    /**
     * <p>Renders the show all discarded piles button in the given VBox and its logic.</p>
     *
     * @param vbox The VBox to render the button in.
     */
    private static void renderShowAllDiscardedPilesButton(VBox vbox) {
        Button button = new Button("Show discarded pile of all players.");
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
        button.getStyleClass().add("secondary-mini-btn");

        vbox.getChildren().add(button);

        return;
    }

    // endregion Utility methods for renderDiscardedPileAreaScreen()

    /**
     * <p>Renders the top left area of the Game Screen.</p>
     */
    private void renderDiscardedPileAreaScreen() {
        this.leftarea.getChildren().clear();

        VBox vbox = new VBox();

        Label label = new Label("Your discarded cards pile.");
        label.getStyleClass().add("text-base");
        vbox.getChildren().add(label);

        GameController.renderDiscardPile(vbox);
        GameController.renderShowAllDiscardedPilesButton(vbox);

        this.leftarea.getChildren().add(vbox);

        return;
    }

    // region Utility methods for renderGameStateOverviewAreaScreen()

    /**
     * <p>Renders the show score button in the given VBox and its logic.</p>
     *
     * @param vbox The VBox to render the button in.
     */
    private static void renderShowScoreButton(VBox vbox) {
        Button showScoreButton = new Button("Show Score Of All Players");
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
        showScoreButton.getStyleClass().add("secondary-mini-btn");

        vbox.getChildren().add(showScoreButton);

        return;
    }

    // endregion Utility methods for renderGameStateOverviewAreaScreen()

    /**
     * <p>Renders the top right area of the Game Screen.</p>
     */
    private void renderGameStateOverviewAreaScreen() {
        this.rightarea.getChildren().clear();

        VBox vbox = new VBox();
        vbox.setId("right-area-vbox");

        Label remainingPlayersLabel = new Label(
            String.format(
                "%d/%d players remaining",
                GameState.getActiveGameMode().getRemainingPlayerCount(),
                GameState.getActiveGameMode().getPlayerCount()
            )
        );
        remainingPlayersLabel.getStyleClass().add("text-base");
        vbox.getChildren().add(remainingPlayersLabel);

        GameController.renderShowScoreButton(vbox);

        Label remainingCardsLabel = new Label(
            String.format(
                "%d %s cards remaining",
                GameState.getActiveGameMode().getRemainingTableCardsCount(),
                GameState.getActiveGameMode().isHiddenCardAvailable() ? "(+1)" : ""
            )
        );
        remainingCardsLabel.getStyleClass().add("text-base");
        vbox.getChildren().add(remainingCardsLabel);

        this.rightarea.getChildren().add(vbox);

        return;
    }

    // region Utility methods for renderChoiceAreaScreen()

    private boolean areExaminingCardsAvailable() {
        return !GameState.getActiveGameMode().getExaminingCards().isEmpty();
    }

    /**
     * <p>Renders the examining cards in the given VBox.</p>
     *
     * @param vbox The VBox to render the examining cards in.
     * @see #renderChoiceAreaScreen()
     */
    private void renderExaminingCards(VBox vbox) {
        Label examiningTitle = new Label("The current examining cards are:");
        examiningTitle.getStyleClass().add("text-lg");

        HBox hbox = new HBox();
        hbox.setId("examining-hbox");

        AnchorPane.setBottomAnchor(hbox, 0.0);
        AnchorPane.setLeftAnchor(hbox, 0.0);
        AnchorPane.setRightAnchor(hbox, 0.0);

        for (int i = 0; i < GameState.getActiveGameMode().getExaminingCards().size(); i++) {
            Label examiningCardLabel = new Label(
                    GameState.getActiveGameMode().getExaminingCards().get(i).getAsString());
            examiningCardLabel.getStyleClass().add("text-lg");

            hbox.getChildren().add(examiningCardLabel);
            continue;
        }

        vbox.getChildren().addAll(examiningTitle, hbox);

        return;
    }

    /**
     * <p>Checks if the screen should show an buttons.</p>
     *
     * @return True if we should not show any buttons, false otherwise.
     *         This is the case when the player is currently playing a card effect
     *         (e.g., guessing a card).
     */
    private static boolean showNoButtons() {
        return GameState.getActiveGameMode().getMostRecentPlayerController().isPlaying();
    }

    /**
     * <p>Checks if the screen should show the end turn button.</p>
     *
     * @param PC The player controller to check.
     * @return True if the player is able to end the turn, false otherwise.
     */
    private static boolean showEndTurnButton(PlayerController PC) {
        return PC.hasPlayedCard() && !PC.isPlaying();
    }

    /**
     * <p>Checks if the screen should show the play hand card button.</p>
     *
     * @param PC The player controller to check.
     * @return True if the player is able to play the hand card, false otherwise.
     */
    private static boolean showHandCardButton(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getHandCard() != null && !PC.isPlaying();
    }

    /**
     * <p>Checks if the screen should show the play table card button.</p>
     *
     * @param PC The player controller to check.
     * @return True if the player is able to play the table card, false otherwise.
     */
    private static boolean showTableCardButton(PlayerController PC) {
        return !PC.hasPlayedCard() && PC.getTableCard() != null && !PC.isPlaying();
    }

    /**
     * <p>Renders the end turn button in the given HBox.</p>
     *
     * @param hbox The HBox to render the end turn button in.
     * @see #renderChoiceAreaScreen()
     */
    private static void renderEndTurnButton(HBox hbox) {
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
                    MasterController.getUniqueIdentifierForAPlayersTurn(
                        GameState.getActiveGameMode().getMostRecentPlayerController()),
                    View.loadFXML(View.PATH_TO_GAME),
                    true,
                    null
                ),
                false
            );

            return;
        });

        hbox.getChildren().add(endTurn);

        return;
    }

    /**
     * <p>Renders the hand card button in the given HBox.</p>
     *
     * @param hbox The HBox to render the hand card button in.
     * @see #renderChoiceAreaScreen()
     */
    private void renderHandCardButton(HBox hbox) {
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

        hbox.getChildren().add(handCardButton);

        return;
    }

    /**
     * <p>Renders the table card button in the given HBox.</p>
     *
     * @param hbox The HBox to render the table card button in.
     * @see #renderChoiceAreaScreen()
     */
    private void renderTableCardButton(HBox hbox) {
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

        hbox.getChildren().add(tableCardButton);

        return;
    }

    /**
     * <p>Renders the player choice buttons in the given VBox.<br />
     * This includes the buttons to play the hand card and the table card if available.
     * Also includes the button to end the turn.</p>
     *
     * @param vbox The VBox to render the player choice buttons in.
     * @see #renderChoiceAreaScreen()
     */
    private void renderPlayerChoiceButtons(VBox vbox) {
        HBox btnContainer = new HBox();
        btnContainer.setId("hbox-bottom-btn-container");

        AnchorPane.setBottomAnchor(btnContainer, 0.0);
        AnchorPane.setLeftAnchor(btnContainer, 0.0);
        AnchorPane.setRightAnchor(btnContainer, 0.0);

        if (GameController.showNoButtons()) {
            this.bottomarea.getChildren().clear();
            return;
        }
        if (GameController.showEndTurnButton(GameState.getActiveGameMode().getMostRecentPlayerController()))
            GameController.renderEndTurnButton(btnContainer);
        if (GameController.showHandCardButton(GameState.getActiveGameMode().getMostRecentPlayerController()))
            this.renderHandCardButton(btnContainer);
        if (GameController.showTableCardButton(GameState.getActiveGameMode().getMostRecentPlayerController()))
            this.renderTableCardButton(btnContainer);

        vbox.getChildren().add(btnContainer);

        return;
    }

    // endregion Utility methods for renderChoiceAreaScreen()

    /**
     * <p>Renders the choice area screen.<br />
     * Shows the cards that are playable by the current player and the buttons to play them.
     * Also shows the cards that can be examined by the current player if available.</p>
     */
    private void renderChoiceAreaScreen() {
        this.bottomarea.getChildren().clear();

        VBox vbox = new VBox();
        vbox.setId("vbox-bottom-area");

        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

        if (this.areExaminingCardsAvailable())
            this.renderExaminingCards(vbox);

        this.renderPlayerChoiceButtons(vbox);

        this.bottomarea.getChildren().add(vbox);

        return;
    }

    // endregion Render Utils

    /**
     * <p>Renders the screen for the player when they have been knocked out.</p>
     */
    private void renderKnockedOutScreen() {
        this.clearScreen();

        this.renderDiscardedPileAreaScreen();
        this.renderGameStateOverviewAreaScreen();
        this.renderChoiceAreaScreen();

        Label label =
                new Label(
                        "You have been knocked out of this round.\n"
                        + "You may only end your turn now. You will be able to play again next round."
                );
        label.getStyleClass().add("text-lg-warning");
        label.getStyleClass().add("title-wrap");

        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);

        this.mainarea.getChildren().add(label);

        return;
    }

    /**
     * <p>Renders the standard screen for the player when they are playing their turn.</p>
     */
    private void renderStandardPlayTurnScreen() {
        this.clearScreen();

        this.renderDiscardedPileAreaScreen();
        this.renderGameStateOverviewAreaScreen();
        this.renderChoiceAreaScreen();

        return;
    }

    /**
     * <p>Decides which player screen to render.</p>
     */
    private void renderPlayTurnScreen() {
        this.mainarea.getChildren().clear();

        if (GameState.getActiveGameMode().getMostRecentPlayerController().isKnockedOut()) {
            this.renderKnockedOutScreen();
            return;
        }

        this.renderStandardPlayTurnScreen();

        return;
    }

    // region Utility methods for renderFirstPlayerInteraction()

    private boolean hasActivePlayerAMessage() {
        return !(GameState.getActiveGameMode().getMostRecentPlayerController()
                    .getMessageForPlayerNextTurn() == null
                || GameState.getActiveGameMode().getMostRecentPlayerController()
                    .getMessageForPlayerNextTurn().isEmpty());
    }

    // endregion Utility methods for renderFirstPlayerInteraction()

    /**
     * <p>Renders the first player interaction screen of their turn.</p>
     */
    private void renderFirstPlayerInteraction() {
        this.clearScreen();

        Button button = new Button("Press To Play Your Turn");
        button.setOnAction(actionEvent -> {
            this.renderPlayTurnScreen();
            return;
        });

        button.getStyleClass().add("primary-btn");

        AnchorPane.setLeftAnchor(button, 60.0);
        AnchorPane.setRightAnchor(button, 60.0);
        AnchorPane.setTopAnchor(button, 160.0);
        AnchorPane.setBottomAnchor(button, 160.0);

        this.mainarea.getChildren().add(button);

        if (this.hasActivePlayerAMessage()) {
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

        return;
    }

    /**
     * <p>Called by JavaFX when the Game Scene is initialized.<br />
     * Handles the rendering of the player interaction screen before
     * the player's turn and prepares the player for their turn.</p>
     *
     * @see appv2.core.PlayerController#prepareForNextTurn()
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @FXML
    private void initialize() {
        GameState.getActiveGameMode().getMostRecentPlayerController().prepareForNextTurn();

        this.setTurnTitle(
                GameState.getActiveGameMode().getMostRecentPlayerController().getPlayerName());
        this.renderFirstPlayerInteraction();

        return;
    }

}
