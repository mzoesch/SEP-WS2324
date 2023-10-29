package appv2.core;

import javafx.application.Platform;


/**
 * <p>Implements high-level methods relevant to the entirety of the
 * life-cycle of the application.</p>
 */
public final class GameInstance {

    private static int exitCode = 0;

    /**
     * <p>Utility class. No instances allowed.</p>
     *
     * @throws IllegalStateException If an instance of this class is created.
     */
    private GameInstance() throws IllegalStateException {
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
        return;
    }

    // region Getters and Setters

    /**
     * <p>Sets the exit code of this application.</p>
     *
     * @param exitCode Exit code to set.
     */
    public static void setExitCode(int exitCode) {
        GameInstance.exitCode = exitCode;
        return;
    }

    /**
     * <p>Gets the exit code of this application.</p>
     *
     * @return Exit code of the application.
     */
    public static int getExitCode() {
        return GameInstance.exitCode;
    }

    // endregion Getters and Setters

}
