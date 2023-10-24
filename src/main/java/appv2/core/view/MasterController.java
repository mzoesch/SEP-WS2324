package appv2.core.view;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Objects;


public class MasterController {

    public static final String WIN_TITLE = "Love Letter @mzoesch";

    public static final int PREF_HEIGHT = 1_280;
    public static final int PREF_WIDTH = 720;

    public static final String MAIN_MENU = "main";
    public static final String RULES = "rules";
    public static final String GAME_INIT = "gameinit";
    public static final String GAME = "game";
    public static final String ROUND_ENDED = "roundended";
    public static final String GAME_ENDED = "gameended";
    public static final String PLAYERS_SCORE = "playersscore";
    public static final String DISCARDED_PILE = "discardedpile";

    private final Scene master;
    private final ArrayList<GameScene> screens;

    private String currentScreen;

    public MasterController(Scene master) {
        this.master = master;
        this.screens = new ArrayList<GameScene>();
        this.currentScreen = "";

        return;
    }

    public void addScreen(GameScene gameScene) {
        this.screens.add(gameScene);
        return;
    }

    public void removeScreen(String identifier) {
        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(identifier)) {
                this.screens.remove(screen);
                return;
            }
        }
        return;
    }

    public String activate(String identifier, boolean bKeepOldAlive) {
        String oldIdentifier = this.currentScreen;

        this.screens.forEach(gameScene -> {
            if (gameScene.getIdentifier().equals(identifier)) {
                this.master.setRoot(gameScene.getPane());
                this.currentScreen = identifier;
            }
        });

        if (Objects.equals(this.currentScreen, oldIdentifier))
            return "";

        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(oldIdentifier) && screen.isKillAfterUse()) {
                if (bKeepOldAlive)
                    return oldIdentifier;

                this.removeScreen(oldIdentifier);
                break;
            }
            continue;
        }

        return "";
    }

    // region Utility methods

    public static String getUniqueIdentifier(String prefix) {
        return String.format("%s-%s", prefix, UUID.randomUUID().toString());
    }

    // endregion Utility methods

    // region Getters and setters

    public Scene getScene() {
        return this.master;
    }

    public ArrayList<GameScene> getGameScenes() {
        return this.screens;
    }

    public GameScene getActiveGameScene() {
        for (GameScene screen : this.screens) {
            if (screen.getIdentifier().equals(this.currentScreen))
                return screen;
        }

        throw new RuntimeException("No active screen found.");
    }

    // endregion Getters and setters

}
