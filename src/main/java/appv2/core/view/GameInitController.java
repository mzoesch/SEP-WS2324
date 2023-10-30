package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;


/**
 * <p>Controller for the Game Init Screen.<br />
 * Handles the creation of new games and the addition of players to the game.</p>
 */
public class GameInitController {

    private static final int INDEX_OF_PLAYER_NAME_INPUT_FIELD = 0;

    @FXML private VBox playernamescontainer;
    @FXML private Label errormessagefield;

    /**
     * <p>Renders the given error message to the error message field.<br />
     * Pass an empty string to display no error message.</p>
     *
     * @param Message The error message to display.
     * @see GameInitController#errormessagefield
     */
    private void renderErrorMessage(String Message) {
        this.errormessagefield.setText(Message);
        return;
    }

    // region Utility methods for addPlayer()

    /**
     * <p>Removes the Player Add Button if existing.</p>
     */
    private void removePlayerAddButton() {
        if (this.playernamescontainer.getChildren().isEmpty())
            return;

        if (
            this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                instanceof Button
        )
            this.playernamescontainer.getChildren().remove(
                    this.playernamescontainer.getChildren().size() - 1);

        return;
    }

    /**
     * <p>Adds the Player Add Button if allowed and not already existing.</p>
     *
     * @see GameState#MAX_PLAYERS
     */
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

    /**
     * <p>Adds a new player name input field to the player names container. Also
     * adds the Remove Player Button as a child and its logic.</p>
     */
    private void addPlayerNameInputField() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hbox");

        TextField textField = new TextField();
        textField.setPromptText("Enter Player Name");
        textField.getStyleClass().add("player-name-text-field");

        Button button = new Button("X");
        button.getStyleClass().add("danger-btn");
        button.getStyleClass().add("remove-player-btn");
        button.setOnAction(actionEvent -> {
            if (this.getPlayerCount() <= GameState.MIN_PLAYERS) {
                this.renderErrorMessage("Can not remove player. Must have at least 2 players.");
                return;
            }
            this.playernamescontainer.getChildren().remove(hBox);

            this.addPlayerAddButton();
            return;
        });

        hBox.getChildren().add(textField);
        hBox.getChildren().add(button);

        this.playernamescontainer.getChildren().add(hBox);

        return;
    }

    // endregion Utility methods for addPlayer()

    /**
     * <p>Adds a new player name input field to the player names container.<br />
     */
    private void addPlayer() {
        if (this.getPlayerCount() >= GameState.MAX_PLAYERS)
            throw new RuntimeException("Something unexpected happened. Too many players.");

        this.removePlayerAddButton();
        this.addPlayerNameInputField();
        this.addPlayerAddButton();

        return;
    }

    /**
     * <p>Routes the user back to the main menu screen.</p>
     */
    @FXML
    protected void onCancelBtn() {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        View.killScreen(MasterController.GAME_INIT);

        return;
    }

    // region Utility methods for onPlayBtn()

    /**
     * <p>Gets the number of players currently defined in the player name text fields.<br />
     * We can't just count the children because we also render the Player Add Button in
     * this area as a child.</p>
     *
     * @return The number of players currently defined.
     */
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

    /**
     * <p>Gets the player name input field at the given index.</p>
     *
     * @param i The index of the player name input field to get.
     * @return The player name input field at the given index.
     */
    private TextField getPlayerNameTextField(int i) {
        return (
            (TextField)
                ((HBox) this.playernamescontainer.getChildren().get(i))
                    .getChildren().get(GameInitController.INDEX_OF_PLAYER_NAME_INPUT_FIELD)
        );
    }

    /**
     * <p>Checks if there are two player with the same name.</p>
     *
     * @param i The index of the player name input field to check.
     * @return True if the player name input field at the given index is unique, false otherwise.
     */
    private boolean isPlayerNameTextFieldUnique(int i) {
        for (int j = i + 1; j < this.playernamescontainer.getChildren().size(); j++) {
            if (this.playernamescontainer.getChildren().get(j) instanceof Button)
                continue;

            if (this.getPlayerNameTextField(j).getText().isEmpty())
                return false;

            if (this.getPlayerNameTextField(i).getText().equals(this.getPlayerNameTextField(j).getText()))
                return false;

            continue;
        }

        return true;
    }

    /**
     * <p>Checks if any player name given in the player name text fields is too long.</p>
     *
     * @return True if a player name is too long, false otherwise.
     */
    private boolean isAPlayerNameToLong() {
        for (int i = 0; i < this.playernamescontainer.getChildren().size(); i++) {
            if (this.playernamescontainer.getChildren().get(i) instanceof Button)
                continue;

            if (this.getPlayerNameTextField(i).getText().length() > GameState.MAX_PLAYER_NAME_LENGTH)
                return true;

            continue;
        }

        return false;
    }

    /**
     * <p>Checks if the player count and player names given are valid.</p>
     *
     * @return True if the form is valid, false otherwise.
     */
    private boolean isFormValid() {
        if (this.getPlayerCount() < GameState.MIN_PLAYERS)
            return false;
        if (this.getPlayerCount() > GameState.MAX_PLAYERS)
            return false;

        for (int i = 0; i < this.playernamescontainer.getChildren().size(); i++) {
            if (this.playernamescontainer.getChildren().get(i) instanceof Button)
                continue;

            if (this.getPlayerNameTextField(i).getText().isEmpty())
                return false;

            if (this.getPlayerNameTextField(i).getText().length() > GameState.MAX_PLAYER_NAME_LENGTH)
                return false;

            if (!this.isPlayerNameTextFieldUnique(i))
                return false;

            continue;
        }

        return true;
    }

    // endregion Utility methods for onPlayBtn()

    @FXML
    protected void onPlayBtn() {
        this.renderErrorMessage("");

        if (!this.isFormValid()) {
            if (this.isAPlayerNameToLong()) {
                this.renderErrorMessage("Can not start game. A player name is too long.");
                return;
            }

            this.renderErrorMessage("Can not start game. Ensure all fields are filled and unique.");
            return;
        }

        String[] playerNames = new String[this.getPlayerCount()];
        for (int i = 0; i < this.getPlayerCount(); i++)
            playerNames[i] = this.getPlayerNameTextField(i).getText();

        GameState.initializeNewGame(this.getPlayerCount(), playerNames);

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

    /**
     * <p>Initializes the Game Init Screen and adds the default number of players.</p>
     *
     * @see GameState#DEFAULT_PLAYERS
     */
    @FXML
    private void initialize() {
        this.renderErrorMessage("");

        for (int i = 0; i < GameState.DEFAULT_PLAYERS; i++)
            this.addPlayer();

        return;
    }

}
