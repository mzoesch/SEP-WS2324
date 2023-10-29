package appv2.core;

import javafx.application.Platform;


/**
 * <p>Implements high-level methods relevant to the entirety of the
 * life-cycle of the application.</p>
 */
public final class GameInstance {

    /**
     * <p>Utility class. No instances allowed.</p>
     *
     * @throws IllegalStateException If an instance of this class is created.
     */
    private GameInstance() {
        super();
        throw new IllegalStateException("Utility class");
    }


    /**
     * <p>Starts the Graphical User Interface of the application.</p>
     */
    public static void run() {
        appv2.core.View.run();
        return;
    }

    /**
     * <p>Kills the application.</p>
     */
    public static void quitApplication() {
        Platform.exit();
        System.exit(0);
        return;
    }

}
