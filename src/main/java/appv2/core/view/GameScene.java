package appv2.core.view;

import javafx.scene.layout.Pane;

public class GameScene {

    private final String identifier;
    private final Pane pane;
    private final boolean killAfterUse;
    private String args;

    public GameScene(String identifier, Pane pane, boolean killAfterUse, String fallback) {
        this.identifier = identifier;
        this.pane = pane;
        this.killAfterUse = killAfterUse;
        this.args = fallback;

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

    public String getFallback() {
        return this.args;
    }

    public void setFallback(String args) {
        this.args = args;
        return;
    }

}
