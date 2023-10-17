import java.io.IOException;
import java.util.Scanner;


public class GameInstance {

    private static final int minPlayerCount = 2;
    private static final int maxPlayerCount = 4;

    public GameInstance() {
        System.out.print("Game Setup.\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int playerCount = GameInstance.GetPlayerCount(scanner);
            new GameMode(scanner, playerCount).StartGame();

            System.out.print("\nThe game has ended.\n");
            System.out.print("Do you want to play again? (Y/n): ");
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
}
