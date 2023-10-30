package appv2.core;


/**
 * <p> High-level supervisor for the entirety of a game. It manages the creation, destruction and activation of
 * Game Modes.</p>
 *
 * @see appv2.core.GameMode
 * @see appv2.core.view.GameInitController
 */
public final class GameState {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    /**
     * <p>Default number of players already added to
     * the lobby when creating a game.</p>
     */
    public static final int DEFAULT_PLAYERS = 2;

    public static final int MAX_PLAYER_NAME_LENGTH = 16;

    /**
     * <p>Current active Game Mode. This is always valid during a game.</p>
     * @see appv2.core.GameMode
     */
    private static GameMode gameMode;

    /**
     * <p>Utility class. No instances allowed.</p>
     * @throws IllegalStateException If an instance of this class is created.
     */
    private GameState() throws IllegalStateException {
        throw new IllegalStateException("GameInstance class");
    }

    /**
     * <p>Initializes a completely new game with the given number of players and their names.</p>
     *
     * @param playerCount Number of players in the game.
     * @param playerNames Names of the players in the game.
     */
    public static void initializeNewGame(int playerCount, String[] playerNames) {
        GameState.gameMode = null;
        GameState.gameMode = new GameMode(playerCount, playerNames);
        GameState.gameMode.prepareForNextRound();

        return;
    }

    // region Getters and Setters

    /**
     * <p>Gets the current active Game Mode. This is always valid during a game.</p>
     *
     * @return The current active Game Mode.
     * @see appv2.core.GameMode
     */
    public static GameMode getActiveGameMode() {
        return GameState.gameMode;
    }

    // endregion Getters and Setters

}
