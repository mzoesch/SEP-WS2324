package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;


public class GameInitController {

    private static final int INDEX_OF_PLAYER_NAME_INPUT_FIELD = 0;

    @FXML private VBox playernamescontainer;
    @FXML private Label errormessagefield;

    private void removePlayerAddButton() {
        if (this.playernamescontainer.getChildren().isEmpty())
            return;

        if (
                this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                        instanceof Button
        )
            this.playernamescontainer.getChildren().remove(this.playernamescontainer.getChildren().size() - 1);

        return;
    }

    private void addPlayerAddButton() {
        if (this.getPlayerCount() >= GameState.MAX_PLAYERS)
            return;

        if (this.playernamescontainer.getChildren().isEmpty())
            return;

        if (this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                instanceof Button)
            return;

        Button button = new Button("Add Player");
        button.getStyleClass().add("secondary-btn");
        button.getStyleClass().add("add-player-btn");

        button.setOnAction(actionEvent -> {
            this.addPlayer();
            return;
        });

        this.playernamescontainer.getChildren().add(button);

        return;
    }

    private void addPlayerNameInputField() {
        TextField textField = new TextField();
        textField.setPromptText("Enter Player Name");
        textField.getStyleClass().add("player-name-text-field");

        Button button = new Button("X");
        button.getStyleClass().add("danger-btn");
        button.getStyleClass().add("remove-player-btn");

        HBox hBox = new HBox(textField, button);
        hBox.getStyleClass().add("hbox");

        button.setOnAction(actionEvent -> {
            if (this.getPlayerCount() <= GameState.MIN_PLAYERS) {
                this.renderErrorMessage("Can not remove player. Must have at least 2 players.");
                return;
            }
            this.playernamescontainer.getChildren().remove(hBox);

            this.addPlayerAddButton();
            return;
        });

        this.playernamescontainer.getChildren().add(hBox);

        return;
    }

    private void addPlayer() {
        if (this.getPlayerCount() >= GameState.MAX_PLAYERS)
            throw new RuntimeException("Something unexpected happened.");

        this.removePlayerAddButton();
        this.addPlayerNameInputField();
        this.addPlayerAddButton();

        return;
    }

    @FXML
    private void initialize() {
        this.renderErrorMessage("");

        for (int i = 0; i < GameState.DEFAULT_PLAYERS; i++)
            this.addPlayer();

        return;
    }

    @FXML
    protected void onCancelBtn() {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        View.killScreen(MasterController.GAME_INIT);
        return;
    }

    private boolean isFormValid() {
        if (this.getPlayerCount() < GameState.MIN_PLAYERS)
            return false;
        if (this.getPlayerCount() > GameState.MAX_PLAYERS)
            return false;

        for (int i = 0; i < this.playernamescontainer.getChildren().size(); i++) {
            if (this.playernamescontainer.getChildren().get(i)
                    instanceof Button)
                continue;

            if (
                (
                    (TextField)
                        ((HBox) this.playernamescontainer.getChildren().get(i))
                        .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                ).getText().isEmpty()
            )
                return false;

            if (
                    (
                            (TextField)
                                    ((HBox) this.playernamescontainer.getChildren().get(i))
                                            .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                    ).getText().length() > GameState.MAX_PLAYER_NAME_LENGTH
            )
                return false;

            for (int j = i + 1; j < this.playernamescontainer.getChildren().size(); j++) {
                if (this.playernamescontainer.getChildren().get(j) instanceof Button)
                    continue;

                if (
                    (
                        (TextField)
                            ((HBox) this.playernamescontainer.getChildren().get(j))
                            .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                    ).getText().isEmpty()
                )
                    return false;

                if (
                    (
                        (TextField)
                            ((HBox) this.playernamescontainer.getChildren().get(i))
                            .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                    ).getText().equals(
                    (
                        (TextField)
                            ((HBox) this.playernamescontainer.getChildren().get(j)
                            ).getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                    ).getText()
                    )
                )
                    return false;

                continue;
            }

            continue;
        }

        return true;
    }

    private boolean isAPlayerNameToLong() {
        for (int i = 0; i < this.playernamescontainer.getChildren().size(); i++) {
            if (this.playernamescontainer.getChildren().get(i)
                    instanceof Button)
                continue;

            if (
                    (
                            (TextField)
                                    ((HBox) this.playernamescontainer.getChildren().get(i))
                                            .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
                    ).getText().length() > GameState.MAX_PLAYER_NAME_LENGTH
            )
                return true;

            continue;
        }

        return false;
    }

    private void renderErrorMessage(String Message) {
        this.errormessagefield.setText(Message);
        return;
    }

    @FXML
    protected void onPlayBtn() {
        if (!this.isFormValid()) {
            if (this.isAPlayerNameToLong()) {
                this.renderErrorMessage("Can not start game. A player name is too long.");
                return;
            }
            this.renderErrorMessage("Can not start game. Ensure all fields are filled and unique.");
            return;
        }

        this.renderErrorMessage("");
        String[] playerNames = new String[this.getPlayerCount()];
        for (int i = 0; i < this.getPlayerCount(); i++) {
            playerNames[i] = (
                    (TextField)
                            ((HBox) this.playernamescontainer.getChildren().get(i)
                            ).getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
            ).getText();

            continue;
        }

        GameState.initializeNewGame(this.getPlayerCount(), playerNames);
        View.renderNewScreen(
                new GameScene(
                        MasterController.getUniqueIdentifier(
                                String.format("%s-player%s",
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

    private int getPlayerCount() {
        if (this.playernamescontainer.getChildren().size() > 4)
            throw new RuntimeException("Something unexpected happened. Too many players.");

        if (this.playernamescontainer.getChildren().isEmpty())
            return 0;

        if (this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                instanceof Button)
            return this.playernamescontainer.getChildren().size() - 1;
        else
            return this.playernamescontainer.getChildren().size();
    }

}
