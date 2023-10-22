package appv2.core.view;

import javafx.scene.layout.Pane;

public record GameScene(String identifier, Pane pane, boolean killAfterUse) {

    public GameScene(String identifier, Pane pane, boolean killAfterUse) {
        this.identifier = identifier;
        this.pane = pane;
        this.killAfterUse = killAfterUse;

        return;
    }

}
