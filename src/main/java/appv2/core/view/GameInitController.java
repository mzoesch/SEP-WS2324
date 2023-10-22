package appv2.core.view;

import appv2.core.View;
import appv2.core.GameState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;


public class GameInitController {

    private static int tCount = 0;

    @FXML private VBox playernamescontainer;
    @FXML private Label errormessagefield;

    private void removePlayerAddButton() {
        if (this.playernamescontainer.getChildren().isEmpty())
            return;

        if (this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                instanceof Button)
            this.playernamescontainer.getChildren().remove(this.playernamescontainer.getChildren().size() - 1);

        return;
    }

    private void addPlayerAddButton() {
        if (this.getPlayerCount() >= GameState.MAX_PLAYERS)
            return;
        if (!this.playernamescontainer.getChildren().isEmpty() && this.playernamescontainer.getChildren().get(this.playernamescontainer.getChildren().size() - 1)
                instanceof Button)
            return;

        Button button = new Button();
        button.setText("Add Player");
        button.setOnAction(actionEvent -> {
            this.addPlayer();
            return;
        });

        this.playernamescontainer.getChildren().add(button);

        return;
    }

    private void addPlayerNameInputField() {
        Button button = new Button();
        button.setText("X");
        TextField textField = new TextField();
        textField.setPromptText("Enter Player Name");
        HBox hBox = new HBox(button, textField);

        // TODO: TO REMOVE
        textField.setText(String.valueOf(GameInitController.tCount++));

        button.setOnAction(actionEvent -> {
            if (this.getPlayerCount() <= GameState.MIN_PLAYERS)
                return;
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
        this.errormessagefield.setText("");

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
            if (this.playernamescontainer.getChildren().get(i) instanceof Button)
                continue;

            if (((TextField) ((HBox) this.playernamescontainer.getChildren().get(i)).getChildren().get(1)).getText().isEmpty())
                return false;

            for (int j = i + 1; j < this.playernamescontainer.getChildren().size(); j++) {
                if (this.playernamescontainer.getChildren().get(j) instanceof Button)
                    continue;

                if (((TextField) ((HBox) this.playernamescontainer.getChildren().get(j)).getChildren().get(1)).getText().isEmpty())
                    return false;

                if (((TextField) ((HBox) this.playernamescontainer.getChildren().get(i)).getChildren().get(1)).getText().equals(((TextField) ((HBox) this.playernamescontainer.getChildren().get(j)).getChildren().get(1)).getText()))
                    return false;

                continue;
            }

            continue;
        }

        return true;
    }

    private void renderErrorMessage(String Message) {
        this.errormessagefield.setText(Message);
        return;
    }

    @FXML
    protected void onPlayBtn() {
        if (!this.isFormValid()) {
            this.renderErrorMessage("Can not start game. Ensure all fields are filled and unique.");
            return;
        }

        this.errormessagefield.setText("");
        int playerCount = this.getPlayerCount();
        String[] playerNames = new String[playerCount];
        playerNames[0] = ((TextField) ((HBox) this.playernamescontainer.getChildren().get(0)).getChildren().get(1)).getText();
        if (playerCount > 1)
            playerNames[1] = ((TextField) ((HBox) this.playernamescontainer.getChildren().get(1)).getChildren().get(1)).getText();
        if (playerCount > 2)
            playerNames[2] = ((TextField) ((HBox) this.playernamescontainer.getChildren().get(2)).getChildren().get(1)).getText();
        if (playerCount > 3)
            playerNames[3] = ((TextField) ((HBox) this.playernamescontainer.getChildren().get(3)).getChildren().get(1)).getText();

        GameState.initializeNewGame(playerCount, playerNames);
        View.renderNewScreen(MasterController.GAME, View.PATH_TO_GAME);
        View.killScreen(MasterController.GAME_INIT);
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
