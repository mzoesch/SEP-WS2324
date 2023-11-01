package app;

import java.io.IOException;
import java.util.Scanner;


/**
 * <p>High-level manager for an instance of the running application.<br />
 * Is valid throughout the entire application runtime and not destroyed until the application is killed.</p>
 */
 public final class GameInstance {

    static private GameMode gameMode;

    private static final int minPlayerCount = 2;
    private static final int maxPlayerCount = 4;

    /**
     * <p>Valid commands for the main menu.</p>
     */
    private static final Command[] ValidCommands = new Command[]{
            new Command("help", "h", "Shows this help message."),
            new Command("clear", "c", "Clears the screen."),
            new Command("start", "s", "Starts a new game."),
            new Command("rules", "r", "Shows the rules of the game."),
            new Command("quit", "q", "Shutdown application.")
    };

    /**
     * <p>Private constructor to prevent instantiation.</p>
     */
    private GameInstance() {
        throw new IllegalStateException("GameInstance class");
    }

    /**
     * <p>Executes a command from the main menu.</p>
     *
     * @param scanner Scanner object to inherit to other objects.
     * @param command Command to execute.
     *
     * @return True if the application should be killed, false otherwise.
     */
    private static boolean executeCommand(Scanner scanner, String command) {
        switch (command) {
            case "h":
            case "help": {
                for (Command cmd : GameInstance.ValidCommands)
                    System.out.printf("%s\n", cmd.toString());

                break;
            }

            case "c":
            case "clear": {
                App.flushStdOut();
                break;
            }

            case "s":
            case "start": {
                int playerCount = GameInstance.getPlayerCount(scanner);
                String[] playerNames = GameInstance.getPlayerNames(scanner, playerCount);

                GameInstance.gameMode = new GameMode(scanner, playerCount, playerNames);
                GameInstance.gameMode.startGame();

                System.out.print("\nThe game has ended.\n");
                break;
            }

            case "r":
            case "rules": {
                System.out.print("\n******************** - * - ********************\n");
                System.out.print("Love Letter Premium Edition by Seiji Kanai.\n");
                System.out.print("Java implementation by Magnus Zoeschinger.\n");
                System.out.print("******************** - * - ********************\n\n");
                try {
                    System.out.print(App.readFile("README.md"));
                } catch (IOException e) {
                    System.out.printf("Something went wrong while reading the rules.\n%s", e.getMessage());
                }

                break;
            }

            case "q":
            case "quit": {
                System.out.print("\nThank you for playing!\n");
                System.exit(0);

                return true;
            }

            default:
                System.out.printf("Something went wrong with your command \"%s\".\n", command);
                return false;
        }

        return false;
    }

    /**
     * <p>Kills the current game instance.</p>
     */
    private static void resetGame() {
        GameInstance.gameMode = null;
        return;
    }

    /**
     * <p>Called at the beginning of the application runtime.<br />
     * Holds the game loop and manages the game state.</p>
     */
    public static void run() {
        GameInstance.resetGame();

        System.out.print("Running game setup.\n");
        System.out.print("Type \\help for information.\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String choice = App.waitForCommand_V2(scanner, GameInstance.ValidCommands);

            boolean bQuitGame = GameInstance.executeCommand(scanner, choice);
            if (bQuitGame)
                break;

            continue;
        }

        scanner.close();
        return;
    }

    /**
     * <p>Prompts the user for a valid player count.</p>
     * 
     * @param scanner The scanner to read the input from.
     * @return The valid player count.
     */
    static private int getPlayerCount(Scanner scanner) {
        return App.waitForInputInteger_V2(
                scanner,
                GameInstance.minPlayerCount,
                GameInstance.maxPlayerCount,
                "Amount of people playing"
        );
    }

    /**
     * <p>Gets and validates the name of all players. Will only return a valid amount of player names.</p>
     *
     * @param scanner The scanner to read the input from.
     * @param playerCount The number of players.
     * @return An array of player names.
     */
    static private String[] getPlayerNames(Scanner scanner, int playerCount) {
        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            while (true) {
                playerNames[i] =
                    App.waitForInputString_V2(
                        scanner,
                        GameMode.MINIMAL_CHARACTER_COUNT_FOR_PLAYER_NAME,
                        GameMode.MAXIMAL_CHARACTER_COUNT_FOR_PLAYER_NAME,
                        String.format("Enter the name of player %d: ", i + 1)
                        );

                boolean bValid = true;
                for (int j = 0; j < i; j++) {
                    if (playerNames[i].equals(playerNames[j])) {
                        System.out.printf("The name \"%s\" is already taken.\n", playerNames[i]);
                        playerNames[i] = null;

                        bValid = false;
                        break;
                    }

                    continue;
                }

                if (bValid)
                    break;

                continue;
            }

            continue;
        }

        return playerNames;
    }

    /**
     * <p>Global getter for the active game mode. Must be valid throughout the entire application lifetime.</p>
     *
     * @return The active game mode.
     */
    static public GameMode getActiveGameMode() {
        return GameInstance.gameMode;
    }
}
