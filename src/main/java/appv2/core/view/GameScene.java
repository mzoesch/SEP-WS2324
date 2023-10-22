package appv2.core.view;

import javafx.scene.layout.Pane;

public class GameScene {

    private final String identifier;
    private final Pane pane;
    private final boolean killAfterUse;
    private String args;

    public GameScene(String identifier, Pane pane, boolean killAfterUse, String args) {
        this.identifier = identifier;
        this.pane = pane;
        this.killAfterUse = killAfterUse;
        this.args = args;

        return;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Pane getPane() {
        return this.pane;
    }

    public boolean isKillAfterUse() {
        return this.killAfterUse;
    }

    public String getArgs() {
        return this.args;
    }

    public void setArgs(String args) {
        this.args = args;
        return;
    }

}
