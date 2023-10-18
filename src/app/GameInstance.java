package app;

import java.util.Scanner;


public final class GameInstance {

    /** Only one Game Mode can be active at a time. */
    static private GameMode gameMode;

    private static final int minPlayerCount = 2;
    private static final int maxPlayerCount = 4;

    private static final Command[] ValidCommands = new Command[]{
            new Command("help", "h", "Shows this help message."),
            new Command("clear", "c", "Clears the screen."),
            new Command("start", "s", "Starts a new game."),
            new Command("quit", "q", "Shutdown application.")
    };

    private GameInstance() {
        throw new IllegalStateException("GameInstance class");
    }

    private static boolean executeCommand(Scanner scanner, String command) {
        switch (command) {
            case "h":
            case "help": {
                for (Command cmd : GameInstance.ValidCommands)
                    System.out.printf("%s\n", cmd.toString());

                return false;
            }

            case "c":
            case "clear": {
                App.clearStdOut();
                return false;
            }

            case "s":
            case "start": {
                int playerCount = GameInstance.getPlayerCount(scanner);
                String[] playerNames = GameInstance.getPlayerNames(scanner, playerCount);

                GameInstance.gameMode = new GameMode(scanner, playerCount, playerNames);
                GameInstance.gameMode.startGame();

                System.out.print("\nThe game has ended.\n");
                return false;
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
    }

    private static void resetGame() {
        GameInstance.gameMode = null;
        return;
    }

    public static void runGame() {
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

    static int getPlayerCount(Scanner scanner) {
        return App.waitForInputInteger_V2(
                scanner,
                GameInstance.minPlayerCount,
                GameInstance.maxPlayerCount,
                "Amount of people playing"
        );
    }

    static String[] getPlayerNames(Scanner scanner, int playerCount) {
        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            playerNames[i] =
                    App.waitForInputString_V2(scanner, 1, String.format("Enter the name of player %d: ", i + 1));
            continue;
        }

        return playerNames;
    }

    static GameMode getActiveGameMode() {
        return GameInstance.gameMode;
    }
}
