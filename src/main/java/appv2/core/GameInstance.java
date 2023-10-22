package appv2.core;

import javafx.application.Platform;


public class GameInstance {

    public GameInstance() {
        super();

        appv2.core.View.run();
        return;
    }

    public static void quitApplication() {
        Platform.exit();
        return;
    }

    // region Getters and Setters

    // endregion Getters and Setters

}
