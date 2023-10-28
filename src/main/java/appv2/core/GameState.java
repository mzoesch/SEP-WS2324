package appv2.core;

public final class GameState {

    public static final int DEFAULT_PLAYERS = 2;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    public static final int MAX_PLAYER_NAME_LENGTH = 16;

    private static GameMode gameMode;

    private GameState() {
        throw new IllegalStateException("GameInstance class");
    }

    public static void initializeNewGame(int playerCount, String[] playerNames) {
        GameState.gameMode = null;
        GameState.gameMode = new GameMode(playerCount, playerNames);
        GameState.gameMode.prepareForNextRound();

        return;
    }

    public static GameMode getActiveGameMode() {
        return GameState.gameMode;
    }

}
