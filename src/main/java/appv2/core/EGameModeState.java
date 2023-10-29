package appv2.core;


/**
 * <p>Represents the possible states a game can be in.</p>
 */
public enum EGameModeState {
    /**
     * <p>Game is running. The caller will have to render the turn of
     * the current active Player Controller.</p>
     */
    GAME_RUNNING,
    /**
     * <p>A round has ended. The caller will have to show
     * a round ended screen.</p>
     */
    ROUND_ENDED,
    /**
     * <p>The game has ended. The caller will have to show
     * a game ended screen.</p>
     */
    GAME_ENDED,
}
