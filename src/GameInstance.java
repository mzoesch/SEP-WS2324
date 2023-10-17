import java.util.Scanner;
import java.util.HashMap;


public class GameInstance {

    // Only one game mode can be active at a time.
    static private GameMode gameMode;

    private static final int minPlayerCount = 2;
    private static final int maxPlayerCount = 4;

    static final HashMap<String, String> validCommands = new HashMap<String, String>() {
        {
            put("help", "Shows this help message.");
            put("quit", "Shutdown application.");
            put("clear", "Clears the screen.");
            put("start", "Starts a new game.");
        }
    };

    public GameInstance() {
        System.out.print("Game Setup.\n");
        System.out.print("Type \\help for information.\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {

            String commandEntered = App.WaitForCommand(
                    scanner, GameInstance.validCommands.keySet().toArray(new String[0]));
            switch (commandEntered) {
                case "help":
                    // TODO: Make this better.
                    for (String command : GameInstance.validCommands.keySet()) {
                        System.out.printf("%s: %s\n", command, GameInstance.validCommands.get(command));
                    }
                    continue;

                case "quit":
                    scanner.close();
                    System.out.print("\n\nThank you for playing!\n\n");
                    System.exit(0);
                    return;

                case "clear":
                    App.ClearScreen(scanner);
                    continue;

                case "start":
                    int playerCount = GameInstance.GetPlayerCount(scanner);
                    String[] playerNames = GameInstance.GetPlayerNames(scanner, playerCount);

                    GameInstance.gameMode = new GameMode(scanner, playerCount, playerNames);
                    GameInstance.gameMode.StartGame();
                    System.out.print("\nThe game has ended.\n");
                    break;

                default:
                    System.out.printf("Something went wrong with your command %s.\n", commandEntered);
                    continue;
            }

            System.out.print("Do you want to play again? (Y/n): ");
            GameInstance.gameMode = null;
            String input = scanner.nextLine();
            if (input.equals("n"))
                break;

            continue;
        }

        scanner.close();
        return;
    }

    static int GetPlayerCount(Scanner scanner) {
        System.out.printf(
                "Current amount of players (%d-%d): ", GameInstance.minPlayerCount, GameInstance.maxPlayerCount);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.nextLine();
        }
        int playerCount = scanner.nextInt();
        if (playerCount < GameInstance.minPlayerCount || playerCount > GameInstance.maxPlayerCount) {
            scanner.nextLine();
            throw new IllegalArgumentException(String.format(
                    "Player count must be between %d and %d.", GameInstance.minPlayerCount, GameInstance.maxPlayerCount
            ));
        }

        scanner.nextLine();
        return playerCount;
    }

    static GameMode GetActiveGameMode() {
        return GameInstance.gameMode;
    }

    static String[] GetPlayerNames(Scanner scanner, int playerCount) {
        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            // TODO: Validate input.
            System.out.printf("Enter the name of player %d: ", i + 1);
            playerNames[i] = scanner.nextLine();
            continue;
        }

        return playerNames;
    }

}
